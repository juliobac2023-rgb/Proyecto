/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectocanchala10;

/**
 *
 * @author julio
 */
public class ProyectoCanchaLa10 {


 

    public static gestorClientes clientes =
            new gestorClientes();

    public static GestorCanchas canchas =
            new GestorCanchas();

    public static gestorReserva reservas =
            new gestorReserva();

    // NUEVO: gestor de la tienda (productos, stock y ventas).
    public static GestorTienda tienda =
            new GestorTienda();

    public static Archivos archivos =
            new Archivos();

    public static void guardar() {

        archivos.guardar(
                clientes,
                canchas,
                reservas,
                tienda
        );
    }

    public static void main(String[] args) {

        archivos.cargar(
                clientes,
                canchas,
                reservas,
                tienda
        );

        // MODELO DE PERSISTENCIA: los datos se leen de los archivos al
        // iniciar, se trabajan en memoria y se escriben en los archivos
        // una sola vez, cuando el programa se cierra. El shutdown hook
        // se ejecuta al salir con la opcion 0 y tambien si se cierra la
        // ventana de la consola o se presiona Ctrl+C.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            guardar();
            System.out.println("Datos guardados en los archivos.");
        }));

        new MenuConsola().iniciar();
    }
}