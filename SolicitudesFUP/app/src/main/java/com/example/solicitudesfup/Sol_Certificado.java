package com.example.solicitudesfup;

import java.io.Serializable;

/**
 * Created by Luis Hurtado on 02/06/2016.
 */
public class Sol_Certificado implements Serializable {

    int id_Sol;
    int cod;
    String correo;
    String celular;

    public Sol_Certificado(int id_Sol, int cod, String correo, String celular) {
        this.id_Sol = id_Sol;
        this.cod = cod;
        this.correo = correo;
        this.celular = celular;
    }

    public Sol_Certificado() {
        this.id_Sol = 0;
        this.cod = 0;
        this.correo = "";
        this.celular = "";
    }

    public int getId_Sol() {return id_Sol;}
    public void setId_Sol(int id_Sol) {this.id_Sol = id_Sol;}
    public int getCod() {return cod;}
    public void setCod(int cod) {this.cod = cod;}
    public String getCorreo() {return correo;}
    public void setCorreo(String correo) {this.correo = correo;}
    public String getCelular() {return celular;}
    public void setCelular(String celular) {this.celular = celular;}
}
