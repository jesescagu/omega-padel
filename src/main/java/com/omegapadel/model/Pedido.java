package com.omegapadel.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "pedido")
public class Pedido extends EntidadBase {

	private static final long serialVersionUID = 4261985202231520506L;

	@ManyToMany
	private List<Anuncio> anuncios;

	@ManyToOne(optional = false)
	private DireccionPostal direccionPostal;

	@ManyToOne(optional = false)
	private Cliente cliente;

	@ManyToMany
	private List<EstadoPedido> listaEstados;

	private String referenciaPedido;

	public List<Anuncio> getAnuncios() {
		return anuncios;
	}

	public void setAnuncios(List<Anuncio> anuncio) {
		this.anuncios = anuncio;
	}

	public DireccionPostal getDireccionPostal() {
		return direccionPostal;
	}

	public void setDireccionPostal(DireccionPostal direccionPostal) {
		this.direccionPostal = direccionPostal;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public List<EstadoPedido> getListaEstados() {
		return listaEstados;
	}

	public void setListaEstados(List<EstadoPedido> listaEstados) {
		this.listaEstados = listaEstados;
	}

	public String getReferenciaPedido() {
		return referenciaPedido;
	}

	public void setReferenciaPedido(String referenciaPedido) {
		this.referenciaPedido = referenciaPedido;
	}

}
