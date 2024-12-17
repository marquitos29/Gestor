package org.example.gestor_marcos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.gestor_marcos.dao.UsuarioDAO;
import org.example.gestor_marcos.model.Alerta;
import org.example.gestor_marcos.model.NotasManager;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AlumnoController implements Initializable {

    @FXML
    private Label lblBienvenido;

    @FXML
    private ListView<String> listCompaneros;

    @FXML
    private TableView<Map<String, String>> tableAsignaturas;

    @FXML
    private TableColumn<Map<String, String>, String> colProfesor, colAsignatura,colNota;

    @FXML
    private Button btnVerNotas, btnCerrarSesion;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    private boolean notasVisibles = false;
    private Map<String, Integer> notasAlumnos;
    private String correo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnCerrarSesion.setOnAction(action -> cerrarSesion());
        btnVerNotas.setOnAction(action -> alternarNotas());

        colAsignatura.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("Asignatura")));
        colProfesor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("Profesor")));
        colNota.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("Notas")));
        colNota.setVisible(false);
    }

    public void setCorreo(String correo) {
        this.correo = correo;
        cargarAsignaturas();
    }

    public void setNombreUsuario(String correo) {
        String nombreAlumno = null;
        try {
            nombreAlumno = usuarioDAO.sacarNombre(usuarioDAO.getIdUsuario(correo));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        lblBienvenido.setText("Este es tu panel de control, " + nombreAlumno + ".");
    }


    public void cargarCompaneros(int id) {
        try {
            ObservableList<String> compañeros = usuarioDAO.obtenerCompanerosDeAlumno(id);
            compañeros.add(0, "Compañeros de tu curso: ");
            listCompaneros.setCellFactory(listView -> new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if (getIndex() == 0) {
                            setStyle("-fx-font-weight: bold; " +
                                    "-fx-font-size: 14px; " +
                                    "-fx-background-color: lightgray;");
                        } else {
                            setStyle("");
                        }
                    }
                }
            });

            listCompaneros.setItems(compañeros);
        } catch (SQLException e) {
            mostrarAlerta("No se pudieron cargar los compañeros del curso: " + e.getMessage());
        }
    }


    private void cargarAsignaturas() {
        ObservableList<Map<String, String>> datos = FXCollections.observableArrayList();
        try {
            List<Map<String, String>> asignaturas = usuarioDAO.obtenerAsignaturasYProfesores(usuarioDAO.getIdUsuario(correo));
            for (Map<String, String> asignatura : asignaturas) {
                String nombreAsignatura = asignatura.get("Asignatura");
                int nota = NotasManager.getNotasAsignaturas().getOrDefault(nombreAsignatura, 0);
                asignatura.put("Notas", String.valueOf(nota));
            }
            datos.addAll(asignaturas);
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar asignaturas: " + e.getMessage());
        }
        tableAsignaturas.setItems(datos);
    }



    private void alternarNotas() {
        notasVisibles = !notasVisibles;
        colNota.setVisible(notasVisibles);
        btnVerNotas.setText(notasVisibles ? "Ocultar Notas" : "Ver Notas");
    }

    public void setNotasAlumnos(Map<String, Integer> notasAlumnos) {
        this.notasAlumnos = notasAlumnos;
        cargarAsignaturas();
    }


    public void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
            Parent parent = loader.load();
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Login");
            stage.setResizable(false);
        } catch (IOException e) {
            mostrarAlerta("No se pudo cambiar la escena: " + e.getMessage());
        }
    }

    public void mostrarAlerta(String mensaje) {
        new Alerta("Error", mensaje).mostrar();
    }
}
