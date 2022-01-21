package com.omegapadel.service;

import java.util.Collection;
import java.util.Optional;

import javax.transaction.Transactional;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Usuario;
import com.omegapadel.repository.UsuarioRepository;

@Service
@Transactional
public class UsuarioService {

	@Inject
	private UsuarioRepository usuarioRepository;

	public <S extends Usuario> S save(S entity) {
		return usuarioRepository.save(entity);
	}

	public Optional<Usuario> findById(Integer id) {
		return usuarioRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return usuarioRepository.existsById(id);
	}

	public Collection<Usuario> findAll() {
		return (Collection<Usuario>) usuarioRepository.findAll();
	}

	public long count() {
		return usuarioRepository.count();
	}

	public void deleteById(Integer id) {
		usuarioRepository.deleteById(id);
	}

	public void delete(Usuario entity) {
		usuarioRepository.delete(entity);
	}
	
	public Usuario create(String usuario, String contrasenya) {
		Usuario u = new Usuario();
		u.setActivo(true);
		u.setContrasenya(contrasenya);
		u.setUsuario(usuario);
		return u;
	}

	public Boolean existeNombreUsuario(String nombreUsuario) {
		Integer i = usuarioRepository.cuentaNombreUsuario(nombreUsuario);
		return i != 0;
	}
	
	
}
