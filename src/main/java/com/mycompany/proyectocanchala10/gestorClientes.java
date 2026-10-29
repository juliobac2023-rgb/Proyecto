/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;

import java.time.LocalDate;

public class gestorClientes {

    private Cliente[] clientes = new Cliente[100];

    private int cantidad = 0;
    private int siguienteCodigo = 1;

    public Cliente registrar(String nombre,
            String telefono, String correo) {

        if (cantidad == clientes.length
                || nombre.trim().isEmpty()
                || telefono.trim().isEmpty()) {
            return null;
        }

        if (buscarTelefono(telefono) != null) {
            return null;
        }

        Cliente nuevo = new Cliente(
                siguienteCodigo,
                nombre,
                telefono,
                correo,
                LocalDate.now()
        );

        clientes[cantidad] = nuevo;

        cantidad++;
        siguienteCodigo++;

        return nuevo;
    }

    public void cargar(Cliente cliente) {

        if (cantidad == clientes.length) {
            return;
        }

        clientes[cantidad] = cliente;
        cantidad++;

        if (cliente.getCodigo() >= siguienteCodigo) {
            siguienteCodigo = cliente.getCodigo() + 1;
        }
    }

    public Cliente buscarCodigo(int codigo) {

        for (int i = 0; i < cantidad; i++) {

            if (clientes[i].getCodigo() == codigo) {
                return clientes[i];
            }
        }

        return null;
    }

    public Cliente buscarTelefono(String telefono) {

        for (int i = 0; i < cantidad; i++) {

            if (clientes[i].getTelefono().equals(telefono)) {
                return clientes[i];
            }
        }

        return null;
    }

    public String buscarNombre(String nombre) {

        String texto = "";

        for (int i = 0; i < cantidad; i++) {

            if (clientes[i].getNombre().toLowerCase()
                    .contains(nombre.toLowerCase())) {

                texto += clientes[i].getCodigo()
                        + " - " + clientes[i].getNombre()
                        + " - " + clientes[i].getTelefono()
                        + "\n";
            }
        }

        return texto.isEmpty()
                ? "No se encontraron clientes."
                : texto;
    }

    public String mostrar() {

        String texto = "CLIENTES\n";

        for (int i = 0; i < cantidad; i++) {

            Cliente c = clientes[i];

            texto += c.getCodigo()
                    + " - " + c.getNombre()
                    + " - " + c.getTelefono()
                    + " - " + c.getCorreo()
                    + " - " + c.getFechaRegistro()
                    + "\n";
        }

        return cantidad == 0 ? "No hay clientes." : texto;
    }

    public Cliente[] getClientes() {
        return clientes;
    }

    public int getCantidad() {
        return cantidad;
    }
}
