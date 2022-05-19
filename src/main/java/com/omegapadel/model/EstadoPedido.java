package com.omegapadel.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "estado_pedido")
public class EstadoPedido extends EntidadBase implements Comparable<EstadoPedido> {

	private static final long serialVersionUID = -9167316515211182609L;

	@NotNull
	private String estado;

	@NotNull
	private String descripcion;

	@NotNull
	private Date fecha;

	@NotNull
	private Integer orden;

	@Size(max = 500)
	private String mensaje;

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public int compareTo(EstadoPedido e2) {

		return this.orden.compareTo(e2.getOrden());

	}

}
