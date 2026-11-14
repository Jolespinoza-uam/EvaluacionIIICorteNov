package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.EquipoController;
import org.example.dao.EquipoDAO;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
    private EntityManagerFactory factory;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Map<String, String> propiedades = new HashMap<>();
        String url = System.getenv("DB_URL");
        String usuario = System.getenv("DB_USER");
        String clave = System.getenv("DB_PASSWORD");

        if (url != null && !url.isBlank()) {
            propiedades.put("jakarta.persistence.jdbc.url", url);
        }
        if (usuario != null && !usuario.isBlank()) {
            propiedades.put("jakarta.persistence.jdbc.user", usuario);
        }
        if (clave != null) {
            propiedades.put("jakarta.persistence.jdbc.password", clave);
        }

        try {
            factory = Persistence.createEntityManagerFactory("inventario", propiedades);

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("equipos.fxml"));
            loader.setController(new EquipoController(new EquipoDAO(factory)));
            stage.setScene(new Scene(loader.load(), 850, 500));
            stage.setTitle("Inventario de equipos");
            stage.show();
        } catch (Exception error) {
            stop();
            throw error;
        }
    }

    @Override
    public void stop() {
        if (factory != null && factory.isOpen()) {
            factory.close();
        }
    }
}