package com.example.hamiltonurbano.testconnectionazure.models;

/**
 * Created by Hamilton Urbano on 26/11/2015.
 */
public class Estudiante {
    String nombre, password;
    String id;

    public Estudiante() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String pass) {
        this.password = pass;
    }
}
