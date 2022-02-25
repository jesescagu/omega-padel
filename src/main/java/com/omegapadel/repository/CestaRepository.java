package com.omegapadel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Cesta;
import com.omegapadel.model.Pedido;

public interface CestaRepository  extends CrudRepository<Cesta, Integer>{

	@Query("select p from Cesta p where p.referenciaProvisional = ?1")
	public Optional<Cesta> getCestaPorReferencia(String referencia);
	
}
