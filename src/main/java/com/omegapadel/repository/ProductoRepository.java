package com.omegapadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.omegapadel.model.Accesorio;
import com.omegapadel.model.Pala;
import com.omegapadel.model.Pelota;
import com.omegapadel.model.Producto;

public interface ProductoRepository extends CrudRepository<Producto, Integer> {

	@Query("select p from Pala p where p not in (:prods)")
	public List<Pala> getProductosDeTipoPala(@Param("prods") List<Producto> productosEscogidos);

	@Query("select p from Pelota p where p not in (:prods)")
	public List<Pelota> getProductosDeTipoPelota(@Param("prods") List<Producto> productosEscogidos);

	@Query("select p from Accesorio p where p not in (:prods)")
	public List<Accesorio> getProductosDeTipoAccesorio(@Param("prods") List<Producto> productosEscogidos);
	
	@Query("select p from Pala p")
	public List<Pala> getProductosDeTipoPala();

	@Query("select p from Pelota p")
	public List<Pelota> getProductosDeTipoPelota();

	@Query("select p from Accesorio p")
	public List<Accesorio> getProductosDeTipoAccesorio();

}
