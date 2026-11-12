package com.mycompany.proyectocanchala10;

import java.time.LocalDateTime;

// Una venta de la tienda. Puede tener uno o varios productos: cada uno
// es un DetalleVenta (composicion: los detalles solo existen dentro de
// su venta). El total no se guarda: se calcula sumando los detalles.
// Si codigoReserva es 0 fue una venta suelta (se paga en caja);
// si no, se cargo a esa reserva y se cobra junto con ella.
public class Venta {

    private int codigo;
    private LocalDateTime fecha;
    private int codigoReserva;
    private String metodo;

    private DetalleVenta[] detalles = new DetalleVenta[20];
    private int cantidadDetalles = 0;

    public Venta(int codigo, LocalDateTime fecha,
            int codigoReserva, String metodo) {

        this.codigo = codigo;
        this.fecha = fecha;
        this.codigoReserva = codigoReserva;
        this.metodo = metodo;
    }

    public boolean agregarDetalle(DetalleVenta detalle) {

        if (cantidadDetalles == detalles.length) {
            return false;
        }

        detalles[cantidadDetalles] = detalle;
        cantidadDetalles++;

        return true;
    }

    // Quita todos los productos (para empezar la venta de nuevo).
    public void vaciar() {
        cantidadDetalles = 0;
    }

    // Se llama cuando la venta se cobra o se carga a una reserva.
    // En ese momento recibe su numero definitivo.
    public void confirmar(int codigo, LocalDateTime fecha,
            int codigoReserva, String metodo) {
        this.codigo = codigo;
        this.fecha = fecha;
        this.codigoReserva = codigoReserva;
        this.metodo = metodo;
    }

    // Unidades de un producto que ya estan en esta venta.
    public int unidadesDe(String codigoProducto) {

        int unidades = 0;

        for (int i = 0; i < cantidadDetalles; i++) {

            if (detalles[i].getCodigoProducto().equalsIgnoreCase(codigoProducto)) {
                unidades += detalles[i].getCantidad();
            }
        }

        return unidades;
    }

    public int getUnidades() {

        int unidades = 0;

        for (int i = 0; i < cantidadDetalles; i++) {
            unidades += detalles[i].getCantidad();
        }

        return unidades;
    }

    public double getTotal() {

        double total = 0;

        for (int i = 0; i < cantidadDetalles; i++) {
            total += detalles[i].getSubtotal();
        }

        return total;
    }

    // Texto corto con los productos: "Agua pura x2, Snack x1".
    public String resumen() {

        String texto = "";

        for (int i = 0; i < cantidadDetalles; i++) {

            if (i > 0) {
                texto += ", ";
            }

            texto += detalles[i].getNombreProducto()
                    + " x" + detalles[i].getCantidad();
        }

        return texto;
    }

    public int getCodigo() {
        return codigo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public int getCodigoReserva() {
        return codigoReserva;
    }

    public String getMetodo() {
        return metodo;
    }

    public DetalleVenta[] getDetalles() {
        return detalles;
    }

    public int getCantidadDetalles() {
        return cantidadDetalles;
    }
}
