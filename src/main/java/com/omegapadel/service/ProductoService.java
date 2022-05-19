package com.omegapadel.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.omegapadel.model.Accesorio;
import com.omegapadel.model.Anuncio;
import com.omegapadel.model.AnuncioCantidad;
import com.omegapadel.model.Pala;
import com.omegapadel.model.Paletero;
import com.omegapadel.model.Pelota;
import com.omegapadel.model.Producto;
import com.omegapadel.model.ProductoTalla;
import com.omegapadel.model.Ropa;
import com.omegapadel.model.Zapatilla;
import com.omegapadel.repository.ProductoRepository;

@Service
@Transactional
public class ProductoService {

	@Inject
	private ProductoRepository productoRepository;
	@Inject
	private ZapatillaService zapatillaService;
	@Inject
	private RopaService ropaService;
	@Inject
	private AccesorioService accesorioService;
	@Inject
	private ProductoTallaService productoTallaService;
	@Inject
	private AnuncioService anuncioService;

	public <S extends Producto> S save(S entity) {
		return productoRepository.save(entity);
	}

	public Optional<Producto> findById(Integer id) {
		return productoRepository.findById(id);
	}

	public boolean existsById(Integer id) {
		return productoRepository.existsById(id);
	}

	public Iterable<Producto> findAll() {
		return productoRepository.findAll();
	}

	public long count() {
		return productoRepository.count();
	}

	public void deleteById(Integer id) {
		productoRepository.deleteById(id);
	}

	public void delete(Producto entity) {
		productoRepository.delete(entity);
	}

	public void deleteAll() {
		productoRepository.deleteAll();
	}

	public List<Producto> tratamientoListaProductos(String tipoProductoEscogido, List<Producto> productosEscogidos) {

		List<Producto> prods = new ArrayList<Producto>();
		for (Producto p : productosEscogidos) {
			if (tipoProductoEscogido.equals("Pala") && p instanceof Pala) {
				prods.add(p);
			}
			if (tipoProductoEscogido.equals("Pelota") && p instanceof Pelota) {
				prods.add(p);
			}
			if (tipoProductoEscogido.equals("Accesorio") && p instanceof Accesorio) {
				prods.add(p);
			}

			// ------

			if (tipoProductoEscogido.equals("Paletero") && p instanceof Paletero) {
				prods.add(p);
			}
			if (tipoProductoEscogido.equals("Zapatilla") && p instanceof Zapatilla) {
				prods.add(p);
			}
			if (tipoProductoEscogido.equals("Ropa") && p instanceof Ropa) {
				prods.add(p);
			}

		}
		return prods;
	}

	public List<? extends Producto> getProductosDeUnTipo(String tipoProductoEscogido,
			List<Producto> productosEscogidos) {

		List<Producto> prods = tratamientoListaProductos(tipoProductoEscogido, productosEscogidos);

		switch (tipoProductoEscogido) {
		case "Pala":
			if (CollectionUtils.isEmpty(prods)) {
				return productoRepository.getProductosDeTipoPala();
			} else {
				return productoRepository.getProductosDeTipoPala(prods);
			}
		case "Pelota":
			if (CollectionUtils.isEmpty(prods)) {
				return productoRepository.getProductosDeTipoPelota();
			} else {
				return productoRepository.getProductosDeTipoPelota(prods);
			}
		case "Accesorio":
			if (CollectionUtils.isEmpty(prods)) {
				return productoRepository.getProductosDeTipoAccesorio();
			} else {
				return productoRepository.getProductosDeTipoAccesorio(prods);
			}

			// ----------
		case "Paletero":
			if (CollectionUtils.isEmpty(prods)) {
				return productoRepository.getProductosDeTipoPaletero();
			} else {
				return productoRepository.getProductosDeTipoPaletero(prods);
			}
		case "Zapatilla":
			if (CollectionUtils.isEmpty(prods)) {
				return productoRepository.getProductosDeTipoZapatilla();
			} else {
				return productoRepository.getProductosDeTipoZapatilla(prods);
			}
		case "Ropa":
			if (CollectionUtils.isEmpty(prods)) {
				return productoRepository.getProductosDeTipoRopa();
			} else {
				return productoRepository.getProductosDeTipoRopa(prods);
			}
		default:
			return new ArrayList<Producto>();
		}

	}

	public Boolean hayStockDeProductos(AnuncioCantidad acNueva, Integer nuevaCantidad) {

		List<Producto> lp = acNueva.getAnuncio().getProductos();
		List<ProductoTalla> lpt = productoTallaService.getProductosTallaDeAnuncioCantidad(acNueva.getId());
		Integer cantidad = nuevaCantidad;

		if (CollectionUtils.isEmpty(lpt)) {

			for (Producto p : lp) {
				Integer stock = p.getStock();
				if (stock.compareTo(cantidad) < 0) {
					return false;
				}
			}
		} else {
			for (Producto p : lp) {
				if (p instanceof Pala || p instanceof Paletero || p instanceof Pelota) {

					Integer stock = p.getStock();
					if (stock.compareTo(cantidad) < 0) {
						return false;
					}

				} else if (p instanceof Zapatilla) {

					ProductoTalla pt = productoTallaService.getProductoTallaDeAnuncioCantidadYProducto(p.getId(),
							acNueva.getId());
					String i = pt.getTalla();

					Zapatilla zapa = zapatillaService.findById(p.getId()).get();
					Map<String, Integer> map = zapa.getMapaTallaStock();
					Integer stock = map.get(i);

					if (stock.compareTo(cantidad) < 0) {
						return false;
					}

				} else if (p instanceof Ropa) {

					ProductoTalla pt = productoTallaService.getProductoTallaDeAnuncioCantidadYProducto(p.getId(),
							acNueva.getId());
					String i = pt.getTalla();

					Ropa ropa = ropaService.findById(p.getId()).get();
					Map<String, Integer> map = ropa.getMapaTallaStock();
					Integer stock = map.get(i);

					if (stock.compareTo(cantidad) < 0) {
						return false;
					}

				} else if (p instanceof Accesorio) {

					Accesorio acc = accesorioService.findById(p.getId()).get();
					if (acc.getTipo().getTipoTalla().equals("UNICA")) {

						Integer stock = acc.getStock();
						if (stock.compareTo(cantidad) < 0) {
							return false;
						}

					} else {

						ProductoTalla pt = productoTallaService.getProductoTallaDeAnuncioCantidadYProducto(p.getId(),
								acNueva.getId());
						String i = pt.getTalla();

						Map<String, Integer> map = acc.getMapaTallaStock();
						Integer stock = map.get(i);

						if (stock.compareTo(cantidad) < 0) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	public Boolean hayStockDeProductosDelAnuncio(Anuncio anuncio) {

		List<Producto> lp = anuncio.getProductos();
		for (Producto p : lp) {
			Integer stock = p.getStock();
			if (stock == 0) {
				return false;
			}
		}
		return true;
	}

	public List<Producto> getProductosConStockBajo(Integer limite) {
		return productoRepository.getProductosConStockBajo(limite);
	}

	public List<Producto> getProductosDesactivados() {
		return productoRepository.getProductosDesactivados();
	}

	public List<Anuncio> getAnunciosDeProducto(Integer productoId) {
		return productoRepository.getAnunciosDeProducto(productoId);
	}

	public void desactivarAnunciosDelProducto(Producto prod) {

		List<Anuncio> ans = getAnunciosDeProducto(prod.getId());
		if(!CollectionUtils.isEmpty(ans)) {
			for(Anuncio an : ans) {
				an.setActivo(false);
				anuncioService.save(an);
			}
		}
	}

}
