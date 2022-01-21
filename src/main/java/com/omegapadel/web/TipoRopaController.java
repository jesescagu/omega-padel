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

import com.omegapadel.model.TipoRopa;
import com.omegapadel.service.RopaService;
import com.omegapadel.service.TipoRopaService;

@Named("tipoRopaController")
@ViewScoped
public class TipoRopaController implements Serializable {

	private static final long serialVersionUID = -2539669053860299114L;

	@Inject
	private TipoRopaService tipoRopaService;
	@Inject
	private RopaService ropaService;

	private Collection<TipoRopa> listaTipoRopas;
	private String nuevaTipoRopa;
	private String nuevaTipoTalla;

	@PostConstruct
	public void init() {
		this.listaTipoRopas = tipoRopaService.findAll();
	}

	public void verTipoRopa() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaTipoRopa.xhtml");
	}

	public void eliminarTipoRopa(TipoRopa elem) throws IOException {

		Integer count = ropaService.countRopaPorTipoRopa(elem.getId());

		if (count == 0) {
			tipoRopaService.delete(elem);
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaTipoRopa.xhtml");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"Existe un ropa de este tipo! No se puede eliminar mientras exista un ropa de ese tipo.", ""));
		}

	}

	public void guardarTipoRopa() throws IOException {
		TipoRopa m = tipoRopaService.getTipoRopaPorNombre(nuevaTipoRopa);
		TipoRopa tipoRopa = tipoRopaService.create(nuevaTipoRopa, nuevaTipoTalla);

		if (m != null && m.getTipoRopa().equals(tipoRopa.getTipoRopa())) {

			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El tipo de ropa ya existe en base de datos", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);

		} else {
			tipoRopaService.save(tipoRopa);
			this.nuevaTipoRopa = null;
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaTipoRopa.xhtml");
		}
	}

	public Collection<TipoRopa> getListaTipoRopas() {
		return listaTipoRopas;
	}

	public void setListaTipoRopas(Collection<TipoRopa> listaTipoRopas) {
		this.listaTipoRopas = listaTipoRopas;
	}

	public String getNuevaTipoRopa() {
		return nuevaTipoRopa;
	}

	public void setNuevaTipoRopa(String nuevaTipoRopa) {
		this.nuevaTipoRopa = nuevaTipoRopa;
	}

	public String getNuevaTipoTalla() {
		return nuevaTipoTalla;
	}

	public void setNuevaTipoTalla(String nuevaTipoTalla) {
		this.nuevaTipoTalla = nuevaTipoTalla;
	}

}
