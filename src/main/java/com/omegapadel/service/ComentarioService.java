package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Actor;
import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Comentario;
import com.omegapadel.repository.ComentarioRepository;

@Service
@Transactional
public class ComentarioService {

	@Inject
	private ComentarioRepository comentarioRepository;

	public <S extends Comentario> S save(S entity) {
		return comentarioRepository.save(entity);
	}

	public Optional<Comentario> findById(Integer id) {
		return comentarioRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return comentarioRepository.existsById(id);
	}

	public Iterable<Comentario> findAll() {
		return comentarioRepository.findAll();
	}

	public long count() {
		return comentarioRepository.count();
	}

	public void deleteById(Integer id) {
		comentarioRepository.deleteById(id);
	}

	public void delete(Comentario entity) {
		comentarioRepository.delete(entity);
	}

	public Comentario create(String titulo, String descripcion, Integer calificacion, Actor actor) {
		Comentario c = new Comentario();
		c.setActor(actor);
		c.setCalificacion(calificacion);
		c.setDescripcion(descripcion);
		c.setTitulo(titulo);
		return c;
	}

	

	public ComentarioRepository getComentarioRepository() {
		return comentarioRepository;
	}

	public void setComentarioRepository(ComentarioRepository comentarioRepository) {
		this.comentarioRepository = comentarioRepository;
	}

	public List<Comentario> getComentariosDeAnuncio(Integer idAnuncio) {
		return comentarioRepository.getComentariosDeAnuncio(idAnuncio);
	}

	
	
	public Double getMediaCalificacionesComentariosDeAnuncio(Integer idAnuncio) {
		return comentarioRepository.getMediaCalificacionesComentariosDeAnuncio(idAnuncio);
	}

	public Integer getNotaMediaAnuncio(Anuncio anuncio) {
		
		Double cal = getMediaCalificacionesComentariosDeAnuncio(anuncio.getId());
		if(cal == null) {
			return 0;
		}
		Long l =  Math.round(cal);
		return l.intValue();
	}
	
}
