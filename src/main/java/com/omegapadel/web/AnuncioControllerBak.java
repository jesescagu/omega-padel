package com.omegapadel.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.omegapadel.model.Anuncio;
import com.omegapadel.service.AnuncioService;

@Named("anuncioControllerBak")
@SessionScoped
public class AnuncioControllerBak implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AnuncioService anuncioService;

	private Anuncio anuncioSeleccionado;

	@PostConstruct
	public void init() {

	}

	
	public String mostrarAnuncio(Anuncio anuncio) {
		
		String vista = "verAnuncio";
		this.anuncioSeleccionado = anuncio;
		return vista;
	}
	
	
	public Anuncio getAnuncioSeleccionado() {
		return anuncioSeleccionado;
	}

	public void setAnuncioSeleccionado(Anuncio anuncioSeleccionado) {
		this.anuncioSeleccionado = anuncioSeleccionado;
	}

}
