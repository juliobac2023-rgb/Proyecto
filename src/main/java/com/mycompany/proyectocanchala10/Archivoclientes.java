/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;


import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;

public class Archivoclientes {

    private String nombreArchivo = "datos/clientes.csv";

    // Evita confundir las comas del texto con las columnas.
    private String limpiar(String texto) {
        return texto.replace(",", " ")
                .replace("\n", " ")
                .replace("\r", " ");
    }

    // GUARDAR CLIENTES

    public void guardar(gestorClientes gestor) {

        try {

            FileWriter archivo = new FileWriter(nombreArchivo, false);

            BufferedWriter bw = new BufferedWriter(archivo);

            for (int i = 0; i < gestor.getCantidad(); i++) {

                Cliente c = gestor.getClientes()[i];

                bw.write(
                        c.getCodigo() + ","
                        + limpiar(c.getNombre()) + ","
                        + limpiar(c.getTelefono()) + ","
                        + limpiar(c.getCorreo()) + ","
                        + c.getFechaRegistro()
                );

                bw.newLine();
            }

            bw.close();

        } catch (IOException e) {

            System.out.println(
                    "Error guardando clientes: " + e.getMessage()
            );
        }
    }

    // LEER CLIENTES

    public void cargar(gestorClientes gestor) {

        if (!new File(nombreArchivo).exists()) {
            return;
        }

        try {

            FileReader archivo = new FileReader(nombreArchivo);

            BufferedReader br = new BufferedReader(archivo);

            String linea = br.readLine();

            while (linea != null) {

                try {

                    String[] datos = linea.split(",", -1);

                    if (datos.length == 5) {

                        Cliente c = new Cliente(
                                Integer.parseInt(datos[0].trim()),
                                datos[1].trim(),
                                datos[2].trim(),
                                datos[3].trim(),
                                LocalDate.parse(datos[4].trim())
                        );

                        gestor.cargar(c);

                    } else {

                        System.out.println(
                                "Cliente omitido: columnas incorrectas"
                        );
                    }

                } catch (Exception e) {

                    System.out.println(
                            "Cliente omitido: " + e.getMessage()
                    );
                }

                linea = br.readLine();
            }

            br.close();

        } catch (IOException e) {

            System.out.println(
                    "Error leyendo clientes: " + e.getMessage()
            );
        }
    }
}
