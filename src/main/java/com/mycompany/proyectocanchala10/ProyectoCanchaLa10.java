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

    public static Archivos archivos =
            new Archivos();

    public static void guardar() {

        archivos.guardar(
                clientes,
                canchas,
                reservas
        );
    }

    public static void main(String[] args) {

        archivos.cargar(
                clientes,
                canchas,
                reservas
        );
    }
}