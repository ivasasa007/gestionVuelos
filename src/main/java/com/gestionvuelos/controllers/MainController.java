package com.gestionvuelos.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainController {

    @FXML
    private TableView<?> tablaVuelos;

    @FXML
    private TableColumn<?, ?> colCodigo;

    @FXML
    private TableColumn<?, ?> colOrigen;

    @FXML
    private TableColumn<?, ?> colDestino;

    @FXML
    private TableColumn<?, ?> colFecha;

    @FXML
    private TableColumn<?, ?> colEstado;

    @FXML
    private Label lblEstado;

    @FXML
    private void onNuevoVuelo() {
        lblEstado.setText("Crear nuevo vuelo");
    }

    @FXML
    private void onEditarVuelo() {
        lblEstado.setText("Editar vuelo seleccionado");
    }

    @FXML
    private void onEliminarVuelo() {
        lblEstado.setText("Eliminar vuelo seleccionado");
    }
}
