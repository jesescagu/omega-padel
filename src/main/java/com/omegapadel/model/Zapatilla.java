package com.omegapadel.model;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "zapatilla")
public class Zapatilla extends Producto {

	private static final long serialVersionUID = 331971962932019649L;

	@ElementCollection(fetch = FetchType.EAGER)
	private Map<String, Integer> mapaTallaStock;

	@NotBlank
	private String sexo;

	public Map<String, Integer> getMapaTallaStock() {
		return mapaTallaStock;
	}

	public void setMapaTallaStock(Map<String, Integer> mapaTallaStock) {
		this.mapaTallaStock = mapaTallaStock;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
}
