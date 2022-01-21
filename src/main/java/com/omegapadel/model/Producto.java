package com.omegapadel.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "producto")
@Inheritance(strategy = InheritanceType.JOINED)
public class Producto extends EntidadBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6303477275427539644L;

	@NotNull
	private Integer stock; // Se puede cambiar manualmente para cuando compre

	@ManyToOne(optional = false)
	private Marca marca;

	@NotBlank
	private String modelo;

	// TODO
//	@NotNull
//	@Unique
//	private String referencia;

//	@ManyToMany(mappedBy="productos")
//	private List<Anuncio> anuncios;

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Marca getMarca() {
		return marca;
	}

	public void setMarca(Marca marca) {
		this.marca = marca;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String nombreClase() {
	    return this.getClass().getSimpleName();
	}
	
}
