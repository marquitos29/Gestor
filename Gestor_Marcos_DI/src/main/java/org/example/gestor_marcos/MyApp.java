package org.example.gestor_marcos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.gestor_marcos.controller.LoginController;

import java.io.IOException;

public class MyApp extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        //No puedo implementarlo con la carga de escena de LoginController ya que no tiene boton y seria complicarme de mas.
        FXMLLoader loader = new FXMLLoader(MyApp.class.getResource("/login-view.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent, 500, 350);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}