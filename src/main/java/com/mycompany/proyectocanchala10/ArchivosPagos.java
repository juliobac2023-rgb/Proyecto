/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;

/**
 *
 * @author julio
 */
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;

public class ArchivosPagos {

    private String nombreArchivo = "datos/pagos.csv";

    private String limpiar(String texto) {
        return texto.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }

    public void guardar(gestorReserva gestor) {
        try {
            FileWriter archivo = new FileWriter(nombreArchivo, false);
            BufferedWriter bw = new BufferedWriter(archivo);

            for (int i = 0; i < gestor.getCantidad(); i++) {
                Reserva r = gestor.getReservas()[i];
                for (int j = 0; j < r.getCantidadPagos(); j++) {
                    Pago p = r.getPagos()[j];
                    bw.write(r.getCodigo() + "," + p.getCodigo() + "," + p.getFecha()
                            + "," + p.getMonto() + "," + limpiar(p.getMetodo())
                            + "," + limpiar(p.getObservacion()));
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error guardando pagos: " + e.getMessage());
        }
    }

    public void cargar(gestorReserva gestor) {
        if (!new File(nombreArchivo).exists()) return;

        try {
            FileReader archivo = new FileReader(nombreArchivo);
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            while (linea != null) {
                try {
                    String[] d = linea.split(",", -1);
                    if (d.length == 6) {
                        int codigo = Integer.parseInt(d[1].trim());
                        Reserva r = gestor.buscar(Integer.parseInt(d[0].trim()));
                        if (r != null) {
                            r.cargarPago(new Pago(codigo,
                                    LocalDateTime.parse(d[2].trim()),
                                    Double.parseDouble(d[3].trim()),
                                    d[4].trim(), d[5].trim()));
                        }
                        gestor.actualizarCodigoPago(codigo);
                    } else System.out.println("Pago omitido: columnas incorrectas");
                } catch (Exception e) {
                    System.out.println("Pago omitido: " + e.getMessage());
                }
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error leyendo pagos: " + e.getMessage());
        }
    }
}