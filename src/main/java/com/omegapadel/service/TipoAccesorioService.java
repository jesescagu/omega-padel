package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.TipoAccesorio;
import com.omegapadel.repository.TipoAccesorioRepository;

@Service
@Transactional
public class TipoAccesorioService {

	@Inject
	private TipoAccesorioRepository tipoAccesorioRepository;

	public <S extends TipoAccesorio> S save(S entity) {
		return tipoAccesorioRepository.save(entity);
	}

	public Optional<TipoAccesorio> findById(Integer id) {
		return tipoAccesorioRepository.findById(id);
	}

	public TipoAccesorio getTipoAccesorioPorNombre(String nombre) {
		return tipoAccesorioRepository.getTipoAccesorioPorNombre(nombre);
	}

	public boolean existsById(Integer id) {
		return tipoAccesorioRepository.existsById(id);
	}

	public List<TipoAccesorio> findAll() {
		return (List<TipoAccesorio>) tipoAccesorioRepository.findAll();
	}

	public long count() {
		return tipoAccesorioRepository.count();
	}

	public void deleteById(Integer id) {
		tipoAccesorioRepository.deleteById(id);
	}

	public void delete(TipoAccesorio entity) {
		tipoAccesorioRepository.delete(entity);
	}

	public TipoAccesorio create(String nombre) {
		
		 String primeraLetra = nombre.substring(0, 1).toUpperCase();
	        String restoLetras = nombre.substring(1, nombre.length()).toLowerCase();
	        String nombreReal = primeraLetra.concat(restoLetras);
		
		TipoAccesorio t = new TipoAccesorio();
		t.setNombre(nombreReal);
		return t;
	}

}
