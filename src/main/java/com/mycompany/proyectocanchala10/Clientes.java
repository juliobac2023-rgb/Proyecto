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
    
    private final String Codigo;
    private final String Nombre;
    private final String Telefono;
    private final String Correo;
    private final LocalDate fechaRegistro;
    
    public Clientes (String Codigo, String Nombre, String Telefono, String Correo){
        this.Codigo = Codigo;
        this.Nombre = Nombre;
        this.Telefono = Telefono;
        this.Correo = Correo;
        this.fechaRegistro = LocalDate.now();
    }
    
    public String getCodigo() {
        return Codigo;
    }
    public String getNombre(){
        return Nombre;
    }
    public String getTelefono(){
        return Telefono;
    }
    public String getCorreo (){
        return Correo;
    }
    public LocalDate getfechaRegistro(){
        return fechaRegistro;
    }
    
}
