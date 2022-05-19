package com.omegapadel.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Administrador;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.Usuario;
import com.omegapadel.repository.AdministradorRepository;

@Service
@Transactional
public class AdministratorService {

	@Inject
	private AdministradorRepository adminiRepository;

	public <S extends Administrador> S save(S entity) {
		return adminiRepository.save(entity);
	}

	public Optional<Administrador> findById(Integer id) {
		return adminiRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return adminiRepository.existsById(id);
	}

	public Iterable<Administrador> findAll() {
		return adminiRepository.findAll();
	}

	public long count() {
		return adminiRepository.count();
	}

	public void deleteById(Integer id) {
		adminiRepository.deleteById(id);
	}

	public void delete(Administrador entity) {
		adminiRepository.delete(entity);
	}

	public List<Integer> getAnyosConAlgunaVenta() {
		return adminiRepository.getAnyosConAlgunaVenta();
	}

	public List<Number[]> getPedidosPalasPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosPalasPorAnyo(anyo);
	}

	public List<Number[]> getPedidosPacksPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosPacksPorAnyo(anyo);
	}

	public List<Number[]> getPedidosPaleterosPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosPaleterosPorAnyo(anyo);
	}

	public List<Number[]> getPedidosZapatillasPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosZapatillasPorAnyo(anyo);
	}

	public List<Number[]> getPedidosRopaPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosRopaPorAnyo(anyo);
	}

	public List<Number[]> getPedidosPelotasPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosPelotasPorAnyo(anyo);
	}

	public List<Number[]> getPedidosAccesoriosPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosAccesoriosPorAnyo(anyo);
	}

	public List<Number[]> getPedidosReembolsadosPacksPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosReembolsadosPacksPorAnyo(anyo);
	}

	public List<Number[]> getPedidosReembolsadosPalasPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosReembolsadosPalasPorAnyo(anyo);
	}

	public List<Number[]> getPedidosReembolsadosPaleterosPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosReembolsadosPaleterosPorAnyo(anyo);
	}

	public List<Number[]> getPedidosReembolsadosZapatillasPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosReembolsadosZapatillasPorAnyo(anyo);
	}

	public List<Number[]> getPedidosReembolsadosRopaPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosReembolsadosRopaPorAnyo(anyo);
	}

	public List<Number[]> getPedidosReembolsadosPelotasPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosReembolsadosPelotasPorAnyo(anyo);
	}

	public List<Number[]> getPedidosReembolsadosAccesoriosPorAnyo(Integer anyo) {
		return adminiRepository.getPedidosReembolsadosAccesoriosPorAnyo(anyo);
	}

	public Administrador create(Usuario usuario, List<DireccionPostal> direccionesPostales) {
		Administrador a = new Administrador();
		a.setDireccionesPostales(direccionesPostales);
		a.setUsuario(usuario);
		return a;
	}

}
