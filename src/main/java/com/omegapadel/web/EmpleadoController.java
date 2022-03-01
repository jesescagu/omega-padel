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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.omegapadel.model.Empleado;
import com.omegapadel.model.PuestoTrabajo;
import com.omegapadel.model.Rol;
import com.omegapadel.model.Usuario;
import com.omegapadel.service.EmpleadoService;
import com.omegapadel.service.PuestoTrabajoService;
import com.omegapadel.service.RolService;
import com.omegapadel.service.UsuarioService;

@Named("empleadoController")
@ViewScoped
public class EmpleadoController implements Serializable {

	private static final long serialVersionUID = -4170294904183575253L;

	@Inject
	private EmpleadoService empleadoService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Inject
	private RolService rolService;

	@Inject
	private PuestoTrabajoService puestoTrabajoService;

	private Empleado empleadoLogado;

	private List<Empleado> listaEmpleados;

	private String nickEscogido;
	private String contrasenyaEscogida;
	private String dniEscogido;
	private String nombreEscogido;
	private String apellidosEscogido;
	private String emailEscogido;
	private String telefonoEscogido;
	private Integer puestoTrabajoEscogido;

	private List<PuestoTrabajo> listaPuestosTrabajo;

	private String contrasenyaActual;
	private String contrasenyaConfirmacion;

	private Boolean edicionPorAdministrador;

	@PostConstruct
	public void init() {

		this.listaPuestosTrabajo = puestoTrabajoService.findAll();
		this.listaEmpleados = empleadoService.findAll();

		FacesContext context = FacesContext.getCurrentInstance();
		Object emp = context.getExternalContext().getSessionMap().get("empleadoParaEditar");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("empleado"))) {
			User user = (User) auth.getPrincipal();
			String nombreUsuario = user.getUsername();

			this.empleadoLogado = empleadoService.buscaEmpleadoPorNombreUsuario(nombreUsuario);

			this.nickEscogido = empleadoLogado.getUsuario().getUsuario();
			this.nombreEscogido = empleadoLogado.getNombre();
			this.apellidosEscogido = empleadoLogado.getApellidos();
			this.emailEscogido = empleadoLogado.getEmail();
			this.puestoTrabajoEscogido = empleadoLogado.getPuesto().getId();
			this.dniEscogido = empleadoLogado.getDni();
			this.telefonoEscogido = empleadoLogado.getTelefono();

			this.contrasenyaEscogida = null;
			this.contrasenyaActual = null;
			this.contrasenyaConfirmacion = null;

			this.edicionPorAdministrador = false;
		} else if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {

			Empleado empleadoParaEditar = null;
			if (emp instanceof Empleado) {
				empleadoParaEditar = (Empleado) emp;
			}

			if (empleadoParaEditar != null) {

				this.nickEscogido = empleadoParaEditar.getUsuario().getUsuario();
				this.nombreEscogido = empleadoParaEditar.getNombre();
				this.apellidosEscogido = empleadoParaEditar.getApellidos();
				this.emailEscogido = empleadoParaEditar.getEmail();
				this.puestoTrabajoEscogido = empleadoParaEditar.getPuesto().getId();
				this.dniEscogido = empleadoParaEditar.getDni();
				this.telefonoEscogido = empleadoParaEditar.getTelefono();

				this.edicionPorAdministrador = true;
			}
		}
	}

	public void abrirNuevoEmpleado() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("empleadoParaEditar");
		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoEmpleado.xhtml");
	}

	public void desactivarEmpleado(Empleado emp) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
			Usuario usu = emp.getUsuario();
			usu.setActivo(false);
			Usuario usuSaved = usuarioService.save(usu);
			emp.setUsuario(usuSaved);
		}

	}

	public void activarEmpleado(Empleado emp) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
			Usuario usu = emp.getUsuario();
			usu.setActivo(true);
			Usuario usuSaved = usuarioService.save(usu);
			emp.setUsuario(usuSaved);
		}

	}

	public void abrirEditarEmpleado() throws IOException {

		if (this.empleadoLogado != null) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("editarEmpleado.xhtml");
		} else {
			FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoEmpleado.xhtml");
		}
	}

	public void abrirEditarEmpleado(Empleado emp) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("empleadoParaEditar", emp);

		FacesContext.getCurrentInstance().getExternalContext().redirect("editarEmpleado.xhtml");
	}

	public String guardarEmpleado() {

		Boolean validacionErronea = false;

		if (this.nickEscogido == null || this.nickEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe escoger un nombre de usuario.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (validacionNickUnico()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"El nombre de usuario ya existe.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}

		}
		if (this.contrasenyaEscogida == null || this.contrasenyaEscogida.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir una contraseña valida.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.nombreEscogido == null || this.nombreEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe introducir su nombre.",
					null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.apellidosEscogido == null || this.apellidosEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir sus apellidos.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.emailEscogido == null || this.emailEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir un email válido.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (validacionEmail()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir un email válido.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}

		if (this.dniEscogido == null || this.dniEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir un DNI válido.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (validacionDNI()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir un dni válido.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}
		if (this.telefonoEscogido == null || this.telefonoEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir un teléfono válido.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (validacionTelefono()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir un email válido.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}

		if (validacionErronea) {
			return "";
		} else {

			String rol = "empleado";

			String dniFinal = this.dniEscogido.replace("-", "").toUpperCase();

			String passCifrada = bCryptPasswordEncoder.encode(this.contrasenyaEscogida);
			Usuario usuario = usuarioService.create(this.nickEscogido, passCifrada);
			Usuario u = usuarioService.save(usuario);

			Rol r = rolService.create(this.nickEscogido, rol);
			rolService.saveRol(r);

			PuestoTrabajo pt = puestoTrabajoService.findById(this.puestoTrabajoEscogido).get();

			Empleado c = empleadoService.create(dniFinal, this.nombreEscogido, this.apellidosEscogido,
					this.emailEscogido, this.telefonoEscogido, null, u, pt);
			empleadoService.save(c);
		}

		// TODO Mensaje de confirmacion y directo al login? Confirmar correo quiza?
		return "listaEmpleados.xhtml";

	}

	public String guardarEdicionEmpleado() {

		Boolean validacionErronea = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (this.nickEscogido == null || this.nickEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe escoger un nombre de usuario.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {

			if (this.contrasenyaActual == null || this.contrasenyaActual.isEmpty()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir una contraseña valida.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			} else {

				if (!bCryptPasswordEncoder.matches(this.contrasenyaActual,
						this.empleadoLogado.getUsuario().getContrasenya())) {
					FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Contraseña actual errónea.", null);
					FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
					validacionErronea = true;
				}
			}
		}
		if (this.nombreEscogido == null || this.nombreEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Debe introducir su nombre.",
					null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.apellidosEscogido == null || this.apellidosEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir sus apellidos.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.emailEscogido == null || this.emailEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir un email válido.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (validacionEmail()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir un email válido.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}

		if (this.dniEscogido == null || this.dniEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir un DNI válido.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (validacionDNI()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir un dni válido.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}
		if (this.telefonoEscogido == null || this.telefonoEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe introducir un teléfono válido.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (validacionTelefono()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir un email válido.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}

		if (validacionErronea) {
			return "";
		} else {

			String rol = "empleado";
			Usuario usuario = null;
			Rol r = null;

			if (auth != null && !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {

				usuario = this.empleadoLogado.getUsuario();
				r = rolService.buscaRolPorNombreUsuario(usuario.getUsuario()).get();

				Usuario usuarioSaved = usuario;

				if (this.contrasenyaEscogida != null && !this.contrasenyaEscogida.isEmpty()) {
					// Quiere cambiar contraseña. Se modifica rol y usuario.

					if (contrasenyaEscogida.equals(this.contrasenyaConfirmacion)) {

						if (!usuario.getUsuario().equals(this.nickEscogido)) {

							if (validacionNickUnico()) {
								FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"El nombre de usuario ya existe.", null);
								FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
								validacionErronea = true;
								return "";
							}

							Rol rolNuevo = rolService.create(this.nickEscogido, rol);
							rolService.saveRol(rolNuevo);
							rolService.delete(r);

							usuario.setUsuario(this.nickEscogido);
						}

						String passCifrada = bCryptPasswordEncoder.encode(this.contrasenyaEscogida);
						usuario.setContrasenya(passCifrada);
						usuarioSaved = usuarioService.save(usuario);

					} else {
						FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Las contraseñas no coinciden.", null);
						FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
						validacionErronea = true;
						return "";
					}
				} else {
					// No quiere cambiar contraseña. Si no cambia el nick no cambia ni rol ni
					// usuario.
					if (!usuario.getUsuario().equals(this.nickEscogido)) {

						if (validacionNickUnico()) {
							FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
									"El nombre de usuario ya existe.", null);
							FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
							validacionErronea = true;
							return "";
						}

						usuario.setUsuario(this.nickEscogido);
						usuarioSaved = usuarioService.save(usuario);

						Rol rolNuevo = rolService.create(this.nickEscogido, rol);
						rolService.saveRol(rolNuevo);
						rolService.delete(r);
					}
				}

				this.empleadoLogado.setNombre(this.nombreEscogido);
				this.empleadoLogado.setApellidos(this.apellidosEscogido);
				this.empleadoLogado.setEmail(this.emailEscogido);
				this.empleadoLogado.setUsuario(usuarioSaved);
				this.empleadoLogado.setDni(this.dniEscogido);
				this.empleadoLogado.setTelefono(this.telefonoEscogido);

				empleadoService.save(this.empleadoLogado);

			} else {

				r = rolService.buscaRolPorNombreUsuario(this.nickEscogido).get();
				Empleado emp = empleadoService.buscaEmpleadoPorNombreUsuario(this.nickEscogido);

				PuestoTrabajo pt = puestoTrabajoService.findById(this.puestoTrabajoEscogido).get();

				emp.setPuesto(pt);
				emp.setNombre(this.nombreEscogido);
				emp.setApellidos(this.apellidosEscogido);
				emp.setEmail(this.emailEscogido);
				emp.setDni(this.dniEscogido);
				emp.setTelefono(this.telefonoEscogido);

				empleadoService.save(emp);

			}
		}

		// TODO Mensaje de confirmacion y directo al login? Confirmar correo quiza?
		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
			return "listaEmpleados.xhtml";
		} else {
			return "index.xhtml";
		}

	}

	public boolean validacionEmail() {

		String regex = ".+@.+\\..+";
		Boolean res = this.emailEscogido.matches(regex);
		return !res;

	}

	public boolean validacionDNI() {

		String regex = "[0-9]{8}-?[A-Za-z]{1}";
		String regex2 = "[0-9]{8}-[A-Za-z]{1}";
		Boolean res = this.dniEscogido.matches(regex) || this.dniEscogido.matches(regex2);
		return !res;

	}

	public boolean validacionTelefono() {

		String regex = "[0-9]{9}";
		Boolean res = this.telefonoEscogido.matches(regex);
		return !res;

	}

	public boolean validacionNickUnico() {

		return usuarioService.existeNombreUsuario(this.nickEscogido)
				|| rolService.buscaRolPorNombreUsuario(this.nickEscogido).isPresent();

	}

	public void verListaEmpleados() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaEmpleados.xhtml");
	}

	public Empleado getEmpleadoLogado() {
		return empleadoLogado;
	}

	public void setEmpleadoLogado(Empleado empleadoLogado) {
		this.empleadoLogado = empleadoLogado;
	}

	public String getNickEscogido() {
		return nickEscogido;
	}

	public void setNickEscogido(String nickEscogido) {
		this.nickEscogido = nickEscogido;
	}

	public String getContrasenyaEscogida() {
		return contrasenyaEscogida;
	}

	public void setContrasenyaEscogida(String contrasenyaEscogida) {
		this.contrasenyaEscogida = contrasenyaEscogida;
	}

	public String getDniEscogido() {
		return dniEscogido;
	}

	public void setDniEscogido(String dniEscogido) {
		this.dniEscogido = dniEscogido;
	}

	public String getNombreEscogido() {
		return nombreEscogido;
	}

	public void setNombreEscogido(String nombreEscogido) {
		this.nombreEscogido = nombreEscogido;
	}

	public String getApellidosEscogido() {
		return apellidosEscogido;
	}

	public void setApellidosEscogido(String apellidosEscogido) {
		this.apellidosEscogido = apellidosEscogido;
	}

	public String getEmailEscogido() {
		return emailEscogido;
	}

	public void setEmailEscogido(String emailEscogido) {
		this.emailEscogido = emailEscogido;
	}

	public String getTelefonoEscogido() {
		return telefonoEscogido;
	}

	public void setTelefonoEscogido(String telefonoEscogido) {
		this.telefonoEscogido = telefonoEscogido;
	}

	public Integer getPuestoTrabajoEscogido() {
		return puestoTrabajoEscogido;
	}

	public void setPuestoTrabajoEscogido(Integer puestoTrabajoEscogido) {
		this.puestoTrabajoEscogido = puestoTrabajoEscogido;
	}

	public String getContrasenyaActual() {
		return contrasenyaActual;
	}

	public void setContrasenyaActual(String contrasenyaActual) {
		this.contrasenyaActual = contrasenyaActual;
	}

	public String getContrasenyaConfirmacion() {
		return contrasenyaConfirmacion;
	}

	public void setContrasenyaConfirmacion(String contrasenyaConfirmacion) {
		this.contrasenyaConfirmacion = contrasenyaConfirmacion;
	}

	public List<PuestoTrabajo> getListaPuestosTrabajo() {
		return listaPuestosTrabajo;
	}

	public void setListaPuestosTrabajo(List<PuestoTrabajo> listaPuestosTrabajo) {
		this.listaPuestosTrabajo = listaPuestosTrabajo;
	}

	public List<Empleado> getListaEmpleados() {
		return listaEmpleados;
	}

	public void setListaEmpleados(List<Empleado> listaEmpleados) {
		this.listaEmpleados = listaEmpleados;
	}

	public Boolean getEdicionPorAdministrador() {
		return edicionPorAdministrador;
	}

	public void setEdicionPorAdministrador(Boolean edicionPorAdministrador) {
		this.edicionPorAdministrador = edicionPorAdministrador;
	}

}
