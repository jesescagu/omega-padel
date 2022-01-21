package com.omegapadel.service;


import java.util.Optional;

import javax.inject.Inject;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.omegapadel.model.Rol;
import com.omegapadel.repository.RolRepository;

@Service
@Transactional
public class RolService {

	@Inject
	private RolRepository rolRepository;

	@Transactional
	public Rol saveRol(Rol rol) throws DataAccessException {
		return rolRepository.save(rol);
	}
	
	@Transactional
	public void saveRol(String nombreUsuario, String rol) throws DataAccessException {
		Rol authority = new Rol();
		authority.setUsuario(nombreUsuario);
		authority.setRol(rol);
		rolRepository.save(authority);
	}

	
	public Rol create(String nombreUsuario, String rol) {
		Rol r = new Rol();
		r.setUsuario(nombreUsuario);
		r.setRol(rol);
		return r;
	}

	public Optional<Rol> buscaRolPorNombreUsuario(String nombreUsuario) {
		return rolRepository.buscaRolPorNombreUsuario(nombreUsuario);
	}

	public void delete(Rol arg0) {
		rolRepository.delete(arg0);
	}

	
	
}
