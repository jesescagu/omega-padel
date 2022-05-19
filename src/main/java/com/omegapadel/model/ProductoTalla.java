package com.omegapadel.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "producto_talla")
public class ProductoTalla extends EntidadBase {

	private static final long serialVersionUID = 1403671627685751911L;

	@ManyToOne(optional = false)
	private AnuncioCantidad anuncioCantidad;

	@ManyToOne(optional = false)
	private Producto producto;

	// Talla UNICA, numerica o letras. Es la seleccionada para la cesta y pedidos.
	@NotNull
	private String talla;

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public String getTalla() {
		return talla;
	}

	public void setTalla(String talla) {
		this.talla = talla;
	}

	public AnuncioCantidad getAnuncioCantidad() {
		return anuncioCantidad;
	}

	public void setAnuncioCantidad(AnuncioCantidad anuncioCantidad) {
		this.anuncioCantidad = anuncioCantidad;
	}

}
