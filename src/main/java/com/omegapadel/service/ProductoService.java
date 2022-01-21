package com.omegapadel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Accesorio;
import com.omegapadel.model.Pala;
import com.omegapadel.model.Pelota;
import com.omegapadel.model.Producto;
import com.omegapadel.repository.ProductoRepository;

@Service
@Transactional
public class ProductoService {

	@Inject
	private ProductoRepository productoRepository;
//	@Inject
//	private PalaRepository palaRepository;
//	@Inject
//	private PelotaRepository pelotaRepository;
//	@Inject
//	private AccesorioRepository accesorioRepository;

	public <S extends Producto> S save(S entity) {
		return productoRepository.save(entity);
	}

	public Optional<Producto> findById(Integer id) {
		return productoRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return productoRepository.existsById(id);
	}

	public Iterable<Producto> findAll() {
		return productoRepository.findAll();
	}

	public long count() {
		return productoRepository.count();
	}

	public void deleteById(Integer id) {
		productoRepository.deleteById(id);
	}

	public void delete(Producto entity) {
		productoRepository.delete(entity);
	}

	public void deleteAll() {
		productoRepository.deleteAll();
	}

	
	public List<Producto> tratamientoListaProductos(String tipoProductoEscogido,
			List<Producto> productosEscogidos){
		
		List<Producto> prods = new ArrayList<Producto>();
		for(Producto p : productosEscogidos) {
			if(tipoProductoEscogido.equals("Pala") && p instanceof Pala) {
				prods.add(p);
			}
			if(tipoProductoEscogido.equals("Pelota") && p instanceof Pelota) {
				prods.add(p);
			}
			if(tipoProductoEscogido.equals("Accesorio") && p instanceof Accesorio) {
				prods.add(p);
			}
		}
		return prods;
	}
	
	
	
	
	public List<? extends Producto> getProductosDeUnTipo(String tipoProductoEscogido,
			List<Producto> productosEscogidos) {

		List<Producto> prods = tratamientoListaProductos(tipoProductoEscogido, productosEscogidos);
		
		switch (tipoProductoEscogido) {
		case "Pala":
			if (CollectionUtils.isEmpty(prods)) {
				return productoRepository.getProductosDeTipoPala();
			} else {
				return productoRepository.getProductosDeTipoPala(prods);
			}
		case "Pelota":
			if (CollectionUtils.isEmpty(prods)) {
				return productoRepository.getProductosDeTipoPelota();
			} else {
				return productoRepository.getProductosDeTipoPelota(prods);
			}
		case "Accesorio":
			if (CollectionUtils.isEmpty(prods)) {
				return productoRepository.getProductosDeTipoAccesorio();
			} else {
				return productoRepository.getProductosDeTipoAccesorio(prods);
			}
		default:
			return new ArrayList<Producto>();
		}

	}

}
