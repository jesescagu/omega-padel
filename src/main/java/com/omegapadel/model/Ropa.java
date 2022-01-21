package com.omegapadel.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "ropa")
public class Ropa extends Producto {

	@NotBlank
	private String talla;

	@NotBlank
	private String sexo;

	@ManyToOne
	private TipoRopa tipoRopa;

	public String getTalla() {
		return talla;
	}

	public void setTalla(String talla) {
		this.talla = talla;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public TipoRopa getTipoRopa() {
		return tipoRopa;
	}

	public void setTipoRopa(TipoRopa tipoRopa) {
		this.tipoRopa = tipoRopa;
	}

}
