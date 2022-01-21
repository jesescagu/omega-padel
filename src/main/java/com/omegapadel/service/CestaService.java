package com.omegapadel.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cesta;
import com.omegapadel.model.Cliente;
import com.omegapadel.repository.CestaRepository;

@Service
@Transactional
public class CestaService {

	@Inject
	private CestaRepository cestRepository;
	@Inject
	private ClienteService clienteService;

	public <S extends Cesta> S save(S entity) {
		return cestRepository.save(entity);
	}

	public Optional<Cesta> findById(Integer id) {
		return cestRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return cestRepository.existsById(id);
	}

	public Iterable<Cesta> findAll() {
		return cestRepository.findAll();
	}

	public long count() {
		return cestRepository.count();
	}

	public void deleteById(Integer id) {
		cestRepository.deleteById(id);
	}

	public void delete(Cesta entity) {
		cestRepository.delete(entity);
	}

	public Cesta create() {
		Cesta c = new Cesta();
		c.setCosto(0.0);

		Map<Integer, Integer> anuncios = new HashMap<Integer, Integer>();
		c.setMapaAnunciosCantidad(anuncios);

		return c;
	}

	public void addAnuncioAlCarrito(Anuncio anuncio, Cliente clienteLogado) {

		if (clienteLogado != null) {
			
			Cesta cestaCliente = clienteLogado.getCesta();
			if (cestaCliente == null) {
				cestaCliente = create();

				Map<Integer, Integer> mapaAnuncios = new HashMap<Integer, Integer>();
				mapaAnuncios.put(anuncio.getId(), 1);

				cestaCliente.setMapaAnunciosCantidad(mapaAnuncios);
				cestaCliente.setCosto(anuncio.getPrecio());
				
			} else {
				Map<Integer, Integer> mapaAnuncios = cestaCliente.getMapaAnunciosCantidad();
				if (mapaAnuncios.containsKey(anuncio.getId())) {
					mapaAnuncios.put(anuncio.getId(), mapaAnuncios.get(anuncio.getId()) + 1);
				} else {
					mapaAnuncios.put(anuncio.getId(), 1);
				}
				cestaCliente.setMapaAnunciosCantidad(mapaAnuncios);
				Double costoTotal = cestaCliente.getCosto() + anuncio.getPrecio();
				cestaCliente.setCosto(costoTotal);
			}
			Cesta cestaSaved = save(cestaCliente);
			clienteLogado.setCesta(cestaSaved);
			clienteService.save(clienteLogado);
		}
	}

}
