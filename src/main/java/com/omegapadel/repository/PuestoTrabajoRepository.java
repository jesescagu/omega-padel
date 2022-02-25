package com.omegapadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Empleado;
import com.omegapadel.model.PuestoTrabajo;

public interface PuestoTrabajoRepository extends CrudRepository<PuestoTrabajo, Integer> {

	@Query("select m from PuestoTrabajo m where m.nombre = ?1")
	public PuestoTrabajo getPuestoTrabajoPorNombre(String nombre);

	@Query("select m from PuestoTrabajo m where m.activo = true")
	public List<PuestoTrabajo> findAllActivos();

	@Query("select e from Empleado e where e.puesto.nombre = ?1")
	public List<Empleado> getEmpleadosDeUnPuestoTrabajo(String nombrePuestoTrabajo);
	
	@Query("select e from Empleado e where e.puesto.nombre = ?1 and e.usuario.activo = true")
	public List<Empleado> getEmpleadosActivosDeUnPuestoTrabajo(String nombrePuestoTrabajo);
}
