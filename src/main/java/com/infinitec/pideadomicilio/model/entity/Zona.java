package com.infinitec.pideadomicilio.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by romeroej on 1/24/17.
 */
@Entity
public class Zona implements Serializable {

    public Zona()
    {}

    //@GeneratedValue(generator = "uuid")
    //@GenericGenerator(name="uuid", strategy="uuid2")
    @Id
    private String id;

    private String nombre;

    @ManyToOne
    private Ciudad ciudad;


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

    public Ciudad getCiudad() {
        return ciudad;
    }

    public void setCiudad(Ciudad ciudad) {
        this.ciudad = ciudad;
    }

    @Transient
    public String getNombreZona()
    {
        return nombre;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zona)) return false;

        Zona zona = (Zona) o;

        if (id != null ? !id.equals(zona.id) : zona.id != null) return false;
        if (nombre != null ? !nombre.equals(zona.nombre) : zona.nombre != null) return false;
        return !(ciudad != null ? !ciudad.equals(zona.ciudad) : zona.ciudad != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (ciudad != null ? ciudad.hashCode() : 0);
        return result;
    }
}
