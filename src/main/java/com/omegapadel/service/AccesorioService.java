package com.omegapadel.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Accesorio;
import com.omegapadel.model.Marca;
import com.omegapadel.model.TipoAccesorio;
import com.omegapadel.repository.AccesorioRepository;
import com.omegapadel.repository.ProductoRepository;

@Service
@Transactional
public class AccesorioService {

	@Inject
	private AccesorioRepository accesorioRepository;
	@Inject
	private ProductoRepository productoRepository;

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

	public Accesorio create(TipoAccesorio ta, Marca marca, String modelo, Integer stock,
			Map<String, Integer> mapaTallasStock, String referencia) {
		Accesorio a = new Accesorio();
		a.setTipo(ta);
		a.setMarca(marca);
		a.setModelo(modelo);
		a.setStock(stock);
		a.setReferencia(referencia);
		a.setMapaTallaStock(mapaTallasStock);
		return a;
	}

	public Boolean existeReferencia(String referencia) {
		return productoRepository.existeReferencia(referencia) > 0;
	}

}
