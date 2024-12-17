package org.example.gestor_marcos.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            createConnection();
        }
        return connection;
    }

    private static void createConnection() {
        String uriConnection = String.format("jdbc:mysql://%s:%s/%s",
                DBSchema.HOST, DBSchema.PORT, DBSchema.DATABASE_NAME);
        try {
            connection = DriverManager.getConnection(uriConnection, "root", "");
            System.out.println("Conexión creada correctamente");
        } catch (SQLException e) {
            System.out.println("Fallo en la conexión");
            e.printStackTrace();
        }
    }


}
