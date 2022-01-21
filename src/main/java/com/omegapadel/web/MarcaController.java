package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.omegapadel.model.Marca;
import com.omegapadel.service.MarcaService;

@Named("marcaController")
@ViewScoped
public class MarcaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1851312393297619839L;

	@Inject
	private MarcaService marcaService;

	private Collection<Marca> listaMarcas;
	private String nuevaMarca;

	@PostConstruct
	public void init() {
		this.listaMarcas = marcaService.findAll();
	}

	public void verMarcas() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaMarcas.xhtml");
	}

	public void eliminarMarca(Marca elem) throws IOException {

		Integer count = marcaService.countProductoDeMarca(elem.getId());

		if (count == 0) {
			marcaService.delete(elem);
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaMarcas.xhtml");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Existe productos de esta marca! No se puede eliminar mientras exista productos de esta marca.", ""));
		}
	}

	public void guardarMarca() throws IOException {
		Marca m = marcaService.getMarcaPorNombre(nuevaMarca);
		Marca marca = marcaService.create(nuevaMarca);

		if (m != null && m.getNombre().equals(marca.getNombre())) {

			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"La marca ya existe en base de datos", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);

		} else {
			marcaService.save(marca);
			this.nuevaMarca = null;
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaMarcas.xhtml");
		}
	}

	public Collection<Marca> getListaMarcas() {
		return listaMarcas;
	}

	public void setListaMarcas(Collection<Marca> listaMarcas) {
		this.listaMarcas = listaMarcas;
	}

	public String getNuevaMarca() {
		return nuevaMarca;
	}

	public void setNuevaMarca(String nuevaMarca) {
		this.nuevaMarca = nuevaMarca;
	}

}
