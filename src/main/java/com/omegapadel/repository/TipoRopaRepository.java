package com.omegapadel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.TipoRopa;

public interface TipoRopaRepository  extends CrudRepository<TipoRopa, Integer>{

	@Query("select m from TipoRopa m where m.tipoRopa = ?1")
	public TipoRopa getTipoRopaPorNombre(String nombre);
	
}
