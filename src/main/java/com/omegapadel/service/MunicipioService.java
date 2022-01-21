package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Municipio;
import com.omegapadel.repository.MunicipioRepository;

@Service
@Transactional
public class MunicipioService {

	@Inject
	private MunicipioRepository municipioRepository;

	public <S extends Municipio> S save(S entity) {
		return municipioRepository.save(entity);
	}

	public Optional<Municipio> findById(Integer id) {
		return municipioRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return municipioRepository.existsById(id);
	}

	public Iterable<Municipio> findAll() {
		return municipioRepository.findAll();
	}

	public long count() {
		return municipioRepository.count();
	}

	public void deleteById(Integer id) {
		municipioRepository.deleteById(id);
	}

	public void delete(Municipio entity) {
		municipioRepository.delete(entity);
	}

	public Municipio createAndSave(String codigo, String descripcion) {
		Municipio m = new Municipio();
		m.setCodigo(codigo);
		m.setDescripcion(descripcion);
		Municipio saved = save(m);
		return saved;
	}

	public List<Municipio> findAllOrdered() {
		return municipioRepository.findAllOrdered();
	}

	public Optional<Municipio> getMunicipioPorCodigo(String codigo) {
		return municipioRepository.getMunicipioPorCodigo(codigo);
	}

}
