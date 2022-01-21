package com.omegapadel.service;

import java.util.Optional;

import javax.transaction.Transactional;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Institucion;
import com.omegapadel.model.TarjetaBancaria;
import com.omegapadel.repository.TarjetaBancariaRepository;

@Service
@Transactional
public class TarjetaBancariaService {

	@Inject
	private TarjetaBancariaRepository tarjetaBancariaRepository;

	public <S extends TarjetaBancaria> S save(S entity) {
		return tarjetaBancariaRepository.save(entity);
	}

	public Optional<TarjetaBancaria> findById(Integer id) {
		return tarjetaBancariaRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return tarjetaBancariaRepository.existsById(id);
	}

	public Iterable<TarjetaBancaria> findAll() {
		return tarjetaBancariaRepository.findAll();
	}

	public long count() {
		return tarjetaBancariaRepository.count();
	}

	public void deleteById(Integer id) {
		tarjetaBancariaRepository.deleteById(id);
	}

	public void delete(TarjetaBancaria entity) {
		tarjetaBancariaRepository.delete(entity);
	}
	
	
	public TarjetaBancaria create(String titular, String numero, Integer anyo, Integer mes, Integer cvv, Institucion institucion) {
		TarjetaBancaria t = new TarjetaBancaria();
		t.setAnyo(anyo);
		t.setCvv(cvv);
		t.setInstitucion(institucion);
		t.setMes(mes);
		t.setNumero(numero);
		t.setTitular(titular);
		return t;
	}
	
}
