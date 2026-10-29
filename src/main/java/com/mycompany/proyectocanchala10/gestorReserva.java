/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;


import java.time.LocalDate;
import java.time.LocalTime;

public class gestorReserva {

    private Reserva[] reservas = new Reserva[500];

    private int cantidad = 0;
    private int siguienteCodigo = 1;
    private int siguientePago = 1;

    public boolean disponible(Cancha cancha,
            LocalDate fecha, LocalTime inicio, int horas) {

        if (cancha == null
                || !cancha.getEstado().equals("DISPONIBLE")
                || horas <= 0
                || horas > 12) {

            return false;
        }

        LocalTime fin = inicio.plusHours(horas);

        if (!fin.isAfter(inicio)
                || inicio.isBefore(cancha.getHoraApertura())
                || fin.isAfter(cancha.getHoraCierre())) {

            return false;
        }

        for (int i = 0; i < cantidad; i++) {

            Reserva r = reservas[i];

            if (r.getCancha().getCodigo().equals(cancha.getCodigo())
                    && r.getFecha().equals(fecha)
                    && !r.getEstado().equals("CANCELADA")
                    && inicio.isBefore(r.getFin())
                    && fin.isAfter(r.getInicio())) {

                return false;
            }
        }

        return true;
    }

    public String consultar(GestorCanchas gestor,
            String tipo, LocalDate fecha,
            LocalTime inicio, int horas) {

        String texto = "CANCHAS DISPONIBLES\n";

        int encontradas = 0;

        for (int i = 0; i < gestor.getCantidad(); i++) {

            Cancha c = gestor.getCanchas()[i];

            if (c.getTipo().equalsIgnoreCase(tipo)
                    && disponible(c, fecha, inicio, horas)) {

                texto += c.getCodigo()
                        + " - " + c.getNombre()
                        + " - Q" + Tarifa.calcular(
                                c, fecha, inicio, horas)
                        + "\n";

                encontradas++;
            }
        }

        return encontradas == 0
                ? "No hay canchas disponibles para ese horario."
                : texto;
    }

    public Reserva registrar(Cliente cliente,
            Cancha cancha, LocalDate fecha,
            LocalTime inicio, int horas) {

        if (cliente == null
                || cantidad == reservas.length
                || !disponible(cancha, fecha, inicio, horas)) {

            return null;
        }

        if (fecha.isBefore(LocalDate.now())
                || (fecha.equals(LocalDate.now())
                && inicio.isBefore(LocalTime.now()))) {

            return null;
        }

        double precio = Tarifa.calcular(
                cancha, fecha, inicio, horas
        );

        Reserva nueva = new Reserva(
                siguienteCodigo,
                fecha,
                cliente,
                cancha,
                inicio,
                horas,
                precio,
                "PENDIENTE"
        );

        reservas[cantidad] = nueva;

        cantidad++;
        siguienteCodigo++;

        return nueva;
    }

    public void cargar(Reserva reserva) {

        if (cantidad == reservas.length) {
            return;
        }

        reservas[cantidad] = reserva;
        cantidad++;

        if (reserva.getCodigo() >= siguienteCodigo) {
            siguienteCodigo = reserva.getCodigo() + 1;
        }
    }

    public Reserva buscar(int codigo) {

        for (int i = 0; i < cantidad; i++) {

            if (reservas[i].getCodigo() == codigo) {
                return reservas[i];
            }
        }

        return null;
    }

    public boolean pagar(Reserva reserva,
            double monto, String metodo, String observacion) {

        if (reserva == null || metodo.trim().isEmpty()) {
            return false;
        }

        Pago pago = new Pago(
                siguientePago,
                java.time.LocalDateTime.now(),
                monto,
                metodo,
                observacion
        );

        if (!reserva.agregarPago(pago)) {
            return false;
        }

        siguientePago++;

        return true;
    }

    public void actualizarCodigoPago(int codigo) {

        if (codigo >= siguientePago) {
            siguientePago = codigo + 1;
        }
    }

    public String mostrar() {

        if (cantidad == 0) {
            return "No hay reservas.";
        }

        String texto = "RESERVAS\n";

        for (int i = 0; i < cantidad; i++) {

            Reserva r = reservas[i];

            texto += "R" + r.getCodigo()
                    + " | " + r.getFecha()
                    + " | " + r.getInicio()
                    + "-" + r.getFin()
                    + " | " + r.getCancha().getNombre()
                    + " | " + r.getCliente().getNombre()
                    + " | " + r.getEstado()
                    + " | Total Q" + r.getTotal()
                    + " | Pagado Q" + r.getPagado()
                    + " | Saldo Q" + r.getSaldo()
                    + "\n";
        }

        return texto;
    }

    public Reserva[] getReservas() {
        return reservas;
    }

    public int getCantidad() {
        return cantidad;
    }
}
