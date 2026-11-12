package com.mycompany.proyectocanchala10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

// Guarda y lee los archivos de la tienda:
//   productos.csv       codigo, nombre, categoria, precio, existencia, stock minimo, estado
//   ventas.csv          numero, fecha, reserva (0 = venta suelta), total, metodo
//   detalle_ventas.csv  numero de venta, codigo producto, nombre, cantidad, precio unitario, subtotal
// Usa try-with-resources: el archivo se cierra solo aunque ocurra un error.
public class ArchivosTienda {

    private String archivoProductos = "datos/productos.csv";
    private String archivoVentas = "datos/ventas.csv";
    private String archivoDetalles = "datos/detalle_ventas.csv";

    private String limpiar(String texto) {
        return texto.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }

    // GUARDAR

    public void guardar(GestorTienda gestor) {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(archivoProductos, false))) {

            for (int i = 0; i < gestor.getCantidad(); i++) {

                Producto p = gestor.getProductos()[i];

                bw.write(limpiar(p.getCodigo()) + ","
                        + limpiar(p.getNombre()) + ","
                        + limpiar(p.getCategoria()) + ","
                        + p.getPrecio() + "," + p.getStock() + ","
                        + p.getStockMinimo() + "," + p.getEstado());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error guardando productos: " + e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(archivoVentas, false))) {

            for (int i = 0; i < gestor.getCantidadVentas(); i++) {

                Venta v = gestor.getVentas()[i];

                bw.write(v.getCodigo() + "," + v.getFecha()
                        + "," + v.getCodigoReserva()
                        + "," + v.getTotal()
                        + "," + limpiar(v.getMetodo()));
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error guardando ventas: " + e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(archivoDetalles, false))) {

            for (int i = 0; i < gestor.getCantidadVentas(); i++) {

                Venta v = gestor.getVentas()[i];

                for (int j = 0; j < v.getCantidadDetalles(); j++) {

                    DetalleVenta d = v.getDetalles()[j];

                    bw.write(v.getCodigo()
                            + "," + limpiar(d.getCodigoProducto())
                            + "," + limpiar(d.getNombreProducto())
                            + "," + d.getCantidad()
                            + "," + d.getPrecioUnitario()
                            + "," + d.getSubtotal());
                    bw.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Error guardando detalle de ventas: " + e.getMessage());
        }
    }

    // LEER

    public void cargar(GestorTienda gestor) {

        // Si todavia no hay archivo de productos, se cargan los de ejemplo.
        if (!new File(archivoProductos).exists()) {
            gestor.cargarEjemplos();
        } else {
            cargarProductos(gestor);
        }

        // Primero las ventas y despues sus detalles.
        if (new File(archivoVentas).exists()) {
            cargarVentas(gestor);
        }

        if (new File(archivoDetalles).exists()) {
            cargarDetalles(gestor);
        }
    }

    private void cargarProductos(GestorTienda gestor) {

        try (BufferedReader br = new BufferedReader(
                new FileReader(archivoProductos))) {

            String linea = br.readLine();

            while (linea != null) {

                try {
                    String[] d = linea.split(",", -1);

                    if (d.length == 7) {

                        if (!gestor.registrar(d[0].trim(), d[1].trim(), d[2].trim(),
                                Double.parseDouble(d[3].trim()),
                                Integer.parseInt(d[4].trim()),
                                Integer.parseInt(d[5].trim()),
                                d[6].trim())) {

                            System.out.println("Producto omitido: datos invalidos");
                        }

                    } else if (d.length == 4) {

                        // Formato anterior (codigo, nombre, precio, stock):
                        // se completa con categoria General y minimo 5.
                        gestor.registrar(d[0].trim(), d[1].trim(), "General",
                                Double.parseDouble(d[2].trim()),
                                Integer.parseInt(d[3].trim()), 5);

                    } else {
                        System.out.println("Producto omitido: columnas incorrectas");
                    }

                } catch (Exception e) {
                    System.out.println("Producto omitido: " + e.getMessage());
                }

                linea = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("Error leyendo productos: " + e.getMessage());
        }
    }

    private void cargarVentas(GestorTienda gestor) {

        try (BufferedReader br = new BufferedReader(
                new FileReader(archivoVentas))) {

            String linea = br.readLine();

            while (linea != null) {

                try {
                    String[] d = linea.split(",", -1);

                    if (d.length == 5) {

                        // El total (columna 3) no se lee: se vuelve a
                        // calcular con los detalles de la venta.
                        gestor.cargar(new Venta(
                                Integer.parseInt(d[0].trim()),
                                LocalDateTime.parse(d[1].trim()),
                                Integer.parseInt(d[2].trim()),
                                d[4].trim()));

                    } else if (d.length == 7) {

                        // Formato anterior: una venta de un solo producto
                        // (codigo, fecha, producto, nombre, cantidad, total, reserva).
                        int unidades = Integer.parseInt(d[4].trim());
                        int reserva = Integer.parseInt(d[6].trim());

                        Venta v = new Venta(
                                Integer.parseInt(d[0].trim()),
                                LocalDateTime.parse(d[1].trim()),
                                reserva,
                                reserva == 0 ? "EFECTIVO" : "CARGO_A_RESERVA");

                        v.agregarDetalle(new DetalleVenta(d[2].trim(), d[3].trim(),
                                unidades,
                                Double.parseDouble(d[5].trim()) / unidades));

                        gestor.cargar(v);

                    } else {
                        System.out.println("Venta omitida: columnas incorrectas");
                    }

                } catch (Exception e) {
                    System.out.println("Venta omitida: " + e.getMessage());
                }

                linea = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("Error leyendo ventas: " + e.getMessage());
        }
    }

    private void cargarDetalles(GestorTienda gestor) {

        try (BufferedReader br = new BufferedReader(
                new FileReader(archivoDetalles))) {

            String linea = br.readLine();

            while (linea != null) {

                try {
                    String[] d = linea.split(",", -1);

                    if (d.length == 6) {

                        Venta v = gestor.buscarVenta(Integer.parseInt(d[0].trim()));

                        if (v != null) {
                            v.agregarDetalle(new DetalleVenta(
                                    d[1].trim(), d[2].trim(),
                                    Integer.parseInt(d[3].trim()),
                                    Double.parseDouble(d[4].trim())));
                        } else {
                            System.out.println("Detalle omitido: la venta no existe");
                        }

                    } else {
                        System.out.println("Detalle omitido: columnas incorrectas");
                    }

                } catch (Exception e) {
                    System.out.println("Detalle omitido: " + e.getMessage());
                }

                linea = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("Error leyendo detalle de ventas: " + e.getMessage());
        }
    }
}
