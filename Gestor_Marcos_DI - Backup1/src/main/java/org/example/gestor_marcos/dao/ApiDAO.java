package org.example.gestor_marcos.dao;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.gestor_marcos.model.Libro;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ApiDAO {

    private String API_URL = "https://all-books-api.p.rapidapi.com/getBooks";

    private List<Libro> obtenerLibrosDesdeAPI(String jsonResponse) {
        List<Libro> libros = new ArrayList<>();
        JsonElement jsonElement = JsonParser.parseString(jsonResponse).getAsJsonArray();

        if (!jsonElement.isJsonArray()){
            System.out.println("La respuesta no es un JSON ARRAY: "+jsonResponse);
            return libros;
        }
        JsonArray jsonArray = jsonElement.getAsJsonArray();

        for (JsonElement elemento : jsonArray) {
            int bookRank = elemento.getAsJsonObject().get("bookRank").getAsInt();
            String bookTitle = elemento.getAsJsonObject().get("bookTitle").getAsString();
            String bookAuthor = elemento.getAsJsonObject().get("bookAuthor").getAsString();
            String bookPublisher = elemento.getAsJsonObject().get("bookPublisher").getAsString();
            String bookImage = elemento.getAsJsonObject().get("bookImage").getAsString();
            String downloadLink = elemento.getAsJsonObject().has("amazonBookUrl")
                    ? elemento.getAsJsonObject().get("amazonBookUrl").getAsString()
                    : null;

            libros.add(new Libro(bookRank, bookTitle, bookAuthor, bookPublisher, bookImage, downloadLink));
        }

        return libros;
    }

    public ObservableList<String> crearLista(String titulo, String autor, String editorial, String portada, String urlCompra) {
        ObservableList<String> lista = FXCollections.observableArrayList();

        lista.add("Título: " + titulo);
        lista.add("Autor: " + autor);
        lista.add("Editorial: " + editorial);
        lista.add("Portada: " + portada);
        lista.add("Url de compra: " + urlCompra);

        return lista;
    }

    public ObservableList<String> getLibroPorRank(int rank) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("X-RapidAPI-Key", "a18990f4f6msh0f59a93f4be4058p182ed6jsnf3de4b26bb24")
                .header("X-RapidAPI-Host", "all-books-api.p.rapidapi.com")
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200){
            return FXCollections.observableArrayList("Error: "+ response.body());
        }

        String jsonResponse = response.body();


        JsonArray jsonArray = JsonParser.parseString(jsonResponse).getAsJsonArray();

        for (JsonElement elemento : jsonArray) {
            if (elemento.getAsJsonObject().get("bookRank").getAsInt() == rank) {
                String titulo = elemento.getAsJsonObject().get("bookTitle").getAsString();
                String autor = elemento.getAsJsonObject().get("bookAuthor").getAsString();
                String editorial = elemento.getAsJsonObject().get("bookPublisher").getAsString();
                String portada = elemento.getAsJsonObject().get("bookImage").getAsString();
                String urlCompra = elemento.getAsJsonObject().has("amazonBookUrl")
                        ? elemento.getAsJsonObject().get("amazonBookUrl").getAsString()
                        : "Sin link";

                return crearLista(titulo, autor, editorial, portada, urlCompra);
            }
        }

        return FXCollections.observableArrayList("No se encontró un libro con ese rank.");
    }
}
