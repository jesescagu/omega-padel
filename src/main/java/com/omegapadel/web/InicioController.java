package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.omegapadel.model.Anuncio;
import com.omegapadel.service.AnuncioService;

@Named("inicioController")
@ViewScoped
public class InicioController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7195815261233628687L;

	Logger logger = LoggerFactory.getLogger(InicioController.class);

	@Inject
	private AnuncioService anuncioService;

	public List<Anuncio> listaAnuncios = new ArrayList<>();
	public Anuncio selectedProduct;

	public List<Anuncio> listaAnunciosFiltrados;
	public String textoBuscador;

	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		String s = (String) context.getExternalContext().getSessionMap().get("paginaBuscador");

		if(s == null || s.equals("false")) {
			getAnunciosRelevantes();
		}else {
			String texto = (String) context.getExternalContext().getSessionMap().get("textoBuscador");
			this.listaAnunciosFiltrados = anuncioService.getAnunciosPorTextoBuscador(texto);
		}
	}

	public void verPaginaInicio() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("paginaBuscador");

		FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");

	}

	public void verProducto() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("verProducto.xhtml");
	}

	private void getAnunciosRelevantes() {
		if (this.listaAnuncios == null || this.listaAnuncios.isEmpty()) {
			this.listaAnuncios = anuncioService.getAnunciosRelevantes();
		}
	}

	public List<Anuncio> completeAnuncio(String texto) {
		String queryLowerCase = texto.toLowerCase();
		this.listaAnunciosFiltrados = anuncioService.getAnunciosPorTextoBuscador(queryLowerCase);
		
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("textoBuscador", queryLowerCase);
		
		return this.listaAnunciosFiltrados;
	}

	public void verAnunciosFiltrados() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("paginaBuscador", "true");

		FacesContext.getCurrentInstance().getExternalContext().redirect("buscar.xhtml");

	}

	public List<Anuncio> getListaAnuncios() {
		return listaAnuncios;
	}

	public void setListaAnuncios(List<Anuncio> listaAnuncios) {
		this.listaAnuncios = listaAnuncios;
	}

	public Anuncio getSelectedProduct() {
		return selectedProduct;
	}

	public void setSelectedProduct(Anuncio selectedProduct) {
		this.selectedProduct = selectedProduct;
	}

	public List<Anuncio> getListaAnunciosFiltrados() {
		return listaAnunciosFiltrados;
	}

	public void setListaAnunciosFiltrados(List<Anuncio> listaAnunciosFiltrados) {
		this.listaAnunciosFiltrados = listaAnunciosFiltrados;
	}

	public String getTextoBuscador() {
		return textoBuscador;
	}

	public void setTextoBuscador(String textoBuscador) {
		this.textoBuscador = textoBuscador;
	}

}
