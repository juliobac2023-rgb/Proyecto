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
import java.time.LocalTime;

public class ArchivosCanchas {

    private String nombreArchivo = "datos/canchas.csv";

    private String limpiar(String texto) {
        return texto.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }

    public void guardar(GestorCanchas gestor) {
        try {
            FileWriter archivo = new FileWriter(nombreArchivo, false);
            BufferedWriter bw = new BufferedWriter(archivo);

            for (int i = 0; i < gestor.getCantidad(); i++) {
                Cancha c = gestor.getCanchas()[i];
                bw.write(limpiar(c.getCodigo()) + "," + limpiar(c.getNombre())
                        + "," + limpiar(c.getTipo()) + "," + c.getCapacidad()
                        + "," + c.getPrecioBase() + "," + c.getEstado()
                        + "," + c.getHoraApertura() + "," + c.getHoraCierre());
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            System.out.println("Error guardando canchas: " + e.getMessage());
        }
    }

    public void cargar(GestorCanchas gestor) {
        if (!new File(nombreArchivo).exists()) {
            gestor.cargarEjemplos();
            return;
        }

        try {
            FileReader archivo = new FileReader(nombreArchivo);
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            while (linea != null) {
                try {
                    String[] d = linea.split(",", -1);
                    if (d.length == 8) {
                        gestor.registrar(d[0].trim(), d[1].trim(), d[2].trim(),
                                Integer.parseInt(d[3].trim()),
                                Double.parseDouble(d[4].trim()), d[5].trim(),
                                LocalTime.parse(d[6].trim()),
                                LocalTime.parse(d[7].trim()));
                    } else System.out.println("Cancha omitida: columnas incorrectas");
                } catch (Exception e) {
                    System.out.println("Cancha omitida: " + e.getMessage());
                }
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error leyendo canchas: " + e.getMessage());
        }
    }
}
