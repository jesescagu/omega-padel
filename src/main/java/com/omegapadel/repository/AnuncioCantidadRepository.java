package com.omegapadel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.AnuncioCantidad;

public interface AnuncioCantidadRepository extends CrudRepository<AnuncioCantidad, Integer> {

	@Query("select a from AnuncioCantidad a where a.cesta.id = ?1")
	public List<AnuncioCantidad> getAnunciosCantidadDeCesta(Integer cesta);

	@Query("select a from AnuncioCantidad a where a.pedido.id = ?1")
	public List<AnuncioCantidad> getAnunciosCantidadDePedido(Integer pedido);

	@Query("select a from AnuncioCantidad a where a.cesta.id = ?1 and a.anuncio.id = ?2")
	public List<AnuncioCantidad> getAnunciosCantidadDeCestaYAnuncio(Integer cestaId, Integer anuncioId);
	
}
 