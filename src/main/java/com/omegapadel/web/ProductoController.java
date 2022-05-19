package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.omegapadel.model.Accesorio;
import com.omegapadel.model.Producto;
import com.omegapadel.model.Ropa;
import com.omegapadel.model.Zapatilla;
import com.omegapadel.service.ConfiguracionService;
import com.omegapadel.service.ProductoService;

@Named("productoController")
@ViewScoped
public class ProductoController implements Serializable {

	private static final long serialVersionUID = 2439851201924000244L;

	@Inject
	private ProductoService productoService;
	@Inject
	private ConfiguracionService configuracionService;

	private List<Producto> listaProductosConStockBajo;
	private List<Producto> listaProductosDesactivados;
	private Integer limiteStockBajo;
	private Boolean estaConfiguradoElLimite;

	@PostConstruct
	public void init() {

		this.limiteStockBajo = configuracionService.findConfiguracion().getLimiteStockBajo();
		this.estaConfiguradoElLimite = this.limiteStockBajo > 0;

		if (this.estaConfiguradoElLimite) {

			listaProductosConStockBajo = productoService.getProductosConStockBajo(this.limiteStockBajo);
			listaProductosDesactivados = productoService.getProductosDesactivados();
		}

	}

	public void verListaProductosBajoStock() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaProductosBajoStock.xhtml");
	}

	public void verListaProductosDesactivados() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaProductosDesactivados.xhtml");

	}

	public void desactivarProducto(Producto prod) {

		prod.setStock(-1);
		productoService.desactivarAnunciosDelProducto(prod);
		productoService.save(prod);

	}

	public void activarProducto(Producto prod) {

		prod.setStock(0);
		productoService.save(prod);

	}

	public Boolean esProductoConTallas(Producto prod) {

		if (prod instanceof Zapatilla || prod instanceof Ropa) {
			return true;
		} else if (prod instanceof Accesorio) {
			Accesorio acc = (Accesorio) prod;
			return !acc.getTipo().getTipoTalla().equals("UNICA");
		} else {
			return false;
		}
	}

	public Map<String, Integer> getMapaTallasStockProducto(Producto prod) {

		Map<String, Integer> res = new HashMap<String, Integer>();
		if (esProductoConTallas(prod)) {
			if (prod instanceof Zapatilla) {
				Zapatilla zapa = (Zapatilla) prod;
				res = zapa.getMapaTallaStock();
			} else if (prod instanceof Ropa) {
				Ropa ropa = (Ropa) prod;
				res = ropa.getMapaTallaStock();
			} else if (prod instanceof Accesorio) {
				Accesorio acc = (Accesorio) prod;
				res = acc.getMapaTallaStock();
			}
		}
		return res;
	}

	public List<Producto> getListaProductosConStockBajo() {
		return listaProductosConStockBajo;
	}

	public void setListaProductosConStockBajo(List<Producto> listaProductosConStockBajo) {
		this.listaProductosConStockBajo = listaProductosConStockBajo;
	}

	public Integer getLimiteStockBajo() {
		return limiteStockBajo;
	}

	public void setLimiteStockBajo(Integer limiteStockBajo) {
		this.limiteStockBajo = limiteStockBajo;
	}

	public Boolean getEstaConfiguradoElLimite() {
		return estaConfiguradoElLimite;
	}

	public void setEstaConfiguradoElLimite(Boolean estaConfiguradoElLimite) {
		this.estaConfiguradoElLimite = estaConfiguradoElLimite;
	}

	public List<Producto> getListaProductosDesactivados() {
		return listaProductosDesactivados;
	}

	public void setListaProductosDesactivados(List<Producto> listaProductosDesactivados) {
		this.listaProductosDesactivados = listaProductosDesactivados;
	}

	@SuppressWarnings("unchecked")
	public boolean filtrarFechaActualizacion(Object value, Object filter, Locale locale) {

		Timestamp valor = null;
		List<LocalDate> listaFiltros = new ArrayList<LocalDate>();

		if (value instanceof Timestamp && filter instanceof ArrayList) {
			valor = (Timestamp) value;
			listaFiltros = (ArrayList<LocalDate>) filter;

			LocalDate fechaValor = valor.toLocalDateTime().toLocalDate();
			LocalDate fechaFiltro1 = listaFiltros.get(0);
			LocalDate fechaFiltro2 = listaFiltros.get(1);

			if ((fechaValor.isAfter(fechaFiltro1) && fechaValor.isBefore(fechaFiltro2))
					|| (fechaValor.isEqual(fechaFiltro1) || fechaValor.isEqual(fechaFiltro2))) {
				return true;
			}
		}
		return false;
	}

	
}
