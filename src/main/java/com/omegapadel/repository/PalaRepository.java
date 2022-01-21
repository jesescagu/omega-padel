package com.omegapadel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Pala;

public interface PalaRepository  extends CrudRepository<Pala, Integer>{

	@Query("select p from Pala p where p.marca.nombre = ?1")
	public List<Pala> getPalasDeMarca(String m);
	
	@Query("select p from Pala p where p.marca.nombre = ?1 and p.modelo = ?2 and p.temporada = ?3")
	public Optional<Pala> getPalaUnica(String marca, String modelo, Integer temporada);
}
