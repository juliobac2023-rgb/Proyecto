package com.mycompany.proyectocanchala10;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

// NUEVO: administra los productos de la tienda, su stock y las ventas.
public class GestorTienda {

    private Producto[] productos = new Producto[50];
    private int cantidad = 0;

    private Venta[] ventas = new Venta[200];
    private int cantidadVentas = 0;
    private int siguienteVenta = 1;

    // Productos de ejemplo, igual que las canchas de ejemplo.
    public void cargarEjemplos() {

        if (cantidad != 0) {
            return;
        }

        registrar("P01", "Agua pura", 5, 30);
        registrar("P02", "Gatorade", 12, 20);
        registrar("P03", "Gaseosa", 8, 30);
        registrar("P04", "Snack", 6, 25);
    }

    public boolean registrar(String codigo, String nombre,
            double precio, int stock) {

        if (cantidad == productos.length
                || codigo.trim().isEmpty()
                || nombre.trim().isEmpty()
                || precio <= 0
                || stock < 0
                || buscar(codigo) != null) {

            return false;
        }

        productos[cantidad] = new Producto(
                codigo.trim(), nombre.trim(), precio, stock
        );

        cantidad++;

        return true;
    }

    public Producto buscar(String codigo) {

        for (int i = 0; i < cantidad; i++) {

            if (productos[i].getCodigo()
                    .equalsIgnoreCase(codigo.trim())) {
                return productos[i];
            }
        }

        return null;
    }

    public boolean reponer(String codigo, int unidades) {

        Producto p = buscar(codigo);

        return p != null && p.reponer(unidades);
    }

    // Venta suelta: se paga en caja y descuenta el stock.
    public Venta venderSuelta(Producto producto, int unidades) {

        if (producto == null || !producto.descontar(unidades)) {
            return null;
        }

        return guardarVenta(producto, unidades, 0);
    }

    // Carga el producto a una reserva (queda como servicio adicional
    // y se cobra junto con la reserva). Descuenta el stock solo si
    // la reserva acepto el servicio.
    public boolean cargarAReserva(Producto producto,
            int unidades, Reserva reserva) {

        if (producto == null
                || reserva == null
                || unidades <= 0
                || unidades > producto.getStock()) {

            return false;
        }

        ServicioAdicional servicio = new ServicioAdicional(
                producto.getNombre() + " x" + unidades,
                producto.getPrecio() * unidades
        );

        if (!reserva.agregarServicio(servicio)) {
            return false;
        }

        producto.descontar(unidades);

        guardarVenta(producto, unidades, reserva.getCodigo());

        return true;
    }

    private Venta guardarVenta(Producto producto,
            int unidades, int codigoReserva) {

        Venta venta = new Venta(
                siguienteVenta,
                LocalDateTime.now(),
                producto.getCodigo(),
                producto.getNombre(),
                unidades,
                producto.getPrecio() * unidades,
                codigoReserva
        );

        cargar(venta);

        return venta;
    }

    // Tambien se usa para recuperar las ventas desde el archivo.
    public void cargar(Venta venta) {

        // Si el arreglo se llena, se duplica su tamano para no perder ventas.
        if (cantidadVentas == ventas.length) {
            ventas = Arrays.copyOf(ventas, ventas.length * 2);
        }

        ventas[cantidadVentas] = venta;
        cantidadVentas++;

        if (venta.getCodigo() >= siguienteVenta) {
            siguienteVenta = venta.getCodigo() + 1;
        }
    }

    public String mostrar() {

        if (cantidad == 0) {
            return "No hay productos.";
        }

        String texto = "PRODUCTOS\n";

        for (int i = 0; i < cantidad; i++) {

            Producto p = productos[i];

            texto += p.getCodigo()
                    + " - " + p.getNombre()
                    + " - " + Formato.dinero(p.getPrecio())
                    + " - Stock: " + p.getStock()
                    + (p.getStock() <= 5 ? "  (BAJO)" : "")
                    + "\n";
        }

        return texto;
    }

    // Muestra las ultimas ventas registradas.
    public String mostrarVentas() {

        if (cantidadVentas == 0) {
            return "No hay ventas.";
        }

        String texto = "ULTIMAS VENTAS\n";

        int inicio = Math.max(0, cantidadVentas - 30);

        for (int i = inicio; i < cantidadVentas; i++) {

            Venta v = ventas[i];

            texto += "V" + v.getCodigo()
                    + " | " + v.getFecha().toLocalDate()
                    + " | " + v.getNombreProducto()
                    + " x" + v.getCantidad()
                    + " | " + Formato.dinero(v.getTotal())
                    + " | " + (v.getCodigoReserva() == 0
                    ? "Venta suelta"
                    : "Cargada a R" + v.getCodigoReserva())
                    + "\n";
        }

        return texto;
    }

    // Reporte de ventas entre dos fechas.
    // Las ventas cargadas a reservas se cobran con la reserva (ya
    // aparecen en los pagos del reporte de reservas), por eso se
    // muestran aparte de las ventas sueltas.
    public String reporte(LocalDate desde, LocalDate hasta) {

        double sueltas = 0;
        double cargadas = 0;
        int unidades = 0;

        int[] vendidas = new int[productos.length];

        for (int i = 0; i < cantidadVentas; i++) {

            Venta v = ventas[i];

            LocalDate dia = v.getFecha().toLocalDate();

            if (dia.isBefore(desde) || dia.isAfter(hasta)) {
                continue;
            }

            if (v.getCodigoReserva() == 0) {
                sueltas += v.getTotal();
            } else {
                cargadas += v.getTotal();
            }

            unidades += v.getCantidad();

            for (int j = 0; j < cantidad; j++) {

                if (productos[j].getCodigo()
                        .equals(v.getCodigoProducto())) {
                    vendidas[j] += v.getCantidad();
                }
            }
        }

        String texto = "REPORTE DE TIENDA DEL " + desde
                + " AL " + hasta + "\n\n";

        texto += "Ventas sueltas (cobradas en caja): "
                + Formato.dinero(sueltas)
                + "\nVentas cargadas a reservas: "
                + Formato.dinero(cargadas)
                + "\nUnidades vendidas: " + unidades;

        texto += "\n\nUNIDADES POR PRODUCTO:\n";

        int mayor = -1;

        for (int j = 0; j < cantidad; j++) {

            texto += productos[j].getNombre()
                    + ": " + vendidas[j] + "\n";

            if (vendidas[j] > 0
                    && (mayor == -1 || vendidas[j] > vendidas[mayor])) {
                mayor = j;
            }
        }

        texto += "Producto mas vendido: "
                + (mayor == -1
                ? "Sin datos"
                : productos[mayor].getNombre());

        texto += "\n\nPRODUCTOS CON STOCK BAJO (5 o menos):\n";

        boolean hayBajos = false;

        for (int j = 0; j < cantidad; j++) {

            if (productos[j].getStock() <= 5) {

                texto += productos[j].getNombre()
                        + ": " + productos[j].getStock() + "\n";

                hayBajos = true;
            }
        }

        if (!hayBajos) {
            texto += "Ninguno\n";
        }

        return texto;
    }

    public Producto[] getProductos() {
        return productos;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Venta[] getVentas() {
        return ventas;
    }

    public int getCantidadVentas() {
        return cantidadVentas;
    }
}
