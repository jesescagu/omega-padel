package com.omegapadel.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Marca;
import com.omegapadel.model.Zapatilla;
import com.omegapadel.repository.ZapatillaRepository;

@Service
@Transactional
public class ZapatillaService {

	@Inject
	private ZapatillaRepository zapatillaRepository;

	public <S extends Zapatilla> S save(S entity) {
		return zapatillaRepository.save(entity);
	}

	public List<Zapatilla> getZapatillasDeMarca(String m) {
		return zapatillaRepository.getZapatillasDeMarca(m);
	}

	public Optional<Zapatilla> findById(Integer id) {
		return zapatillaRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return zapatillaRepository.existsById(id);
	}

	public List<Zapatilla> findAll() {
		return (List<Zapatilla>) zapatillaRepository.findAll();
	}

	public long count() {
		return zapatillaRepository.count();
	}

	public void deleteById(Integer id) {
		zapatillaRepository.deleteById(id);
	}

	public void delete(Zapatilla entity) {
		zapatillaRepository.delete(entity);
	}

	public Zapatilla create(Marca marca, String modelo, Integer stock, String sexo, Map<Integer,Integer> mapaTallaStock) {
		
		Optional<Zapatilla> zapa = getZapatillaUnica(marca.getNombre(), modelo, sexo);

		Zapatilla p = null;
		if (!zapa.isPresent()) {
			p = new Zapatilla();
		} else {
			p = zapa.get();
		}
		
		p.setMapaTallaStock(mapaTallaStock);
		p.setMarca(marca);
		p.setModelo(modelo);
		p.setStock(stock);
		p.setSexo(sexo);
		return p;
	}

	public Optional<Zapatilla> getZapatillaUnica(String marca, String modelo, String sexo) {
		return zapatillaRepository.getZapatillaUnica(marca, modelo, sexo);
	}

}
