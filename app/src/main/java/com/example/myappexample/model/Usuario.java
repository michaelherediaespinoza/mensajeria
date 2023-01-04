package com.example.myappexample.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Usuario{

    @SerializedName("fecha")
    private String fecha;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("mesaje")
    private String mesaje;


    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMesaje() {
        return mesaje;
    }

    public void setMesaje(String mesaje) {
        this.mesaje = mesaje;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "fecha='" + fecha + '\'' +
                ", nombre='" + nombre + '\'' +
                ", mesaje='" + mesaje + '\'' +
                '}';
    }
}
