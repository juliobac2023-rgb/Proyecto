package com.mycompany.proyectocanchala10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

// NUEVO: guarda y lee los productos y las ventas de la tienda.
// Mismo formato CSV que los demas archivos. Usa try-with-resources,
// asi el archivo se cierra solo aunque ocurra un error.
public class ArchivosTienda {

    private String archivoProductos = "datos/productos.csv";
    private String archivoVentas = "datos/ventas.csv";

    private String limpiar(String texto) {
        return texto.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }

    public void guardar(GestorTienda gestor) {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(archivoProductos, false))) {

            for (int i = 0; i < gestor.getCantidad(); i++) {

                Producto p = gestor.getProductos()[i];

                bw.write(limpiar(p.getCodigo()) + ","
                        + limpiar(p.getNombre()) + ","
                        + p.getPrecio() + "," + p.getStock());
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
                        + "," + limpiar(v.getCodigoProducto())
                        + "," + limpiar(v.getNombreProducto())
                        + "," + v.getCantidad() + "," + v.getTotal()
                        + "," + v.getCodigoReserva());
                bw.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error guardando ventas: " + e.getMessage());
        }
    }

    public void cargar(GestorTienda gestor) {

        // Si todavia no hay archivo de productos, se cargan los de ejemplo.
        if (!new File(archivoProductos).exists()) {
            gestor.cargarEjemplos();
        } else {
            cargarProductos(gestor);
        }

        if (new File(archivoVentas).exists()) {
            cargarVentas(gestor);
        }
    }

    private void cargarProductos(GestorTienda gestor) {

        try (BufferedReader br = new BufferedReader(
                new FileReader(archivoProductos))) {

            String linea = br.readLine();

            while (linea != null) {

                try {
                    String[] d = linea.split(",", -1);

                    if (d.length == 4) {
                        gestor.registrar(d[0].trim(), d[1].trim(),
                                Double.parseDouble(d[2].trim()),
                                Integer.parseInt(d[3].trim()));
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

                    if (d.length == 7) {
                        gestor.cargar(new Venta(
                                Integer.parseInt(d[0].trim()),
                                LocalDateTime.parse(d[1].trim()),
                                d[2].trim(), d[3].trim(),
                                Integer.parseInt(d[4].trim()),
                                Double.parseDouble(d[5].trim()),
                                Integer.parseInt(d[6].trim())));
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
}
