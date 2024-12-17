package org.example.gestor_marcos.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class CargaController {

    @FXML
    private ProgressBar barraProgreso;

    @FXML
    private Label txtCarga;

    public void iniciarCarga(Runnable onFinish) {
        Task<Void> taskProgress = new Task<>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i <= 100; i++) {
                    updateProgress(i, 100);
                    updateMessage("Cargando... " + i + "%");
                    Thread.sleep(40);
                }
                return null;
            }
        };

        taskProgress.setOnSucceeded(event -> onFinish.run());
        barraProgreso.progressProperty().bind(taskProgress.progressProperty());
        txtCarga.textProperty().bind(taskProgress.messageProperty());

        Thread thread = new Thread(taskProgress);
        thread.setDaemon(true);
        thread.start();
    }
}
