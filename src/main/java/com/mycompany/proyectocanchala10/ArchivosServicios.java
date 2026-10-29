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

public class ArchivosServicios {

    private String nombreArchivo = "datos/servicios.csv";

    private String limpiar(String texto) {
        return texto.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }

    public void guardar(gestorReserva gestor) {
        try {
            FileWriter archivo = new FileWriter(nombreArchivo, false);
            BufferedWriter bw = new BufferedWriter(archivo);

            for (int i = 0; i < gestor.getCantidad(); i++) {
                Reserva r = gestor.getReservas()[i];
                for (int j = 0; j < r.getCantidadServicios(); j++) {
                    ServicioAdicional s = r.getServicios()[j];
                    bw.write(r.getCodigo() + "," + limpiar(s.getNombre()) + "," + s.getPrecio());
                    bw.newLine();
                }
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error guardando servicios: " + e.getMessage());
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
                    if (d.length == 3) {
                        Reserva r = gestor.buscar(Integer.parseInt(d[0].trim()));
                        if (r != null) {
                            r.cargarServicio(new ServicioAdicional(d[1].trim(),
                                    Double.parseDouble(d[2].trim())));
                        }
                    } else System.out.println("Servicio omitido: columnas incorrectas");
                } catch (Exception e) {
                    System.out.println("Servicio omitido: " + e.getMessage());
                }
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error leyendo servicios: " + e.getMessage());
        }
    }
}

