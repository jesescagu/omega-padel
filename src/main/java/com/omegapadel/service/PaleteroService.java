package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Marca;
import com.omegapadel.model.Paletero;
import com.omegapadel.repository.PaleteroRepository;

@Service
@Transactional
public class PaleteroService {

	@Inject
	private PaleteroRepository paleteroRepository;

	public <S extends Paletero> S save(S entity) {
		return paleteroRepository.save(entity);
	}

	public List<Paletero> getPaleterosDeMarca(String m) {
		return paleteroRepository.getPaleterosDeMarca(m);
	}

	public Optional<Paletero> findById(Integer id) {
		return paleteroRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return paleteroRepository.existsById(id);
	}

	public List<Paletero> findAll() {
		return (List<Paletero>) paleteroRepository.findAll();
	}

	public long count() {
		return paleteroRepository.count();
	}

	public void deleteById(Integer id) {
		paleteroRepository.deleteById(id);
	}

	public void delete(Paletero entity) {
		paleteroRepository.delete(entity);
	}

	public Paletero create(Marca marca, String modelo, Integer stock) {
		
		Optional<Paletero> paletero = getPaleteroUnica(marca.getNombre(), modelo);

		Paletero p = null;
		if (!paletero.isPresent()) {
			p = new Paletero();
		} else {
			p = paletero.get();
		}
		
		p.setMarca(marca);
		p.setModelo(modelo);
		p.setStock(stock);
		return p;
	}
	
	public Optional<Paletero> getPaleteroUnica(String marca, String modelo) {
		return paleteroRepository.getPaleteroUnica(marca, modelo);
	}

}
