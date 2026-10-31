package com.mycompany.proyectocanchala10;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// NUEVO: version de consola del sistema.
// Solo pide datos y muestra resultados: toda la logica sigue
// en los gestores (clientes, canchas, reservas y tienda).
public class MenuConsola {

    private Scanner teclado = new Scanner(System.in);

    private gestorClientes clientes = ProyectoCanchaLa10.clientes;
    private GestorCanchas canchas = ProyectoCanchaLa10.canchas;
    private gestorReserva reservas = ProyectoCanchaLa10.reservas;
    private GestorTienda tienda = ProyectoCanchaLa10.tienda;

    private String[] estadosCancha = {"DISPONIBLE", "MANTENIMIENTO", "INACTIVA"};
    private String[] metodosPago = {"EFECTIVO", "TARJETA", "TRANSFERENCIA"};
    private String[] estadosReserva = {"EN_CURSO", "FINALIZADA"};

    // MENU PRINCIPAL

    public void iniciar() {

        System.out.println("=== SISTEMA DE CANCHAS LA 10 ===");

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\nMENU PRINCIPAL");
            System.out.println("1. Clientes");
            System.out.println("2. Canchas");
            System.out.println("3. Reservas");
            System.out.println("4. Tienda");
            System.out.println("5. Reportes");
            System.out.println("0. Salir");

            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    menuClientes();
                    break;
                case 2:
                    menuCanchas();
                    break;
                case 3:
                    menuReservas();
                    break;
                case 4:
                    menuTienda();
                    break;
                case 5:
                    menuReportes();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }

        System.out.println("Hasta luego.");
    }

    // CLIENTES

    private void menuClientes() {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\nCLIENTES");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Ver todos");
            System.out.println("3. Buscar por nombre");
            System.out.println("4. Buscar por telefono");
            System.out.println("5. Editar cliente");
            System.out.println("0. Volver");

            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    registrarCliente();
                    break;
                case 2:
                    System.out.println(clientes.mostrar());
                    break;
                case 3:
                    System.out.println(clientes.buscarNombre(
                            leerTexto("Nombre a buscar: ")));
                    break;
                case 4:
                    buscarPorTelefono();
                    break;
                case 5:
                    editarCliente();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private Cliente registrarCliente() {

        String nombre = leerTexto("Nombre: ");
        String telefono = leerTexto("Telefono: ");
        String correo = leerTexto("Correo (opcional): ");

        Cliente nuevo = clientes.registrar(nombre, telefono, correo);

        if (nuevo == null) {
            System.out.println("No se pudo registrar: nombre o telefono vacio, "
                    + "telefono ya registrado o limite de clientes alcanzado.");
            return null;
        }

        System.out.println("Cliente registrado con codigo "
                + nuevo.getCodigo() + ".");

        return nuevo;
    }

    private void buscarPorTelefono() {

        Cliente c = clientes.buscarTelefono(leerTexto("Telefono: "));

        if (c == null) {
            System.out.println("No existe un cliente con ese telefono.");
        } else {
            System.out.println(c.getCodigo() + " - " + c.getNombre()
                    + " - " + c.getTelefono() + " - " + c.getCorreo());
        }
    }

    private void editarCliente() {

        Cliente c = clientes.buscarCodigo(leerEntero("Codigo del cliente: "));

        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        String nombre = leerConDefecto("Nombre", c.getNombre());
        String telefono = leerConDefecto("Telefono", c.getTelefono());
        String correo = leerConDefecto("Correo", c.getCorreo());

        if (clientes.editar(c.getCodigo(), nombre, telefono, correo)) {
            System.out.println("Cliente actualizado.");
        } else {
            System.out.println("No se pudo editar: datos vacios "
                    + "o telefono usado por otro cliente.");
        }
    }

    // Pide el telefono de un cliente; si no existe ofrece registrarlo.
    private Cliente elegirCliente() {

        String telefono = leerTexto("Telefono del cliente: ");

        Cliente c = clientes.buscarTelefono(telefono);

        if (c != null) {
            System.out.println("Cliente: " + c.getNombre());
            return c;
        }

        if (confirmar("Ese cliente no existe. Registrarlo ahora")) {
            return registrarCliente();
        }

        return null;
    }

    // CANCHAS

    private void menuCanchas() {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\nCANCHAS");
            System.out.println("1. Ver canchas");
            System.out.println("2. Registrar cancha nueva");
            System.out.println("3. Cambiar estado de una cancha");
            System.out.println("0. Volver");

            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    System.out.println(canchas.mostrar());
                    break;
                case 2:
                    registrarCancha();
                    break;
                case 3:
                    cambiarEstadoCancha();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private void registrarCancha() {

        String codigo = leerTexto("Codigo (ej. C04): ");
        String nombre = leerTexto("Nombre: ");
        String tipo = leerTexto("Tipo (ej. Futbol 5): ");
        int capacidad = leerEntero("Capacidad (jugadores): ");
        double precio = leerDecimal("Precio base por hora (Q): ");
        String estado = elegirOpcion("Estado inicial", estadosCancha);
        LocalTime abre = leerHora("Hora de apertura (HH:mm): ");
        LocalTime cierra = leerHora("Hora de cierre (HH:mm): ");

        if (canchas.registrar(codigo, nombre, tipo, capacidad,
                precio, estado, abre, cierra)) {

            System.out.println("Cancha registrada.");

        } else {
            System.out.println("No se pudo registrar: codigo repetido, "
                    + "datos vacios, numeros menores o iguales a 0, "
                    + "apertura despues del cierre o limite alcanzado.");
        }
    }

    private void cambiarEstadoCancha() {

        System.out.println(canchas.mostrar());

        Cancha c = canchas.buscar(leerTexto("Codigo de la cancha: "));

        if (c == null) {
            System.out.println("Cancha no encontrada.");
            return;
        }

        System.out.println("Estado actual: " + c.getEstado());

        String estado = elegirOpcion("Nuevo estado", estadosCancha);

        if (!estado.equals("DISPONIBLE")) {

            int futuras = reservas.reservasFuturasActivas(c.getCodigo());

            if (futuras > 0) {
                System.out.println("AVISO: esta cancha tiene " + futuras
                        + " reserva(s) activa(s) de hoy en adelante. "
                        + "No se cancelan solas; revisalas en Reservas.");
            }
        }

        canchas.cambiarEstado(c.getCodigo(), estado);

        System.out.println("Estado actualizado a " + estado + ".");
    }

    // RESERVAS

    private void menuReservas() {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\nRESERVAS");
            System.out.println("1. Consultar disponibilidad");
            System.out.println("2. Nueva reserva");
            System.out.println("3. Ver todas las reservas");
            System.out.println("4. Agenda de un dia");
            System.out.println("5. Reservas de un cliente");
            System.out.println("6. Ver comprobante");
            System.out.println("7. Registrar pago");
            System.out.println("8. Agregar servicio manual");
            System.out.println("9. Cambiar estado (en curso / finalizada)");
            System.out.println("10. Cancelar reserva");
            System.out.println("0. Volver");

            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    consultarDisponibilidad();
                    break;
                case 2:
                    nuevaReserva();
                    break;
                case 3:
                    System.out.println(reservas.mostrar());
                    break;
                case 4:
                    System.out.println(reservas.mostrarPorFecha(
                            leerFecha("Fecha")));
                    break;
                case 5:
                    reservasDeCliente();
                    break;
                case 6:
                    System.out.println(reservas.comprobante(pedirReserva()));
                    break;
                case 7:
                    pagarReserva(pedirReserva());
                    break;
                case 8:
                    agregarServicioManual();
                    break;
                case 9:
                    cambiarEstadoReserva();
                    break;
                case 10:
                    cancelarReserva();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private void consultarDisponibilidad() {

        String tipo = leerTexto("Tipo de cancha (ej. Futbol 5): ");
        LocalDate fecha = leerFecha("Fecha");
        LocalTime inicio = leerHora("Hora de inicio (HH:mm): ");
        int horas = leerEntero("Cantidad de horas: ");

        System.out.println(reservas.consultar(
                canchas, tipo, fecha, inicio, horas));
    }

    private void nuevaReserva() {

        Cliente cliente = elegirCliente();

        if (cliente == null) {
            return;
        }

        System.out.println(canchas.mostrar());

        Cancha cancha = canchas.buscar(leerTexto("Codigo de la cancha: "));

        if (cancha == null) {
            System.out.println("Cancha no encontrada.");
            return;
        }

        LocalDate fecha = leerFecha("Fecha");
        LocalTime inicio = leerHora("Hora de inicio (HH:mm): ");
        int horas = leerEntero("Cantidad de horas: ");

        if (!reservas.disponible(cancha, fecha, inicio, horas)) {
            System.out.println("La cancha no esta disponible en ese horario "
                    + "(ocupada, fuera de su horario, en mantenimiento "
                    + "o horas invalidas).");
            return;
        }

        System.out.println("Precio total: " + Formato.dinero(
                Tarifa.calcular(cancha, fecha, inicio, horas)));

        if (!confirmar("Confirmar la reserva")) {
            return;
        }

        Reserva r = reservas.registrar(cliente, cancha, fecha, inicio, horas);

        if (r == null) {
            System.out.println("No se pudo crear la reserva: la fecha u hora "
                    + "ya paso o se alcanzo el limite de reservas.");
            return;
        }

        System.out.println("Reserva R" + r.getCodigo()
                + " creada (PENDIENTE). Se confirma al registrar el primer pago.");

        if (confirmar("Registrar un pago ahora")) {
            pagarReserva(r);
        }
    }

    private void reservasDeCliente() {

        Cliente c = clientes.buscarCodigo(leerEntero("Codigo del cliente: "));

        if (c == null) {
            System.out.println("Cliente no encontrado.");
            return;
        }

        System.out.println(reservas.mostrarCliente(c.getCodigo()));
    }

    // Pide el numero de una reserva (sin la R) y la busca.
    private Reserva pedirReserva() {

        Reserva r = reservas.buscar(leerEntero("Codigo de la reserva (numero): "));

        if (r == null) {
            System.out.println("Reserva no encontrada.");
        }

        return r;
    }

    private void pagarReserva(Reserva r) {

        if (r == null) {
            return;
        }

        System.out.println("Total: " + Formato.dinero(r.getTotal())
                + " | Pagado: " + Formato.dinero(r.getPagado())
                + " | Saldo: " + Formato.dinero(r.getSaldo()));

        if (r.getSaldo() <= 0) {
            System.out.println("La reserva ya esta pagada completa.");
            return;
        }

        double monto = leerDecimal("Monto a pagar (Q): ");
        String metodo = elegirOpcion("Metodo de pago", metodosPago);
        String observacion = leerTexto("Observacion (opcional): ");

        if (reservas.pagar(r, monto, metodo, observacion)) {

            System.out.println("Pago registrado. Estado: " + r.getEstado()
                    + " | Saldo: " + Formato.dinero(r.getSaldo()));

        } else {
            System.out.println("No se pudo registrar el pago: reserva "
                    + "cancelada, monto invalido o mayor al saldo.");
        }
    }

    private void agregarServicioManual() {

        Reserva r = pedirReserva();

        if (r == null) {
            return;
        }

        String nombre = leerTexto("Nombre del servicio (ej. Arbitro): ");
        double precio = leerDecimal("Precio (Q): ");

        if (nombre.isEmpty() || precio <= 0) {
            System.out.println("Nombre vacio o precio invalido.");
            return;
        }

        if (r.agregarServicio(new ServicioAdicional(nombre, precio))) {
            System.out.println("Servicio agregado. Nuevo total: "
                    + Formato.dinero(r.getTotal()));
        } else {
            System.out.println("No se pudo agregar: la reserva esta "
                    + "cancelada o finalizada, o ya tiene 10 servicios.");
        }
    }

    private void cambiarEstadoReserva() {

        Reserva r = pedirReserva();

        if (r == null) {
            return;
        }

        System.out.println("Estado actual: " + r.getEstado());

        String estado = elegirOpcion("Nuevo estado", estadosReserva);

        if (r.cambiarEstado(estado)) {

            System.out.println("Estado actualizado a " + estado + ".");

            if (estado.equals("FINALIZADA") && r.getSaldo() > 0) {
                System.out.println("AVISO: la reserva termino con saldo "
                        + "pendiente de " + Formato.dinero(r.getSaldo()) + ".");
            }

        } else {
            System.out.println("No se pudo cambiar: solo se pasa de "
                    + "CONFIRMADA a EN_CURSO y de EN_CURSO a FINALIZADA.");
        }
    }

    private void cancelarReserva() {

        Reserva r = pedirReserva();

        if (r == null) {
            return;
        }

        if (r.getPagado() > 0) {
            System.out.println("AVISO: ya tiene pagos por "
                    + Formato.dinero(r.getPagado())
                    + ". El sistema no maneja devoluciones.");
        }

        String motivo = leerTexto("Motivo de la cancelacion: ");
        String operador = leerTexto("Quien cancela (operador): ");

        if (!confirmar("Cancelar la reserva R" + r.getCodigo())) {
            return;
        }

        if (r.cancelar(motivo, operador, LocalDate.now())) {
            System.out.println("Reserva cancelada.");
        } else {
            System.out.println("No se pudo cancelar: ya estaba cancelada o "
                    + "finalizada, o falta el motivo o el operador.");
        }
    }

    // TIENDA

    private void menuTienda() {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\nTIENDA");
            System.out.println("1. Ver productos y stock");
            System.out.println("2. Registrar producto nuevo");
            System.out.println("3. Reponer stock");
            System.out.println("4. Venta suelta (se paga en caja)");
            System.out.println("5. Cargar producto a una reserva");
            System.out.println("6. Ver ultimas ventas");
            System.out.println("0. Volver");

            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                    System.out.println(tienda.mostrar());
                    break;
                case 2:
                    registrarProducto();
                    break;
                case 3:
                    reponerStock();
                    break;
                case 4:
                    ventaSuelta();
                    break;
                case 5:
                    cargarProductoAReserva();
                    break;
                case 6:
                    System.out.println(tienda.mostrarVentas());
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private void registrarProducto() {

        String codigo = leerTexto("Codigo (ej. P05): ");
        String nombre = leerTexto("Nombre: ");
        double precio = leerDecimal("Precio (Q): ");
        int stock = leerEntero("Stock inicial: ");

        if (tienda.registrar(codigo, nombre, precio, stock)) {
            System.out.println("Producto registrado.");
        } else {
            System.out.println("No se pudo registrar: codigo repetido, "
                    + "datos vacios, precio invalido, stock negativo "
                    + "o limite de productos alcanzado.");
        }
    }

    private void reponerStock() {

        System.out.println(tienda.mostrar());

        String codigo = leerTexto("Codigo del producto: ");
        int unidades = leerEntero("Unidades a agregar: ");

        if (tienda.reponer(codigo, unidades)) {
            System.out.println("Stock actualizado. Ahora hay "
                    + tienda.buscar(codigo).getStock() + ".");
        } else {
            System.out.println("No se pudo reponer: producto no existe "
                    + "o la cantidad no es mayor a 0.");
        }
    }

    private Producto elegirProducto() {

        System.out.println(tienda.mostrar());

        Producto p = tienda.buscar(leerTexto("Codigo del producto: "));

        if (p == null) {
            System.out.println("Producto no encontrado.");
        }

        return p;
    }

    private void ventaSuelta() {

        Producto p = elegirProducto();

        if (p == null) {
            return;
        }

        int unidades = leerEntero("Cantidad: ");

        Venta v = tienda.venderSuelta(p, unidades);

        if (v == null) {
            System.out.println("No se pudo vender: cantidad invalida "
                    + "o stock insuficiente (hay " + p.getStock() + ").");
            return;
        }

        System.out.println("Venta registrada. Total a cobrar: "
                + Formato.dinero(v.getTotal()));
    }

    private void cargarProductoAReserva() {

        Reserva r = pedirReserva();

        if (r == null) {
            return;
        }

        Producto p = elegirProducto();

        if (p == null) {
            return;
        }

        int unidades = leerEntero("Cantidad: ");

        if (tienda.cargarAReserva(p, unidades, r)) {

            System.out.println("Producto cargado a la reserva R"
                    + r.getCodigo() + ". Nuevo total: "
                    + Formato.dinero(r.getTotal()));

        } else {
            System.out.println("No se pudo cargar: stock insuficiente "
                    + "(hay " + p.getStock() + "), cantidad invalida, "
                    + "reserva cancelada o finalizada, o ya tiene 10 servicios.");
        }
    }

    // REPORTES

    private void menuReportes() {

        int opcion = -1;

        while (opcion != 0) {

            System.out.println("\nREPORTES");
            System.out.println("1. Reporte de reservas por fechas");
            System.out.println("2. Reporte de tienda por fechas");
            System.out.println("0. Volver");

            opcion = leerEntero("Opcion: ");

            switch (opcion) {
                case 1:
                case 2:
                    mostrarReporte(opcion);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion no valida.");
            }
        }
    }

    private void mostrarReporte(int tipo) {

        LocalDate desde = leerFecha("Desde");
        LocalDate hasta = leerFecha("Hasta");

        if (hasta.isBefore(desde)) {
            System.out.println("La fecha final no puede ser antes de la inicial.");
            return;
        }

        if (tipo == 1) {
            System.out.println(reportes.resumen(
                    reservas, canchas, desde, hasta));
        } else {
            System.out.println(tienda.reporte(desde, hasta));
        }
    }

    // LECTURA DE DATOS
    // Repiten la pregunta hasta que el dato sea valido,
    // asi el programa no se cae si el usuario se equivoca.

    private String leerTexto(String mensaje) {

        System.out.print(mensaje);

        return teclado.nextLine().trim();
    }

    // Si el usuario solo presiona Enter se conserva el valor actual.
    private String leerConDefecto(String campo, String actual) {

        String texto = leerTexto(campo + " [" + actual
                + "] (Enter para dejarlo igual): ");

        return texto.isEmpty() ? actual : texto;
    }

    private int leerEntero(String mensaje) {

        while (true) {

            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero entero valido.");
            }
        }
    }

    private double leerDecimal(String mensaje) {

        while (true) {

            try {
                return Double.parseDouble(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Escribe un numero valido (ej. 25.50).");
            }
        }
    }

    private LocalDate leerFecha(String mensaje) {

        while (true) {

            String texto = leerTexto(mensaje + " (AAAA-MM-DD o 'hoy'): ");

            if (texto.equalsIgnoreCase("hoy")) {
                return LocalDate.now();
            }

            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no valida. Ejemplo: 2026-12-31");
            }
        }
    }

    private LocalTime leerHora(String mensaje) {

        while (true) {

            try {
                return LocalTime.parse(leerTexto(mensaje));
            } catch (DateTimeParseException e) {
                System.out.println("Hora no valida. Ejemplo: 18:00");
            }
        }
    }

    // Muestra una lista numerada y devuelve la opcion elegida.
    private String elegirOpcion(String titulo, String[] opciones) {

        while (true) {

            System.out.println(titulo + ":");

            for (int i = 0; i < opciones.length; i++) {
                System.out.println("  " + (i + 1) + ". " + opciones[i]);
            }

            int eleccion = leerEntero("Opcion: ");

            if (eleccion >= 1 && eleccion <= opciones.length) {
                return opciones[eleccion - 1];
            }

            System.out.println("Opcion no valida.");
        }
    }

    private boolean confirmar(String mensaje) {

        String respuesta = leerTexto(mensaje + "? (s/n): ");

        return respuesta.equalsIgnoreCase("s")
                || respuesta.equalsIgnoreCase("si");
    }
}
