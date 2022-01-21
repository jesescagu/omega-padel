package com.omegapadel.service;

import java.sql.Blob;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;
import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Imagen;
import com.omegapadel.repository.ImagenRepository;

@Service
@Transactional
public class ImagenService {

	@Inject
	private ImagenRepository imagenRepository;

	public <S extends Imagen> S save(S entity) {
		return imagenRepository.save(entity);
	}

	public Optional<Imagen> findById(Integer id) {
		return imagenRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return imagenRepository.existsById(id);
	}

	public Collection<Imagen> findAll() {
		return (Collection<Imagen>) imagenRepository.findAll();
	}

	public long count() {
		return imagenRepository.count();
	}

	public void deleteById(Integer id) {
		imagenRepository.deleteById(id);
	}

	public void delete(Imagen entity) {
		imagenRepository.delete(entity);
	}

	public Imagen create(String nombre, byte[] content) {

		try {
			Imagen c = new Imagen();
			c.setNombre(getNombreImagenUnicoGenerado(nombre));
			Blob blob;
			blob = new SerialBlob(content);
			c.setContent(blob);
			return c;

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalArgumentException();
		}
	}

	public Optional<Imagen> getImagenPorNombre(String nombre) {
		return imagenRepository.getImagenPorNombre(nombre);
	}
	
	

	public List<Imagen> getImagenesDelProducto(Integer idProducto) {
		return imagenRepository.getImagenesDelProducto(idProducto);
	}

	public String getNombreImagenUnicoGenerado(String nombre) {
		String cadenaGenerada = "";
		String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Integer longitud = 10;
		String resultado = "";

		for (int x = 0; x < longitud; x++) {
			int indiceAleatorio = ThreadLocalRandom.current().nextInt(0, banco.length() - 1);
			char caracterAleatorio = banco.charAt(indiceAleatorio);
			cadenaGenerada += caracterAleatorio;
		}

		resultado = cadenaGenerada + "-" + nombre;

		Optional<Imagen> oi = getImagenPorNombre(resultado);

		if (oi.isPresent()) {
			return getNombreImagenUnicoGenerado(nombre);
		} else {
			return resultado;
		}

	}

	public List<Imagen> getImagenesDelAnuncio(Integer idAnuncio) {
		return imagenRepository.getImagenesDelAnuncio(idAnuncio);
	}

	

}
