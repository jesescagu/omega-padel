package com.omegapadel.service;

import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Actor;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.Municipio;
import com.omegapadel.repository.DireccionPostalRepository;

@Service
@Transactional
public class DireccionPostalService {

	@Inject
	private DireccionPostalRepository direccionPostalRepository;

	public <S extends DireccionPostal> S save(S entity) {
		return direccionPostalRepository.save(entity);
	}

	public Optional<DireccionPostal> findById(Integer id) {
		return direccionPostalRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return direccionPostalRepository.existsById(id);
	}

	public Iterable<DireccionPostal> findAll() {
		return direccionPostalRepository.findAll();
	}

	public long count() {
		return direccionPostalRepository.count();
	}

	public void deleteById(Integer id) {
		direccionPostalRepository.deleteById(id);
	}

	public void delete(DireccionPostal entity) {
		direccionPostalRepository.delete(entity);
	}
	
	public DireccionPostal create(String direccion, String codigoPostal, Municipio municipio, Actor actor) {
		
		DireccionPostal dp = new DireccionPostal();
		dp.setDireccion(direccion);
		dp.setCodigoPostal(codigoPostal);
		dp.setMunicipio(municipio);
		dp.setActor(actor);
		return dp;
		
	}
}
