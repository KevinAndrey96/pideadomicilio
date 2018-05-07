package com.infinitec.pideadomicilio.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by romeroej on 1/24/17.
 */
@Entity
public class Comercio implements Serializable {

    public Comercio(){}


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;

    private String nombre;
    private String direccion;
    private String url;
    private String img;
    private float peso;


    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "telSort")
    private Set<String> telefonos;

    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "tagSort")
    private Set<String> tags;

    @ManyToOne
    private Zona zona;

    @ManyToOne
    private Categoria categoria;

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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Set<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(Set<String> telefonos) {
        this.telefonos = telefonos;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public Zona getZona() {
        return zona;
    }

    public void setZona(Zona zona) {
        this.zona = zona;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comercio)) return false;

        Comercio comercio = (Comercio) o;

        if (id != null ? !id.equals(comercio.id) : comercio.id != null) return false;
        if (nombre != null ? !nombre.equals(comercio.nombre) : comercio.nombre != null) return false;
        if (direccion != null ? !direccion.equals(comercio.direccion) : comercio.direccion != null) return false;
        if (url != null ? !url.equals(comercio.url) : comercio.url != null) return false;
        return !(img != null ? !img.equals(comercio.img) : comercio.img != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (direccion != null ? direccion.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        return result;
    }
}
