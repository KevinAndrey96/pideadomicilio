package com.infinitec.pideadomicilio.controller.init;


import org.omnifaces.cdi.Startup;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by romeroej on 2/3/16.
 */
@Named
@Startup
public class StartupBean {
    public enum States {BEFORESTARTED, STARTED, PAUSED, SHUTTINGDOWN}

    ;


    @Inject
    private Logger log;


    private States state;

    @PostConstruct
    public void initialize() {
        state = States.BEFORESTARTED;
        // Perform intialization


        log.info("Service Starting");
        state = States.STARTED;
        log.info("Service Started");
    }

    @PreDestroy
    public void terminate() {
        state = States.SHUTTINGDOWN;
        // Perform termination
        log.info("Shut down in progress");
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
    }
}