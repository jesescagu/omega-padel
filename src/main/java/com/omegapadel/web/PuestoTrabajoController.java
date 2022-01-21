package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.omegapadel.model.PuestoTrabajo;
import com.omegapadel.service.PuestoTrabajoService;

@Component
@ViewScoped
public class PuestoTrabajoController implements Serializable {

	private static final long serialVersionUID = -5295592703681510007L;

	@Inject
	private PuestoTrabajoService puestoTrabajoService;

	private String nuevoPuestoTrabajo;
	private List<PuestoTrabajo> listaPuestoTrabajos;

	@PostConstruct
	public void init() {
		this.listaPuestoTrabajos = puestoTrabajoService.findAll();
	}
	
	public void guardarPuestoTrabajo() throws IOException {
		PuestoTrabajo m = puestoTrabajoService.getPuestoTrabajoPorNombre(nuevoPuestoTrabajo);
		PuestoTrabajo puestoTrabajo = puestoTrabajoService.create(nuevoPuestoTrabajo);

		if (m != null && m.getNombre().equals(puestoTrabajo.getNombre())) {

			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"La puestoTrabajo ya existe en base de datos", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);

		} else {
			puestoTrabajoService.save(puestoTrabajo);
			this.nuevoPuestoTrabajo = null;
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaPuestoTrabajos.xhtml");
		}
	}

	public void eliminarPuestoTrabajo(PuestoTrabajo elem) throws IOException {

		// TODO
//		puestoTrabajoService.delete(elem);
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaPuestoTrabajo.xhtml");
	}

	public void verListaPuestoTrabajo() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaPuestoTrabajo.xhtml");
	}

	public String getNuevoPuestoTrabajo() {
		return nuevoPuestoTrabajo;
	}

	public void setNuevoPuestoTrabajo(String nuevoPuestoTrabajo) {
		this.nuevoPuestoTrabajo = nuevoPuestoTrabajo;
	}

	public List<PuestoTrabajo> getListaPuestoTrabajos() {
		return listaPuestoTrabajos;
	}

	public void setListaPuestoTrabajos(List<PuestoTrabajo> listaPuestoTrabajos) {
		this.listaPuestoTrabajos = listaPuestoTrabajos;
	}

}
