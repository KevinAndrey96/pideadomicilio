package com.infinitec.pideadomicilio.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by romeroej on 1/24/17.
 */
@Entity
public class Ciudad implements Serializable {

    public Ciudad(){}


    @Id
    private String id;

    private String nombre;
    private String indicativo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIndicativo() {
        return indicativo;
    }

    public void setIndicativo(String indicativo) {
        this.indicativo = indicativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ciudad)) return false;

        Ciudad ciudad = (Ciudad) o;

        if (id != null ? !id.equals(ciudad.id) : ciudad.id != null) return false;
        if (nombre != null ? !nombre.equals(ciudad.nombre) : ciudad.nombre != null) return false;
        return !(indicativo != null ? !indicativo.equals(ciudad.indicativo) : ciudad.indicativo != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (indicativo != null ? indicativo.hashCode() : 0);
        return result;
    }



}
