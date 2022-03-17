package com.omegapadel.service;

import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.AnuncioCantidad;
import com.omegapadel.model.Cesta;
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
	@Inject
	private ConfiguracionService configuracionService;
	@Inject
	private CestaService cestaService;

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

	public Pedido create(Cliente cliente, DireccionPostal direccionPostal, List<AnuncioCantidad> anuncios,
			String referencia) {
		Pedido p = new Pedido();
		p.setCliente(cliente);
		p.setDireccionPostal(direccionPostal);
		p.setReferenciaPedido(referencia);

		Double precioProductos = getPrecioTotalAnuncios(anuncios);
		p.setPrecioTotalProductos(precioProductos);
		Double precioEnvio = configuracionService.getImporteEnvio(precioProductos);
		p.setPrecioEnvio(precioEnvio);

		SortedSet<EstadoPedido> estados = new TreeSet<EstadoPedido>();
		EstadoPedido ep = estadoPedidoService.createEstadoPendientePago();
		estados.add(ep);
		p.setListaEstados(estados);
		return p;
	}

	public Double getPrecioTotalAnuncios(List<AnuncioCantidad> anuncios) {

		Double total = 0.0;
		for (AnuncioCantidad a : anuncios) {
			Double precio = a.getAnuncio().getPrecio();
			Integer cantidad = a.getCantidad();
			total = total + (precio * cantidad);
		}
		return total;
	}

	public String getReferenciaPedidoUnicoGenerado() {
		String cadenaGenerada = "";
		String banco = "1234567890";
		Integer longitud = 10;
		String resultado = "";

		for (int x = 0; x < longitud; x++) {
			int indiceAleatorio = ThreadLocalRandom.current().nextInt(0, banco.length() - 1);
			char caracterAleatorio = banco.charAt(indiceAleatorio);
			cadenaGenerada += caracterAleatorio;
		}

		resultado = cadenaGenerada;

		Optional<Pedido> oPedido = getPedidoPorReferencia(resultado);
		Optional<Cesta> oCesta = cestaService.getCestaPorReferencia(resultado);

		if (oPedido.isPresent() || oCesta.isPresent()) {
			return getReferenciaPedidoUnicoGenerado();
		} else {
			return resultado;
		}
	}

	public List<Pedido> getPedidosDeClienteConId(Integer idCliente) {
		return pedidoRepository.getPedidosDeClienteConId(idCliente);
	}

	public Optional<Pedido> getPedidoPorReferencia(String referencia) {
		return pedidoRepository.getPedidoPorReferencia(referencia);
	}

	public List<Pedido> getPedidoPorUltimoEstado(String ultimoEstado) {
		return pedidoRepository.getPedidoPorUltimoEstado(ultimoEstado);
	}

}
