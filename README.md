# CanchaPro — Complejo Deportivo "La 10"

Sistema de gestión y reservas de canchas sintéticas. Proyecto final de Programación 2 (Universidad Mariano Gálvez de Guatemala).

Hecho en Java con Programación Orientada a Objetos, interfaz gráfica Swing (JFrame Form de NetBeans) y persistencia en archivos CSV. No usa base de datos.

## Requisitos

- **Java 26** (JDK 26). Es el que trae Apache NetBeans 26.
- Apache NetBeans (para abrir el proyecto y editar los JFrame en la vista Design).

## Cómo ejecutar

### Desde NetBeans

1. `File > Open Project` y elegir esta carpeta.
2. Presionar **F6** (Run Project). Se abre la pantalla de inicio.

La clase principal es `com.mycompany.proyectocanchala10.ProyectoCanchaLa10`.

### Con el ejecutable .jar

Desde la carpeta del proyecto (donde está la carpeta `datos`):

```bash
java -jar CanchaPro.jar
```

Para usar la versión de consola en lugar de las ventanas:

```bash
java -jar CanchaPro.jar consola
```

El `.jar` se vuelve a generar en NetBeans con **Clean and Build** (queda en `target/CanchaPro.jar`).

> El programa busca la carpeta `datos` en la carpeta desde donde se ejecuta. Por eso el `.jar` debe ejecutarse desde la carpeta del proyecto.

## Archivos de datos

Todos están en la carpeta `datos/`. Se leen al iniciar y se escriben al cerrar el programa (o con **Configuración > Guardar datos ahora**).

| Archivo | Contenido |
|---|---|
| `clientes.csv` | código, nombre, teléfono, correo, fecha de registro |
| `canchas.csv` | código, nombre, tipo, capacidad, precio base, estado, apertura, cierre |
| `reservas.csv` | código, fecha, cliente, cancha, inicio, horas, total, estado, fecha de cancelación, motivo, operador |
| `servicios.csv` | reserva, nombre del servicio, precio |
| `pagos.csv` | reserva, código de pago, fecha y hora, monto, método, observación |
| `tarifas.csv` | nombre de la tarifa, precio por hora |
| `productos.csv` | código, nombre, categoría, precio, existencia, stock mínimo, estado |
| `ventas.csv` | número, fecha, reserva (0 = venta suelta), total, método |
| `detalle_ventas.csv` | venta, producto, nombre, cantidad, precio unitario, subtotal |
| `configuracion.txt` | `modulo_tienda=true` o `modulo_tienda=false` |

Si un archivo no existe, el sistema arranca con los datos por defecto (las 3 canchas del caso de estudio y productos de ejemplo). Si una línea está dañada, se omite con un aviso y el programa sigue funcionando.

## Restaurar los datos de prueba

La carpeta `datos_prueba/` tiene un juego de datos listo para la demostración: 6 clientes, 3 canchas, 9 reservas (finalizadas, confirmadas, pendiente y cancelada), pagos, servicios, productos y ventas.

Con el programa **cerrado**, copiar su contenido sobre `datos/`:

```bash
cp datos_prueba/* datos/
```

En Windows también se puede copiar y pegar los archivos con el Explorador.

## Módulo de Tienda (opcional)

Se activa o desactiva sin recompilar, de dos formas:

- En el programa: **Configuración > Módulo de Tienda / Cafetería activado**.
- Editando `datos/configuracion.txt` con el programa cerrado: `modulo_tienda=false`.

Con el módulo desactivado desaparecen el botón **Tienda** y el reporte de tienda. Las reservas siguen funcionando igual.

## Pantallas

| Pantalla | Qué hace |
|---|---|
| Inicio | Menú y resumen del día: reservas de hoy, en curso, pendientes, con saldo e ingresos |
| Clientes | Registrar, editar y buscar por código, nombre o teléfono |
| Canchas | Registrar canchas y cambiar su estado |
| Reservas | Nueva reserva, precio automático, pagos, servicios, cambio de estado, cancelación y comprobante |
| Disponibilidad | Tabla de un día: una fila por hora y una columna por cancha |
| Tienda | Productos, inventario y ventas con varios productos (en caja o cargadas a una reserva) |
| Reportes | Reporte de reservas con filtros por fechas, cancha y estado; reporte de tienda |
| Configuración | Módulo de tienda, tarifas por hora y guardado de datos |

## Estructura del código

```
src/main/java/com/mycompany/proyectocanchala10/
  ProyectoCanchaLa10.java   clase principal
  Cliente, Cancha, Reserva, ServicioAdicional, Pago      modelo de reservas
  Producto, Venta, DetalleVenta                          modelo de tienda
  gestorClientes, GestorCanchas, gestorReserva, GestorTienda   reglas de negocio
  Tarifa, reportes, Formato, Configuracion               cálculo, reportes y configuración
  Archivos, Archivoclientes, ArchivosCanchas, ...        persistencia en CSV
  MenuConsola                                            versión de consola
  vistas/                                                JFrame Form (.java + .form)
```

La documentación está en la carpeta `docs/`:

- [docs/DocumentoTecnico.md](docs/DocumentoTecnico.md): descripción, clases, reglas, archivos, pruebas y conclusiones.
- [docs/UML.md](docs/UML.md): diagrama de clases.
- `docs/capturas/`: capturas de cada pantalla.
