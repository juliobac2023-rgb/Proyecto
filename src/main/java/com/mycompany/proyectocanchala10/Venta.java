package com.mycompany.proyectocanchala10;

import java.time.LocalDateTime;

// NUEVO: una venta de la tienda.
// Si codigoReserva es 0 fue una venta suelta (se paga en caja);
// si no, se cargo a esa reserva y se cobra junto con ella.
public class Venta {

    private int codigo;
    private LocalDateTime fecha;
    private String codigoProducto;
    private String nombreProducto;
    private int cantidad;
    private double total;
    private int codigoReserva;

    public Venta(int codigo, LocalDateTime fecha,
            String codigoProducto, String nombreProducto,
            int cantidad, double total, int codigoReserva) {

        this.codigo = codigo;
        this.fecha = fecha;
        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.total = total;
        this.codigoReserva = codigoReserva;
    }

    public int getCodigo() {
        return codigo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getTotal() {
        return total;
    }

    public int getCodigoReserva() {
        return codigoReserva;
    }
}
