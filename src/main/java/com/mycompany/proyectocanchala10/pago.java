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
public class pago {
  private int Codigo;
  private LocalDateTime fecha;
  private double Monto;
  private String Metodo;
  private String Observacion;
  
  public pago(int Codigo, LocalDateTime fecha, double Monto, String Metodo, String Observacion){
      
      this.Codigo = Codigo;
      this.fecha = fecha;
      this.Monto = Monto;
      this.Metodo = Metodo;
      this.Observacion = Observacion;
  }
  public int getCodigo(){
      return Codigo;
  }
  public LocalDateTime getfecha (){
      return fecha;
  }
  public double getMonto(){
      return Monto;
  }
  public String Metodo(){
      return Metodo;
  }
  public String getObservacion(){
      return Observacion;
  }
  
  
                              
    }
         
    
    
    

