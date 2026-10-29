/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;



import java.time.LocalDate;

public class reportes {

    public static String resumen(gestorReserva gestor,
            GestorCanchas canchas,
            LocalDate desde, LocalDate hasta) {

        int total = 0;
        int canceladas = 0;
        int pendientes = 0;

        double cobrado = 0;

        int[] usos = new int[30];
        int[] horas = new int[24];
        int[] clientes = new int[100];
        int[] reservasCliente = new int[100];

        int cantidadClientes = 0;

        String texto = "REPORTE DEL " + desde
                + " AL " + hasta + "\n\n";

        texto += "RESERVAS DEL PERIODO:\n";

        for (int i = 0; i < gestor.getCantidad(); i++) {

            Reserva r = gestor.getReservas()[i];

            if (r.getFecha().isBefore(desde)
                    || r.getFecha().isAfter(hasta)) {
                continue;
            }

            total++;

            texto += "R" + r.getCodigo()
                    + " | " + r.getFecha()
                    + " | " + r.getCancha().getNombre()
                    + " | " + r.getEstado()
                    + " | Total Q" + r.getTotal()
                    + " | Saldo Q" + r.getSaldo() + "\n";

            if (r.getEstado().equals("CANCELADA")) {

                canceladas++;

                texto += "  Cancelada por "
                        + r.getOperadorCancelacion()
                        + ": " + r.getMotivoCancelacion()
                        + "\n";

            } else {

                if (r.getSaldo() > 0) {
                    pendientes++;

                    texto += "  PENDIENTE Q"
                            + r.getSaldo() + "\n";
                }

                for (int j = 0; j < canchas.getCantidad(); j++) {

                    if (r.getCancha().getCodigo().equals(
                            canchas.getCanchas()[j].getCodigo())) {
                        usos[j]++;
                    }
                }

                int limite = r.getFin().getHour()
                        + (r.getFin().getMinute() > 0 ? 1 : 0);

                for (int h = r.getInicio().getHour();
                        h < limite; h++) {
                    horas[h]++;
                }

                int pos = -1;

                for (int j = 0; j < cantidadClientes; j++) {

                    if (clientes[j] == r.getCliente().getCodigo()) {
                        pos = j;
                    }
                }

                if (pos == -1 && cantidadClientes < clientes.length) {

                    pos = cantidadClientes;

                    clientes[pos] = r.getCliente().getCodigo();

                    cantidadClientes++;
                }

                if (pos != -1) {
                    reservasCliente[pos]++;
                }
            }
        }

        // Contamos los pagos realizados dentro del periodo.

        for (int i = 0; i < gestor.getCantidad(); i++) {

            Reserva r = gestor.getReservas()[i];

            for (int j = 0; j < r.getCantidadPagos(); j++) {

                Pago p = r.getPagos()[j];

                LocalDate fechaPago = p.getFecha().toLocalDate();

                if (!fechaPago.isBefore(desde)
                        && !fechaPago.isAfter(hasta)) {

                    cobrado += p.getMonto();
                }
            }
        }

        texto += "\nRESUMEN:"
                + "\nReservas: " + total
                + "\nCanceladas: " + canceladas
                + "\nReservas con saldo: " + pendientes
                + "\nIngresos cobrados en el periodo: Q" + cobrado;

        texto += "\n\nRESERVAS POR CANCHA:\n";

        int indiceMayor = -1;

        for (int j = 0; j < canchas.getCantidad(); j++) {

            texto += canchas.getCanchas()[j].getNombre()
                    + ": " + usos[j] + "\n";

            if (usos[j] > 0
                    && (indiceMayor == -1
                    || usos[j] > usos[indiceMayor])) {

                indiceMayor = j;
            }
        }

        texto += "Cancha mas utilizada: "
                + (indiceMayor == -1
                ? "Sin datos"
                : canchas.getCanchas()[indiceMayor].getNombre());

        int horaMayor = -1;

        for (int h = 0; h < horas.length; h++) {

            if (horas[h] > 0
                    && (horaMayor == -1
                    || horas[h] > horas[horaMayor])) {

                horaMayor = h;
            }
        }

        texto += "\nHorario con mayor demanda: "
                + (horaMayor == -1
                ? "Sin datos"
                : horaMayor + ":00 ("
                + horas[horaMayor] + " reservas)");

        int clienteMayor = -1;

        for (int j = 0; j < cantidadClientes; j++) {

            if (clienteMayor == -1
                    || reservasCliente[j] > reservasCliente[clienteMayor]) {

                clienteMayor = j;
            }
        }

        texto += "\nCliente con mas reservas: "
                + (clienteMayor == -1
                ? "Sin datos"
                : "Codigo " + clientes[clienteMayor]
                + " (" + reservasCliente[clienteMayor]
                + " reservas)");

        return texto;
    }
}
