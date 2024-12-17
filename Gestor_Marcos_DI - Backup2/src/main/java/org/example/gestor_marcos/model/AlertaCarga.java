package org.example.gestor_marcos.model;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.gestor_marcos.controller.CargaController;

import java.io.IOException;

public class AlertaCarga {

    private Stage stage;
    private CargaController cargaController;

    public AlertaCarga() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/carga-view.fxml"));
            Parent root = loader.load();
            cargaController = loader.getController();
            stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("Cargando...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void mostrar(Runnable onFinish) {
        cargaController.iniciarCarga(() -> {
            stage.close();
            onFinish.run();
        });
        stage.show();
    }
}
