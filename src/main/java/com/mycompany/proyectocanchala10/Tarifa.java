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
public class Tarifa {
    
    private String Codigo;
    private String Descripcion;
    private LocalTime HoraInicio;
    private LocalTime HoraFin;
    private double PrecioHora;
    
    public Tarifa (String Codigo, String Descripcion, LocalTime HoraInicio, LocalTime HoraFin, double PrecioHora){
        if (PrecioHora < 0){
            throw new IllegalArgumentException("Precio Incorrecto");
        }
        this.Codigo = Codigo;
        this.Descripcion = Descripcion;
        this.HoraFin = HoraFin;
        this.HoraInicio = HoraInicio;
    }
    public String getCodigo(){
         return Codigo;
    }
    public String getDescripcion (){
        return Descripcion;
    }
    public LocalTime getHoraInicio(){
            return HoraInicio;
    }
    public LocalTime getHoraFin(){
        return HoraFin;
    }
    public double PrecioHora(){
        return PrecioHora;
    }
    public void setPrecioHora (double PrecioHora){
        if (PrecioHora < 0){
            throw new IllegalArgumentException("Precio Incorrecto");
        }
        this.PrecioHora = PrecioHora;
    }
}