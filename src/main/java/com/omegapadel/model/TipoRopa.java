package com.omegapadel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "tipo_ropa")
public class TipoRopa extends EntidadBase {

	@NotBlank
	private String tipoTalla;

	@NotBlank
	@Column(unique = true)
	private String tipoRopa;

	public String getTipoTalla() {
		return tipoTalla;
	}

	public void setTipoTalla(String tipoTalla) {
		this.tipoTalla = tipoTalla;
	}

	public String getTipoRopa() {
		return tipoRopa;
	}

	public void setTipoRopa(String tipoRopa) {
		this.tipoRopa = tipoRopa;
	}

}
