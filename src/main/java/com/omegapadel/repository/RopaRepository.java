package com.omegapadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Ropa;

public interface RopaRepository  extends CrudRepository<Ropa, Integer>{

	@Query("select p from Ropa p where p.marca.nombre = ?1")
	public List<Ropa> getRopaDeMarca(String m);
	
	@Query("select count(a) from Ropa a where a.tipoRopa.id = ?1")
	public Integer countRopaPorTipoRopa(Integer tipoRopaId);
	
}
