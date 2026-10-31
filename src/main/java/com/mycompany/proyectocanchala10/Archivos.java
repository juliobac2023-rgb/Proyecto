/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;

// CORREGIDO: el import de File estaba repetido 3 veces; basta con uno.
import java.io.File;

public class Archivos {

    private Archivoclientes archivoClientes = new Archivoclientes();

    private ArchivosCanchas archivoCanchas = new ArchivosCanchas();

    private ArchivosReservas archivoReservas = new ArchivosReservas();

    private ArchivosServicios archivoServicios = new ArchivosServicios();

    private ArchivosPagos archivoPagos = new ArchivosPagos();

    // NUEVO: archivo de la tienda (productos y ventas).
    private ArchivosTienda archivoTienda = new ArchivosTienda();

    // NUEVO: ahora tambien recibe el gestor de la tienda.
    public void guardar(gestorClientes clientes,
            GestorCanchas canchas, gestorReserva reservas,
            GestorTienda tienda) {

        new File("datos").mkdirs();

        archivoClientes.guardar(clientes);
        archivoCanchas.guardar(canchas);
        archivoReservas.guardar(reservas);
        archivoServicios.guardar(reservas);
        archivoPagos.guardar(reservas);
        archivoTienda.guardar(tienda);
    }

    public void cargar(gestorClientes clientes,
            GestorCanchas canchas, gestorReserva reservas,
            GestorTienda tienda) {

        new File("datos").mkdirs();

        archivoClientes.cargar(clientes);
        archivoCanchas.cargar(canchas);
        archivoReservas.cargar(reservas, clientes, canchas);
        archivoServicios.cargar(reservas);
        archivoPagos.cargar(reservas);
        archivoTienda.cargar(tienda);
    }
}
