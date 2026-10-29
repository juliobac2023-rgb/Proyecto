/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;


import java.time.LocalTime;

public class Cancha {

    private String codigo;
    private String nombre;
    private String tipo;
    private int capacidad;
    private double precioBase;
    private String estado;
    private LocalTime horaApertura;
    private LocalTime horaCierre;

    public Cancha(String codigo, String nombre,String tipo, int capacidad,double precioBase, String estado,LocalTime horaApertura, LocalTime horaCierre) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.estado = estado;
        this.horaApertura = horaApertura;
        this.horaCierre = horaCierre;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public String getEstado() {
        return estado;
    }

    public LocalTime getHoraApertura() {
        return horaApertura;
    }

    public LocalTime getHoraCierre() {
        return horaCierre;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

    
            


