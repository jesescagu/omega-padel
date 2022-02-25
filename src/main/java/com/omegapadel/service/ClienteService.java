package com.omegapadel.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cesta;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.Usuario;
import com.omegapadel.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {

	@Inject
	private ClienteRepository clienteRepository;

	public <S extends Cliente> S save(S entity) {
		return clienteRepository.save(entity);
	}

	public Cliente buscaClientePorNombreUsuario(String nombreUsuario) {
		return clienteRepository.buscaClientePorNombreUsuario(nombreUsuario);
	}

	public Optional<Cliente> findById(Integer id) {
		return clienteRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return clienteRepository.existsById(id);
	}

	public Collection<Cliente> findAll() {
		return (Collection<Cliente>) clienteRepository.findAll();
	}

	public long count() {
		return clienteRepository.count();
	}

	public void deleteById(Integer id) {
		clienteRepository.deleteById(id);
	}

	public void delete(Cliente entity) {
		clienteRepository.delete(entity);
	}

	public Cliente create(String nombre, String apellidos, String email, List<DireccionPostal> direccionesPostales,
			Usuario usuario, List<Anuncio> anunciosFavoritos) {
		Cliente c = new Cliente();
		c.setAnunciosFavoritos(anunciosFavoritos);
		c.setApellidos(apellidos);
		c.setDireccionesPostales(direccionesPostales);
		c.setEmail(email);
		c.setNombre(nombre);
		c.setUsuario(usuario);
		return c;
	}

}
