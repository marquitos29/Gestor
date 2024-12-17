package org.example.gestor_marcos.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.gestor_marcos.database.DBConnection;
import org.example.gestor_marcos.database.DBSchema;
import org.example.gestor_marcos.model.NotasManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsuarioDAO {

    private Connection connection;

    public UsuarioDAO() {
        connection = new DBConnection().getConnection();
    }

    public void insertarUsuario(String nombre, String apellido, String correo, String pass, String curso, int perfil) throws SQLException {
        String query = String.format(
                "INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
                DBSchema.TAB_STU,
                DBSchema.COL_NAME_STU,
                DBSchema.COL_SNAME_STU,
                DBSchema.COL_MAIL_STU,
                DBSchema.COL_PASS_STU,
                DBSchema.COL_COURSE_STU,
                DBSchema.COL_ID_PRO_FK_STU
        );
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, apellido);
            preparedStatement.setString(3, correo);
            preparedStatement.setString(4, pass);
            preparedStatement.setString(5, curso);
            preparedStatement.setInt(6, perfil);
            preparedStatement.executeUpdate();
        }
    }

    public int comprobarUsuario(String correo, String pass, int rango) throws SQLException {
        if (rango == 1) {
            System.out.println("Buscando profesor con correo: " + correo + " y contraseña: " + pass);
            String query = String.format(
                    "SELECT %s FROM %s WHERE %s = ? AND %s = ?",
                    DBSchema.COL_ID_THE,
                    DBSchema.TAB_THE,
                    DBSchema.COL_MAIL_THE,
                    DBSchema.COL_PASS_THE
            );
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, correo);
            preparedStatement.setString(2, pass);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Profesor encontrado. ID: " + resultSet.getInt(DBSchema.COL_ID_THE));
                return resultSet.getInt(DBSchema.COL_ID_THE);
            } else {
                System.out.println("Profesor no encontrado.");
                return -1;
            }
        } else {
            System.out.println("Buscando alumno con correo: " + correo + " y contraseña: " + pass);
            String query = String.format(
                    "SELECT %s FROM %s WHERE %s = ? AND %s = ?",
                    DBSchema.COL_ID_STU,
                    DBSchema.TAB_STU,
                    DBSchema.COL_MAIL_STU,
                    DBSchema.COL_PASS_STU
            );
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, correo);
            preparedStatement.setString(2, pass);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Alumno encontrado. ID: " + resultSet.getInt(DBSchema.COL_ID_STU));
                return resultSet.getInt(DBSchema.COL_ID_STU);
            } else {
                System.out.println("Alumno no encontrado.");
                return -1;
            }
        }
    }



    public int getIdUsuario(String correo) throws SQLException {

        String queryAlumno = String.format(
                "SELECT %s FROM %s WHERE %s = ?",
                DBSchema.COL_ID_STU, DBSchema.TAB_STU, DBSchema.COL_MAIL_STU
        );
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryAlumno)) {
            preparedStatement.setString(1, correo);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(DBSchema.COL_ID_STU);
            }
        }


        String queryProfesor = String.format(
                "SELECT %s FROM %s WHERE %s = ?",
                DBSchema.COL_ID_THE, DBSchema.TAB_THE, DBSchema.COL_MAIL_THE
        );
        try (PreparedStatement preparedStatement = connection.prepareStatement(queryProfesor)) {
            preparedStatement.setString(1, correo);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(DBSchema.COL_ID_THE);
            }
        }


        throw new SQLException("No se encontró el usuario en alumnos ni profesores.");
    }


    public int getIdFk(int id) throws SQLException {
        try {
            String queryAlumno = String.format(
                    "SELECT %s FROM %s WHERE %s = ?",
                    DBSchema.COL_ID_PRO_FK_STU, DBSchema.TAB_STU, DBSchema.COL_ID_STU
            );
            PreparedStatement preparedStatement = connection.prepareStatement(queryAlumno);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getInt(DBSchema.COL_ID_PRO_FK_STU);

        } catch (Exception e) {
            String queryProfesor = String.format(
                    "SELECT %s FROM %s WHERE %s = ?",
                    DBSchema.COL_ID_PRO_FK_THE, DBSchema.TAB_THE, DBSchema.COL_ID_THE
            );
            PreparedStatement preparedStatement = connection.prepareStatement(queryProfesor);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return resultSet.getInt(DBSchema.COL_ID_PRO_FK_THE);
            throw new SQLException("No se encontró el usuario en alumnos ni profesores.");
        }
        return 0;
    }


    public String sacarNombre(int id) throws SQLException {
        String query;
        int perfil = getIdFk(id);
        if (perfil == 1) {
            query = String.format(
                    "SELECT %s FROM %s WHERE %s = ?",
                    DBSchema.COL_NAME_THE, DBSchema.TAB_THE, DBSchema.COL_ID_THE
            );
        } else {
            query = String.format(
                    "SELECT %s FROM %s WHERE %s = ?",
                    DBSchema.COL_NAME_STU, DBSchema.TAB_STU, DBSchema.COL_ID_STU
            );
        }

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString(perfil == 1 ? DBSchema.COL_NAME_THE : DBSchema.COL_NAME_STU);
        } else new SQLException("No se encontró el usuario.");
        return query;
    }

    public ObservableList<String> obtenerCompanerosDeAlumno(int alumnoId) throws SQLException {
        ObservableList<String> companeros = FXCollections.observableArrayList();
        String query = String.format(
                "SELECT %s, %s FROM %s WHERE %s = (SELECT %s FROM %s WHERE %s = ?) AND %s != ?;",
                DBSchema.COL_NAME_STU, DBSchema.COL_SNAME_STU, DBSchema.TAB_STU,
                DBSchema.COL_COURSE_STU, DBSchema.COL_COURSE_STU, DBSchema.TAB_STU,
                DBSchema.COL_ID_STU, DBSchema.COL_ID_STU
        );
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, alumnoId);
        preparedStatement.setInt(2, alumnoId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String nombreCompleto = resultSet.getString(DBSchema.COL_NAME_STU) + " " + resultSet.getString(DBSchema.COL_SNAME_STU);
            companeros.add(nombreCompleto);
        }
        return companeros;
    }


    public void eliminarAlumnoDeProfesor(String nombreAlumno, int profesorId) throws SQLException {
        String query = String.format(
                "DELETE FROM %s WHERE %s = (SELECT %s FROM %s WHERE %s = ?) AND %s = ?",
                DBSchema.TAB_COURSE, DBSchema.COL_FK_STUDENT_USER_COURSE,
                DBSchema.COL_ID_STU, DBSchema.TAB_STU, DBSchema.COL_NAME_STU,
                DBSchema.COL_FK_TEACHER_USER_COURSE
        );

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, nombreAlumno);
            preparedStatement.setInt(2, profesorId);
            preparedStatement.executeUpdate();
        }
    }


    public List<Map<String, String>> obtenerAsignaturasYProfesores(int idAlumno) throws SQLException {
        List<Map<String, String>> lista = new ArrayList<>();
        String query = String.format("SELECT s.%s, CONCAT(p.%s, ' ', p.%s) AS %s FROM %s s INNER JOIN %s p ON s.%s = p.%s WHERE s.%s = (SELECT c.%s FROM %s c WHERE c.%s = (SELECT a.%s FROM %s a WHERE a.%s = ?))",
                DBSchema.COL_NAME_SUBJECT, DBSchema.COL_NAME_THE, DBSchema.COL_SNAME_THE, "nombre_completo_profesor", DBSchema.TAB_SUBJECT, DBSchema.TAB_THE, DBSchema.COL_FK_TEACHER_USER_SUBJECT, DBSchema.COL_ID_THE, DBSchema.COL_FK_COURSE_SUBJECT, DBSchema.COL_ID_COURSE, DBSchema.TAB_COURSE, DBSchema.COL_NAME_COURSE, DBSchema.COL_COURSE_STU, DBSchema.TAB_STU, DBSchema.COL_ID_STU);
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idAlumno);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, String> row = new HashMap<>();
                    row.put("Asignatura", resultSet.getString(DBSchema.COL_NAME_SUBJECT));
                    row.put("Profesor", resultSet.getString("nombre_completo_profesor"));
                    lista.add(row);
                }
            }
        }
        return lista;
    }

    public List<Map<String, String>> obtenerAlumnosConAsignaturasYNotas(int idProfesor) throws SQLException {
        List<Map<String, String>> lista = new ArrayList<>();
        String query = String.format(
                "SELECT a.%s AS alumno, s.%s AS asignatura " +
                        "FROM %s a " +
                        "JOIN %s c ON a.%s = c.%s " +
                        "JOIN %s s ON c.%s = s.%s " +
                        "WHERE s.%s = ?",
                DBSchema.COL_NAME_STU, DBSchema.COL_NAME_SUBJECT,
                DBSchema.TAB_STU, DBSchema.TAB_COURSE,
                DBSchema.COL_ID_STU, DBSchema.COL_FK_STUDENT_USER_COURSE,
                DBSchema.TAB_SUBJECT, DBSchema.COL_ID_COURSE, DBSchema.COL_FK_COURSE_SUBJECT,
                DBSchema.COL_FK_TEACHER_USER_SUBJECT
        );
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idProfesor);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Map<String, String> row = new HashMap<>();
                row.put("alumno", resultSet.getString("alumno"));
                row.put("asignatura", resultSet.getString("asignatura"));
                lista.add(row);
            }
        }
        return lista;
    }

    public boolean esProfesor(String correo) throws SQLException {
        String query = String.format(
                "SELECT COUNT(*) FROM %s WHERE %s = ?",
                DBSchema.TAB_THE, DBSchema.COL_MAIL_THE
        );
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, correo);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
    }



    public List<Map<String, String>> obtenerAlumnosConNotas() throws SQLException {
        List<Map<String, String>> lista = new ArrayList<>();
        String query = "SELECT nombre_alumno FROM alumnos";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            Map<String, String> row = new HashMap<>();
            row.put("alumno", resultSet.getString("nombre_alumno"));
            row.put("nota", String.valueOf((int) (Math.random() * 10) + 1));
            lista.add(row);
        }
        return lista;
    }


    public List<String> obtenerTodosLosAlumnos() throws SQLException {
        List<String> alumnos = new ArrayList<>();
        String query = String.format("SELECT %s FROM %s", DBSchema.COL_NAME_STU, DBSchema.TAB_STU);
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                alumnos.add(resultSet.getString(DBSchema.COL_NAME_STU));
            }
        }
        return alumnos;
    }

    public List<Map<String, String>> obtenerAlumnosPorProfesor(int idProfesor) throws SQLException {
        List<Map<String, String>> listaAlumnos = new ArrayList<>();

        String query = String.format(
                "SELECT a.%s, a.%s, a.%s, s.%s " +
                        "FROM %s s " +
                        "INNER JOIN %s c ON s.%s = c.%s " +
                        "INNER JOIN %s a ON c.%s = a.%s " +
                        "WHERE s.%s = ?",
                DBSchema.COL_NAME_STU, DBSchema.COL_SNAME_STU, DBSchema.COL_MAIL_STU, DBSchema.COL_NAME_SUBJECT,
                DBSchema.TAB_SUBJECT, DBSchema.TAB_COURSE, DBSchema.COL_ID_COURSE, DBSchema.COL_FK_COURSE_SUBJECT,
                DBSchema.TAB_STU, DBSchema.COL_ID_COURSE, DBSchema.COL_FK_STUDENT_USER_COURSE,
                DBSchema.COL_FK_TEACHER_USER_SUBJECT
        );

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idProfesor);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Map<String, String> alumno = new HashMap<>();
                    String nombreCompleto = resultSet.getString(DBSchema.COL_NAME_STU) + " " +
                            resultSet.getString(DBSchema.COL_SNAME_STU);
                    alumno.put("alumno", nombreCompleto);
                    alumno.put("correo", resultSet.getString(DBSchema.COL_MAIL_STU));
                    String asignatura = resultSet.getString(DBSchema.COL_NAME_SUBJECT);

                    // Generar nota con NotasManager
                    int nota = NotasManager.obtenerNota(asignatura, nombreCompleto);
                    alumno.put("nota", String.valueOf(nota));

                    listaAlumnos.add(alumno);
                }
            }
        }
        return listaAlumnos;
    }
}
