package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;

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

import com.omegapadel.model.Cliente;
import com.omegapadel.model.Rol;
import com.omegapadel.model.Usuario;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.RolService;
import com.omegapadel.service.UsuarioService;

@Named("clienteController")
@ViewScoped
public class ClienteController implements Serializable {

	private static final long serialVersionUID = 5525551969255078248L;

	@Inject
	private ClienteService clienteService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Inject
	private RolService rolService;

	private Cliente clienteLogado;

	private String nickEscogido;
	private String contrasenyaEscogida;
	private String nombreEscogido;
	private String apellidosEscogido;
	private String emailEscogido;

	private String contrasenyaActual;
	private String contrasenyaConfirmacion;

	private Authentication auth;

	@PostConstruct
	public void init() {

		this.auth = SecurityContextHolder.getContext().getAuthentication();

		if (this.auth != null
				&& this.auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {

			String nombreUsuario = null;
			Object princ = auth.getPrincipal();
			if (princ instanceof User) {
				User user = (User) princ;
				nombreUsuario = user.getUsername();
			} else {
				nombreUsuario = (String) auth.getPrincipal();
			}

			this.clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);

			this.nickEscogido = clienteLogado.getUsuario().getUsuario();
			this.nombreEscogido = clienteLogado.getNombre();
			this.apellidosEscogido = clienteLogado.getApellidos();
			this.emailEscogido = clienteLogado.getEmail();

			this.contrasenyaEscogida = null;
			this.contrasenyaActual = null;
			this.contrasenyaConfirmacion = null;
		}
	}

	public void abrirNuevoCliente() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoCliente.xhtml");
	}

	public void abrirEditarCliente() throws IOException {

		if (this.clienteLogado != null) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("editarCliente.xhtml");
		} else {
			FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoCliente.xhtml");
		}
	}

	public String guardarCliente() {

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

		if (validacionErronea) {
			return "";
		} else {

			String rol = "cliente";

			String passCifrada = bCryptPasswordEncoder.encode(this.contrasenyaEscogida);
			Usuario usuario = usuarioService.create(this.nickEscogido, passCifrada);
			Usuario u = usuarioService.save(usuario);

			Rol r = rolService.create(this.nickEscogido, rol);
			rolService.saveRol(r);

			Cliente c = clienteService.create(this.nombreEscogido, this.apellidosEscogido, this.emailEscogido, null, u,
					null);
			clienteService.save(c);
		}

		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
				"Registro completado!", "Se ha registrado correctamente."));
		return "login.xhtml";
	}

	public String guardarEdicionCliente() {

		Boolean validacionErronea = false;

		if (this.nickEscogido == null || this.nickEscogido.isEmpty()) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe escoger un nombre de usuario.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.auth != null
				&& this.auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {

			if (this.contrasenyaActual == null || this.contrasenyaActual.isEmpty()) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir una contraseña valida.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			} else {

				if (!bCryptPasswordEncoder.matches(this.contrasenyaActual,
						this.clienteLogado.getUsuario().getContrasenya())) {
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

		if (validacionErronea) {
			return "";
		} else {
			String rol = "cliente";
			Usuario usuario = this.clienteLogado.getUsuario();
			Rol r = rolService.buscaRolPorNombreUsuario(usuario.getUsuario()).get();

			Usuario usuarioAnterior = usuarioService.getUsuarioPorNombre(usuario.getUsuario());

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
					Usuario usuarioSaved = usuarioService.save(usuario);

					Authentication authentication = new UsernamePasswordAuthenticationToken(this.nickEscogido,
							usuarioSaved.getContrasenya(), this.auth.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);

					this.clienteLogado.setUsuario(usuarioSaved);
					clienteService.save(this.clienteLogado);
					usuarioService.delete(usuarioAnterior);

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
					Usuario usuarioSaved = usuarioService.save(usuario);

					Authentication authentication = new UsernamePasswordAuthenticationToken(this.nickEscogido,
							usuarioSaved.getContrasenya(), this.auth.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);

					Rol rolNuevo = rolService.create(this.nickEscogido, rol);
					rolService.saveRol(rolNuevo);
					rolService.delete(r);

					this.clienteLogado.setUsuario(usuarioSaved);
					clienteService.save(this.clienteLogado);
					usuarioService.delete(usuarioAnterior);
				}

			}

			this.clienteLogado.setNombre(this.nombreEscogido);
			this.clienteLogado.setApellidos(this.apellidosEscogido);
			this.clienteLogado.setEmail(this.emailEscogido);
			clienteService.save(this.clienteLogado);

		}

		// TODO Mensaje de confirmacion y directo al login? Confirmar correo quiza?
		return "index.xhtml";
	}

	public boolean validacionEmail() {

		String regex = ".+@.+\\..+";
		Boolean res = this.emailEscogido.matches(regex);
		return !res;

	}

	public boolean validacionNickUnico() {

		return usuarioService.existeNombreUsuario(this.nickEscogido)
				|| rolService.buscaRolPorNombreUsuario(this.nickEscogido).isPresent();

	}

	public String logout() {
		return "/logout";
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

	public Cliente getClienteLogado() {
		return clienteLogado;
	}

	public void setClienteLogado(Cliente clienteLogado) {
		this.clienteLogado = clienteLogado;
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

	public Authentication getAuth() {
		return auth;
	}

	public void setAuth(Authentication auth) {
		this.auth = auth;
	}

}
