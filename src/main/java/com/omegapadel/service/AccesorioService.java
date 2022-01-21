package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Accesorio;
import com.omegapadel.model.Marca;
import com.omegapadel.model.TipoAccesorio;
import com.omegapadel.repository.AccesorioRepository;

@Service
@Transactional
public class AccesorioService {

	@Inject
	private AccesorioRepository accesorioRepository;

	public <S extends Accesorio> S save(S entity) {
		return accesorioRepository.save(entity);
	}

	public Optional<Accesorio> findById(Integer id) {
		return accesorioRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return accesorioRepository.existsById(id);
	}

	public List<Accesorio> findAll() {
		return (List<Accesorio>) accesorioRepository.findAll();
	}

	public long count() {
		return accesorioRepository.count();
	}

	public void deleteById(Integer id) {
		accesorioRepository.deleteById(id);
	}

	public void delete(Accesorio entity) {
		accesorioRepository.delete(entity);
	}

	public Integer countAccesoriosPorTipoAccesorio(Integer tipoAccesorioId) {
		return accesorioRepository.countAccesoriosPorTipoAccesorio(tipoAccesorioId);
	}

	public Accesorio create(TipoAccesorio ta, Marca marca, String modelo, Integer stock) {
		Accesorio a = new Accesorio();
		a.setTipo(ta);
		a.setMarca(marca);
		a.setModelo(modelo);
		a.setStock(stock);
		return a;
	}

}
