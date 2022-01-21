package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Marca;
import com.omegapadel.model.Pala;
import com.omegapadel.repository.PalaRepository;

@Service
@Transactional
public class PalaService {

	@Inject
	private PalaRepository palaRepository;

	public <S extends Pala> S save(S entity) {
		return palaRepository.save(entity);
	}

	public List<Pala> getPalasDeMarca(String m) {
		return palaRepository.getPalasDeMarca(m);
	}

	public Optional<Pala> findById(Integer id) {
		return palaRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return palaRepository.existsById(id);
	}

	public List<Pala> findAll() {
		return (List<Pala>) palaRepository.findAll();
	}

	public long count() {
		return palaRepository.count();
	}

	public void deleteById(Integer id) {
		palaRepository.deleteById(id);
	}

	public void delete(Pala entity) {
		palaRepository.delete(entity);
	}

	public Optional<Pala> getPalaUnica(String marca, String modelo, Integer temporada) {
		return palaRepository.getPalaUnica(marca, modelo, temporada);
	}

	public Pala create(Marca marca, String modelo, Integer stock, Integer temporada) {

		Optional<Pala> pala = getPalaUnica(marca.getNombre(), modelo, temporada);

		Pala p = null;
		if (!pala.isPresent()) {
			p = new Pala();
		} else {
			p = pala.get();
		}

		p.setMarca(marca);
		p.setModelo(modelo);
		p.setStock(stock);
		p.setTemporada(temporada);
		return p;
	}
}
