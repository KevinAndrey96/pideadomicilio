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
public class Categoria implements Serializable {

    public Categoria(){}


    //@GeneratedValue(generator = "uuid")
    //@GenericGenerator(name="uuid", strategy="uuid2")
    @Id
    private String id;

    private String nombre;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria)) return false;

        Categoria categoria = (Categoria) o;

        if (id != null ? !id.equals(categoria.id) : categoria.id != null) return false;
        return !(nombre != null ? !nombre.equals(categoria.nombre) : categoria.nombre != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        return result;
    }
}
