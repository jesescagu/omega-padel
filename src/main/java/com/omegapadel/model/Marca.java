package com.omegapadel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "marca")
public class Marca extends EntidadBase {

	private static final long serialVersionUID = 5342891097730175627L;

	@NotBlank
	@Column(unique = true)
	private String nombre;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
