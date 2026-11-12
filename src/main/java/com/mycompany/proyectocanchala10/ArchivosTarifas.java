package com.mycompany.proyectocanchala10;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

// Guarda y lee las tarifas de referencia en datos/tarifas.csv.
// Formato: nombre de la tarifa, precio por hora.
public class ArchivosTarifas {

    private String nombreArchivo = "datos/tarifas.csv";

    public void guardar() {

        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(nombreArchivo, false))) {

            bw.write("LUNES_VIERNES_DIA," + Tarifa.getPrecioDia());
            bw.newLine();
            bw.write("LUNES_VIERNES_NOCHE," + Tarifa.getPrecioNoche());
            bw.newLine();
            bw.write("FIN_DE_SEMANA," + Tarifa.getPrecioFinSemana());
            bw.newLine();

        } catch (IOException e) {
            System.out.println("Error guardando tarifas: " + e.getMessage());
        }
    }

    public void cargar() {

        // Si no hay archivo se usan las tarifas por defecto (150, 225 y 250).
        if (!new File(nombreArchivo).exists()) {
            return;
        }

        double dia = Tarifa.getPrecioDia();
        double noche = Tarifa.getPrecioNoche();
        double finSemana = Tarifa.getPrecioFinSemana();

        try (BufferedReader br = new BufferedReader(
                new FileReader(nombreArchivo))) {

            String linea = br.readLine();

            while (linea != null) {

                try {
                    String[] d = linea.split(",", -1);

                    if (d.length == 2) {

                        double precio = Double.parseDouble(d[1].trim());

                        if (d[0].trim().equals("LUNES_VIERNES_DIA")) {
                            dia = precio;
                        } else if (d[0].trim().equals("LUNES_VIERNES_NOCHE")) {
                            noche = precio;
                        } else if (d[0].trim().equals("FIN_DE_SEMANA")) {
                            finSemana = precio;
                        }

                    } else {
                        System.out.println("Tarifa omitida: columnas incorrectas");
                    }

                } catch (Exception e) {
                    System.out.println("Tarifa omitida: " + e.getMessage());
                }

                linea = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("Error leyendo tarifas: " + e.getMessage());
        }

        if (!Tarifa.configurar(dia, noche, finSemana)) {
            System.out.println("Tarifas invalidas en el archivo: se usan las de defecto.");
        }
    }
}
