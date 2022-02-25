package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;

import com.omegapadel.model.Empleado;
import com.omegapadel.model.PuestoTrabajo;
import com.omegapadel.service.PuestoTrabajoService;

@Named("puestoTrabajoController")
@ViewScoped
public class PuestoTrabajoController implements Serializable {

	private static final long serialVersionUID = -5295592703681510007L;

	@Inject
	private PuestoTrabajoService puestoTrabajoService;

	private String nuevoPuestoTrabajo;
	private List<PuestoTrabajo> listaPuestoTrabajos;

	@PostConstruct
	public void init() {
		this.listaPuestoTrabajos = puestoTrabajoService.findAllActivos();
	}

	public void guardarPuestoTrabajo() throws IOException {
		PuestoTrabajo m = puestoTrabajoService.getPuestoTrabajoPorNombre(nuevoPuestoTrabajo);
		PuestoTrabajo puestoTrabajo = puestoTrabajoService.create(nuevoPuestoTrabajo);

		if (m != null && m.getNombre().equals(puestoTrabajo.getNombre())) {

			if (!m.isActivo()) {

				m.setActivo(true);
				puestoTrabajoService.save(m);
				this.nuevoPuestoTrabajo = null;
				FacesContext.getCurrentInstance().getExternalContext().redirect("listaPuestoTrabajo.xhtml");
				
			} else {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_WARN,
						"El puesto de trabajo ya existe en base de datos", "");
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			}
		} else {
			puestoTrabajoService.save(puestoTrabajo);
			this.nuevoPuestoTrabajo = null;
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaPuestoTrabajo.xhtml");
		}
	}

	public void eliminarPuestoTrabajo(PuestoTrabajo elem) throws IOException {

		// TODO Mirar si hay algun trabajador activo con este puesto de trabajo o
		// desactivarlo.
		
		List<Empleado> empleadosDelPuesto = puestoTrabajoService.getEmpleadosDeUnPuestoTrabajo(elem.getNombre());
		if(CollectionUtils.isEmpty(empleadosDelPuesto)) {
			puestoTrabajoService.delete(elem);
		}else {
			
			List<Empleado> empleadosActivosDelPuesto = puestoTrabajoService.getEmpleadosActivosDeUnPuestoTrabajo(elem.getNombre());
			if(CollectionUtils.isEmpty(empleadosActivosDelPuesto)) {
				elem.setActivo(false);
				puestoTrabajoService.save(elem);
			}else {
				FacesContext.getCurrentInstance().
                addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"No se puede eliminar, ya que existen empleados activos en este puesto de trabajo", ""));
			}
		}
		
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
