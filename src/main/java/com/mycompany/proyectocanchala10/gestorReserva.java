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

            texto += linea(reservas[i]);
        }

        return texto;
    }

    // NUEVO: la linea que antes se armaba dentro de mostrar() paso aqui
    // (el texto es identico) para reutilizarla en las nuevas consultas.
    private String linea(Reserva r) {

        return "R" + r.getCodigo()
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

    // NUEVO: agenda de un dia (todas las reservas de esa fecha).
    public String mostrarPorFecha(LocalDate fecha) {

        String texto = "";

        for (int i = 0; i < cantidad; i++) {

            if (reservas[i].getFecha().equals(fecha)) {
                texto += linea(reservas[i]);
            }
        }

        return texto.isEmpty()
                ? "No hay reservas para esa fecha."
                : "RESERVAS DEL " + fecha + "\n" + texto;
    }

    // NUEVO: todas las reservas de un cliente.
    public String mostrarCliente(int codigoCliente) {

        String texto = "";

        for (int i = 0; i < cantidad; i++) {

            if (reservas[i].getCliente().getCodigo() == codigoCliente) {
                texto += linea(reservas[i]);
            }
        }

        return texto.isEmpty()
                ? "Ese cliente no tiene reservas."
                : "RESERVAS DEL CLIENTE\n" + texto;
    }

    // NUEVO: cuenta las reservas de hoy en adelante que siguen activas
    // (ni canceladas ni finalizadas) en una cancha. Sirve para avisar
    // antes de poner la cancha en mantenimiento.
    public int reservasFuturasActivas(String codigoCancha) {

        int total = 0;

        for (int i = 0; i < cantidad; i++) {

            Reserva r = reservas[i];

            if (r.getCancha().getCodigo().equalsIgnoreCase(codigoCancha)
                    && !r.getFecha().isBefore(LocalDate.now())
                    && !r.getEstado().equals("CANCELADA")
                    && !r.getEstado().equals("FINALIZADA")) {

                total++;
            }
        }

        return total;
    }

    // NUEVO: comprobante de una reserva con servicios, pagos y saldo.
    public String comprobante(Reserva r) {

        if (r == null) {
            return "Reserva no encontrada.";
        }

        String texto = "===== COMPROBANTE DE RESERVA R"
                + r.getCodigo() + " =====\n";

        texto += "Cliente: " + r.getCliente().getNombre()
                + " (Tel. " + r.getCliente().getTelefono() + ")\n";
        texto += "Cancha: " + r.getCancha().getNombre()
                + " (" + r.getCancha().getTipo() + ")\n";
        texto += "Fecha: " + r.getFecha() + "\n";
        texto += "Horario: " + r.getInicio() + " a " + r.getFin()
                + " (" + r.getHoras() + " h)\n";
        texto += "Estado: " + r.getEstado() + "\n";

        if (r.getEstado().equals("CANCELADA")) {
            texto += "Cancelada el " + r.getFechaCancelacion()
                    + " por " + r.getOperadorCancelacion()
                    + ": " + r.getMotivoCancelacion() + "\n";
        }

        double servicios = 0;

        for (int i = 0; i < r.getCantidadServicios(); i++) {
            servicios += r.getServicios()[i].getPrecio();
        }

        texto += "\nAlquiler de cancha: "
                + Formato.dinero(r.getTotal() - servicios) + "\n";

        if (r.getCantidadServicios() > 0) {

            texto += "Servicios y productos:\n";

            for (int i = 0; i < r.getCantidadServicios(); i++) {

                ServicioAdicional s = r.getServicios()[i];

                texto += "  - " + s.getNombre()
                        + ": " + Formato.dinero(s.getPrecio()) + "\n";
            }
        }

        texto += "TOTAL: " + Formato.dinero(r.getTotal()) + "\n";

        if (r.getCantidadPagos() > 0) {

            texto += "\nPagos:\n";

            for (int i = 0; i < r.getCantidadPagos(); i++) {

                Pago p = r.getPagos()[i];

                texto += "  #" + p.getCodigo()
                        + " " + p.getFecha().withNano(0).toString().replace('T', ' ')
                        + " | " + p.getMetodo()
                        + " | " + Formato.dinero(p.getMonto())
                        + (p.getObservacion() == null
                        || p.getObservacion().isEmpty()
                        ? "" : " | " + p.getObservacion())
                        + "\n";
            }
        }

        texto += "\nPAGADO: " + Formato.dinero(r.getPagado())
                + "\nSALDO: " + Formato.dinero(r.getSaldo()) + "\n";

        return texto;
    }

    public Reserva[] getReservas() {
        return reservas;
    }

    public int getCantidad() {
        return cantidad;
    }
}
