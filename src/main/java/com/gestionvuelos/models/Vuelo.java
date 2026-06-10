package com.gestionvuelos.models;

import java.time.LocalDate;

public class Vuelo {

    private String numeroVuelo;
    private String destino;
    private LocalDate fechaSalida;
    private int duracion;

    public Vuelo() {
    }

    public Vuelo(String numeroVuelo, String destino, LocalDate fechaSalida, int duracion) {
        this.numeroVuelo = numeroVuelo;
        this.destino = destino;
        this.fechaSalida = fechaSalida;
        this.duracion = duracion;
    }

    public String getNumeroVuelo() {
        return numeroVuelo;
    }

    public void setNumeroVuelo(String numeroVuelo) {
        this.numeroVuelo = numeroVuelo;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}
