package com.omegapadel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Cliente;

public interface ClienteRepository extends CrudRepository<Cliente, Integer>{
	
	@Query("select c from Cliente c where c.usuario.usuario = ?1")
	public Cliente buscaClientePorNombreUsuario(String nombreUsuario);
	
}
