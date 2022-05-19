package com.omegapadel.model;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "accesorio")
public class Accesorio extends Producto {

	private static final long serialVersionUID = 3187667657614372092L;

	@ManyToOne
	private TipoAccesorio tipo;

	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, Integer> mapaTallaStock;

	public TipoAccesorio getTipo() {
		return tipo;
	}

	public void setTipo(TipoAccesorio tipo) {
		this.tipo = tipo;
	}

	public Map<String, Integer> getMapaTallaStock() {
		return mapaTallaStock;
	}

	public void setMapaTallaStock(Map<String, Integer> mapaTallaStock) {
		this.mapaTallaStock = mapaTallaStock;
	}

}
