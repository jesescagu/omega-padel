package com.omegapadel.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@MappedSuperclass
public class EntidadBase implements Serializable {

	private static final long serialVersionUID = -3019687232984868307L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;
	protected Integer version;
	protected Date fechaCreacion;
	protected Date fechaActualizacion;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public boolean esnuevo() {
		return this.id == null;
	}

	@PrePersist
	protected void prePersist() {
		if (this.fechaCreacion == null)
			fechaCreacion = Calendar.getInstance().getTime();
		if (this.fechaActualizacion == null)
			fechaActualizacion = Calendar.getInstance().getTime();
		if (this.version == null)
			this.version = 1;
	}

	@PreUpdate
	protected void preUpdate() {
		this.fechaActualizacion = Calendar.getInstance().getTime();
		this.version++;
	}

	@PreRemove
	protected void preRemove() {
		this.fechaActualizacion = Calendar.getInstance().getTime();
		this.version++;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntidadBase other = (EntidadBase) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
