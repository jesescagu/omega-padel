package com.omegapadel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Imagen;

public interface ImagenRepository extends CrudRepository<Imagen, Integer>{
	
	@Query("select i from Imagen i where i.nombre = ?1")
	public Optional<Imagen> getImagenPorNombre(String nombre);
	
	@Query("select i from Imagen i where i.producto.id = ?1")
	public List<Imagen> getImagenesDelProducto(Integer idProducto);
	
	@Query("select i from Imagen i where i.anuncio.id = ?1")
	public List<Imagen> getImagenesDelAnuncio(Integer idAnuncio);
	
}
