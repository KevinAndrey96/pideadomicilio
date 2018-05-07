package com.infinitec.pideadomicilio.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by romeroej on 1/30/17.
 */
@Entity
public class User {

    @Id
    private String username;
    private String password;

    private String nit;
    private String nombreEmpresa;
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }
}
