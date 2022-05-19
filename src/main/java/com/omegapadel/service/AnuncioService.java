package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Empleado;
import com.omegapadel.model.Producto;
import com.omegapadel.repository.AnuncioRepository;

@Service
@Transactional
public class AnuncioService {

	@Inject
	private AnuncioRepository anuncioRepository;

	public <S extends Anuncio> S save(S entity) {
		return anuncioRepository.save(entity);
	}

	public Optional<Anuncio> findById(Integer id) {
		return anuncioRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return anuncioRepository.existsById(id);
	}

	public List<Anuncio> findAll() {
		return (List<Anuncio>) anuncioRepository.findAll();
	}

	public long count() {
		return anuncioRepository.count();
	}

	public void deleteById(Integer id) {
		anuncioRepository.deleteById(id);
	}

	public void delete(Anuncio entity) {
		anuncioRepository.delete(entity);
	}

	public List<Anuncio> getAnunciosRelevantes() {
		return anuncioRepository.getAnunciosRelevantes();
	}

	public List<Anuncio> getAnunciosPorMarcaPelota(String marca) {
		return anuncioRepository.getAnunciosPorMarcaPelota(marca);
	}

	public List<Anuncio> getAnunciosPorMarcaPala(String marca) {
		return anuncioRepository.getAnunciosPorMarcaPala(marca);
	}

	public List<Anuncio> getAnunciosPorMarcaPaletero(String marca) {
		return anuncioRepository.getAnunciosPorMarcaPaletero(marca);
	}

	public List<Anuncio> getAnunciosPorTipoRopa(String tipo) {
		return anuncioRepository.getAnunciosPorTipoRopa(tipo);
	}

	public List<Anuncio> getAnunciosPorMarcaZapatilla(String marca) {
		return anuncioRepository.getAnunciosPorMarcaZapatilla(marca);
	}

	public Anuncio create(String titulo, String descripcion, Double precio, Empleado empleado,
			List<Producto> productos) {
		Anuncio a = new Anuncio();
		a.setDescripcion(descripcion);
		a.setEmpleado(empleado);
		a.setPrecio(precio);
		a.setProductos(productos);
		a.setTitulo(titulo);
		a.setActivo(true);
		return a;
	}

	public List<Anuncio> getAnunciosDesactivados() {
		return anuncioRepository.getAnunciosDesactivados();
	}

	public List<Anuncio> getAnunciosPorMarcaPack(String marca) {
		return anuncioRepository.getAnunciosPorMarcaPack(marca);
	}

	public Integer countAnunciosDeProducto(Integer productoId) {
		return anuncioRepository.countAnunciosDeProducto(productoId);
	}

	public List<Anuncio> getAnunciosPorTipo(String tipo) {

		switch (tipo) {
		case "Pala":
			return anuncioRepository.getAnunciosPala();
		case "Pelota":
			return anuncioRepository.getAnunciosPelota();
		case "Paletero":
			return anuncioRepository.getAnunciosPaletero();
		case "Zapatilla":
			return anuncioRepository.getAnunciosZapatilla();
		case "Ropa":
			return anuncioRepository.getAnunciosRopa();
		case "Accesorio":
			return anuncioRepository.getAnunciosAccesorio();
		default:
			return null;
		}
	}

	public List<Anuncio> getPacksAnuncios() {
		return anuncioRepository.getPacksAnuncios();
	}

	public List<Anuncio> getAnunciosPorMarcaRopa(String marca) {
		return anuncioRepository.getAnunciosPorMarcaRopa(marca);
	}

	public List<Anuncio> getAnunciosPorTipoAccesorio(String tipoAccesorio) {
		return anuncioRepository.getAnunciosPorTipoAccesorio(tipoAccesorio);
	}

	public List<Anuncio> getAnunciosPorTextoBuscador(String texto){
		
		return anuncioRepository.getAnunciosPorTextoBuscador(texto);
	}
	
}
