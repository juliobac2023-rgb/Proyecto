package com.mycompany.proyectocanchala10;

import java.io.File;

// Coordina todos los archivos del sistema: cada modulo tiene su propia
// clase de archivos y esta las llama en el orden correcto.
public class Archivos {

    private Archivoclientes archivoClientes = new Archivoclientes();

    private ArchivosCanchas archivoCanchas = new ArchivosCanchas();

    private ArchivosReservas archivoReservas = new ArchivosReservas();

    private ArchivosServicios archivoServicios = new ArchivosServicios();

    private ArchivosPagos archivoPagos = new ArchivosPagos();

    // Archivos de la tienda (productos, ventas y detalle de ventas).
    private ArchivosTienda archivoTienda = new ArchivosTienda();

    // Tarifas de referencia (tarifas.csv).
    private ArchivosTarifas archivoTarifas = new ArchivosTarifas();

    public void guardar(gestorClientes clientes,
            GestorCanchas canchas, gestorReserva reservas,
            GestorTienda tienda) {

        new File("datos").mkdirs();

        archivoTarifas.guardar();
        archivoClientes.guardar(clientes);
        archivoCanchas.guardar(canchas);
        archivoReservas.guardar(reservas);
        archivoServicios.guardar(reservas);
        archivoPagos.guardar(reservas);
        archivoTienda.guardar(tienda);
    }

    // El orden importa: las reservas necesitan que ya existan los clientes
    // y las canchas; los servicios y pagos necesitan las reservas.
    public void cargar(gestorClientes clientes,
            GestorCanchas canchas, gestorReserva reservas,
            GestorTienda tienda) {

        new File("datos").mkdirs();

        archivoTarifas.cargar();
        archivoClientes.cargar(clientes);
        archivoCanchas.cargar(canchas);
        archivoReservas.cargar(reservas, clientes, canchas);
        archivoServicios.cargar(reservas);
        archivoPagos.cargar(reservas);
        archivoTienda.cargar(tienda);
    }
}
