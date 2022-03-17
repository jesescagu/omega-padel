package com.omegapadel.model;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "ropa")
public class Ropa extends Producto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 659083532689046607L;

	@ElementCollection
	private Map<String, Integer> mapaTallaStock;

	@NotBlank
	private String sexo;

	@ManyToOne
	private TipoRopa tipoRopa;

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Map<String, Integer> getMapaTallaStock() {
		return mapaTallaStock;
	}

	public void setMapaTallaStock(Map<String, Integer> mapaTallaStock) {
		this.mapaTallaStock = mapaTallaStock;
	}

	public TipoRopa getTipoRopa() {
		return tipoRopa;
	}

	public void setTipoRopa(TipoRopa tipoRopa) {
		this.tipoRopa = tipoRopa;
	}

}
