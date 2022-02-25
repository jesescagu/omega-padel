package com.omegapadel.model;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cesta")
public class Cesta extends EntidadBase {

	private static final long serialVersionUID = 301045156780864291L;

	private String referenciaProvisional; // Se crea y se mantiene hasta que se cree el pedido con esta referencia,
											// despues se crea otra.

	@ElementCollection
	private Map<Integer, Integer> mapaAnunciosCantidad;

	public Map<Integer, Integer> getMapaAnunciosCantidad() {
		return mapaAnunciosCantidad;
	}

	public void setMapaAnunciosCantidad(Map<Integer, Integer> mapaAnunciosCantidad) {
		this.mapaAnunciosCantidad = mapaAnunciosCantidad;
	}

	public String getReferenciaProvisional() {
		return referenciaProvisional;
	}

	public void setReferenciaProvisional(String referenciaProvisional) {
		this.referenciaProvisional = referenciaProvisional;
	}

}
