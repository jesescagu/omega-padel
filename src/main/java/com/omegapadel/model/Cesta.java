package com.omegapadel.model;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cesta")
public class Cesta extends EntidadBase {

	private static final long serialVersionUID = 301045156780864291L;

	private Double costo;

	@ElementCollection
	private Map<Integer, Integer> mapaAnunciosCantidad;

	public Double getCosto() {
		return costo;
	}

	public void setCosto(Double costo) {
		this.costo = costo;
	}

	public Map<Integer, Integer> getMapaAnunciosCantidad() {
		return mapaAnunciosCantidad;
	}

	public void setMapaAnunciosCantidad(Map<Integer, Integer> mapaAnunciosCantidad) {
		this.mapaAnunciosCantidad = mapaAnunciosCantidad;
	}

}
