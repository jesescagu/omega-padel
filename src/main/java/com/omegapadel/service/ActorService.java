package com.omegapadel.service;

import java.util.Optional;

import javax.transaction.Transactional;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Actor;
import com.omegapadel.repository.ActorRepository;

@Service
@Transactional
public class ActorService {

	@Inject
	private ActorRepository actorRepository;

	public <S extends Actor> S save(S entity) {
		return actorRepository.save(entity);
	}

	public Optional<Actor> findById(Integer id) {
		return actorRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return actorRepository.existsById(id);
	}

	public Iterable<Actor> findAll() {
		return actorRepository.findAll();
	}

	public long count() {
		return actorRepository.count();
	}

	public void deleteById(Integer id) {
		actorRepository.deleteById(id);
	}

	public void delete(Actor entity) {
		actorRepository.delete(entity);
	}
	
	
	
}
