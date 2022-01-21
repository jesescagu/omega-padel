package com.omegapadel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Pelota;

public interface PelotaRepository  extends CrudRepository<Pelota, Integer>{

	@Query("select p from Pelota p where p.marca.nombre = ?1")
	public List<Pelota> getPelotasDeMarca(String m);
	
	@Query("select p from Pelota p where p.marca.nombre = ?1 and p.modelo = ?2 and p.numero = ?3")
	public Optional<Pelota> getPelotaUnica(String marca, String modelo, Integer numero);
	
}
