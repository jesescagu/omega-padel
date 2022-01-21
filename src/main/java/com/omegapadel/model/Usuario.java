package com.omegapadel.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	private String usuario;

	@NotBlank
	private String contrasenya;

	@NotNull
	private boolean activo;

	public  String getUsuario() {
		return usuario;
	}

	public  void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public  String getContrasenya() {
		return contrasenya;
	}

	public  void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	public  boolean isActivo() {
		return activo;
	}

	public  void setActivo(boolean activo) {
		this.activo = activo;
	}

}
