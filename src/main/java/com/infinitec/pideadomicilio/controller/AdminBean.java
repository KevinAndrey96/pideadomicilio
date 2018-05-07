package com.infinitec.pideadomicilio.controller;


import com.infinitec.pideadomicilio.account.CurrentUserManager;
import com.infinitec.pideadomicilio.ejb.GeneralTool;
import com.infinitec.pideadomicilio.model.entity.User;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by romeroej on 1/24/17.
 */

@Named
@ViewScoped
@Transactional
public class AdminBean implements Serializable {


    private static final long serialVersionUID = 1L;

    @Inject
    private Logger log;

    @Inject
    private GeneralTool gt;

    @PersistenceContext(unitName = "mongo-ogm")
    private EntityManager em;

    @Inject
    private CurrentUserManager cum;

    @PostConstruct
    public void postConstruct() {
        log.info("Postconstruct " + this.getClass().getSimpleName());
        reload();

    }

    //logic

    public void reload() {
        if (cum.getCurrentAccount().getRole().equals("ADMIN"))
            fetchUsers();


    }

    public void fetchUsers() {
        Query query = em
                .createQuery("from User ");

        userList = query.getResultList();
        log.info(String.format("%s users en catalogo", userList.size()));
    }




    public void addUser() {
        user = new User();
    }

    public void edituser(User obj) {
        user = obj;
        merge = true;
    }

    public void remove(Object obj) {
        gt.remove(obj);
        this.reload();
    }

    public void saveUsuario() {
        try {
            gt.createUser(user, merge);
        }catch (Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ya existe un usuario con ese username.", ""));
            return;
        }

        this.reload();
        user = null;
        merge = false;
    }


    public void cancelar() {
        user = null;
        merge = false;
    }

    //properties
    private List<User> userList = new ArrayList<>();
    private User user = null;
    private boolean merge = false;


    //getters


    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getEditable() {
        return merge;
    }



    public void cambiarPassword()
    {
        if(nuevoPassword.equals(nuevoPassword2))
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambio Exitoso.", ""));
            User user = cum.getCurrentAccount();
            user.setPassword(gt.encrypt(nuevoPassword));

            gt.persist(user,true);

            RequestContext.getCurrentInstance().execute("PF('dlg2').hide();");

        }else
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: Los Passwords ingresados con son iguales", ""));

            log.info("Password no concuerdan.");
            nuevoPassword=nuevoPassword2="";
        }
    }

    private String nuevoPassword="";
    private String nuevoPassword2="";

    public String getNuevoPassword() {
        return nuevoPassword;
    }

    public void setNuevoPassword(String nuevoPassword) {
        this.nuevoPassword = nuevoPassword;
    }

    public String getNuevoPassword2() {
        return nuevoPassword2;
    }

    public void setNuevoPassword2(String nuevoPassword2) {
        this.nuevoPassword2 = nuevoPassword2;
    }
}

