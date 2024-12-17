package org.example.gestor_marcos.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import org.example.gestor_marcos.MyApp;
import org.example.gestor_marcos.model.Alerta;

import java.io.IOException;

public class MenuController {

    @FXML
    private MenuItem menuInicio;

    @FXML
    private MenuItem menuSalir;

    private Stage stageActual;

    public void initialize() {
        menuInicio.setOnAction(event -> volverAlInicio());
        menuSalir.setOnAction(event -> salirDeAplicacion());
    }

    private void volverAlInicio() {
        try {
            stageActual = (Stage) menuInicio.getParentPopup().getOwnerWindow();
            new MyApp().start(stageActual);
        } catch (IOException e) {
            new Alerta("Error", "No se pudo volver al login.").mostrar();
        }
    }

    private void salirDeAplicacion() {
        System.exit(0);
    }
}
