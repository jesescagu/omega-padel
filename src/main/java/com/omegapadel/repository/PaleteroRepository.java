package com.omegapadel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Paletero;

public interface PaleteroRepository extends CrudRepository<Paletero, Integer> {

	@Query("select p from Paletero p where p.marca.nombre = ?1")
	public List<Paletero> getPaleterosDeMarca(String m);

	@Query("select p from Paletero p where p.marca.nombre = ?1 and p.modelo = ?2")
	public Optional<Paletero> getPaleteroUnica(String marca, String modelo);

}
