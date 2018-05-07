package com.infinitec.pideadomicilio.converters;

/**
 * Created by romeroej on 1/25/17.
 */

import com.infinitec.pideadomicilio.model.entity.Ciudad;
import com.infinitec.pideadomicilio.service.DataService;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;


@FacesConverter("ciudadConverter")
public class CiudadConverter implements Converter {

    @Inject
    private DataService service;

    public Object getAsObject(FacesContext fc, UIComponent uic, String value) {


        if (value != null && value.trim().length() > 0) {
            try {


                Ciudad ciudad = service.getCiudadList().stream().filter(c -> c.getId().equals(value)).findAny().get();
                return ciudad;


            } catch (Exception e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de conversion", "No es una ciudad valida"));
            }
        } else {
            return null;
        }
    }

    public String getAsString(FacesContext fc, UIComponent uic, Object object) {
        if (object != null) {
            return ((Ciudad) object).getId();
        } else {
            return null;
        }
    }
}