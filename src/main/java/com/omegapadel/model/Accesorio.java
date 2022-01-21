package com.omegapadel.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "accesorio")
public class Accesorio extends Producto{

	@ManyToOne
	private TipoAccesorio tipo;

	public TipoAccesorio getTipo() {
		return tipo;
	}

	public void setTipo(TipoAccesorio tipo) {
		this.tipo = tipo;
	}

}
