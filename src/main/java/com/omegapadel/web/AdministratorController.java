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

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.omegapadel.model.Administrador;
import com.omegapadel.model.Empleado;
import com.omegapadel.model.PuestoTrabajo;
import com.omegapadel.model.Rol;
import com.omegapadel.model.Usuario;
import com.omegapadel.service.AdministratorService;
import com.omegapadel.service.PuestoTrabajoService;
import com.omegapadel.service.RolService;
import com.omegapadel.service.UsuarioService;

@Named("administradorController")
@ViewScoped
public class AdministratorController implements Serializable {

	private static final long serialVersionUID = -4170294904183575253L;

	@Inject
	private AdministratorService administradorService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Inject
	private RolService rolService;

	private Administrador admin;

	private String nickEscogido;
	private String contrasenyaEscogida;

	private String contrasenyaActual;
	private String contrasenyaConfirmacion;

	@PostConstruct
	public void init() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {

			List<Administrador> admList = administradorService.findAll();
			this.admin = admList.get(0);

			if (this.admin != null) {
				this.nickEscogido = this.admin.getUsuario().getUsuario();
				this.contrasenyaActual = null;
				this.contrasenyaConfirmacion = null;
			}
		}
	}

	public void abrirEditarAdministrador() throws IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("editarAdmin.xhtml");
		}
	}

	public String guardarEdicionAdministrador() {

		Boolean validacionErronea = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {

			if (this.nickEscogido == null || this.nickEscogido.isEmpty()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe escoger un nombre de usuario.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}

			if (this.contrasenyaActual == null || this.contrasenyaActual.isEmpty()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir una contraseña valida.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			} else {

				if (!bCryptPasswordEncoder.matches(this.contrasenyaActual, this.admin.getUsuario().getContrasenya())) {
					FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Contraseña actual errónea.", null);
					FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
					validacionErronea = true;
				}
			}

			if (validacionErronea) {
				return "";
			} else {

				String rol = "admin";
				Usuario usuario = this.admin.getUsuario();
				Rol r = rolService.buscaRolPorNombreUsuario(usuario.getUsuario()).get();
				Usuario usuarioAnterior = usuarioService.getUsuarioPorNombre(usuario.getUsuario());

				if (!usuario.getUsuario().equals(this.nickEscogido)) {

					if (validacionNickUnico()) {
						FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"El nombre de usuario ya existe.", null);
						FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
						validacionErronea = true;
						return "";
					}

					usuario.setUsuario(this.nickEscogido);

					Rol rolNuevo = rolService.create(this.nickEscogido, rol);
					rolService.saveRol(rolNuevo);
					rolService.delete(r);
				}

				if (this.contrasenyaEscogida != null && !this.contrasenyaEscogida.isEmpty()) {

					if (contrasenyaEscogida.equals(this.contrasenyaConfirmacion)) {

						String passCifrada = bCryptPasswordEncoder.encode(this.contrasenyaEscogida);
						usuario.setContrasenya(passCifrada);

					} else {
						FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Las contraseñas no coinciden.", null);
						FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
						validacionErronea = true;
						return "";
					}
				}

				Usuario usuarioSaved = usuarioService.save(usuario);
				this.admin.setUsuario(usuarioSaved);
				administradorService.save(this.admin);

				Authentication authentication = new UsernamePasswordAuthenticationToken(this.nickEscogido,
						usuarioSaved.getContrasenya(), auth.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);

				usuarioService.delete(usuarioAnterior);
			}
		}
		return "index.xhtml";
	}

	public boolean validacionNickUnico() {

		return usuarioService.existeNombreUsuario(this.nickEscogido)
				|| rolService.buscaRolPorNombreUsuario(this.nickEscogido).isPresent();

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

	public Administrador getAdmin() {
		return admin;
	}

	public void setAdmin(Administrador admin) {
		this.admin = admin;
	}

}
