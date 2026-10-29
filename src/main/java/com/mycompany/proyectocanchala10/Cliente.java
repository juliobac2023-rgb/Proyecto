/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;
import java.time.LocalDate;

// Guarda los datos de un cliente.
public class Cliente {

    private int codigo;
    private String nombre;
    private String telefono;
    private String correo;
    private LocalDate fechaRegistro;

    public Cliente (int codigo, String nombre,
            String telefono, String correo,
            LocalDate fechaRegistro) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.fechaRegistro = fechaRegistro;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void editar(String nombre, String telefono,
            String correo) {

        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }
}
