package com.omegapadel.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pelota")
public class Pelota extends Producto{

	private Integer numero; //Numero de pelotas del pack

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
}
