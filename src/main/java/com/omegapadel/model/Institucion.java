package com.omegapadel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "institucion")
public class Institucion extends EntidadBase {

	@NotBlank
	@Column(unique = true)
	private String nombre; // VISA, MASTERCARD, ....
	
}
