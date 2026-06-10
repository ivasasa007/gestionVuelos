package com.gestionvuelos.dao;

import com.gestionvuelos.database.ConexionBD;
import com.gestionvuelos.models.Vuelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VueloDAO {

    public void crearTabla() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS flight ("
                + "id_flight SERIAL PRIMARY KEY, "
                + "num_flight VARCHAR(100) UNIQUE NOT NULL, "
                + "destination VARCHAR(100) NOT NULL, "
                + "departure DATE NOT NULL, "
                + "duration INT DEFAULT 30"
                + ")";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    public void insertar(Vuelo vuelo) throws SQLException {
        String sql = "INSERT INTO flight (num_flight, destination, departure, duration) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, vuelo.getNumeroVuelo());
            statement.setString(2, vuelo.getDestino());
            statement.setDate(3, java.sql.Date.valueOf(vuelo.getFechaSalida()));
            statement.setInt(4, vuelo.getDuracion());
            statement.executeUpdate();
        }
    }

    public void eliminar(String numeroVuelo) throws SQLException {
        String sql = "DELETE FROM flight WHERE num_flight = ?";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, numeroVuelo);
            statement.executeUpdate();
        }
    }

    public List<Vuelo> listarTodos() throws SQLException {
        String sql = "SELECT num_flight, destination, departure, duration FROM flight ORDER BY departure";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return crearListaVuelos(resultSet);
        }
    }

    public List<Vuelo> filtrarDuracionMayor180() throws SQLException {
        String sql = "SELECT num_flight, destination, departure, duration "
                + "FROM flight "
                + "WHERE duration > 180 "
                + "ORDER BY departure";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return crearListaVuelos(resultSet);
        }
    }

    public List<Vuelo> filtrarPorDestino(String destino) throws SQLException {
        String sql = "SELECT num_flight, destination, departure, duration "
                + "FROM flight "
                + "WHERE destination = ? "
                + "ORDER BY departure";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, destino);

            try (ResultSet resultSet = statement.executeQuery()) {
                return crearListaVuelos(resultSet);
            }
        }
    }

    private List<Vuelo> crearListaVuelos(ResultSet resultSet) throws SQLException {
        List<Vuelo> vuelos = new ArrayList<>();

        while (resultSet.next()) {
            Vuelo vuelo = new Vuelo(
                    resultSet.getString("num_flight"),
                    resultSet.getString("destination"),
                    resultSet.getDate("departure").toLocalDate(),
                    resultSet.getInt("duration")
            );
            vuelos.add(vuelo);
        }

        return vuelos;
    }
}
