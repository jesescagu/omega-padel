package com.omegapadel.service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Anuncio;
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
	private ClienteService clienteService;
	@Inject
	private PedidoService pedidoService;

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

	public Cesta create(Map<Integer, Integer> anuncios) {
		Cesta c = new Cesta();

		String referenciaProvisional = getReferenciaPedidoUnicoGenerado();
		c.setReferenciaProvisional(referenciaProvisional);

		c.setMapaAnunciosCantidad(anuncios);

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
//			if (cestaCliente == null) {
//				cestaCliente = create();
//
//				Map<Integer, Integer> mapaAnuncios = new HashMap<Integer, Integer>();
//				mapaAnuncios.put(anuncio.getId(), 1);
//
//				cestaCliente.setMapaAnunciosCantidad(mapaAnuncios);
//				cestaCliente.setCosto(anuncio.getPrecio());
//				
//			} else {
			Map<Integer, Integer> mapaAnuncios = cestaCliente.getMapaAnunciosCantidad();
			if (mapaAnuncios.containsKey(anuncio.getId())) {
				mapaAnuncios.put(anuncio.getId(), mapaAnuncios.get(anuncio.getId()) + 1);
			} else {
				mapaAnuncios.put(anuncio.getId(), 1);
			}
			cestaCliente.setMapaAnunciosCantidad(mapaAnuncios);
//			}
			Cesta cestaSaved = save(cestaCliente);
			clienteLogado.setCesta(cestaSaved);
			clienteService.save(clienteLogado);
		}
	}

	public Optional<Cesta> getCestaPorReferencia(String referencia) {
		return cestRepository.getCestaPorReferencia(referencia);
	}

}
