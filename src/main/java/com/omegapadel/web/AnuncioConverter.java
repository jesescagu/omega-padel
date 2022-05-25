package com.omegapadel.web;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import com.omegapadel.model.Anuncio;
import com.omegapadel.service.AnuncioService;

@Named
@FacesConverter(value = "anuncioConverter", managed = true)
public class AnuncioConverter implements Converter<Anuncio> {

    @Inject
    private AnuncioService anuncioService;

    @Override
    public Anuncio getAsObject(FacesContext context, UIComponent component, String value) {
        if (value != null && value.trim().length() > 0) {
            try {
                return anuncioService.findById(Integer.parseInt(value)).get();
            }
            catch (NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de conversión", "No es un anuncio válido."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Anuncio value) {
        if (value != null) {
            return String.valueOf(value.getId());
        }
        else {
            return null;
        }
    }
}