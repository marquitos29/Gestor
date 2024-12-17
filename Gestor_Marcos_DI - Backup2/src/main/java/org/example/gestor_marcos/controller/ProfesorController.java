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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ProfesorController implements Initializable {

    @FXML
    private Label lblBienvenido;

    @FXML
    private TableView<Map<String, String>> tableAlumnos;

    @FXML
    private TableColumn<Map<String, String>, String> colAlumno, colNota;

    @FXML
    private Button btnEliminarAlumno, btnCerrarSesion, btnModificar;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private int idProfesor;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnEliminarAlumno.setOnAction(event -> eliminarAlumno());
        btnCerrarSesion.setOnAction(event -> cerrarSesion());
        btnModificar.setOnAction(event -> modificarNota());

        colAlumno.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().get("alumno")));
        colNota.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().get("nota")));
        cargarAlumnos();
        cargarAlumnosYNotas();
    }

    public void setNombreUsuario(int idProfesor, String nombreProfesor) {
        this.idProfesor = idProfesor;
        lblBienvenido.setText("Bienvenido Don " + nombreProfesor);
        cargarAlumnos();
    }

    private void cargarAlumnos() {
        ObservableList<Map<String, String>> datos = FXCollections.observableArrayList();
        try {
            List<Map<String, String>> alumnos = usuarioDAO.obtenerAlumnosPorProfesor(idProfesor);
            datos.addAll(alumnos);
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar alumnos: " + e.getMessage());
        }
        tableAlumnos.setItems(datos);
    }

    private void modificarNota() {
        Map<String, String> alumnoSeleccionado = tableAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            TextInputDialog dialog = new TextInputDialog(alumnoSeleccionado.get("nota"));
            dialog.setTitle("Modificar Nota");
            dialog.setHeaderText("Modificar nota del alumno");
            dialog.setContentText("Nueva nota:");

            dialog.showAndWait().ifPresent(nuevaNota -> {
                try {
                    int nota = Integer.parseInt(nuevaNota);
                    NotasManager.actualizarNota(alumnoSeleccionado.get("asignatura"), alumnoSeleccionado.get("alumno"), nota);
                    alumnoSeleccionado.put("nota", nuevaNota);
                    tableAlumnos.refresh();
                } catch (NumberFormatException e) {
                    mostrarAlerta("Introduce un número válido.");
                }
            });
        } else {
            mostrarAlerta("Selecciona un alumno para modificar la nota.");
        }
    }

    public void cargarAlumnosYNotas() {
        ObservableList<Map<String, String>> datos = FXCollections.observableArrayList();
        try {
            List<Map<String, String>> alumnos = usuarioDAO.obtenerAlumnosConAsignaturasYNotas(idProfesor);
            datos.addAll(alumnos);
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar los alumnos: " + e.getMessage());
        }
        tableAlumnos.setItems(datos);
    }


    private void eliminarAlumno() {
        Map<String, String> alumnoSeleccionado = tableAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado == null) {
            mostrarAlerta("Selecciona un alumno para eliminar.");
            return;
        }

        try {
            String nombreAlumno = alumnoSeleccionado.get("alumno");
            usuarioDAO.eliminarAlumnoDeProfesor(nombreAlumno, idProfesor);
            tableAlumnos.getItems().remove(alumnoSeleccionado);
            mostrarAlerta("Alumno eliminado correctamente.");
        } catch (SQLException e) {
            mostrarAlerta("No se pudo eliminar al alumno: " + e.getMessage());
        }
    }

    public void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
            Parent parent = loader.load();
            Stage stage = (Stage) btnCerrarSesion.getScene().getWindow();
            stage.setScene(new Scene(parent));
            stage.setTitle("Login");
        } catch (IOException e) {
            mostrarAlerta("No se pudo volver al login: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensaje) {
        new Alert(Alert.AlertType.ERROR, mensaje, ButtonType.OK).showAndWait();
    }
}
