package com.infinitec.pideadomicilio.model.entity.pojo;

import com.infinitec.pideadomicilio.model.entity.Comercio;

/**
 * Created by romeroej on 2/1/17.
 */
public class ResumenBusqueda {
    private Comercio comercio;
    private String busqueda;
    private String tipo;
    private String resultado;
    private float cantidad;

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public String getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(String busqueda) {
        this.busqueda = busqueda;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    @Override
    public String toString() {
        return "ResumenBusqueda{" +
                "comercio=" + comercio.getNombre() +
                ", busqueda='" + busqueda + '\'' +
                ", tipo='" + tipo + '\'' +
                ", resultado='" + resultado + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
