package com.omegapadel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.AnuncioCantidad;
import com.omegapadel.model.Cesta;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.Pedido;
import com.omegapadel.model.Producto;
import com.omegapadel.model.ProductoTalla;
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
	@Inject
	private ProductoTallaService productoTallaService;

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

	public void addAnuncioAlCarrito(Anuncio anuncio, Cliente clienteLogado, Map<Producto, String> tallasPorProducto) {

		if (clienteLogado != null) {
			Cesta cestaCliente = clienteLogado.getCesta();

			List<AnuncioCantidad> anuncioTCOpt = anuncioCantidadService
					.getAnunciosCantidadDeCestaYAnuncio(cestaCliente.getId(), anuncio.getId());

			if (!CollectionUtils.isEmpty(anuncioTCOpt)) {

				AnuncioCantidad anuncioTC = null;
				List<AnuncioCantidad> listaAc = new ArrayList<AnuncioCantidad>(anuncioTCOpt);

				if (listaAc.size() > 1) {
					for (AnuncioCantidad ac : anuncioTCOpt) {
						List<ProductoTalla> lpt = productoTallaService.getProductosTallaDeAnuncioCantidad(ac.getId());
						for (ProductoTalla pt : lpt) {
							String tallaElegida = tallasPorProducto.get(pt.getProducto());
							if (!pt.getTalla().equals(tallaElegida)) {
								listaAc.remove(ac);
								break;
							}
						}
					}
				}

				if (CollectionUtils.isEmpty(listaAc)) {
					creaAnuncioCantidadNuevo(anuncio, cestaCliente, tallasPorProducto);
				}

				if (listaAc.size() == 1) {

					anuncioTC = listaAc.get(0);
					List<ProductoTalla> prodsT = productoTallaService
							.getProductosTallaDeAnuncioCantidad(anuncioTC.getId());

					if (prodsT.size() != 0) {

						if (prodsT.size() != tallasPorProducto.size()) {
							creaAnuncioCantidadNuevo(anuncio, cestaCliente, tallasPorProducto);
						} else {

							Boolean esMismaTalla = true;
							for (ProductoTalla pt : prodsT) {
								String tallaEscogida = tallasPorProducto.get(pt.getProducto());
								if (!pt.getTalla().equals(tallaEscogida)) {
									esMismaTalla = false;
									break;
								}
							}

							if (esMismaTalla) {
								aumentaCantidadAnuncioCantidad(anuncioTC);
							} else {
								creaAnuncioCantidadNuevo(anuncio, cestaCliente, tallasPorProducto);
							}

						}
					} else {
						aumentaCantidadAnuncioCantidad(anuncioTC);
					}
				}
			} else {
				creaAnuncioCantidadNuevo(anuncio, cestaCliente, tallasPorProducto);
			}
		}
	}

	private void creaAnuncioCantidadNuevo(Anuncio anuncio, Cesta cestaCliente,
			Map<Producto, String> tallasPorProducto) {

		AnuncioCantidad anuncioTCN = anuncioCantidadService.create(anuncio, cestaCliente);
		AnuncioCantidad acSaved = anuncioCantidadService.save(anuncioTCN);

		if (tallasPorProducto.size() != 0) {
			for (Producto p : tallasPorProducto.keySet()) {
				String talla = tallasPorProducto.get(p);
				ProductoTalla pt = productoTallaService.create(acSaved, p, talla);
				productoTallaService.save(pt);
			}
		}

	}

	private void aumentaCantidadAnuncioCantidad(AnuncioCantidad anuncioTC) {

		Integer cantidad = anuncioTC.getCantidad() + 1;
		anuncioTC.setCantidad(cantidad);
		anuncioCantidadService.save(anuncioTC);

	}

	public Optional<Cesta> getCestaPorReferencia(String referencia) {
		return cestRepository.getCestaPorReferencia(referencia);
	}

}
