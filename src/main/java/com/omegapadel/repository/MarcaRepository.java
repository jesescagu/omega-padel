package com.omegapadel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Marca;

public interface MarcaRepository extends CrudRepository<Marca, Integer>{
	
	@Query("select m from Marca m where m.nombre = ?1")
	public Marca getMarcaPorNombre(String nombre);

	@Query("select count(m) from Producto m where m.marca.id = ?1")
	public Integer countProductoDeMarca(Integer marcaId);
	
}
