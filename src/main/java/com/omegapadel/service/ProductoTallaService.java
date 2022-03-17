package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.AnuncioCantidad;
import com.omegapadel.model.Producto;
import com.omegapadel.model.ProductoTalla;
import com.omegapadel.repository.ProductoTallaRepository;

@Service
@Transactional
public class ProductoTallaService {

	@Inject
	private ProductoTallaRepository productoTallaRepository;

	public <S extends ProductoTalla> S save(S entity) {
		return productoTallaRepository.save(entity);
	}

	public Optional<ProductoTalla> findById(Integer id) {
		return productoTallaRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return productoTallaRepository.existsById(id);
	}

	public List<ProductoTalla> findAll() {
		return (List<ProductoTalla>) productoTallaRepository.findAll();
	}

	public long count() {
		return productoTallaRepository.count();
	}

	public void deleteById(Integer id) {
		productoTallaRepository.deleteById(id);
	}

	public void delete(ProductoTalla entity) {
		productoTallaRepository.delete(entity);
	}

	public ProductoTalla create(AnuncioCantidad anuncio, Producto producto, String talla) {
		ProductoTalla p = new ProductoTalla();
		p.setProducto(producto);
		p.setTalla(talla);
		p.setAnuncioCantidad(anuncio);
		return p;
	}

}
