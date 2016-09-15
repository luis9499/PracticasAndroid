package com.example.solicitudesfup;

import java.io.Serializable;

/**
 * Created by Luis Hurtado on 02/06/2016.
 */
public class Notas implements Serializable {

    int cod_asig;
    int cod;
    String asignatura;
    double nota1;
    double nota2;
    double nota3;
    double definitiva;

    public Notas(int cod_asig, int cod, String asignatura, double nota1, double nota2, double nota3, double definitiva) {
        this.cod_asig = cod_asig;
        this.cod = cod;
        this.asignatura = asignatura;
        this.nota1 = nota1;
        this.nota2 = nota2;
        this.nota3 = nota3;
        this.definitiva = definitiva;
    }

    public Notas() {
        this.cod_asig = 0;
        this.cod = 0;
        this.asignatura = "";
        this.nota1 = 0.0;
        this.nota2 = 0.0;
        this.nota3 = 0.0;
        this.definitiva = 0.0;
    }

    public int getCod_asig() {return cod_asig;}
    public void setCod_asig(int cod_asig) {this.cod_asig = cod_asig;}

    public int getCod() {return cod;}
    public void setCod(int cod) {this.cod = cod;}
    public String getAsignatura() {return asignatura;}
    public void setAsignatura(String asignatura) {this.asignatura = asignatura;}
    public double getNota1(double v) {return nota1;}
    public void setNota1(double nota1) {this.nota1 = nota1;}
    public double getNota2(double v) {return nota2;}
    public void setNota2(double nota2) {this.nota2 = nota2;}
    public double getNota3(double v) {return nota3;}
    public void setNota3(double nota3) {this.nota3 = nota3;}
    public double getDefinitiva(double v) {return definitiva;}
    public void setDefinitiva(double definitiva) {this.definitiva = definitiva;}


    public int getNota1() {
        return 0;
    }

    public int getNota2() {
        return 0;
    }

    public int getNota3() {
        return 0;
    }

    public int getDefinitiva() {
        return 0;
    }
}
