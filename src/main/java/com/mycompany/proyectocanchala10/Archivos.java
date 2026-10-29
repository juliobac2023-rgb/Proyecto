/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;

import java.io.File;

import java.io.File;

import java.io.File;

public class Archivos {

    private Archivoclientes archivoClientes = new Archivoclientes();

    private ArchivosCanchas archivoCanchas = new ArchivosCanchas();

    private ArchivosReservas archivoReservas = new ArchivosReservas();

    private ArchivosServicios archivoServicios = new ArchivosServicios();

    private ArchivosPagos archivoPagos = new ArchivosPagos();

    public void guardar(gestorClientes clientes,
            GestorCanchas canchas, gestorReserva reservas) {

        new File("datos").mkdirs();

        archivoClientes.guardar(clientes);
        archivoCanchas.guardar(canchas);
        archivoReservas.guardar(reservas);
        archivoServicios.guardar(reservas);
        archivoPagos.guardar(reservas);
    }

    public void cargar(gestorClientes clientes,
            GestorCanchas canchas, gestorReserva reservas) {

        new File("datos").mkdirs();

        archivoClientes.cargar(clientes);
        archivoCanchas.cargar(canchas);
        archivoReservas.cargar(reservas, clientes, canchas);
        archivoServicios.cargar(reservas);
        archivoPagos.cargar(reservas);
    }
}
