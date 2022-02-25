package com.omegapadel.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "actor")
@Inheritance(strategy=InheritanceType.JOINED)
public class Actor extends EntidadBase{
	
	private static final long serialVersionUID = 7618612533931913209L;

	@NotNull
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario", referencedColumnName = "usuario")
	private Usuario	usuario;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "actor")
	private List<DireccionPostal> direccionesPostales;

	public List<DireccionPostal> getDireccionesPostales() {
		
		return new ArrayList<DireccionPostal>(this.direccionesPostales);
	}

	public void setDireccionesPostales(List<DireccionPostal> direccionesPostales) {
		this.direccionesPostales = direccionesPostales;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	
	
}