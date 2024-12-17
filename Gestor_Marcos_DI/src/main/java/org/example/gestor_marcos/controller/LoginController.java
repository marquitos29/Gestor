package org.example.gestor_marcos.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import org.example.gestor_marcos.dao.UsuarioDAO;
import org.example.gestor_marcos.model.Alerta;
import javafx.scene.control.*;
import org.example.gestor_marcos.model.NotasManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class LoginController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    private Button btnRegistrarse, btnAcceder, btnLibros;

    @FXML
    private TextField editCorreo, editPass;

    private UsuarioDAO usuarioDAO;
    @Getter
    private Map<String, Integer> notasAlumnos = new HashMap<>();
    private int id;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarioDAO = new UsuarioDAO();
        btnRegistrarse.setOnAction(this);
        btnAcceder.setOnAction(this);
        btnLibros.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnAcceder) {
            manejarAcceso();
        } else if (actionEvent.getSource() == btnLibros) {
            cambiarEscena(btnLibros, "/libros-view.fxml", "Libros", 600, 600);
        } else if (actionEvent.getSource() == btnRegistrarse) {
            cambiarEscena(btnRegistrarse, "/register-view.fxml", "Registro", 300, 530);
        }
    }


    public void manejarAcceso() {
        String correo = editCorreo.getText();
        String pass = editPass.getText();
        if (correo.isEmpty() || pass.isEmpty()) {
            mostrarAlerta("Rellena todos los campos.");
        } else {
            try {
                int idUsuario = usuarioDAO.getIdUsuario(correo);
                if (idUsuario > 0) {
                    int perfil = usuarioDAO.esProfesor(correo) ? 1 : 2; // Nuevo método que determina el perfil
                    id = usuarioDAO.comprobarUsuario(correo, pass, perfil);
                    if (perfil == 1 && id != -1) {
                        cargarVistaProfesor();
                    } else if (perfil == 2 && id != -1) {
                        cargarVistaAlumno();
                    } else {
                        mostrarAlerta("Usuario o contraseña incorrectos.");
                    }
                } else {
                    mostrarAlerta("Usuario no encontrado.");
                }
            } catch (SQLException e) {
                mostrarAlerta("Error al verificar el usuario en la base de datos: " + e.getMessage());
            }
        }
    }



    public void cargarVistaProfesor() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/profesor-view.fxml"));
            Parent parent = loader.load();
            ProfesorController controller = loader.getController();

            controller.cargarAlumnosYNotas();
            controller.setNombreUsuario(usuarioDAO.getIdUsuario(editCorreo.getText()), usuarioDAO.sacarNombre(usuarioDAO.getIdUsuario(editCorreo.getText())));



            Stage stage = (Stage) btnAcceder.getScene().getWindow();
            stage.setScene(new Scene(parent, 600, 400));
            stage.setTitle("Vista del Profesor");
        } catch (IOException | SQLException e) {
            mostrarAlerta("Error al cargar la vista del profesor.");
        }

    }



    public void cargarVistaAlumno() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/alumno-view.fxml"));
            Parent parent = loader.load();
            AlumnoController controller = loader.getController();


            List<Map<String, String>> asignaturas = usuarioDAO.obtenerAsignaturasYProfesores(id);
            for (Map<String, String> asignatura : asignaturas) {
                String nombreAsignatura = asignatura.get("Asignatura");
                NotasManager.inicializarNotasSiNoExisten(nombreAsignatura);
            }


            controller.cargarCompaneros(id);
            controller.setCorreo(editCorreo.getText());
            controller.setNombreUsuario(editCorreo.getText());
            controller.setNotasAlumnos(notasAlumnos);
            controller.setCorreo(editCorreo.getText());
            controller.setNombreUsuario(editCorreo.getText());

            Stage stage = (Stage) btnAcceder.getScene().getWindow();
            stage.setScene(new Scene(parent, 600, 400));
            stage.setTitle("Vista del Alumno");
        } catch (IOException | SQLException e) {
            mostrarAlerta("Error al cargar la vista del alumno.");
        }
    }



    public void cambiarEscena(Button boton, String rutaFXML, String titulo, int ancho, int alto) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent parent = loader.load();
            Stage stage = (Stage) boton.getScene().getWindow();
            stage.setScene(new Scene(parent, ancho, alto));
            stage.setTitle(titulo);
            stage.setResizable(false);
        } catch (IOException e) {
            mostrarAlerta("No se pudo cambiar la escena.");
        }
    }

    public void generarNotasAleatorias() {
        try {
            List<String> alumnos = usuarioDAO.obtenerTodosLosAlumnos();
            for (String alumno : alumnos) {
                notasAlumnos.put(alumno, (int) (Math.random() * 10) + 1);
            }
        } catch (SQLException e) {
            mostrarAlerta("Error al generar notas aleatorias.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alerta alerta = new Alerta("Error", mensaje);
        alerta.mostrar();
    }
}
