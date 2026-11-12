package com.mycompany.proyectocanchala10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Configuracion del sistema. Se guarda en datos/configuracion.txt con el
// formato clave=valor. Ejemplo:  modulo_tienda=true
// Asi el modulo de Tienda se activa o desactiva SIN recompilar: basta con
// cambiar el archivo (o usar la pantalla de Configuracion).
public class Configuracion {

    private String nombreArchivo = "datos/configuracion.txt";

    // El modulo de tienda viene activado por defecto.
    private boolean moduloTienda = true;

    public boolean isModuloTienda() {
        return moduloTienda;
    }

    public void setModuloTienda(boolean moduloTienda) {
        this.moduloTienda = moduloTienda;
    }

    public void cargar() {

        // Si el archivo no existe se crea con los valores por defecto.
        if (!new File(nombreArchivo).exists()) {
            guardar();
            return;
        }

        try (BufferedReader br = new BufferedReader(
                new FileReader(nombreArchivo))) {

            String linea = br.readLine();

            while (linea != null) {

                String[] d = linea.split("=", 2);

                if (d.length == 2
                        && d[0].trim().equalsIgnoreCase("modulo_tienda")) {

                    moduloTienda = d[1].trim().equalsIgnoreCase("true");
                }

                linea = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("Error leyendo configuracion: " + e.getMessage());
        }
    }

    public void guardar() {

        new File("datos").mkdirs();

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(nombreArchivo, false))) {

            bw.write("modulo_tienda=" + moduloTienda);
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error guardando configuracion: " + e.getMessage());
        }
    }
}
