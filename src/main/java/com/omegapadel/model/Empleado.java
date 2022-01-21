package com.omegapadel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "empleado")
public class Empleado extends Actor {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8737277335958209330L;

	@Column(unique = true)
	@NotBlank
	private String dni;

	@ManyToOne(optional = true)
	private PuestoTrabajo puesto;

	@NotBlank
	private String nombre;

	@NotBlank
	private String apellidos;

	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String telefono;
	
	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public PuestoTrabajo getPuesto() {
		return puesto;
	}

	public void setPuesto(PuestoTrabajo puesto) {
		this.puesto = puesto;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

}
