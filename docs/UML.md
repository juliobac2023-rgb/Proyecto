# Diagrama de clases UML — CanchaPro

Este diagrama representa el código que está implementado. Está escrito en Mermaid: GitHub lo dibuja automáticamente al abrir este archivo, y también se puede pegar en <https://mermaid.live> para exportarlo como imagen.

## Modelo y gestores

```mermaid
classDiagram
    direction LR

    class Cliente {
        -int codigo
        -String nombre
        -String telefono
        -String correo
        -LocalDate fechaRegistro
        +editar(nombre, telefono, correo) void
    }

    class Cancha {
        -String codigo
        -String nombre
        -String tipo
        -int capacidad
        -double precioBase
        -String estado
        -LocalTime horaApertura
        -LocalTime horaCierre
        +setEstado(estado) void
    }

    class Reserva {
        -int codigo
        -LocalDate fecha
        -LocalTime inicio
        -LocalTime fin
        -int horas
        -double total
        -String estado
        -LocalDate fechaCancelacion
        -String motivoCancelacion
        -String operadorCancelacion
        +agregarServicio(servicio) boolean
        +puedeAgregarServicios(cuantos) boolean
        +agregarPago(pago) boolean
        +getPagado() double
        +getSaldo() double
        +cambiarEstado(nuevoEstado) boolean
        +cancelar(motivo, operador, fecha) boolean
    }

    class ServicioAdicional {
        -String Nombre
        -double Precio
    }

    class Pago {
        -int codigo
        -LocalDateTime fecha
        -double monto
        -String metodo
        -String observacion
    }

    class gestorClientes {
        -Cliente[] clientes
        -int cantidad
        -int siguienteCodigo
        +registrar(nombre, telefono, correo) Cliente
        +editar(codigo, nombre, telefono, correo) boolean
        +buscarCodigo(codigo) Cliente
        +buscarTelefono(telefono) Cliente
        +buscarNombre(nombre) String
        +coincide(cliente, texto) boolean
    }

    class GestorCanchas {
        -Cancha[] canchas
        -int cantidad
        +registrar(...) boolean
        +buscar(codigo) Cancha
        +cambiarEstado(codigo, estado) boolean
        +estadoValido(estado) boolean
    }

    class gestorReserva {
        -Reserva[] reservas
        -int cantidad
        -int siguienteCodigo
        -int siguientePago
        +disponible(cancha, fecha, inicio, horas) boolean
        +ocupante(cancha, fecha, inicio, fin) Reserva
        +consultar(canchas, tipo, fecha, inicio, horas) String
        +registrar(cliente, cancha, fecha, inicio, horas) Reserva
        +pagar(reserva, monto, metodo, observacion) boolean
        +buscar(codigo) Reserva
        +comprobante(reserva) String
        +contarFecha(fecha) int
        +contarEstado(estado) int
        +contarConSaldo() int
        +ingresos(desde, hasta) double
    }

    class Tarifa {
        -double precioDia$
        -double precioNoche$
        -double precioFinSemana$
        -LocalTime horaNoche$
        +configurar(dia, noche, finSemana)$ boolean
        +tarifaPorHora(fecha, horaInicio)$ double
        +calcular(cancha, fecha, horaInicio, horas)$ double
    }

    class reportes {
        +resumen(reservas, canchas, desde, hasta)$ String
        +resumen(reservas, canchas, desde, hasta, cancha, estado)$ String
    }

    gestorClientes "1" o-- "0..100" Cliente
    GestorCanchas "1" o-- "0..30" Cancha
    gestorReserva "1" o-- "0..500" Reserva
    Reserva "0..*" --> "1" Cliente : es de
    Reserva "0..*" --> "1" Cancha : ocupa
    Reserva "1" *-- "0..10" ServicioAdicional
    Reserva "1" *-- "0..30" Pago
    gestorReserva ..> Tarifa : calcula el precio
    Tarifa ..> Cancha
    reportes ..> gestorReserva
    reportes ..> GestorCanchas
```

## Módulo de Tienda

```mermaid
classDiagram
    direction LR

    class Producto {
        -String codigo
        -String nombre
        -String categoria
        -double precio
        -int stock
        -int stockMinimo
        -String estado
        +descontar(unidades) boolean
        +reponer(unidades) boolean
        +stockBajo() boolean
        +estaActivo() boolean
    }

    class Venta {
        -int codigo
        -LocalDateTime fecha
        -int codigoReserva
        -String metodo
        +agregarDetalle(detalle) boolean
        +unidadesDe(codigoProducto) int
        +getTotal() double
        +getUnidades() int
        +resumen() String
        +confirmar(codigo, fecha, codigoReserva, metodo) void
        +vaciar() void
    }

    class DetalleVenta {
        -String codigoProducto
        -String nombreProducto
        -int cantidad
        -double precioUnitario
        +getSubtotal() double
    }

    class GestorTienda {
        -Producto[] productos
        -Venta[] ventas
        -int siguienteVenta
        +registrar(codigo, nombre, categoria, precio, stock, minimo) boolean
        +buscar(codigo) Producto
        +reponer(codigo, unidades) boolean
        +cambiarEstado(codigo, estado) boolean
        +nuevaVenta() Venta
        +agregarAVenta(venta, producto, unidades) boolean
        +confirmarVenta(venta, metodo, reserva) boolean
        +ingresosSueltas(desde, hasta) double
        +reporte(desde, hasta) String
    }

    class Reserva
    class ServicioAdicional

    GestorTienda "1" o-- "0..50" Producto
    GestorTienda "1" o-- "0..*" Venta
    Venta "1" *-- "1..20" DetalleVenta
    DetalleVenta "0..*" ..> "1" Producto : codigoProducto
    Venta "0..*" ..> "0..1" Reserva : codigoReserva (opcional)
    GestorTienda ..> ServicioAdicional : crea al cargar a una reserva
```

## Persistencia, configuración e interfaz

```mermaid
classDiagram
    direction TB

    class ProyectoCanchaLa10 {
        +gestorClientes clientes$
        +GestorCanchas canchas$
        +gestorReserva reservas$
        +GestorTienda tienda$
        +Archivos archivos$
        +Configuracion configuracion$
        +main(args)$ void
        +guardar()$ void
    }

    class Configuracion {
        -boolean moduloTienda
        +cargar() void
        +guardar() void
        +isModuloTienda() boolean
        +setModuloTienda(valor) void
    }

    class Archivos {
        +cargar(clientes, canchas, reservas, tienda) void
        +guardar(clientes, canchas, reservas, tienda) void
    }

    class Archivoclientes
    class ArchivosCanchas
    class ArchivosReservas
    class ArchivosServicios
    class ArchivosPagos
    class ArchivosTienda
    class ArchivosTarifas

    class FrmPrincipal
    class FrmClientes
    class FrmCanchas
    class FrmReservas
    class FrmDisponibilidad
    class FrmTienda
    class FrmReportes
    class FrmConfiguracion
    class MenuConsola

    Archivos *-- Archivoclientes
    Archivos *-- ArchivosCanchas
    Archivos *-- ArchivosReservas
    Archivos *-- ArchivosServicios
    Archivos *-- ArchivosPagos
    Archivos *-- ArchivosTienda
    Archivos *-- ArchivosTarifas

    ProyectoCanchaLa10 --> Archivos
    ProyectoCanchaLa10 --> Configuracion
    ProyectoCanchaLa10 ..> FrmPrincipal : abre
    ProyectoCanchaLa10 ..> MenuConsola : modo consola

    FrmPrincipal ..> FrmClientes
    FrmPrincipal ..> FrmCanchas
    FrmPrincipal ..> FrmReservas
    FrmPrincipal ..> FrmDisponibilidad
    FrmPrincipal ..> FrmTienda
    FrmPrincipal ..> FrmReportes
    FrmPrincipal ..> FrmConfiguracion
```

Cada JFrame usa los gestores que necesita (por ejemplo, `FrmReservas` usa `gestorClientes`, `GestorCanchas` y `gestorReserva`). Los JFrame heredan de `javax.swing.JFrame`.

## Notas de diseño

- **Composición:** los `Pago` y los `ServicioAdicional` solo existen dentro de su `Reserva`; los `DetalleVenta` solo existen dentro de su `Venta`.
- **Asociación:** una `Reserva` guarda la referencia a su `Cliente` y a su `Cancha` (objetos, no códigos).
- **Agregación:** cada gestor guarda sus objetos en un arreglo con un contador.
- **Estados:** son textos con valores fijos, validados por el modelo.
  - Cancha: `DISPONIBLE`, `MANTENIMIENTO`, `INACTIVA`.
  - Reserva: `PENDIENTE`, `CONFIRMADA`, `EN_CURSO`, `FINALIZADA`, `CANCELADA`.
  - Producto: `ACTIVO`, `INACTIVO`.
- **Herencia:** no se usa entre las clases del modelo porque no hay una relación "es un" real entre ellas; solo los JFrame heredan de `JFrame`.
