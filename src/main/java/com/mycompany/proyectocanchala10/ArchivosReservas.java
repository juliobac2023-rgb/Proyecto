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
import java.time.LocalDate;
import java.time.LocalTime;

public class ArchivosReservas {

    private String nombreArchivo = "datos/reservas.csv";

    private String limpiar(String texto) {
        return texto.replace(",", " ").replace("\n", " ").replace("\r", " ");
    }

    public void guardar(gestorReserva gestor) {
        try {
            FileWriter archivo = new FileWriter(nombreArchivo, false);
            BufferedWriter bw = new BufferedWriter(archivo);

            for (int i = 0; i < gestor.getCantidad(); i++) {
                Reserva r = gestor.getReservas()[i];
                String cancelacion = r.getFechaCancelacion() == null
                        ? "" : r.getFechaCancelacion().toString();

                bw.write(r.getCodigo() + "," + r.getFecha() + ","
                        + r.getCliente().getCodigo() + "," + r.getCancha().getCodigo()
                        + "," + r.getInicio() + "," + r.getHoras()
                        + "," + r.getTotal() + "," + r.getEstado()
                        + "," + cancelacion + "," + limpiar(r.getMotivoCancelacion())
                        + "," + limpiar(r.getOperadorCancelacion()));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("Error guardando reservas: " + e.getMessage());
        }
    }

    public void cargar(gestorReserva gestor, gestorClientes clientes,
            GestorCanchas canchas) {
        if (!new File(nombreArchivo).exists()) return;

        try {
            FileReader archivo = new FileReader(nombreArchivo);
            BufferedReader br = new BufferedReader(archivo);
            String linea = br.readLine();

            while (linea != null) {
                try {
                    String[] d = linea.split(",", -1);
                    if (d.length == 11) {
                        Cliente cliente = clientes.buscarCodigo(Integer.parseInt(d[2].trim()));
                        Cancha cancha = canchas.buscar(d[3].trim());
                        if (cliente != null && cancha != null) {
                            Reserva r = new Reserva(Integer.parseInt(d[0].trim()),
                                    LocalDate.parse(d[1].trim()), cliente, cancha,
                                    LocalTime.parse(d[4].trim()), Integer.parseInt(d[5].trim()),
                                    Double.parseDouble(d[6].trim()), d[7].trim());

                            if (!d[8].trim().isEmpty()) {
                                r.cargarCancelacion(LocalDate.parse(d[8].trim()), d[9].trim(),
                                        d[10].trim());
                            }
                            gestor.cargar(r);
                        } else System.out.println("Reserva omitida: cliente o cancha no existe");
                    } else System.out.println("Reserva omitida: columnas incorrectas");
                } catch (Exception e) {
                    System.out.println("Reserva omitida: " + e.getMessage());
                }
                linea = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error leyendo reservas: " + e.getMessage());
        }
    }
}

