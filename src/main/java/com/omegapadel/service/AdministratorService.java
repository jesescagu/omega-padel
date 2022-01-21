package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Administrador;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.Usuario;
import com.omegapadel.repository.AdministradorRepository;

@Service
@Transactional
public class AdministratorService {

	@Inject
	private AdministradorRepository adminiRepository;

	public <S extends Administrador> S save(S entity) {
		return adminiRepository.save(entity);
	}

	public Optional<Administrador> findById(Integer id) {
		return adminiRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return adminiRepository.existsById(id);
	}

	public Iterable<Administrador> findAll() {
		return adminiRepository.findAll();
	}

	public long count() {
		return adminiRepository.count();
	}

	public void deleteById(Integer id) {
		adminiRepository.deleteById(id);
	}

	public void delete(Administrador entity) {
		adminiRepository.delete(entity);
	}
	
	public Administrador create(Usuario usuario, List<DireccionPostal> direccionesPostales) {
		Administrador a = new Administrador();
		a.setDireccionesPostales(direccionesPostales);
		a.setUsuario(usuario);
		return a;
	}
	
}
