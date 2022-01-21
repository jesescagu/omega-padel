package com.omegapadel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Zapatilla;

public interface ZapatillaRepository  extends CrudRepository<Zapatilla, Integer>{

	@Query("select p from Zapatilla p where p.marca.nombre = ?1")
	public List<Zapatilla> getZapatillasDeMarca(String m);
	
	@Query("select p from Zapatilla p where p.marca.nombre = ?1 and p.modelo = ?2 and p.sexo = ?3")
	public Optional<Zapatilla> getZapatillaUnica(String marca, String modelo, String sexo);
	
}
