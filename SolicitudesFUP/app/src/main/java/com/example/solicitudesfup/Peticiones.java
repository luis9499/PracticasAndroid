package com.example.solicitudesfup;

import java.io.Serializable;

/**
 * Created by Luis Hurtado on 02/06/2016.
 */
public class Peticiones implements Serializable{

    int id_Peticion;
    int cod;
    String correo;
    String peticion;
    String estado;

    public Peticiones(int id_Peticion, int cod, String correo, String peticion, String estado) {
        this.id_Peticion = id_Peticion;
        this.cod = cod;
        this.correo = correo;
        this.peticion = peticion;
        this.estado = estado;
    }

    public Peticiones() {
        this.id_Peticion = 0;
        this.cod = 0;
        this.correo = "";
        this.peticion = "";
        this.estado = "";
    }

    public int getId_Peticion() {return id_Peticion;}
    public void setId_Peticion(int id_Peticion) {this.id_Peticion = id_Peticion;}
    public int getCod() {return cod;}
    public void setCod(int cod) {this.cod = cod;}
    public String getCorreo() {return correo;}
    public void setCorreo(String correo) {this.correo = correo;}
    public String getPeticion() {return peticion;}
    public void setPeticion(String peticion) {this.peticion = peticion;}
    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}
}
