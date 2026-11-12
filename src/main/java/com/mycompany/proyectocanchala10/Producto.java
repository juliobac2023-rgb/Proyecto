package com.mycompany.proyectocanchala10;

// Producto que se vende en la tienda de la cancha (agua, snacks, etc.).
public class Producto {

    private String codigo;
    private String nombre;
    private String categoria;
    private double precio;
    private int stock;        // existencia actual
    private int stockMinimo;  // con esta cantidad o menos se avisa "stock bajo"
    private String estado;    // ACTIVO o INACTIVO

    public Producto(String codigo, String nombre, String categoria,
            double precio, int stock, int stockMinimo, String estado) {

        this.codigo = codigo;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // Un producto INACTIVO sigue en el inventario pero no se puede vender.
    public boolean estaActivo() {
        return estado.equals("ACTIVO");
    }

    public boolean stockBajo() {
        return stock <= stockMinimo;
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
