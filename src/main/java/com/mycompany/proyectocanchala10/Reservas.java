/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;

import java.time.LocalDateTime;

/**
 *
 * @author julio
 */
public class Reservas {
    private int Codigo;
    private LocalDateTime Fecha;
    private Clientes cliente;
    private Cancha Cancha;
    private LocalTime Inicio;
    private LocalTime Fin;
    private int horas;
    private double Total;
    private String Estado;
    
    private LocalDate fechaCancelacion;
    private String motivoCancelacion = "";
    private String OperadorCancelacion = "";
    
    private ServicioAdicional[] servicios = new ServicioAdicional[10];
    
}
    

