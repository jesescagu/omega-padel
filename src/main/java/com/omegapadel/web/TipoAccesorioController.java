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

import com.omegapadel.model.TipoAccesorio;
import com.omegapadel.service.AccesorioService;
import com.omegapadel.service.TipoAccesorioService;

@Named("tipoAccesorioController")
@ViewScoped
public class TipoAccesorioController implements Serializable {

	private static final long serialVersionUID = -2539669053860299114L;

	@Inject
	private TipoAccesorioService tipoAccesorioService;

	@Inject
	private AccesorioService accesorioService;

	private Collection<TipoAccesorio> listaTipoAccesorios;
	private String nuevaTipoAccesorio;

	@PostConstruct
	public void init() {
		this.listaTipoAccesorios = tipoAccesorioService.findAll();
	}

	public void verTipoAccesorios() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaTipoAccesorio.xhtml");
	}

	public void eliminarTipoAccesorio(TipoAccesorio elem) throws IOException {

		Integer count = accesorioService.countAccesoriosPorTipoAccesorio(elem.getId());

		if (count == 0) {
			tipoAccesorioService.delete(elem);
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaTipoAccesorio.xhtml");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Existe un accesorio de este tipo! No se puede eliminar mientras exista un accesorio de ese tipo.", ""));
		}

	}

	public void guardarTipoAccesorio() throws IOException {
		TipoAccesorio m = tipoAccesorioService.getTipoAccesorioPorNombre(nuevaTipoAccesorio);
		TipoAccesorio tipoAccesorio = tipoAccesorioService.create(nuevaTipoAccesorio);

		if (m != null && m.getNombre().equals(tipoAccesorio.getNombre())) {

			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El tipo de accesorio ya existe en base de datos", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);

		} else {
			tipoAccesorioService.save(tipoAccesorio);
			this.nuevaTipoAccesorio = null;
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaTipoAccesorio.xhtml");
		}
	}

	public Collection<TipoAccesorio> getListaTipoAccesorios() {
		return listaTipoAccesorios;
	}

	public void setListaTipoAccesorios(Collection<TipoAccesorio> listaTipoAccesorios) {
		this.listaTipoAccesorios = listaTipoAccesorios;
	}

	public String getNuevaTipoAccesorio() {
		return nuevaTipoAccesorio;
	}

	public void setNuevaTipoAccesorio(String nuevaTipoAccesorio) {
		this.nuevaTipoAccesorio = nuevaTipoAccesorio;
	}

}
