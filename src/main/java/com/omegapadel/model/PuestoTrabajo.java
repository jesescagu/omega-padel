package com.omegapadel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "puesto_trabajo")
public class PuestoTrabajo extends EntidadBase {

	private static final long serialVersionUID = 5973626445721198407L;
	
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
