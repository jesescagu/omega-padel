package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Marca;
import com.omegapadel.model.Pelota;
import com.omegapadel.repository.PelotaRepository;

@Service
@Transactional
public class PelotaService {

	@Inject
	private PelotaRepository pelotaRepository;

	public <S extends Pelota> S save(S entity) {
		return pelotaRepository.save(entity);
	}

	public Optional<Pelota> findById(Integer id) {
		return pelotaRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return pelotaRepository.existsById(id);
	}

	public List<Pelota> findAll() {
		return (List<Pelota>) pelotaRepository.findAll();
	}

	public long count() {
		return pelotaRepository.count();
	}

	public void deleteById(Integer id) {
		pelotaRepository.deleteById(id);
	}

	public void delete(Pelota entity) {
		pelotaRepository.delete(entity);
	}

	public List<Pelota> getPelotasDeMarca(String m) {
		return pelotaRepository.getPelotasDeMarca(m);
	}

	
	
	public Optional<Pelota> getPelotaUnica(String marca, String modelo, Integer numero) {
		return pelotaRepository.getPelotaUnica(marca, modelo, numero);
	}

	public Pelota create(Marca marca, String modelo, Integer numero, Integer stock) {
		Optional<Pelota> pelota = getPelotaUnica(marca.getNombre(), modelo, numero);

		Pelota p = null;
		if (!pelota.isPresent()) {
			p = new Pelota();
		} else {
			p = pelota.get();
		}
		p.setMarca(marca);
		p.setModelo(modelo);
		p.setNumero(numero);
		p.setStock(stock);
		return p;
	}

}
