/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectocanchala10;

import com.mycompany.proyectocanchala10.vistas.FrmPrincipal;

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

    // Configuracion del sistema (por ejemplo, modulo_tienda=true/false).
    public static Configuracion configuracion =
            new Configuracion();

    public static void guardar() {

        configuracion.guardar();

        archivos.guardar(
                clientes,
                canchas,
                reservas,
                tienda
        );
    }

    public static void main(String[] args) {

        iniciarDatos();

        // Por defecto se abren las ventanas (JFrame).
        // Para usar la version de consola: ejecutar con el argumento "consola".
        if (args.length > 0 && args[0].equalsIgnoreCase("consola")) {
            new MenuConsola().iniciar();
        } else {
            FrmPrincipal.abrir();
        }
    }

    // Carga los archivos y deja programado el guardado al cerrar.
    static void iniciarDatos() {

        configuracion.cargar();

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
        // ventana de la consola o se presiona Ctrl+C. En la version con
        // ventanas se ejecuta al cerrar el menu principal o con "Salir".
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            guardar();
            System.out.println("Datos guardados en los archivos.");
        }));
    }
}