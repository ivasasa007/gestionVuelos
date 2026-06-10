package com.gestionvuelos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL vista = MainApp.class.getResource("/views/MainView.fxml");

        if (vista == null) {
            throw new IOException("No se ha encontrado el archivo MainView.fxml");
        }

        FXMLLoader fxmlLoader = new FXMLLoader(vista);
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Gestion de Vuelos");
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
