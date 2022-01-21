package com.omegapadel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Rol;

public interface RolRepository extends CrudRepository<Rol, String> {

	@Query("select r from Rol r where r.usuario = ?1")
	public Optional<Rol> buscaRolPorNombreUsuario(String nombreUsuario);
	
}
