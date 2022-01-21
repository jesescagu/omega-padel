package com.omegapadel.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Cliente;
import com.omegapadel.model.Empleado;

public interface EmpleadoRepository extends CrudRepository<Empleado, Integer>{
	
	@Query("select c from Empleado c where c.usuario.usuario = ?1")
	public Empleado buscaEmpleadoPorNombreUsuario(String nombreUsuario);
	
}
