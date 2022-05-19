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

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.omegapadel.model.Cliente;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.Empleado;
import com.omegapadel.model.Municipio;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.DireccionPostalService;
import com.omegapadel.service.EmpleadoService;
import com.omegapadel.service.MunicipioService;

@Named("direccionPostalController")
@ViewScoped
public class DireccionPostalController implements Serializable {

	private static final long serialVersionUID = 4443797871472080802L;

	@Inject
	private DireccionPostalService direccionPostalService;
	@Inject
	private ClienteService clienteService;
	@Inject
	private EmpleadoService empleadoService;
	@Inject
	private MunicipioService municipioService;

	private List<DireccionPostal> listaDireccionesPostales;
	private DireccionPostal direccionPostalSeleccionada;
	private List<Municipio> listaMunicipios;

	private String codigoMunicipioSeleccionado;
	private String direccionSeleccionada;
	private String codigoPostalSeleccionado;

	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		Object DPSeleccionadaObject = context.getExternalContext().getSessionMap().get("direccionSeleccionada");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {

			String nombreUsuario = null;
			Object princ = auth.getPrincipal();
			if (princ instanceof User) {
				User user = (User) princ;
				nombreUsuario = user.getUsername();
			} else {
				nombreUsuario = (String) auth.getPrincipal();
			}

			Cliente clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);
			this.listaDireccionesPostales = clienteLogado.getDireccionesPostales();

		}
		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("empleado"))) {
			User user = (User) auth.getPrincipal();
			String nombreUsuario = user.getUsername();

			Empleado empleadoLogado = empleadoService.buscaEmpleadoPorNombreUsuario(nombreUsuario);
			this.listaDireccionesPostales = empleadoLogado.getDireccionesPostales();

		}
		
		if (DPSeleccionadaObject != null && DPSeleccionadaObject instanceof DireccionPostal) {

			this.direccionPostalSeleccionada = (DireccionPostal) DPSeleccionadaObject;
			this.listaMunicipios = municipioService.findAllOrdered();

			this.codigoPostalSeleccionado = this.direccionPostalSeleccionada.getCodigoPostal();
			this.direccionSeleccionada = this.direccionPostalSeleccionada.getDireccion();
			this.codigoMunicipioSeleccionado = this.direccionPostalSeleccionada.getMunicipio().getCodigo();

		} else {

			this.listaMunicipios = municipioService.findAllOrdered();
			this.direccionPostalSeleccionada = null;
			this.codigoPostalSeleccionado = "";
			this.direccionSeleccionada = "";
			this.codigoMunicipioSeleccionado = "";
		}
	}

	public void abrirListaDireccionesActor() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaDirecciones.xhtml");
	}

	public void abrirEditarDireccionPostal(DireccionPostal dp) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("direccionSeleccionada", dp);

		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevaDireccionPostal.xhtml");

	}

	public void abrirNuevaDireccionPostal() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("direccionSeleccionada");
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevaDireccionPostal.xhtml");

	}

	public String guardarDireccionPostal() throws IOException {
		Boolean validacionErronea = false;

		if (this.direccionSeleccionada == null || this.direccionSeleccionada.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir una dirección.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.codigoPostalSeleccionado == null || this.codigoPostalSeleccionado.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir un código postal.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.codigoMunicipioSeleccionado == null || this.codigoMunicipioSeleccionado.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe seleccionar un municipio válido.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (validacionErronea) {
			return "";
		} else {

			Municipio municipioSeleccionado = municipioService.getMunicipioPorCodigo(this.codigoMunicipioSeleccionado)
					.get();

			DireccionPostal dp = null;
			if (this.direccionPostalSeleccionada == null || (this.direccionPostalSeleccionada.getPedidos() != null
					&& this.direccionPostalSeleccionada.getPedidos().size() != 0)) {

				if (this.direccionPostalSeleccionada != null) {
					eliminarDireccionPostal(this.direccionPostalSeleccionada);
				}

				Authentication auth = SecurityContextHolder.getContext().getAuthentication();

				if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {

					String nombreUsuario = null;
					Object princ = auth.getPrincipal();
					if (princ instanceof User) {
						User user = (User) princ;
						nombreUsuario = user.getUsername();
					} else {
						nombreUsuario = (String) auth.getPrincipal();
					}

					Cliente clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);
					dp = direccionPostalService.create(direccionSeleccionada, codigoPostalSeleccionado,
							municipioSeleccionado, clienteLogado);

				}
				if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("empleado"))) {
					User user = (User) auth.getPrincipal();
					String nombreUsuario = user.getUsername();

					Empleado empleadoLogado = empleadoService.buscaEmpleadoPorNombreUsuario(nombreUsuario);
					dp = direccionPostalService.create(direccionSeleccionada, codigoPostalSeleccionado,
							municipioSeleccionado, empleadoLogado);

				}
			} else {
				dp = this.direccionPostalSeleccionada;

				dp.setCodigoPostal(this.codigoPostalSeleccionado);
				dp.setDireccion(this.direccionSeleccionada);
				dp.setMunicipio(municipioService.getMunicipioPorCodigo(this.codigoMunicipioSeleccionado).get());
			}

			direccionPostalService.save(dp);
			abrirListaDireccionesActor();

			return "index.xhtml";
		}

	}

	public void eliminarDireccionPostal(DireccionPostal dp) throws IOException {

		Boolean esDireccionConPedidos = dp.getPedidos().size() != 0;
		if (esDireccionConPedidos) {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();

			if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {

				String nombreUsuario = null;
				Object princ = auth.getPrincipal();
				if (princ instanceof User) {
					User user = (User) princ;
					nombreUsuario = user.getUsername();
				} else {
					nombreUsuario = (String) auth.getPrincipal();
				}

				Cliente clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);
				List<DireccionPostal> listDPs = clienteLogado.getDireccionesPostales();
				listDPs.remove(dp);
				clienteLogado.setDireccionesPostales(listDPs);
				clienteService.save(clienteLogado);

				dp.setActor(null);
				direccionPostalService.save(dp);

			}
			if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("empleado"))) {
				User user = (User) auth.getPrincipal();
				String nombreUsuario = user.getUsername();

				Empleado empleadoLogado = empleadoService.buscaEmpleadoPorNombreUsuario(nombreUsuario);
				List<DireccionPostal> listDPs = empleadoLogado.getDireccionesPostales();
				listDPs.remove(dp);
				empleadoLogado.setDireccionesPostales(listDPs);
				empleadoService.save(empleadoLogado);

				dp.setActor(null);
				direccionPostalService.save(dp);
			}

		} else {
			direccionPostalService.delete(dp);
		}

		abrirListaDireccionesActor();

	}

	public List<DireccionPostal> getListaDireccionesPostales() {
		return listaDireccionesPostales;
	}

	public void setListaDireccionesPostales(List<DireccionPostal> listaDireccionesPostales) {
		this.listaDireccionesPostales = listaDireccionesPostales;
	}

	public DireccionPostal getDireccionPostalSeleccionada() {
		return direccionPostalSeleccionada;
	}

	public void setDireccionPostalSeleccionada(DireccionPostal direccionPostalSeleccionada) {
		this.direccionPostalSeleccionada = direccionPostalSeleccionada;
	}

	public List<Municipio> getListaMunicipios() {
		return listaMunicipios;
	}

	public void setListaMunicipios(List<Municipio> listaMunicipios) {
		this.listaMunicipios = listaMunicipios;
	}

	public String getCodigoMunicipioSeleccionado() {
		return codigoMunicipioSeleccionado;
	}

	public void setCodigoMunicipioSeleccionado(String codigoMunicipioSeleccionado) {
		this.codigoMunicipioSeleccionado = codigoMunicipioSeleccionado;
	}

	public String getDireccionSeleccionada() {
		return direccionSeleccionada;
	}

	public void setDireccionSeleccionada(String direccionSeleccionada) {
		this.direccionSeleccionada = direccionSeleccionada;
	}

	public String getCodigoPostalSeleccionado() {
		return codigoPostalSeleccionado;
	}

	public void setCodigoPostalSeleccionado(String codigoPostalSeleccionado) {
		this.codigoPostalSeleccionado = codigoPostalSeleccionado;
	}

}
