package com.example.solicitudesfup;

import java.io.Serializable;

/**
 * Created by Luis Hurtado on 02/06/2016.
 */
public class Estudiante implements Serializable{
    int cod;
    int cedula;
    String nombres;
    String apeliidos;
    String programa;
    int semestre;
    String estado;

    public Estudiante(int cod, int cedula, String nombres, String apeliidos, String programa, int semestre, String estado) {
        this.cod = cod;
        this.cedula = cedula;
        this.nombres = nombres;
        this.apeliidos = apeliidos;
        this.programa = programa;
        this.semestre = semestre;
        this.estado = estado;
    }

    public Estudiante() {
        this.cod = 0;
        this.cedula = 0;
        this.nombres = "";
        this.apeliidos = "";
        this.programa = "";
        this.semestre = 0;
        this.estado = "";
    }

    public int getCod(){return cod;}
    public void setCod(int cod) {this.cod=cod;}
    public int getCedula(){return cedula;}
    public void setCedula(int cedula){this.cedula=cedula;}
    public String getNombres(){return nombres;}
    public void setNombres(String nombres){this.nombres=nombres;}
    public String getApeliidos(){return apeliidos;}
    public void setApeliidos(String apeliidos){this.apeliidos=apeliidos;}
    public String getPrograma(){return programa;}
    public void setPrograma(String programa){this.programa=programa;}
    public int getSemestre(){return semestre;}
    public void setSemestre(int semestre){this.semestre=semestre;}
    public String getEstado(){return estado;}
    public void setEstado(String estado){this.estado=estado;}
}
