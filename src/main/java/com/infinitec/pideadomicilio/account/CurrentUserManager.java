
package com.infinitec.pideadomicilio.account;


import com.infinitec.pideadomicilio.model.entity.User;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.Serializable;


@SessionScoped
@Named
public class CurrentUserManager implements Serializable {
    private User currentUser;


    @Inject
    private Logger log;

    @PersistenceContext
    private EntityManager em;

    @Inject
    PideADomicilioAuthenticator auth;


    public String login() {


        log.info("Performing Login...");
        return auth.authenticate();

    }

    public boolean isLoggedIn() {

        return currentUser != null;

    }

    @Transactional
    public void logout() {

        log.info("Performing Logout (user unlock)...");


        if (currentUser == null)
            return;


        currentUser = null;
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
            FacesContext.getCurrentInstance().getExternalContext().redirect("/");

        } catch (Exception ex) {
            log.error("Concurrent Lock {}", ex.getMessage());
            //ex.printStackTrace();
        }

    }

    @Produces
    @Authenticated
    @Named("currentUser")
    public User getCurrentAccount() {
        return currentUser;
    }

    // Injecting HttpServletRequest instead of HttpSession as the latter conflicts with a Weld bean on GlassFish 3.0.1
    public void onLogin(@Observes @Authenticated User user, HttpServletRequest request) {
        currentUser = user;


        // reward authenticated users with a longer session
        // default is kept short to prevent search engines from driving up # of sessions
        //int timeoutsecs = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("timeout"));
        //int timeoutsecsAdm = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getInitParameter("timeoutadmin"));


        log.info("Timeout: " + request.getSession().getMaxInactiveInterval() + "");


    }

    @PostConstruct
    public void executePostConstruct() {
        log.info("Postconstruct for " + this.getClass().getName());


    }

    @PreDestroy
    public void beforeLogout() {


    }


}
