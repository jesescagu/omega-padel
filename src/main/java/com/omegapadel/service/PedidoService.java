package com.omegapadel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.EstadoPedido;
import com.omegapadel.model.Pedido;
import com.omegapadel.repository.PedidoRepository;

@Service
@Transactional
public class PedidoService {

	@Inject
	private PedidoRepository pedidoRepository;
	@Inject
	private EstadoPedidoService estadoPedidoService;

	public <S extends Pedido> S save(S entity) {
		return pedidoRepository.save(entity);
	}

	public Optional<Pedido> findById(Integer id) {
		return pedidoRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return pedidoRepository.existsById(id);
	}

	public Iterable<Pedido> findAll() {
		return pedidoRepository.findAll();
	}

	public long count() {
		return pedidoRepository.count();
	}

	public void deleteById(Integer id) {
		pedidoRepository.deleteById(id);
	}

	public void delete(Pedido entity) {
		pedidoRepository.delete(entity);
	}

	public Pedido create(Cliente cliente, DireccionPostal direccionPostal, List<Anuncio> anuncios) {
		Pedido p = new Pedido();
		p.setAnuncios(anuncios);
		p.setCliente(cliente);
		p.setDireccionPostal(direccionPostal);
		p.setReferenciaPedido(getReferenciaPedidoUnicoGenerado());

		List<EstadoPedido> estados = new ArrayList<EstadoPedido>();
		EstadoPedido ep = estadoPedidoService.createEstadoRealizado();
		estados.add(ep);
		p.setListaEstados(estados);
		return p;
	}

	public List<Pedido> getPedidosDeClienteConId(Integer idCliente) {
		return pedidoRepository.getPedidosDeClienteConId(idCliente);
	}

	public Optional<Pedido> getPedidoPorReferencia(String referencia) {
		return pedidoRepository.getPedidoPorReferencia(referencia);
	}

	public String getReferenciaPedidoUnicoGenerado() {
		String cadenaGenerada = "";
		String banco = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Integer longitud = 10;
		String resultado = "";

		for (int x = 0; x < longitud; x++) {
			int indiceAleatorio = ThreadLocalRandom.current().nextInt(0, banco.length() - 1);
			char caracterAleatorio = banco.charAt(indiceAleatorio);
			cadenaGenerada += caracterAleatorio;
		}

		resultado = cadenaGenerada;

		Optional<Pedido> oi = getPedidoPorReferencia(resultado);

		if (oi.isPresent()) {
			return getReferenciaPedidoUnicoGenerado();
		} else {
			return resultado;
		}
	}

}
