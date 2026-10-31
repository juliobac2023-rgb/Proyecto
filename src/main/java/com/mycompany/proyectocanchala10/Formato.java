package com.mycompany.proyectocanchala10;

import java.util.Locale;

// NUEVO: muestra los montos siempre como Q150.00
// (sin depender del idioma de la computadora).
public class Formato {

    public static String dinero(double monto) {

        return "Q" + String.format(Locale.US, "%.2f", monto);
    }
}
