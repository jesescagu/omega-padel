package com.omegapadel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Municipio;

public interface MunicipioRepository  extends CrudRepository<Municipio, Integer>{

	@Query("select m from Municipio m order by m.descripcion")
	public List<Municipio> findAllOrdered();
	
	@Query("select m from Municipio m where m.codigo = ?1")
	public Optional<Municipio> getMunicipioPorCodigo(String codigo);
	
}
