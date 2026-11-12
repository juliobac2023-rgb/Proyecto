package com.mycompany.proyectocanchala10;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

// Administra los productos de la tienda, su stock y las ventas.
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

        registrar("P01", "Agua pura", "Bebidas", 5, 30, 5);
        registrar("P02", "Gatorade", "Bebidas", 12, 20, 5);
        registrar("P03", "Gaseosa", "Bebidas", 8, 30, 5);
        registrar("P04", "Snack", "Snacks", 6, 25, 5);
    }

    // PRODUCTOS

    // Registra un producto nuevo (queda ACTIVO).
    public boolean registrar(String codigo, String nombre, String categoria,
            double precio, int stock, int stockMinimo) {

        return registrar(codigo, nombre, categoria,
                precio, stock, stockMinimo, "ACTIVO");
    }

    // Tambien se usa para recuperar los productos desde el archivo.
    public boolean registrar(String codigo, String nombre, String categoria,
            double precio, int stock, int stockMinimo, String estado) {

        if (cantidad == productos.length
                || codigo.trim().isEmpty()
                || nombre.trim().isEmpty()
                || categoria.trim().isEmpty()
                || precio <= 0
                || stock < 0
                || stockMinimo < 0
                || !estadoValido(estado)
                || buscar(codigo) != null) {

            return false;
        }

        productos[cantidad] = new Producto(
                codigo.trim(), nombre.trim(), categoria.trim(),
                precio, stock, stockMinimo, estado
        );

        cantidad++;

        return true;
    }

    public boolean estadoValido(String estado) {

        return estado.equals("ACTIVO") || estado.equals("INACTIVO");
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

    // Un producto INACTIVO se conserva pero ya no se puede vender.
    public boolean cambiarEstado(String codigo, String estado) {

        Producto p = buscar(codigo);

        if (p == null || !estadoValido(estado)) {
            return false;
        }

        p.setEstado(estado);

        return true;
    }

    // VENTAS
    // Una venta se arma en tres pasos:
    //   1. nuevaVenta()            -> venta vacia (todavia no se guarda)
    //   2. agregarAVenta(...)      -> una vez por cada producto
    //   3. confirmarVenta(...)     -> descuenta el stock y la guarda

    public Venta nuevaVenta() {

        return new Venta(siguienteVenta, LocalDateTime.now(), 0, "");
    }

    // Agrega un producto a una venta que todavia no se ha confirmado.
    // No deja pasar del stock, contando lo que ya esta en la venta.
    public boolean agregarAVenta(Venta venta, Producto producto, int unidades) {

        if (venta == null
                || producto == null
                || !producto.estaActivo()
                || unidades <= 0
                || venta.unidadesDe(producto.getCodigo()) + unidades
                > producto.getStock()) {

            return false;
        }

        return venta.agregarDetalle(new DetalleVenta(
                producto.getCodigo(), producto.getNombre(),
                unidades, producto.getPrecio()
        ));
    }

    // Confirma la venta. Si reserva es null es una venta suelta (se paga
    // en caja con el metodo indicado). Si no, cada producto queda como
    // servicio adicional de la reserva y se cobra junto con ella.
    // El stock solo se descuenta cuando todo esta correcto.
    public boolean confirmarVenta(Venta venta, String metodo, Reserva reserva) {

        if (venta == null || venta.getCantidadDetalles() == 0) {
            return false;
        }

        if (reserva == null && metodo.trim().isEmpty()) {
            return false;
        }

        // Primero se revisa todo, sin cambiar nada.
        for (int i = 0; i < venta.getCantidadDetalles(); i++) {

            String codigo = venta.getDetalles()[i].getCodigoProducto();

            Producto p = buscar(codigo);

            if (p == null
                    || !p.estaActivo()
                    || venta.unidadesDe(codigo) > p.getStock()) {
                return false;
            }
        }

        if (reserva != null
                && !reserva.puedeAgregarServicios(venta.getCantidadDetalles())) {
            return false;
        }

        // Ahora si: se descuenta el inventario y se carga a la reserva.
        for (int i = 0; i < venta.getCantidadDetalles(); i++) {

            DetalleVenta d = venta.getDetalles()[i];

            buscar(d.getCodigoProducto()).descontar(d.getCantidad());

            if (reserva != null) {

                reserva.agregarServicio(new ServicioAdicional(
                        d.getNombreProducto() + " x" + d.getCantidad(),
                        d.getSubtotal()
                ));
            }
        }

        if (reserva == null) {
            venta.confirmar(siguienteVenta, LocalDateTime.now(),
                    0, metodo.trim());
        } else {
            venta.confirmar(siguienteVenta, LocalDateTime.now(),
                    reserva.getCodigo(), "CARGO_A_RESERVA");
        }

        cargar(venta);

        return true;
    }

    // Atajo: venta suelta de un solo producto.
    public Venta venderSuelta(Producto producto, int unidades, String metodo) {

        Venta venta = nuevaVenta();

        if (!agregarAVenta(venta, producto, unidades)
                || !confirmarVenta(venta, metodo, null)) {
            return null;
        }

        return venta;
    }

    public Venta venderSuelta(Producto producto, int unidades) {

        return venderSuelta(producto, unidades, "EFECTIVO");
    }

    // Atajo: carga un solo producto a una reserva.
    public boolean cargarAReserva(Producto producto,
            int unidades, Reserva reserva) {

        if (reserva == null) {
            return false;
        }

        Venta venta = nuevaVenta();

        return agregarAVenta(venta, producto, unidades)
                && confirmarVenta(venta, "", reserva);
    }

    // Guarda la venta en el arreglo. Tambien se usa para recuperar
    // las ventas desde el archivo.
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

    public Venta buscarVenta(int codigo) {

        for (int i = 0; i < cantidadVentas; i++) {

            if (ventas[i].getCodigo() == codigo) {
                return ventas[i];
            }
        }

        return null;
    }

    // CONSULTAS Y REPORTES

    public String mostrar() {

        if (cantidad == 0) {
            return "No hay productos.";
        }

        String texto = "PRODUCTOS\n";

        for (int i = 0; i < cantidad; i++) {

            Producto p = productos[i];

            texto += p.getCodigo()
                    + " - " + p.getNombre()
                    + " (" + p.getCategoria() + ")"
                    + " - " + Formato.dinero(p.getPrecio())
                    + " - Stock: " + p.getStock()
                    + (p.stockBajo() ? "  (BAJO)" : "")
                    + (p.estaActivo() ? "" : "  [INACTIVO]")
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
                    + " | " + v.resumen()
                    + " | " + Formato.dinero(v.getTotal())
                    + " | " + (v.getCodigoReserva() == 0
                    ? "Venta suelta (" + v.getMetodo() + ")"
                    : "Cargada a R" + v.getCodigoReserva())
                    + "\n";
        }

        return texto;
    }

    // Total de las ventas sueltas (cobradas en caja) entre dos fechas.
    public double ingresosSueltas(LocalDate desde, LocalDate hasta) {

        double total = 0;

        for (int i = 0; i < cantidadVentas; i++) {

            LocalDate dia = ventas[i].getFecha().toLocalDate();

            if (ventas[i].getCodigoReserva() == 0
                    && !dia.isBefore(desde) && !dia.isAfter(hasta)) {

                total += ventas[i].getTotal();
            }
        }

        return total;
    }

    // Reporte de ventas entre dos fechas.
    // Las ventas cargadas a reservas se cobran con la reserva (ya
    // aparecen en los pagos del reporte de reservas), por eso se
    // muestran aparte de las ventas sueltas.
    public String reporte(LocalDate desde, LocalDate hasta) {

        double sueltas = 0;
        double cargadas = 0;
        int unidades = 0;
        int numeroVentas = 0;

        // Un contador por producto (misma posicion que en productos[]).
        int[] vendidas = new int[productos.length];

        // Categorias encontradas y cuanto se vendio de cada una.
        String[] categorias = new String[productos.length + 1];
        double[] montos = new double[productos.length + 1];
        int cantidadCategorias = 0;

        for (int i = 0; i < cantidadVentas; i++) {

            Venta v = ventas[i];

            LocalDate dia = v.getFecha().toLocalDate();

            if (dia.isBefore(desde) || dia.isAfter(hasta)) {
                continue;
            }

            numeroVentas++;

            if (v.getCodigoReserva() == 0) {
                sueltas += v.getTotal();
            } else {
                cargadas += v.getTotal();
            }

            unidades += v.getUnidades();

            for (int k = 0; k < v.getCantidadDetalles(); k++) {

                DetalleVenta d = v.getDetalles()[k];

                String categoria = "Sin categoria";

                for (int j = 0; j < cantidad; j++) {

                    if (productos[j].getCodigo()
                            .equalsIgnoreCase(d.getCodigoProducto())) {

                        vendidas[j] += d.getCantidad();
                        categoria = productos[j].getCategoria();
                    }
                }

                // Busca la categoria; si no esta se agrega al final.
                int pos = -1;

                for (int j = 0; j < cantidadCategorias; j++) {

                    if (categorias[j].equalsIgnoreCase(categoria)) {
                        pos = j;
                    }
                }

                if (pos == -1) {
                    pos = cantidadCategorias;
                    categorias[pos] = categoria;
                    cantidadCategorias++;
                }

                montos[pos] += d.getSubtotal();
            }
        }

        String texto = "REPORTE DE TIENDA DEL " + desde
                + " AL " + hasta + "\n\n";

        texto += "Numero de ventas: " + numeroVentas
                + "\nVentas sueltas (cobradas en caja): "
                + Formato.dinero(sueltas)
                + "\nVentas cargadas a reservas: "
                + Formato.dinero(cargadas)
                + "\nTotal vendido: " + Formato.dinero(sueltas + cargadas)
                + "\nUnidades vendidas: " + unidades
                + "\nTicket promedio: "
                + (numeroVentas == 0
                ? "Sin datos"
                : Formato.dinero((sueltas + cargadas) / numeroVentas));

        texto += "\n\nINGRESOS POR CATEGORIA:\n";

        if (cantidadCategorias == 0) {
            texto += "Sin datos\n";
        }

        for (int j = 0; j < cantidadCategorias; j++) {
            texto += categorias[j] + ": " + Formato.dinero(montos[j]) + "\n";
        }

        texto += "\nUNIDADES POR PRODUCTO:\n";

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

        texto += "\n\nPRODUCTOS CON STOCK BAJO (en su minimo o menos):\n";

        boolean hayBajos = false;

        for (int j = 0; j < cantidad; j++) {

            if (productos[j].stockBajo()) {

                texto += productos[j].getNombre()
                        + ": " + productos[j].getStock()
                        + " (minimo " + productos[j].getStockMinimo() + ")\n";

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
