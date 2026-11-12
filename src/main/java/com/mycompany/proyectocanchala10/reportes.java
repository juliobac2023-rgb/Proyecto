package com.mycompany.proyectocanchala10;

import java.time.LocalDate;

// Reporteria de reservas: no solo lista, tambien cuenta, suma,
// agrupa y ordena para dar informacion util del negocio.
public class reportes {

    // Reporte de un periodo, sin filtros (todas las canchas y estados).
    public static String resumen(gestorReserva gestor,
            GestorCanchas canchas,
            LocalDate desde, LocalDate hasta) {

        return resumen(gestor, canchas, desde, hasta, "", "");
    }

    // Reporte de un periodo con filtros opcionales.
    // codigoCancha vacio = todas las canchas; estado vacio = todos los estados.
    public static String resumen(gestorReserva gestor,
            GestorCanchas canchas,
            LocalDate desde, LocalDate hasta,
            String codigoCancha, String estado) {

        int total = 0;
        int canceladas = 0;
        int pendientes = 0;

        double cobrado = 0;
        double porCobrar = 0;

        // Un contador por cancha (misma posicion que en el gestor)
        // y uno por cada hora del dia.
        int[] usos = new int[canchas.getCanchas().length];
        int[] horas = new int[24];

        // Clientes que aparecen en el periodo y cuantas reservas tienen.
        Cliente[] clientes = new Cliente[gestor.getCantidad() + 1];
        int[] reservasCliente = new int[gestor.getCantidad() + 1];

        int cantidadClientes = 0;

        String texto = "REPORTE DEL " + desde
                + " AL " + hasta + "\n";

        texto += "Cancha: " + (codigoCancha.isEmpty() ? "todas" : codigoCancha)
                + " | Estado: " + (estado.isEmpty() ? "todos" : estado)
                + "\n\n";

        texto += "RESERVAS DEL PERIODO:\n";

        for (int i = 0; i < gestor.getCantidad(); i++) {

            Reserva r = gestor.getReservas()[i];

            if (r.getFecha().isBefore(desde)
                    || r.getFecha().isAfter(hasta)
                    || !coincide(r, codigoCancha, estado)) {
                continue;
            }

            total++;

            texto += "R" + r.getCodigo()
                    + " | " + r.getFecha()
                    + " | " + r.getInicio() + "-" + r.getFin()
                    + " | " + r.getCancha().getNombre()
                    + " | " + r.getCliente().getNombre()
                    + " | " + r.getEstado()
                    + " | Total " + Formato.dinero(r.getTotal())
                    + " | Saldo " + Formato.dinero(r.getSaldo()) + "\n";

            if (r.getEstado().equals("CANCELADA")) {

                canceladas++;

                texto += "  Cancelada el " + r.getFechaCancelacion()
                        + " por " + r.getOperadorCancelacion()
                        + ": " + r.getMotivoCancelacion()
                        + "\n";

            } else {

                if (r.getSaldo() > 0) {

                    pendientes++;
                    porCobrar += r.getSaldo();

                    texto += "  PAGO PENDIENTE "
                            + Formato.dinero(r.getSaldo()) + "\n";
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

                    if (clientes[j] == r.getCliente()) {
                        pos = j;
                    }
                }

                if (pos == -1) {

                    pos = cantidadClientes;

                    clientes[pos] = r.getCliente();

                    cantidadClientes++;
                }

                reservasCliente[pos]++;
            }
        }

        if (total == 0) {
            texto += "No hay reservas con esos filtros.\n";
        }

        // Ingresos: se suman los pagos realizados dentro del periodo.

        for (int i = 0; i < gestor.getCantidad(); i++) {

            Reserva r = gestor.getReservas()[i];

            if (!coincide(r, codigoCancha, estado)) {
                continue;
            }

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
                + "\nTotal por cobrar: " + Formato.dinero(porCobrar)
                + "\nIngresos cobrados en el periodo: " + Formato.dinero(cobrado);

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

        // Las 3 horas con mas reservas: se busca la mayor, se marca
        // como usada y se repite.

        texto += "\n\nHORARIOS DE MAYOR DEMANDA:\n";

        boolean[] horaUsada = new boolean[24];
        int mostrados = 0;

        for (int k = 0; k < 3; k++) {

            int mayor = -1;

            for (int h = 0; h < horas.length; h++) {

                if (!horaUsada[h] && horas[h] > 0
                        && (mayor == -1 || horas[h] > horas[mayor])) {
                    mayor = h;
                }
            }

            if (mayor == -1) {
                break;
            }

            horaUsada[mayor] = true;
            mostrados++;

            texto += mostrados + ". " + mayor + ":00 ("
                    + horas[mayor] + " reservas)\n";
        }

        if (mostrados == 0) {
            texto += "Sin datos\n";
        }

        // Los 5 clientes con mas reservas, con la misma tecnica.

        texto += "\nCLIENTES CON MAS RESERVAS:\n";

        boolean[] clienteUsado = new boolean[cantidadClientes + 1];
        mostrados = 0;

        for (int k = 0; k < 5; k++) {

            int mayor = -1;

            for (int j = 0; j < cantidadClientes; j++) {

                if (!clienteUsado[j]
                        && (mayor == -1
                        || reservasCliente[j] > reservasCliente[mayor])) {
                    mayor = j;
                }
            }

            if (mayor == -1) {
                break;
            }

            clienteUsado[mayor] = true;
            mostrados++;

            texto += mostrados + ". " + clientes[mayor].getNombre()
                    + " (codigo " + clientes[mayor].getCodigo() + "): "
                    + reservasCliente[mayor] + " reservas\n";
        }

        if (mostrados == 0) {
            texto += "Sin datos\n";
        }

        return texto;
    }

    // Dice si una reserva pasa los filtros de cancha y estado.
    private static boolean coincide(Reserva r,
            String codigoCancha, String estado) {

        if (!codigoCancha.isEmpty()
                && !r.getCancha().getCodigo().equalsIgnoreCase(codigoCancha)) {
            return false;
        }

        if (!estado.isEmpty() && !r.getEstado().equals(estado)) {
            return false;
        }

        return true;
    }
}
