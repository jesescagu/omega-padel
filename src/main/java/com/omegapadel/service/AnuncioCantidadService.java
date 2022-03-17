package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.AnuncioCantidad;
import com.omegapadel.model.Cesta;
import com.omegapadel.model.Pedido;
import com.omegapadel.repository.AnuncioCantidadRepository;

@Service
@Transactional
public class AnuncioCantidadService {

	@Inject
	private AnuncioCantidadRepository anuncioCantidadRepository;

	public <S extends AnuncioCantidad> S save(S entity) {
		return anuncioCantidadRepository.save(entity);
	}

	public Optional<AnuncioCantidad> findById(Integer id) {
		return anuncioCantidadRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return anuncioCantidadRepository.existsById(id);
	}

	public List<AnuncioCantidad> findAll() {
		return (List<AnuncioCantidad>) anuncioCantidadRepository.findAll();
	}

	public long count() {
		return anuncioCantidadRepository.count();
	}

	public void deleteById(Integer id) {
		anuncioCantidadRepository.deleteById(id);
	}

	public void delete(AnuncioCantidad entity) {
		anuncioCantidadRepository.delete(entity);
	}

	public AnuncioCantidad create(Anuncio anuncio, Cesta cesta) {
		AnuncioCantidad a = new AnuncioCantidad();
		a.setAnuncio(anuncio);
		a.setCesta(cesta);
		a.setCantidad(1);
		a.setPedido(null);
		return a;
	}

	public void intercambiaCestaPorPedidoAnuncios(Cesta cesta, Pedido pedido) {

		List<AnuncioCantidad> listaAnuncios = getAnunciosCantidadDeCesta(cesta.getId());
		for (AnuncioCantidad a : listaAnuncios) {
			a.setCesta(null);
			a.setPedido(pedido);
			save(a);
		}

	}

	public List<AnuncioCantidad> getAnunciosCantidadDeCesta(Integer cesta) {
		return anuncioCantidadRepository.getAnunciosCantidadDeCesta(cesta);
	}

	public List<AnuncioCantidad> getAnunciosCantidadDePedido(Integer pedido) {
		return anuncioCantidadRepository.getAnunciosCantidadDePedido(pedido);
	}

	public Optional<AnuncioCantidad> getAnunciosCantidadDeCestaYAnuncio(Integer cestaId, Integer anuncioId) {
		return anuncioCantidadRepository.getAnunciosCantidadDeCestaYAnuncio(cestaId, anuncioId);
	}

}
