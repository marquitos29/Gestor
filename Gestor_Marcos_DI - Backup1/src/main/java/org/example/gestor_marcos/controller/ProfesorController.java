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

    @FXML
    private ListView<String> listAlumnos;

    @FXML
    private TextField txtNotaMedia;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private int idProfesor;

    private Map<String, Double> notasAlumnos = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnEliminarAlumno.setOnAction(event -> eliminarAlumno());
        btnCerrarSesion.setOnAction(event -> cerrarSesion());
        btnModificar.setOnAction(event -> modificarNota());

        colAlumno.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("alumno")));
        colNota.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().get("nota")));

        cargarAlumnos();
    }

    public void setNombreUsuario(String nombreAlumno) {
        lblBienvenido.setText("Este es tu panel de control, " + nombreAlumno + ".");
    }

    public void cargarAlumnos() {
        ObservableList<Map<String, String>> datos = FXCollections.observableArrayList();
        try {
            List<Map<String, String>> alumnos = usuarioDAO.obtenerAlumnosConNotas();
            datos.addAll(alumnos);
        } catch (SQLException e) {
            mostrarAlerta("Error al cargar alumnos: " + e.getMessage());
        }
        tableAlumnos.setItems(datos);
    }

    public void modificarNota() {
        Map<String, String> alumnoSeleccionado = tableAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado != null) {
            TextInputDialog dialog = new TextInputDialog(alumnoSeleccionado.get("nota"));
            dialog.setTitle("Modificar Nota");
            dialog.setHeaderText("Modificar nota del alumno");
            dialog.setContentText("Nueva nota:");

            dialog.showAndWait().ifPresent(nuevaNota -> {
                alumnoSeleccionado.put("nota", nuevaNota);
                tableAlumnos.refresh();
            });
        } else {
            mostrarAlerta("Selecciona un alumno para modificar la nota.");
        }
    }

    public void modificarNota(String asignatura, int nuevaNota) {
        NotasManager.actualizarNota(asignatura, nuevaNota);
    }


    public void eliminarAlumno() {
        String alumnoSeleccionado = listAlumnos.getSelectionModel().getSelectedItem();
        if (alumnoSeleccionado == null) {
            mostrarAlerta("Selecciona un alumno para eliminar.");
            return;
        }

        try {
            usuarioDAO.eliminarAlumnoDeProfesor(alumnoSeleccionado, idProfesor);
            listAlumnos.getItems().remove(alumnoSeleccionado);
            notasAlumnos.remove(alumnoSeleccionado);
            mostrarAlerta("El alumno ha sido eliminado de la base de datos.");
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

    public void mostrarAlerta(String mensaje) {
        new Alerta("Error", mensaje).mostrar();
    }
}
