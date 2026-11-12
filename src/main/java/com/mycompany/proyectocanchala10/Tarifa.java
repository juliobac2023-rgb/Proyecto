package com.mycompany.proyectocanchala10;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

// Calcula el precio de una reserva. Las tarifas de referencia NO estan
// fijas en el codigo: se leen de datos/tarifas.csv (ver ArchivosTarifas)
// y se pueden cambiar desde la pantalla de Configuracion.
public class Tarifa {

    // Tarifas de referencia por hora.
    private static double precioDia = 150;        // lunes a viernes, antes de las 17:00
    private static double precioNoche = 225;      // lunes a viernes, desde las 17:00
    private static double precioFinSemana = 250;  // sabado y domingo

    // Hora en la que empieza la tarifa de la noche.
    private static LocalTime horaNoche = LocalTime.of(17, 0);

    // Cambia las tarifas. Devuelve false si alguna no es mayor a 0.
    public static boolean configurar(double dia, double noche, double finSemana) {

        if (dia <= 0 || noche <= 0 || finSemana <= 0) {
            return false;
        }

        precioDia = dia;
        precioNoche = noche;
        precioFinSemana = finSemana;

        return true;
    }

    // Tarifa de referencia que corresponde a una fecha y hora de inicio
    // (nivel medio del proyecto: se usa la tarifa de la hora inicial).
    public static double tarifaPorHora(LocalDate fecha, LocalTime horaInicio) {

        DayOfWeek dia = fecha.getDayOfWeek();

        if (dia == DayOfWeek.SATURDAY || dia == DayOfWeek.SUNDAY) {
            return precioFinSemana;
        }

        if (!horaInicio.isBefore(horaNoche)) {
            return precioNoche;
        }

        return precioDia;
    }

    // El precio base de la cancha equivale a la tarifa de dia. Si una
    // cancha tiene un precio base distinto, las demas tarifas suben o
    // bajan en la misma proporcion.
    public static double calcular(Cancha cancha,
            LocalDate fecha, LocalTime horaInicio, int horas) {

        double tarifa = tarifaPorHora(fecha, horaInicio);

        return cancha.getPrecioBase() * tarifa / precioDia * horas;
    }

    public static double getPrecioDia() {
        return precioDia;
    }

    public static double getPrecioNoche() {
        return precioNoche;
    }

    public static double getPrecioFinSemana() {
        return precioFinSemana;
    }

    public static LocalTime getHoraNoche() {
        return horaNoche;
    }
}
