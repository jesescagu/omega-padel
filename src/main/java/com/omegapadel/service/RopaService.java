package com.omegapadel.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Marca;
import com.omegapadel.model.Ropa;
import com.omegapadel.model.TipoRopa;
import com.omegapadel.repository.ProductoRepository;
import com.omegapadel.repository.RopaRepository;

@Service
@Transactional
public class RopaService {

	@Inject
	private RopaRepository ropaRepository;
	@Inject
	private ProductoRepository productoRepository;

	public <S extends Ropa> S save(S entity) {
		return ropaRepository.save(entity);
	}

	public List<Ropa> getRopaDeMarca(String m) {
		return ropaRepository.getRopaDeMarca(m);
	}

	public Optional<Ropa> findById(Integer id) {
		return ropaRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return ropaRepository.existsById(id);
	}

	public List<Ropa> findAll() {
		return (List<Ropa>) ropaRepository.findAll();
	}

	public long count() {
		return ropaRepository.count();
	}

	public void deleteById(Integer id) {
		ropaRepository.deleteById(id);
	}

	public void delete(Ropa entity) {
		ropaRepository.delete(entity);
	}

	public Ropa create(Marca marca, String modelo, Integer stock, String sexo, TipoRopa tipoRopa,
			Map<String, Integer> mapaTallasStock, String referencia) {
		Ropa r = new Ropa();
		r.setMarca(marca);
		r.setModelo(modelo);
		r.setStock(stock);
		r.setSexo(sexo);
		r.setTipoRopa(tipoRopa);
		r.setMapaTallaStock(mapaTallasStock);
		r.setReferencia(referencia);
		return r;

	}

	public Integer countRopaPorTipoRopa(Integer tipoRopaId) {
		return ropaRepository.countRopaPorTipoRopa(tipoRopaId);
	}

	public Boolean existeReferencia(String referencia) {
		return productoRepository.existeReferencia(referencia) > 0;
	}

}
