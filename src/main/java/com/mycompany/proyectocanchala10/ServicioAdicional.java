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
    private String Codigo;
    private String Nombre;
    private double Precio;
    
    public ServicioAdicional (String Codigo, String Nombre, double Precio){
        if (Precio < 0){
            throw new IllegalArgumentException ( "Preicio Negativo");
        }
        this.Codigo = Codigo;
        this.Nombre = Nombre;
        this.Precio = Precio;
    }
    public String Codigo(){
        return Codigo;
    }
    public String Nombre(){
        return Nombre;
    }
    public double Precio(){
        return Precio;
    }
    public void setPrecio (double precio){
        if (precio < 0){
            throw new IllegalArgumentException("Precio Negativo");
        }
        this.Precio = Precio;
    }
            
}
