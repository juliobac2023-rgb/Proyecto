/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public class Tarifa {

    public static double calcular(Cancha cancha,
            LocalDate fecha, LocalTime horaInicio, int horas) {

        double precio = cancha.getPrecioBase();

        DayOfWeek dia = fecha.getDayOfWeek();

        if (dia == DayOfWeek.SATURDAY
                || dia == DayOfWeek.SUNDAY) {

            precio = precio * 250 / 150;

        } else if (!horaInicio.isBefore(LocalTime.of(17, 0))) {

            precio = precio * 225 / 150;
        }

        return precio * horas;
    }
}
