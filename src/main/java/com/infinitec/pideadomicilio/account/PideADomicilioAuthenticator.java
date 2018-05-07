/*
 * nFinite Info - 2012 
 */
package com.infinitec.pideadomicilio.account;


import com.infinitec.pideadomicilio.ejb.GeneralTool;
import com.infinitec.pideadomicilio.model.entity.User;
import org.slf4j.Logger;

import javax.ejb.Stateful;
import javax.enterprise.event.Event;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;


/**
 * @author RomeroEJ</a>
 */
@Stateful
@Named
public class PideADomicilioAuthenticator implements Serializable {

    @Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ExtendedCredentials credentials;


    @Inject
    @Authenticated
    private Event<User> loginEventSrc;

    @Inject
    private GeneralTool gt;


    public String authenticate() {


        if ((credentials.getUsername() == null) || (credentials.getPassword() == null)) {
            //gt.facesMsg(FacesContext.getCurrentInstance(), "Usuario o Password Invalido");
            //setStatus(AuthenticationStatus.FAILURE);
            return "failure";

        }


        credentials.setUsername(credentials.getUsername().toUpperCase());

        log.info("Logging in " + credentials.getUsername());


        User user = gt.validateUser(credentials);


        if (user != null) {

            return finilizeAuth(user);

        }


        log.info("Login Failed. User is not on AD");
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Usuario o password invalido", ""));

        //gt.facesMsg(FacesContext.getCurrentInstance(), "Usuario o password invalido");
        //setStatus(AuthenticationStatus.FAILURE);
        return "failure";

        //to here


    }


    public String finilizeAuth(User user) {

        loginEventSrc.fire(user);

        //gt.facesMsg(FacesContext.getCurrentInstance(), "Estas conectado como " + user.getUsername());
        return "success";


    }


    private void forceLogout(String razon) {


        log.info("Invalidando sesion por " + razon);


        //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(razon));


    }



}
