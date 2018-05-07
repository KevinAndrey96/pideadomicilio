package com.infinitec.pideadomicilio.model.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Created by romeroej on 1/24/17.
 */
@Entity
public class Banner implements Serializable {

    public Banner() {
    }


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String nombre;
    private String img;
    private String imgResp;
    private int peso;


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


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getPeso() {
        return peso;
    }


    public void setPeso(int peso) {
        this.peso = peso;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getImgResp() {
        return imgResp;
    }

    public void setImgResp(String imgResp) {
        this.imgResp = imgResp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Banner)) return false;

        Banner banner = (Banner) o;

        if (peso != banner.peso) return false;
        if (id != null ? !id.equals(banner.id) : banner.id != null) return false;
        if (nombre != null ? !nombre.equals(banner.nombre) : banner.nombre != null) return false;
        if (img != null ? !img.equals(banner.img) : banner.img != null) return false;
        return !(categoria != null ? !categoria.equals(banner.categoria) : banner.categoria != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (nombre != null ? nombre.hashCode() : 0);
        result = 31 * result + (img != null ? img.hashCode() : 0);
        result = 31 * result + peso;
        result = 31 * result + (categoria != null ? categoria.hashCode() : 0);
        return result;
    }
}
