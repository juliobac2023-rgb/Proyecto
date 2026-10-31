/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectocanchala10;


import java.time.LocalDate;
import java.time.LocalTime;

public class Reserva {

    private int codigo;
    private LocalDate fecha;
    private Cliente cliente;
    private Cancha cancha;
    private LocalTime inicio;
    private LocalTime fin;
    private int horas;
    private double total;
    private String estado;

    private LocalDate fechaCancelacion;
    private String motivoCancelacion = "";
    private String operadorCancelacion = "";

    private ServicioAdicional[] servicios = new ServicioAdicional[10];
    private int cantidadServicios = 0;

    private Pago[] pagos = new Pago[30];
    private int cantidadPagos = 0;

    public Reserva(int codigo, LocalDate fecha,
            Cliente cliente, Cancha cancha,
            LocalTime inicio, int horas,
            double total, String estado) {

        this.codigo = codigo;
        this.fecha = fecha;
        this.cliente = cliente;
        this.cancha = cancha;
        this.inicio = inicio;
        this.fin = inicio.plusHours(horas);
        this.horas = horas;
        this.total = total;
        this.estado = estado;
    }

    public int getCodigo() {
        return codigo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public LocalTime getInicio() {
        return inicio;
    }

    public LocalTime getFin() {
        return fin;
    }

    public int getHoras() {
        return horas;
    }

    public double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFechaCancelacion() {
        return fechaCancelacion;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public String getOperadorCancelacion() {
        return operadorCancelacion;
    }

    public ServicioAdicional[] getServicios() {
        return servicios;
    }

    public int getCantidadServicios() {
        return cantidadServicios;
    }

    public Pago[] getPagos() {
        return pagos;
    }

    public int getCantidadPagos() {
        return cantidadPagos;
    }

    public boolean agregarServicio(ServicioAdicional servicio) {

        // CORREGIDO: antes se comparaba con ServicioAdicional.length, que
        // siempre valia 0, y por eso nunca dejaba agregar servicios.
        // Ahora se compara con el tamano real del arreglo (10).
        if (cantidadServicios == servicios.length
                || estado.equals("CANCELADA")
                || estado.equals("FINALIZADA")) {
            return false;
        }

        servicios[cantidadServicios] = servicio;
        cantidadServicios++;

        total += servicio.getPrecio();

        return true;
    }

    // Recupera un servicio ya guardado en un archivo.
    public void cargarServicio(ServicioAdicional servicio) {

        if (cantidadServicios < servicios.length) {
            servicios[cantidadServicios] = servicio;
            cantidadServicios++;
        }
    }

    public void cargarPago(Pago pago) {

        if (cantidadPagos < pagos.length) {
            pagos[cantidadPagos] = pago;
            cantidadPagos++;
        }
    }

    public double getPagado() {

        double suma = 0;

        for (int i = 0; i < cantidadPagos; i++) {
            suma += pagos[i].getMonto();
        }

        return suma;
    }

    public double getSaldo() {

        return Math.max(0, total - getPagado());
    }

    public boolean agregarPago(Pago pago) {

        if (estado.equals("CANCELADA")
                || cantidadPagos == pagos.length
                || pago.getMonto() <= 0
                || pago.getMonto() > getSaldo() + 0.00001) {
            return false;
        }

        pagos[cantidadPagos] = pago;
        cantidadPagos++;

        if (estado.equals("PENDIENTE")) {
            estado = "CONFIRMADA";
        }

        return true;
    }

    public boolean cambiarEstado(String nuevoEstado) {

        if (estado.equals("CANCELADA")
                || estado.equals("FINALIZADA")) {
            return false;
        }

        if (nuevoEstado.equals("EN_CURSO")
                && estado.equals("CONFIRMADA")) {

            estado = nuevoEstado;
            return true;
        }

        if (nuevoEstado.equals("FINALIZADA")
                && estado.equals("EN_CURSO")) {

            estado = nuevoEstado;
            return true;
        }

        return false;
    }

    public boolean cancelar(String motivo,
            String operador, LocalDate fechaCancelacion) {

        if (estado.equals("CANCELADA")
                || estado.equals("FINALIZADA")
                || motivo.trim().isEmpty()
                || operador.trim().isEmpty()) {
            return false;
        }

        estado = "CANCELADA";

        this.motivoCancelacion = motivo;
        this.operadorCancelacion = operador;
        this.fechaCancelacion = fechaCancelacion;

        return true;
    }

    public void cargarCancelacion(LocalDate fecha,
            String motivo, String operador) {

        fechaCancelacion = fecha;
        motivoCancelacion = motivo;
        operadorCancelacion = operador;
    }

    // CORREGIDO: se elimino el metodo getPago(), que NetBeans genero vacio
    // y lanzaba UnsupportedOperationException. Se usa getPagos().
}

    

