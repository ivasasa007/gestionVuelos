package com.gestionvuelos.dao;

import com.gestionvuelos.database.ConexionBD;
import com.gestionvuelos.models.Vuelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VueloDAO {

    public void crearTabla() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS vuelo (
                    numero_vuelo TEXT PRIMARY KEY,
                    destino TEXT NOT NULL,
                    fecha_salida TEXT NOT NULL,
                    duracion INTEGER NOT NULL
                )
                """;

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.executeUpdate();
        }
    }

    public void insertar(Vuelo vuelo) throws SQLException {
        String sql = """
                INSERT INTO vuelo (numero_vuelo, destino, fecha_salida, duracion)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, vuelo.getNumeroVuelo());
            statement.setString(2, vuelo.getDestino());
            statement.setString(3, vuelo.getFechaSalida().toString());
            statement.setInt(4, vuelo.getDuracion());
            statement.executeUpdate();
        }
    }

    public void eliminar(String numeroVuelo) throws SQLException {
        String sql = "DELETE FROM vuelo WHERE numero_vuelo = ?";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setString(1, numeroVuelo);
            statement.executeUpdate();
        }
    }

    public List<Vuelo> listarTodos() throws SQLException {
        String sql = "SELECT numero_vuelo, destino, fecha_salida, duracion FROM vuelo";

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return crearListaVuelos(resultSet);
        }
    }

    public List<Vuelo> filtrarDuracionMayor180() throws SQLException {
        String sql = """
                SELECT numero_vuelo, destino, fecha_salida, duracion
                FROM vuelo
                WHERE duracion > 180
                """;

        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return crearListaVuelos(resultSet);
        }
    }

    public List<Vuelo> filtrarPorDestino(String destino) throws SQLException {
        String sql = """
                SELECT numero_vuelo, destino, fecha_salida, duracion
                FROM vuelo
                WHERE destino = ?
                """;

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
                    resultSet.getString("numero_vuelo"),
                    resultSet.getString("destino"),
                    LocalDate.parse(resultSet.getString("fecha_salida")),
                    resultSet.getInt("duracion")
            );
            vuelos.add(vuelo);
        }

        return vuelos;
    }
}
