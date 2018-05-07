package com.infinitec.pideadomicilio.controller.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;


/**
 * Created by romeroej on 12/3/15.
 */
public class LoggerProducer {
    @Produces
    public Logger getLogger(InjectionPoint p) {
        return LoggerFactory.getLogger(p.getMember().getDeclaringClass().getName());
    }
}