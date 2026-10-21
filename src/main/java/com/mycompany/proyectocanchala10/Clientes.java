/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;
import java.time.LocalDate;
/**
 *
 * @author julio
 */
public class Clientes {
    
    private  int Codigo;
    private String Nombre;
    private int Telefono;
    private  String Correo;
    private  LocalDate fecha;
    
public Clientes (int Codigo, String Nombre, int Telefono, String Correo, LocalDate fecha){
        
    this.Codigo = Codigo;
    this.Nombre = Nombre;
    this.Telefono = Telefono;
    this.Correo = Correo;
    this.fecha = fecha;
}
public int getCodigo(){
    return Codigo;
}
public String getNombre(){
    return Nombre;
}
public int getTelefono(){
    return Telefono;
}
public String Correo(){
    return Correo;
}
public LocalDate getfecha(){
    return fecha;
}
public void Editar (String Nombre, int Telefono, String Correo){
    this.Nombre = Nombre;
    this.Telefono = Telefono;
    this.Correo = Correo;
}

}


    
    