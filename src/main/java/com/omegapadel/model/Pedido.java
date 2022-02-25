package com.omegapadel.model;

import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "pedido")
public class Pedido extends EntidadBase {

	private static final long serialVersionUID = 4261985202231520506L;

	@ElementCollection
	private Map<Integer, Integer> mapaAnunciosCantidad;

	@ManyToOne(optional = false)
	private DireccionPostal direccionPostal;

	@ManyToOne(optional = false)
	private Cliente cliente;

	@ManyToMany
	@OrderBy("orden ASC")
	private SortedSet<EstadoPedido> listaEstados;

	@Column(unique = true)
	@NotBlank
	private String referenciaPedido;

	@NotNull
	private Double precioTotalProductos;

	@NotNull
	private Double precioEnvio;

	@NotNull
	private String ultimoEstado;

	public Map<Integer, Integer> getMapaAnunciosCantidad() {
		return mapaAnunciosCantidad;
	}

	public void setMapaAnunciosCantidad(Map<Integer, Integer> mapaAnunciosCantidad) {
		this.mapaAnunciosCantidad = mapaAnunciosCantidad;
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

	public SortedSet<EstadoPedido> getListaEstados() {
		return Collections.unmodifiableSortedSet(listaEstados);
	}

	public void setListaEstados(SortedSet<EstadoPedido> listaEstados) {
		this.listaEstados = listaEstados;
		this.ultimoEstado = listaEstados.last().getEstado();
	}
	
	public void addEstadoPedidoNuevo(EstadoPedido estado) {
		this.listaEstados.add(estado);
		this.ultimoEstado = estado.getEstado();
	}
	
	public EstadoPedido getUltimoEstadoPedido() {
		return listaEstados.last();
	}

	public String getReferenciaPedido() {
		return referenciaPedido;
	}

	public void setReferenciaPedido(String referenciaPedido) {
		this.referenciaPedido = referenciaPedido;
	}

	public Double getPrecioTotalProductos() {
		return precioTotalProductos;
	}

	public void setPrecioTotalProductos(Double precioTotal) {
		this.precioTotalProductos = precioTotal;
	}

	public Double getPrecioEnvio() {
		return precioEnvio;
	}

	public void setPrecioEnvio(Double precioEnvio) {
		this.precioEnvio = precioEnvio;
	}

	public String getUltimoEstado() {
		return ultimoEstado;
	}

}
