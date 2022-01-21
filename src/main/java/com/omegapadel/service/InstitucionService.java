package com.omegapadel.service;

import java.util.Optional;

import javax.transaction.Transactional;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Institucion;
import com.omegapadel.repository.InstitucionRepository;

@Service
@Transactional
public class InstitucionService {

	@Inject
	private InstitucionRepository institucionRepository;

	public <S extends Institucion> S save(S entity) {
		return institucionRepository.save(entity);
	}

	public Optional<Institucion> findById(Integer id) {
		return institucionRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return institucionRepository.existsById(id);
	}

	public Iterable<Institucion> findAll() {
		return institucionRepository.findAll();
	}

	public long count() {
		return institucionRepository.count();
	}

	public void deleteById(Integer id) {
		institucionRepository.deleteById(id);
	}

	public void delete(Institucion entity) {
		institucionRepository.delete(entity);
	}
	
	public Institucion create() {
		Institucion i = new Institucion();
		return i;
	}
	
}
