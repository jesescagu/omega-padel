package com.omegapadel.service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.AnuncioCantidad;
import com.omegapadel.model.Cesta;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.Pedido;
import com.omegapadel.repository.CestaRepository;

@Service
@Transactional
public class CestaService {

	@Inject
	private CestaRepository cestRepository;
	@Inject
	private PedidoService pedidoService;
	@Inject
	private AnuncioCantidadService anuncioCantidadService;

	public <S extends Cesta> S save(S entity) {
		return cestRepository.save(entity);
	}

	public Optional<Cesta> findById(Integer id) {
		return cestRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return cestRepository.existsById(id);
	}

	public Iterable<Cesta> findAll() {
		return cestRepository.findAll();
	}

	public long count() {
		return cestRepository.count();
	}

	public void deleteById(Integer id) {
		cestRepository.deleteById(id);
	}

	public void delete(Cesta entity) {
		cestRepository.delete(entity);
	}

	public Cesta create() {
		Cesta c = new Cesta();

		String referenciaProvisional = getReferenciaPedidoUnicoGenerado();
		c.setReferenciaProvisional(referenciaProvisional);

		return c;
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

		Optional<Pedido> oPedido = pedidoService.getPedidoPorReferencia(resultado);
		Optional<Cesta> oCesta = getCestaPorReferencia(resultado);

		if (oPedido.isPresent() || oCesta.isPresent()) {
			return getReferenciaPedidoUnicoGenerado();
		} else {
			return resultado;
		}
	}

	public void addAnuncioAlCarrito(Anuncio anuncio, Cliente clienteLogado) {

		if (clienteLogado != null) {
			Cesta cestaCliente = clienteLogado.getCesta();

			Optional<AnuncioCantidad> anuncioTCOpt = null;
			anuncioTCOpt = anuncioCantidadService.getAnunciosCantidadDeCestaYAnuncio(cestaCliente.getId(),
					anuncio.getId());

			if (anuncioTCOpt.isPresent()) {
				AnuncioCantidad anuncioTC = anuncioTCOpt.get();
				Integer cantidad = anuncioTC.getCantidad() + 1;
				anuncioTC.setCantidad(cantidad);
				anuncioCantidadService.save(anuncioTC);
			} else {
				AnuncioCantidad anuncioTC = anuncioCantidadService.create(anuncio, cestaCliente);
				anuncioCantidadService.save(anuncioTC);
			}
		}
	}

	public Optional<Cesta> getCestaPorReferencia(String referencia) {
		return cestRepository.getCestaPorReferencia(referencia);
	}

}
