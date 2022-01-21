package com.omegapadel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Integer>{

	@Query("select count(u) from Usuario u where u.usuario = ?1")
	public Integer cuentaNombreUsuario(String nombreUsuario);
	
}
