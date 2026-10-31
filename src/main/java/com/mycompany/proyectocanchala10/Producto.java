package com.mycompany.proyectocanchala10;

// NUEVO: producto que se vende en la tienda de la cancha (agua, snacks, etc.).
public class Producto {

    private String codigo;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(String codigo, String nombre,
            double precio, int stock) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    // Resta unidades del inventario. Falla si no alcanza el stock.
    public boolean descontar(int unidades) {

        if (unidades <= 0 || unidades > stock) {
            return false;
        }

        stock -= unidades;

        return true;
    }

    // Suma unidades al inventario.
    public boolean reponer(int unidades) {

        if (unidades <= 0) {
            return false;
        }

        stock += unidades;

        return true;
    }
}
