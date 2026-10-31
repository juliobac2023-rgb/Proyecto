package com.mycompany.proyectocanchala10;

import java.time.LocalDateTime;

// Pago individual: una reserva puede tener varios.
public class Pago {
    private int codigo;
    private LocalDateTime fecha;
    private double monto;
    private String metodo;
    private String observacion;

    public Pago(int codigo, LocalDateTime fecha, double monto, String metodo, String observacion) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.monto = monto;
        this.metodo = metodo;
        this.observacion = observacion;
    }
    public int getCodigo() { return codigo; }
    public LocalDateTime getFecha() { return fecha; }
    public double getMonto() { return monto; }
    public String getMetodo() { return metodo; }
    public String getObservacion() { return observacion; }
}
