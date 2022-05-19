package com.omegapadel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Accesorio;

public interface AccesorioRepository  extends CrudRepository<Accesorio, Integer>{

	@Query("select count(a) from Accesorio a where a.tipo.id = ?1")
	public Integer countAccesoriosPorTipoAccesorio(Integer tipoAccesorioId);
	
}
