package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.TipoRopa;
import com.omegapadel.repository.TipoRopaRepository;

@Service
@Transactional
public class TipoRopaService {

	@Inject
	private TipoRopaRepository tipoRopaRepository;

	public <S extends TipoRopa> S save(S entity) {
		return tipoRopaRepository.save(entity);
	}

	public Optional<TipoRopa> findById(Integer id) {
		return tipoRopaRepository.findById(id);
	}

	public TipoRopa getTipoRopaPorNombre(String nombre) {
		return tipoRopaRepository.getTipoRopaPorNombre(nombre);
	}

	public boolean existsById(Integer id) {
		return tipoRopaRepository.existsById(id);
	}

	public List<TipoRopa> findAll() {
		return (List<TipoRopa>) tipoRopaRepository.findAll();
	}

	public long count() {
		return tipoRopaRepository.count();
	}

	public void deleteById(Integer id) {
		tipoRopaRepository.deleteById(id);
	}

	public void delete(TipoRopa entity) {
		tipoRopaRepository.delete(entity);
	}

	public TipoRopa create(String nombre, String tipoTalla) {

		String primeraLetra = nombre.substring(0, 1).toUpperCase();
		String restoLetras = nombre.substring(1, nombre.length()).toLowerCase();
		String nombreReal = primeraLetra.concat(restoLetras);

		TipoRopa t = new TipoRopa();
		t.setTipoRopa(nombreReal);
		t.setTipoTalla(tipoTalla);
		return t;
	}

}
