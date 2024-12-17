module org.example.gestor_marcos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.json;
    requires com.google.gson;
    requires java.sql;
    requires static lombok;
    requires java.net.http;

    opens org.example.gestor_marcos to javafx.fxml, com.google.gson;
    exports org.example.gestor_marcos;
    exports org.example.gestor_marcos.model;
    opens org.example.gestor_marcos.model to com.google.gson, javafx.fxml, org.json;
    exports org.example.gestor_marcos.dao;
    opens org.example.gestor_marcos.dao to com.google.gson, javafx.fxml,org.json;
    exports org.example.gestor_marcos.controller;
    opens org.example.gestor_marcos.controller to com.google.gson, javafx.fxml,org.json;
    exports org.example.gestor_marcos.database;
    opens org.example.gestor_marcos.database to com.google.gson, javafx.fxml, java.sql;
}