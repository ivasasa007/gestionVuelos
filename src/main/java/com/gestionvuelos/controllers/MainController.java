package com.gestionvuelos.controllers;

import com.gestionvuelos.dao.VueloDAO;
import com.gestionvuelos.models.Vuelo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MainController {

    private final VueloDAO vueloDAO = new VueloDAO();

    @FXML
    private TableView<Vuelo> tablaVuelos;

    @FXML
    private TableColumn<Vuelo, String> colNumeroVuelo;

    @FXML
    private TableColumn<Vuelo, String> colDestino;

    @FXML
    private TableColumn<Vuelo, LocalDate> colFechaSalida;

    @FXML
    private TableColumn<Vuelo, Integer> colDuracion;

    @FXML
    private TextField txtNumeroVuelo;

    @FXML
    private TextField txtDestino;

    @FXML
    private DatePicker dpFechaSalida;

    @FXML
    private TextField txtDuracion;

    @FXML
    private ComboBox<String> cmbFiltro;

    @FXML
    private Label lblEstado;

    @FXML
    private void initialize() {
        colNumeroVuelo.setCellValueFactory(new PropertyValueFactory<>("numeroVuelo"));
        colDestino.setCellValueFactory(new PropertyValueFactory<>("destino"));
        colFechaSalida.setCellValueFactory(new PropertyValueFactory<>("fechaSalida"));
        colDuracion.setCellValueFactory(new PropertyValueFactory<>("duracion"));

        cmbFiltro.setItems(FXCollections.observableArrayList(
                "Vuelos de más de 3 horas",
                "Vuelos por destino"
        ));

        try {
            vueloDAO.crearTabla();
            cargarVuelos();
        } catch (SQLException e) {
            mostrarError("No se pudo iniciar la base de datos.");
        }
    }

    @FXML
    private void onAnadirVuelo() {
        try {
            Vuelo vuelo = leerVueloFormulario();
            vueloDAO.insertar(vuelo);
            limpiarFormulario();
            cargarVuelos();
            lblEstado.setText("Vuelo añadido");
        } catch (NumberFormatException e) {
            mostrarError("La duración debe ser un número.");
        } catch (IllegalArgumentException e) {
            mostrarError(e.getMessage());
        } catch (SQLException e) {
            mostrarError("No se pudo añadir el vuelo.");
        }
    }

    @FXML
    private void onEliminarVuelo() {
        Vuelo vuelo = tablaVuelos.getSelectionModel().getSelectedItem();

        if (vuelo == null) {
            mostrarError("Selecciona un vuelo para eliminar.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar vuelo");
        confirmacion.setHeaderText("¿Quieres eliminar el vuelo " + vuelo.getNumeroVuelo() + "?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        if (confirmacion.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        try {
            vueloDAO.eliminar(vuelo.getNumeroVuelo());
            cargarVuelos();
            lblEstado.setText("Vuelo eliminado");
        } catch (SQLException e) {
            mostrarError("No se pudo eliminar el vuelo.");
        }
    }

    @FXML
    private void onActualizarVuelos() {
        cargarVuelos();
        cmbFiltro.getSelectionModel().clearSelection();
        lblEstado.setText("Vuelos actualizados");
    }

    @FXML
    private void onAplicarFiltro() {
        String filtro = cmbFiltro.getValue();

        if (filtro == null) {
            cargarVuelos();
            return;
        }

        try {
            if (filtro.equals("Vuelos de más de 3 horas")) {
                mostrarVuelos(vueloDAO.filtrarDuracionMayor180());
            } else {
                String destino = txtDestino.getText().trim();
                if (destino.isEmpty()) {
                    mostrarError("Escribe un destino para filtrar.");
                    return;
                }
                mostrarVuelos(vueloDAO.filtrarPorDestino(destino));
            }
            lblEstado.setText("Filtro aplicado");
        } catch (SQLException e) {
            mostrarError("No se pudo aplicar el filtro.");
        }
    }

    private void cargarVuelos() {
        try {
            mostrarVuelos(vueloDAO.listarTodos());
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los vuelos.");
        }
    }

    private void mostrarVuelos(List<Vuelo> vuelos) {
        tablaVuelos.setItems(FXCollections.observableArrayList(vuelos));
    }

    private Vuelo leerVueloFormulario() {
        String numeroVuelo = txtNumeroVuelo.getText().trim();
        String destino = txtDestino.getText().trim();
        LocalDate fechaSalida = dpFechaSalida.getValue();
        String duracionTexto = txtDuracion.getText().trim();

        if (numeroVuelo.isEmpty() || destino.isEmpty() || fechaSalida == null || duracionTexto.isEmpty()) {
            throw new IllegalArgumentException("Rellena todos los campos.");
        }

        int duracion = Integer.parseInt(duracionTexto);
        if (duracion <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que 0.");
        }

        return new Vuelo(numeroVuelo, destino, fechaSalida, duracion);
    }

    private void limpiarFormulario() {
        txtNumeroVuelo.clear();
        txtDestino.clear();
        dpFechaSalida.setValue(null);
        txtDuracion.clear();
    }

    private void mostrarError(String mensaje) {
        lblEstado.setText(mensaje);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
