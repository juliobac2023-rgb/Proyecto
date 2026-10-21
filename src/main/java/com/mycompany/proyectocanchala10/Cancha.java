/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;
import java.time.LocalTime;
/**
 *
 * @author julio
 */
public class Cancha {
    private String Codigo;
    private String Nombre;
    private String Tipo;
    private int Capacidad;
    private double Preciobase;
    private String Estado;
    private LocalTime Apertura;
    private LocalTime Cirre;
    
    public Cancha(String Codigo, String Nombre, String Tipo, int Capacidad, double Preciobase, String Estado, LocalTime Apertura, LocalTime Cirre){
        
        this.Codigo = Codigo;
        this.Nombre = Nombre;
        this.Tipo = Tipo;
        this.Capacidad = Capacidad;
        this.Preciobase = Preciobase;
        this.Estado = Estado;
        this.Apertura = Apertura;
        this.Cirre = Cirre;  
    }
    public String getCodigo(){
        return Codigo;
    }
    public String getNombre(){
        return Nombre;
    }
    public String getTipo(){
        return Tipo;
    }
    public int getCapacidad(){
        return Capacidad;
    }
    public double Preciobase(){
        return Preciobase;
    }
    public String getEstado(){
        return Estado;
    }
    public LocalTime getApertura(){
        return Apertura;
    }
    public LocalTime getCierre(){
        return Cirre;
    }
    public void setEstado (String Estado){
        this.Estado = Estado;
    }
    
            

}   
