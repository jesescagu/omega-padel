package com.omegapadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Comentario;

public interface ComentarioRepository  extends CrudRepository<Comentario, Integer>{

	@Query("select avg(c.calificacion + 0.0) from Comentario c where c.anuncio.id = ?1")
	public Double getMediaCalificacionesComentariosDeAnuncio(Integer idAnuncio);
	
	@Query("select c from Comentario c where c.anuncio.id = ?1")
	public List<Comentario> getComentariosDeAnuncio(Integer idAnuncio);
	
}
