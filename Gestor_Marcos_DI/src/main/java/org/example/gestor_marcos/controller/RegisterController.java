package org.example.gestor_marcos.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.gestor_marcos.MyApp;
import org.example.gestor_marcos.dao.UsuarioDAO;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RegisterController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    private TextField editNombre, editApellido, editAsignatura, editCurso, editCorreo, editPass1, editPass2;

    @FXML
    private Button btnVolver, btnRegistrar;

    @FXML
    private RadioButton radioAlumno, radioProfesor;

    @FXML
    private GridPane gridPane;

    private ToggleGroup grupoRadios;
    private UsuarioDAO usuarioDAO;
    private MyApp myApp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        grupoRadios = new ToggleGroup();
        usuarioDAO = new UsuarioDAO();
        myApp = new MyApp();
        radioAlumno.setToggleGroup(grupoRadios);
        radioProfesor.setToggleGroup(grupoRadios);
        radioProfesor.setUserData(1);
        radioAlumno.setUserData(2);
        btnRegistrar.setOnAction(this);
        btnVolver.setOnAction(this);

        radioProfesor.setOnAction(actionEvent -> {
            if (radioProfesor.isSelected()) {
                editCurso.setVisible(false);
                editAsignatura.setVisible(true);
                GridPane.setRowIndex(editCorreo, 5);
                GridPane.setRowIndex(editPass1, 6);
                GridPane.setRowIndex(editPass2, 7);
            }
        });

        radioAlumno.setOnAction(actionEvent -> {
            if (radioAlumno.isSelected()) {
                editAsignatura.setVisible(false);
                editCurso.setVisible(true);
                GridPane.setRowIndex(editCurso, 4);
                GridPane.setRowIndex(editCorreo, 5);
                GridPane.setRowIndex(editPass1, 6);
                GridPane.setRowIndex(editPass2, 7);
            }
        });
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnVolver) {
            volverAlInicio();
        } else if (actionEvent.getSource() == btnRegistrar) {
            registrarUsuario();
        }
    }

    private void volverAlInicio() {
        try {
            Stage stageActual = (Stage) btnVolver.getScene().getWindow();
            myApp.start(stageActual);
        } catch (IOException e) {
            mostrarAlerta("Error", "Error al volver al inicio.");
        }
    }

    private void registrarUsuario() {
        if (validarCampos()) {
            try {
                if (radioProfesor.isSelected()) {
                    usuarioDAO.insertarUsuario(
                            editNombre.getText(),
                            editApellido.getText(),
                            editCorreo.getText(),
                            editAsignatura.getText(),
                            editPass1.getText(),
                            1
                    );
                } else if (radioAlumno.isSelected()) {
                    usuarioDAO.insertarUsuario(
                            editNombre.getText(),
                            editApellido.getText(),
                            editCorreo.getText(),
                            editCurso.getText(),
                            editPass1.getText(),
                            2
                    );
                }
                mostrarAlerta("Éxito", "Usuario registrado correctamente.");
                volverAlInicio();
            } catch (SQLException e) {
                mostrarAlerta("Error", "No se pudo registrar el usuario.");
                e.printStackTrace();
            }
        }
    }

    private boolean validarCampos() {
        if (grupoRadios.getSelectedToggle() == null) {
            mostrarAlerta("Error", "Selecciona un tipo de perfil.");
            return false;
        }
        if (editNombre.getText().isEmpty() || editApellido.getText().isEmpty() ||
                editCorreo.getText().isEmpty() || editPass1.getText().isEmpty() ||
                editPass2.getText().isEmpty()) {
            mostrarAlerta("Error", "Rellena todos los campos.");
            return false;
        }
        if (!editPass1.getText().equals(editPass2.getText())) {
            mostrarAlerta("Error", "Las contraseñas no coinciden.");
            return false;
        }
        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
