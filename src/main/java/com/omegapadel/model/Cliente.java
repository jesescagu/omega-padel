package com.omegapadel.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "cliente")
public class Cliente extends Actor {

	private static final long serialVersionUID = -4290190578496384388L;

	@NotBlank
	private String nombre;

	@NotBlank
	private String apellidos;

	@NotBlank
	@Email
	private String email;

	// TODO
	// private List<TarjetaBancaria> tarjetaBancaria;

	@OneToOne(optional = true)
	private Cesta cesta;

	@ManyToMany
	private List<Anuncio> anunciosFavoritos;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	public Cesta getCesta() {
		return cesta;
	}

	public void setCesta(Cesta cesta) {
		this.cesta = cesta;
	}

	public List<Anuncio> getAnunciosFavoritos() {
		return anunciosFavoritos;
	}

	public void setAnunciosFavoritos(List<Anuncio> anunciosFavoritos) {
		this.anunciosFavoritos = anunciosFavoritos;
	}

}
