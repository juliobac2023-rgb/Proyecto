/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;
import com.canchapro.enums.*;
import java.time.LocalTime;
/**
 *
 * @author julio
 */
public class Cancha {
    private final String Codigo;
    private String Nombre;
    private final TipoDeCancha tipo;
    private final int Capacidad;
    private double PrecioBase;
    private EstadoCancha Estado;
    private final LocalTime Apertura;
    private final LocalTime Cierre;
    
    public Cancha(String Codigo, String Nombre, TipoDeCancha tipo, int Capacidad,double PrecioBase, LocalTime Apertura, LocalTime Cierre){
        
        this.Codigo = Codigo;
        this.Nombre = Nombre;
        this.tipo = tipo;
        this.Capacidad = Capacidad;
        this.PrecioBase = PrecioBase;
        this.Apertura = Apertura;
        this.Cierre = Cierre;
        
        this.Estado = EstadoCancha.DISPONIBLE;            
    }
    public boolean EstaActiva (){
        return Estado == EstadoCancha.DISPONIBLE;
    }
    public boolean DentroDelHorario (LocalTime inicio, LocalTime fin){
        return !inicio.isBefore(Apertura) && !fin.isAfter(Cierre) && fin.isAfter(inicio);
    }
    
    public String getCodigo() {
        return Codigo;
    }
    public String getNombre() {
        return Nombre;
    }
    public TipoDeCancha gettipo (){
        return tipo;
    }
    public int getCapacidad(){
        return Capacidad;
    }
    public double getPrecioBase(){
        return PrecioBase;
    }
    public EstadoCancha getEstado(){
        return Estado;
    }
    public LocalTime getApertura(){
        return Apertura;
    }
    public LocalTime getCierre(){
        return Cierre;
    }
    
    public void setEstado (EstadoCancha Estado){
        this.Estado = Estado;
    }
    public void setNombre (String Nombre){
        this.Nombre = Nombre;
    }
    
    public void setPrecioBase (double PrecioBase){
        if (PrecioBase < 0) {
            throw new IllegalArgumentException("Precio Incorrecto");
        }
        this.PrecioBase = PrecioBase;
    }
    @Override
    public String toString(){
        return Codigo + "-" + Nombre;
    }
}   
