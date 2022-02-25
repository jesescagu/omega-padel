package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Empleado;
import com.omegapadel.model.PuestoTrabajo;
import com.omegapadel.repository.PuestoTrabajoRepository;

@Service
@Transactional
public class PuestoTrabajoService {

	@Inject
	private PuestoTrabajoRepository puestoTrabajoRepository;

	public <S extends PuestoTrabajo> S save(S entity) {
		return puestoTrabajoRepository.save(entity);
	}

	public Optional<PuestoTrabajo> findById(Integer id) {
		return puestoTrabajoRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return puestoTrabajoRepository.existsById(id);
	}

	public List<PuestoTrabajo> findAll() {
		return (List<PuestoTrabajo>) puestoTrabajoRepository.findAll();
	}

	public List<PuestoTrabajo> findAllActivos() {
		return puestoTrabajoRepository.findAllActivos();
	}

	public long count() {
		return puestoTrabajoRepository.count();
	}

	public void deleteById(Integer id) {
		puestoTrabajoRepository.deleteById(id);
	}

	public void delete(PuestoTrabajo entity) {
		puestoTrabajoRepository.delete(entity);
	}

	public PuestoTrabajo create(String nombre) {

		String primeraLetra = nombre.substring(0, 1).toUpperCase();
		String restoLetras = nombre.substring(1, nombre.length()).toLowerCase();
		String nombreReal = primeraLetra.concat(restoLetras);

		PuestoTrabajo pt = new PuestoTrabajo();
		pt.setNombre(nombreReal);
		pt.setActivo(true);
		return pt;
	}

	public PuestoTrabajo getPuestoTrabajoPorNombre(String nombre) {
		return puestoTrabajoRepository.getPuestoTrabajoPorNombre(nombre);
	}

	public List<Empleado> getEmpleadosDeUnPuestoTrabajo(String nombrePuestoTrabajo) {
		return puestoTrabajoRepository.getEmpleadosDeUnPuestoTrabajo(nombrePuestoTrabajo);
	}

	public List<Empleado> getEmpleadosActivosDeUnPuestoTrabajo(String nombrePuestoTrabajo) {
		return puestoTrabajoRepository.getEmpleadosActivosDeUnPuestoTrabajo(nombrePuestoTrabajo);
	}

}
