package com.omegapadel.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Marca;
import com.omegapadel.repository.MarcaRepository;

@Service
@Transactional
public class MarcaService {

	@Inject
	private MarcaRepository marcaRepository;

	@Inject
	private AnuncioService anuncioService;

	public <S extends Marca> S save(S entity) {
		return marcaRepository.save(entity);
	}

	public List<String> getTodasMarcasPalas() {

		Collection<Marca> marcas = findAll();
		List<String> res = new ArrayList<String>();
		for (Marca m : marcas) {

			List<Anuncio> anuncios = anuncioService.getAnunciosPorMarcaPala(m.getNombre());
			if (!CollectionUtils.isEmpty(anuncios)) {
				res.add(m.getNombre());
			}
		}
		return res;
	}

	public List<String> getTodasMarcasPaletero() {

		Collection<Marca> marcas = findAll();
		List<String> res = new ArrayList<String>();
		for (Marca m : marcas) {

			List<Anuncio> anuncios = anuncioService.getAnunciosPorMarcaPaletero(m.getNombre());
			if (!CollectionUtils.isEmpty(anuncios)) {
				res.add(m.getNombre());
			}
		}
		return res;
	}

	public List<String> getTodasMarcasRopa() {

		Collection<Marca> marcas = findAll();
		List<String> res = new ArrayList<String>();
		for (Marca m : marcas) {

//			List<Anuncio> anuncios = anuncioService.getAnunciosPorMarcaRopa(m.getNombre());
			List<Anuncio> anuncios = anuncioService.findAll();
			if (!CollectionUtils.isEmpty(anuncios)) {
				res.add(m.getNombre());
			}
		}
		return res;
	}

	public List<String> getTodasMarcasZapatillas() {

		Collection<Marca> marcas = findAll();
		List<String> res = new ArrayList<String>();
		for (Marca m : marcas) {

			List<Anuncio> anuncios = anuncioService.getAnunciosPorMarcaZapatilla(m.getNombre());
			if (!CollectionUtils.isEmpty(anuncios)) {
				res.add(m.getNombre());
			}
		}
		return res;
	}

	public List<String> getTodasMarcasPelotas() {

		Collection<Marca> marcas = findAll();
		List<String> res = new ArrayList<String>();
		for (Marca m : marcas) {

			List<Anuncio> anuncios = anuncioService.getAnunciosPorMarcaPelota(m.getNombre());
			if (!CollectionUtils.isEmpty(anuncios)) {
				res.add(m.getNombre());
			}
		}
		return res;
	}

	public List<String> getTodasMarcasPacks() {

		Collection<Marca> marcas = findAll();
		List<String> res = new ArrayList<String>();
		for (Marca m : marcas) {

			List<Anuncio> anuncios = anuncioService.getAnunciosPorMarcaPack(m.getNombre());
			if (!CollectionUtils.isEmpty(anuncios)) {
				res.add(m.getNombre());
			}
		}
		return res;
	}

	public Optional<Marca> findById(Integer id) {
		return marcaRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return marcaRepository.existsById(id);
	}

	public List<Marca> findAll() {
		return (List<Marca>) marcaRepository.findAll();
	}

	public long count() {
		return marcaRepository.count();
	}

	public void deleteById(Integer id) {
		marcaRepository.deleteById(id);
	}

	public void delete(Marca entity) {

		marcaRepository.delete(entity);
	}

	public Marca create(String nombre) {

		String primeraLetra = nombre.substring(0, 1).toUpperCase();
		String restoLetras = nombre.substring(1, nombre.length()).toLowerCase();
		String nombreReal = primeraLetra.concat(restoLetras);

		Marca c = new Marca();
		c.setNombre(nombreReal);
		return c;
	}

	public Marca getMarcaPorNombre(String nombre) {
		return marcaRepository.getMarcaPorNombre(nombre);
	}

	public Integer countProductoDeMarca(Integer marcaId) {
		return marcaRepository.countProductoDeMarca(marcaId);
	}

}
