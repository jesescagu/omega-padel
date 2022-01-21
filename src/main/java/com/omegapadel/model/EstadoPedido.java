package com.omegapadel.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "estado_pedido")
public class EstadoPedido extends EntidadBase {

	private static final long serialVersionUID = -9167316515211182609L;

	@NotNull
	private String estado;

	@NotNull
	private Date fecha;

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

}
