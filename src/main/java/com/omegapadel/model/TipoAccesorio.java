package com.omegapadel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tipo_accesorio")
public class TipoAccesorio extends EntidadBase {

	private static final long serialVersionUID = -1169933786237644353L;

	@Column(unique = true)
	@NotBlank
	private String nombre;

	@NotBlank
	private String tipoTalla;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoTalla() {
		return tipoTalla;
	}

	public void setTipoTalla(String tipoTalla) {
		this.tipoTalla = tipoTalla;
	}

}
