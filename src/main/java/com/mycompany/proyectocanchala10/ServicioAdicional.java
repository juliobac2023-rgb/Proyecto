/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;
/**
 *
 * @author julio
 */
public class ServicioAdicional {
 
    private String Nombre;
    private double Precio;
    
    public ServicioAdicional ( String Nombre, double Precio){
        this.Precio = Precio;
        this.Nombre = Nombre;
    }
    public String getNombre(){
        return Nombre;
    }
    public double getPrecio(){
        return Precio;
    }
}
