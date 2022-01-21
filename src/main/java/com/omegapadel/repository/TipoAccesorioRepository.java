package com.omegapadel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.TipoAccesorio;

public interface TipoAccesorioRepository  extends CrudRepository<TipoAccesorio, Integer>{

	@Query("select m from TipoAccesorio m where m.nombre = ?1")
	public TipoAccesorio getTipoAccesorioPorNombre(String nombre);
	
}
