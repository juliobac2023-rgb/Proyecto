package com.mycompany.proyectocanchala10;

// Una linea de una venta: un producto, cuantas unidades y a que precio.
// Se guarda el nombre y el precio del momento de la venta, asi el
// historial no cambia si despues se modifica el producto.
public class DetalleVenta {

    private String codigoProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;

    public DetalleVenta(String codigoProducto, String nombreProducto,
            int cantidad, double precioUnitario) {

        this.codigoProducto = codigoProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
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

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }
}
