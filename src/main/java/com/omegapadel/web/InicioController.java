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

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import com.omegapadel.model.Accesorio;
import com.omegapadel.model.Administrador;
import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.Empleado;
import com.omegapadel.model.Marca;
import com.omegapadel.model.Pala;
import com.omegapadel.model.Pelota;
import com.omegapadel.model.Producto;
import com.omegapadel.model.PuestoTrabajo;
import com.omegapadel.model.Rol;
import com.omegapadel.model.TipoAccesorio;
import com.omegapadel.model.Usuario;
import com.omegapadel.service.AccesorioService;
import com.omegapadel.service.AdministratorService;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.CestaService;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.ComentarioService;
import com.omegapadel.service.DireccionPostalService;
import com.omegapadel.service.EmpleadoService;
import com.omegapadel.service.InstitucionService;
import com.omegapadel.service.MarcaService;
import com.omegapadel.service.PalaService;
import com.omegapadel.service.PedidoService;
import com.omegapadel.service.PelotaService;
import com.omegapadel.service.ProductoService;
import com.omegapadel.service.PuestoTrabajoService;
import com.omegapadel.service.RolService;
import com.omegapadel.service.TarjetaBancariaService;
import com.omegapadel.service.TipoAccesorioService;
import com.omegapadel.service.UsuarioService;

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

	@PostConstruct
	public void init() {

		getAnunciosRelevantes();
	}

	public void verPaginaInicio() throws IOException {
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

}
