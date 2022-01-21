package com.omegapadel.web;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named("accesorioController")
@ViewScoped
public class AccesorioController implements Serializable{

	private static final long serialVersionUID = -1801997740179256443L;

	@PostConstruct
	public void init() {
		//TODO
	}
	
	public String verAccesoriosDelTipo(String nombreTipoAccesorio) {
		return "";
		//TODO
	}
	
}
