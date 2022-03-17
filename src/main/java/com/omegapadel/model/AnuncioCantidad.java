package com.omegapadel.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "anuncio_cantidad")
public class AnuncioCantidad extends EntidadBase {

	private static final long serialVersionUID = -3872091947256500667L;

	@ManyToOne(optional = false)
	private Anuncio anuncio;

	@ManyToOne(optional = true)
	private Cesta cesta;

	@ManyToOne(optional = true)
	private Pedido pedido;

	@NotNull
	@Min(value = 1)
	private Integer cantidad;

	public Anuncio getAnuncio() {
		return anuncio;
	}

	public void setAnuncio(Anuncio anuncio) {
		this.anuncio = anuncio;
	}

	public Cesta getCesta() {
		return cesta;
	}

	public void setCesta(Cesta cesta) {
		this.cesta = cesta;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
}
