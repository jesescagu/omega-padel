package com.omegapadel.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pala")
public class Pala extends Producto{

	private static final long serialVersionUID = 9010518865688734089L;
	private Integer temporada;

	@NotNull
	@Min(value = 1000)
	@Max(value = 9000)
	public Integer getTemporada() {
		return temporada;
	}

	public void setTemporada(Integer temporada) {
		this.temporada = temporada;
	}
	
}
