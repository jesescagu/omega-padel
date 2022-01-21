package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Cliente;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.Empleado;
import com.omegapadel.model.PuestoTrabajo;
import com.omegapadel.model.Usuario;
import com.omegapadel.repository.EmpleadoRepository;

@Service
@Transactional
public class EmpleadoService {

	@Inject
	private EmpleadoRepository empleadoRepository;

	public <S extends Empleado> S save(S entity) {
		return empleadoRepository.save(entity);
	}

	public Optional<Empleado> findById(Integer id) {
		return empleadoRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return empleadoRepository.existsById(id);
	}

	public List<Empleado> findAll() {
		return (List<Empleado>) empleadoRepository.findAll();
	}

	public long count() {
		return empleadoRepository.count();
	}

	public void deleteById(Integer id) {
		empleadoRepository.deleteById(id);
	}

	public void delete(Empleado entity) {
		empleadoRepository.delete(entity);
	}

	public Empleado create(String dni, String nombre, String apellidos, String email,
			String telefono, List<DireccionPostal> direccionesPostales, Usuario usuario, PuestoTrabajo puesto) {
		Empleado c = new Empleado();
		c.setApellidos(apellidos);
		c.setTelefono(telefono);
		c.setEmail(email);
		c.setDireccionesPostales(direccionesPostales);
		c.setDni(dni);
		c.setNombre(nombre);
		c.setUsuario(usuario);
		c.setPuesto(puesto);
		return c;
	}

	public Empleado buscaEmpleadoPorNombreUsuario(String nombreUsuario) {
		return empleadoRepository.buscaEmpleadoPorNombreUsuario(nombreUsuario);
	}

	
}
