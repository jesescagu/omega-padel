package com.omegapadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.omegapadel.model.Accesorio;
import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Pala;
import com.omegapadel.model.Paletero;
import com.omegapadel.model.Pelota;
import com.omegapadel.model.Producto;
import com.omegapadel.model.Ropa;
import com.omegapadel.model.Zapatilla;

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

	// ------------

	@Query("select p from Paletero p")
	public List<Paletero> getProductosDeTipoPaletero();

	@Query("select p from Zapatilla p")
	public List<Zapatilla> getProductosDeTipoZapatilla();

	@Query("select p from Ropa p")
	public List<Ropa> getProductosDeTipoRopa();

	@Query("select p from Paletero p where p not in (:prods)")
	public List<Paletero> getProductosDeTipoPaletero(@Param("prods") List<Producto> productosEscogidos);

	@Query("select p from Zapatilla p where p not in (:prods)")
	public List<Zapatilla> getProductosDeTipoZapatilla(@Param("prods") List<Producto> productosEscogidos);

	@Query("select p from Ropa p where p not in (:prods)")
	public List<Ropa> getProductosDeTipoRopa(@Param("prods") List<Producto> productosEscogidos);

	@Query("select count(p) from Producto p where p.referencia = ?1")
	public Long existeReferencia(String referencia);
	
	@Query("select p from Producto p where p.stock < ?1 and p.stock >= 0")
	public List<Producto> getProductosConStockBajo(Integer limite);
	
	@Query("select p from Producto p where p.stock < 0")
	public List<Producto> getProductosDesactivados();
	
	@Query("select a from Anuncio a join a.productos p where p.id = ?1")
	public List<Anuncio> getAnunciosDeProducto(Integer productoId);
}
