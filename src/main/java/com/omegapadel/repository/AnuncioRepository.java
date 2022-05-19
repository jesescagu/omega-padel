package com.omegapadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Anuncio;

public interface AnuncioRepository extends CrudRepository<Anuncio, Integer> {

	// TODO Afinar los resultados de la pagina de inicio.
	@Query("select a from Anuncio a where a.activo = 1 order by a.fechaActualizacion desc")
	public List<Anuncio> getAnunciosRelevantes();

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and p.marca.nombre = ?1 and type(p) = Pala and a.activo = 1")
	public List<Anuncio> getAnunciosPorMarcaPala(String marca);

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and p.marca.nombre = ?1 and type(p) = Paletero and a.activo = 1")
	public List<Anuncio> getAnunciosPorMarcaPaletero(String marca);

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and type(p) = Ropa and a.activo = 1 and p.tipoRopa.tipoRopa = ?1")
	public List<Anuncio> getAnunciosPorTipoRopa(String tipoRopa);

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and p.marca.nombre = ?1 and type(p) = Ropa and a.activo = 1")
	public List<Anuncio> getAnunciosPorMarcaRopa(String marca);

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and p.marca.nombre = ?1 and type(p) = Zapatilla and a.activo = 1")
	public List<Anuncio> getAnunciosPorMarcaZapatilla(String marca);

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and p.marca.nombre = ?1 and type(p) = Pelota and a.activo = 1")
	public List<Anuncio> getAnunciosPorMarcaPelota(String marca);

	@Query("select distinct a from Anuncio a join a.productos p where size(a.productos) > 1 and p.marca.nombre = ?1 and a.activo = 1")
	public List<Anuncio> getAnunciosPorMarcaPack(String marca);
	
	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and type(p) = Accesorio and a.activo = 1 and p.tipo.nombre = ?1")
	public List<Anuncio> getAnunciosPorTipoAccesorio(String tipoAccesorio);

	@Query("select a from Anuncio a where a.activo = 0")
	public List<Anuncio> getAnunciosDesactivados();

	@Query("select count(p) from Anuncio a join a.productos p where p.id = ?1")
	public Integer countAnunciosDeProducto(Integer productoId);

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and type(p) = Pala and a.activo = 1")
	public List<Anuncio> getAnunciosPala();

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and type(p) = Paletero and a.activo = 1")
	public List<Anuncio> getAnunciosPaletero();

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and type(p) = Zapatilla and a.activo = 1")
	public List<Anuncio> getAnunciosZapatilla();
	
	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and type(p) = Ropa and a.activo = 1")
	public List<Anuncio> getAnunciosRopa();
	
	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and type(p) = Accesorio and a.activo = 1")
	public List<Anuncio> getAnunciosAccesorio();

	@Query("select a from Anuncio a join a.productos p where size(a.productos) = 1 and type(p) = Pelota and a.activo = 1")
	public List<Anuncio> getAnunciosPelota();

	@Query("select distinct a from Anuncio a join a.productos p where size(a.productos) > 1 and a.activo = 1")
	public List<Anuncio> getPacksAnuncios();
	
	//BUSCADOR
	
	@Query("select distinct a from Anuncio a join a.productos p where a.activo = 1 and (lower(a.titulo) like concat('%', ?1,'%') or (lower(p.marca.nombre) like concat('%', ?1,'%') or lower(p.modelo) like concat('%', ?1,'%')))")
	public List<Anuncio> getAnunciosPorTextoBuscador(String texto);
	
}
