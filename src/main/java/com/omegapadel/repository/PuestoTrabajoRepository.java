package com.omegapadel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.PuestoTrabajo;

public interface PuestoTrabajoRepository  extends CrudRepository<PuestoTrabajo, Integer>{

	@Query("select m from PuestoTrabajo m where m.nombre = ?1")
	public PuestoTrabajo getPuestoTrabajoPorNombre(String nombre);
	
}
