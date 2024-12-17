package org.example.gestor_marcos.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.gestor_marcos.MyApp;
import org.example.gestor_marcos.dao.ApiDAO;
import org.example.gestor_marcos.model.Alerta;
import org.example.gestor_marcos.model.AlertaCarga;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class LibrosController implements Initializable {

    @FXML
    private ComboBox<String> cbCursos, cbAsignaturas;

    @FXML
    private Button btnVolver;

    @FXML
    private ListView<String> listLibros;

    private final Map<String, ObservableList<String>> mapaCursosAsignaturas = new HashMap<>();
    private final Map<String, ObservableList<String>> mapaAsignaturaLibro = new HashMap<>();

    private final ApiDAO apiDAO = new ApiDAO();
    private MyApp myApp;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        inicializarDatos();
        cbCursos.valueProperty().addListener((observable, oldValue, newValue) -> cargarAsignaturas(newValue));
        cbAsignaturas.valueProperty().addListener((observable, oldValue, newValue) -> cargarLibros(newValue));
        btnVolver.setOnAction(event -> volverLogin());
    }

    private void inicializarDatos() {
        mapaCursosAsignaturas.put("DAM", FXCollections.observableArrayList("Desarrollo de interfaces", "Base de datos", "Programación"));
        mapaCursosAsignaturas.put("ASIR", FXCollections.observableArrayList("Redes", "Sistemas Operativos", "Seguridad"));
        mapaCursosAsignaturas.put("DAW", FXCollections.observableArrayList("Diseño Web", "JavaScript", "PHP"));
        mapaCursosAsignaturas.put("AF", FXCollections.observableArrayList("Contabilidad y fiscalidad", "Gestión financiera", "Gestión logística y comercial"));

        cbCursos.setItems(FXCollections.observableArrayList(mapaCursosAsignaturas.keySet()));

        new AlertaCarga().mostrar(() -> {
            try {
                inicializarLibros();
            } catch (IOException | InterruptedException e) {
                mostrarAlerta("Error al cargar los libros desde la API.");
            }
        });
    }

    private void inicializarLibros() throws IOException, InterruptedException {
        mapaAsignaturaLibro.put("Desarrollo de interfaces", apiDAO.getLibroPorRank(1));
        mapaAsignaturaLibro.put("Base de datos", apiDAO.getLibroPorRank(2));
        mapaAsignaturaLibro.put("Programación", apiDAO.getLibroPorRank(3));
        mapaAsignaturaLibro.put("Redes", apiDAO.getLibroPorRank(4));
        mapaAsignaturaLibro.put("Sistemas Operativos", apiDAO.getLibroPorRank(5));
        mapaAsignaturaLibro.put("Seguridad", apiDAO.getLibroPorRank(6));
        mapaAsignaturaLibro.put("Diseño Web", apiDAO.getLibroPorRank(7));
        mapaAsignaturaLibro.put("JavaScript", apiDAO.getLibroPorRank(8));
        mapaAsignaturaLibro.put("PHP", apiDAO.getLibroPorRank(9));
        mapaAsignaturaLibro.put("Contabilidad y fiscalidad", apiDAO.getLibroPorRank(10));
        mapaAsignaturaLibro.put("Gestión financiera", apiDAO.getLibroPorRank(11));
        mapaAsignaturaLibro.put("Gestión logística y comercial", apiDAO.getLibroPorRank(12));
    }

    private void cargarAsignaturas(String curso) {
        if (curso != null) {
            cbAsignaturas.setItems(mapaCursosAsignaturas.getOrDefault(curso, FXCollections.observableArrayList()));
            cbAsignaturas.getSelectionModel().clearSelection();
        }
    }

    private void cargarLibros(String asignatura) {
        if (asignatura != null && mapaAsignaturaLibro.containsKey(asignatura)) {
            listLibros.setItems(mapaAsignaturaLibro.get(asignatura));
        } else {
            listLibros.setItems(FXCollections.observableArrayList("Seleccione una asignatura..."));
        }
    }

    private void volverLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
            Parent parent = loader.load();
            Stage stage = (Stage) btnVolver.getScene().getWindow();
            stage.setScene(new Scene(parent, 500, 350));
            stage.setTitle("Login");
            stage.setResizable(false);
        } catch (IOException e) {
            mostrarAlerta("No se pudo cambiar la escena.");
        }
    }

    private void mostrarAlerta(String mensaje) {
        new Alerta("Error", mensaje).mostrar();
    }
}
