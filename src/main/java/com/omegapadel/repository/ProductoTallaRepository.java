package com.omegapadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.ProductoTalla;

public interface ProductoTallaRepository extends CrudRepository<ProductoTalla, Integer> {

	@Query("select a from ProductoTalla a where a.anuncioCantidad.id = ?1")
	public List<ProductoTalla> getProductosTallaDeAnuncioCantidad(Integer anuncioCantidad);
	
	@Query("select a from ProductoTalla a where a.producto.id = ?1")
	public List<ProductoTalla> getProductosTallaDeProducto(Integer producto);

}
 