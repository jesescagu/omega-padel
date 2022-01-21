package com.omegapadel.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tarjeta_bancaria")
public class TarjetaBancaria extends EntidadBase {

	@NotBlank
	private String numero;

	@NotNull
	@Min(100)
	@Max(999)
	private Integer cvv;

	@NotBlank
	private String titular;

	@NotNull
	@Min(1)
	@Max(12)
	private Integer mes;

	@NotNull
	// TODO dos cifras o cuatro??
	private Integer anyo;

	@ManyToOne(optional = false)
	private Institucion institucion; // visa, mastercard.

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Integer getCvv() {
		return cvv;
	}

	public void setCvv(Integer cvv) {
		this.cvv = cvv;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAnyo() {
		return anyo;
	}

	public void setAnyo(Integer anyo) {
		this.anyo = anyo;
	}

	public Institucion getInstitucion() {
		return institucion;
	}

	public void setInstitucion(Institucion institucion) {
		this.institucion = institucion;
	}

}
