package com.omegapadel.model;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "zapatilla")
public class Zapatilla extends Producto {

	private static final long serialVersionUID = 331971962932019649L;

//	@NotNull
//	@Min(value = 01)
//	@Max(value = 99)
//	private Integer talla;

	@ElementCollection
	private Map<Integer, Integer> mapaTallaStock;

	@NotBlank
	private String sexo;

	public Map<Integer, Integer> getMapaTallaStock() {
		return mapaTallaStock;
	}

	public void setMapaTallaStock(Map<Integer, Integer> mapaTallaStock) {
		this.mapaTallaStock = mapaTallaStock;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
}
