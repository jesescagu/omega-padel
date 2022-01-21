package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.Comentario;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.ComentarioService;

@Named("comentarioController")
@ViewScoped
public class ComentarioController implements Serializable {

	private static final long serialVersionUID = -9086408631627462052L;


	@Inject
	private AnuncioController anuncioController;




	
	private Anuncio anuncioSeleccionado;

	@PostConstruct
	public void init() {

	}

	





}
