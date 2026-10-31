/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;


import java.time.LocalTime;

public class GestorCanchas {

    private Cancha[] canchas = new Cancha[30];
    private int cantidad = 0;

    public void cargarEjemplos() {

        if (cantidad != 0) {
            return;
        }

        registrar("C01", "La Bombonera", "Futbol 5",
                10, 150, "DISPONIBLE",
                LocalTime.of(8, 0), LocalTime.of(23, 0));

        registrar("C02", "El Maracana", "Futbol 5",
                10, 150, "DISPONIBLE",
                LocalTime.of(8, 0), LocalTime.of(23, 0));

        registrar("C03", "El Monumental", "Futbol 7",
                14, 150, "DISPONIBLE",
                LocalTime.of(8, 0), LocalTime.of(23, 0));
    }

    public boolean registrar(String codigo, String nombre,
            String tipo, int capacidad, double precio,
            String estado, LocalTime abre, LocalTime cierra) {

        if (cantidad == canchas.length
                || buscar(codigo) != null
                || codigo.trim().isEmpty()
                || nombre.trim().isEmpty()
                || tipo.trim().isEmpty()
                || capacidad <= 0
                || precio <= 0
                || !abre.isBefore(cierra)
                || !estadoValido(estado)) {

            return false;
        }

        canchas[cantidad] = new Cancha(
                codigo, nombre, tipo, capacidad,
                precio, estado, abre, cierra
        );

        cantidad++;

        return true;
    }

    public boolean estadoValido(String estado) {

        return estado.equals("DISPONIBLE")
                || estado.equals("MANTENIMIENTO")
                || estado.equals("INACTIVA");
    }

    public Cancha buscar(String codigo) {

        for (int i = 0; i < cantidad; i++) {

            if (canchas[i].getCodigo().equalsIgnoreCase(codigo)) {
                return canchas[i];
            }
        }

        return null;
    }

    public String mostrar() {

        String texto = "CANCHAS\n";

        for (int i = 0; i < cantidad; i++) {

            Cancha c = canchas[i];

            texto += c.getCodigo()
                    + " - " + c.getNombre()
                    + " - " + c.getTipo()
                    + " - " + c.getCapacidad()
                    + " jugadores - Q" + c.getPrecioBase()
                    + " base - " + c.getEstado()
                    + " (" + c.getHoraApertura()
                    + " a " + c.getHoraCierre() + ")\n";
        }

        return texto;
    }

    // NUEVO: cambia el estado de una cancha
    // (DISPONIBLE, MANTENIMIENTO o INACTIVA).
    public boolean cambiarEstado(String codigo, String estado) {

        Cancha c = buscar(codigo);

        if (c == null || !estadoValido(estado)) {
            return false;
        }

        c.setEstado(estado);

        return true;
    }

    public Cancha[] getCanchas() {
        return canchas;
    }

    public int getCantidad() {
        return cantidad;
    }
}
