package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cesta;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.Configuracion;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.CestaService;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.ConfiguracionService;
import com.omegapadel.service.DireccionPostalService;

@Named("cestaController")
@ViewScoped
public class CestaController implements Serializable {

	private static final long serialVersionUID = 982685617877081241L;

	@Inject
	private CestaService cestaService;
	@Inject
	private ClienteService clienteService;
	@Inject
	private AnuncioService anuncioService;
	@Inject
	private DireccionPostalService direccionPostalService;
	@Inject
	private ConfiguracionService configuracionService;

	private Cliente clienteLogado;

	public List<Anuncio> listaProductosCesta;
	private List<DireccionPostal> listaDireccionesUsuario;
	private DireccionPostal direccionPostalSeleccionada;

	private Integer anuncioIdParaEditar;
	private Integer valorCantidadEnEdicion;

	@PostConstruct
	public void init() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		this.anuncioIdParaEditar = null;
		this.valorCantidadEnEdicion = null;

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
			User user = (User) auth.getPrincipal();
			String nombreUsuario = user.getUsername();
			this.clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);

			FacesContext context = FacesContext.getCurrentInstance();
			Object anuncioParaeditarObject = context.getExternalContext().getSessionMap().get("anuncioIdParaEditar");
			Object valorCantidadEnEdicionObject = context.getExternalContext().getSessionMap()
					.get("valorCantidadEnEdicion");

			if (anuncioParaeditarObject != null && valorCantidadEnEdicionObject != null) {
				this.anuncioIdParaEditar = (Integer) anuncioParaeditarObject;
				this.valorCantidadEnEdicion = (Integer) valorCantidadEnEdicionObject;

				context.getExternalContext().getSessionMap().remove("anuncioIdParaEditar");
				context.getExternalContext().getSessionMap().remove("valorCantidadEnEdicion");
			}

			this.listaProductosCesta = new ArrayList<Anuncio>();
			Cesta c = this.clienteLogado.getCesta();
			if (c != null) {
				Set<Integer> setCesta = c.getMapaAnunciosCantidad().keySet();
				setCesta.stream().forEach(ci -> this.listaProductosCesta.add(anuncioService.findById(ci).get()));
			}

			this.listaDireccionesUsuario = this.clienteLogado.getDireccionesPostales();

			if (!CollectionUtils.isEmpty(this.listaDireccionesUsuario)) {

				if (this.listaDireccionesUsuario.size() == 1) {

					this.direccionPostalSeleccionada = this.listaDireccionesUsuario.get(0);
					
				} else {
					
					Object direccionPostalSeleccionadaObject = context.getExternalContext().getSessionMap()
							.get("direccionPostalSeleccionada");

					if (direccionPostalSeleccionadaObject != null) {
						Integer idDp = (Integer) direccionPostalSeleccionadaObject;
						this.direccionPostalSeleccionada = direccionPostalService.findById(idDp).get();
					}
				}
			}
		}

	}

	public Integer getCantidadProductoEnCesta(Anuncio anuncio) {

		Map<Integer, Integer> maps = this.clienteLogado.getCesta().getMapaAnunciosCantidad();
		Integer i = maps.get(anuncio.getId());
		return i;
	}

	public void cambiarCantidadManualmenteProductoDelCarrito(Anuncio anuncio) throws IOException {

		Cesta cesta = this.clienteLogado.getCesta();
		Map<Integer, Integer> maps = cesta.getMapaAnunciosCantidad();

		this.anuncioIdParaEditar = anuncio.getId();
		this.valorCantidadEnEdicion = maps.get(this.anuncioIdParaEditar);

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("anuncioIdParaEditar", this.anuncioIdParaEditar);
		context.getExternalContext().getSessionMap().put("valorCantidadEnEdicion", this.valorCantidadEnEdicion);

		FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");

	}

	public void cambioCantidadProducto(Anuncio anuncio) throws IOException {
		Cesta cesta = this.clienteLogado.getCesta();

		Map<Integer, Integer> maps = cesta.getMapaAnunciosCantidad();
		Integer i = this.valorCantidadEnEdicion;
		maps.put(anuncio.getId(), i);

		cesta.setMapaAnunciosCantidad(maps);
		cestaService.save(cesta);
		FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");
	}

	public void quitarUnProductoDelCarrito(Anuncio anuncio) throws IOException {
		Cesta cesta = this.clienteLogado.getCesta();

		Map<Integer, Integer> maps = cesta.getMapaAnunciosCantidad();
		Integer i = maps.get(anuncio.getId());

		maps.put(anuncio.getId(), i - 1);

		cesta.setMapaAnunciosCantidad(maps);
		cestaService.save(cesta);
		FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");
	}

	public void addUnProductoDelCarrito(Anuncio anuncio) throws IOException {
		Cesta cesta = this.clienteLogado.getCesta();

		Map<Integer, Integer> maps = cesta.getMapaAnunciosCantidad();
		Integer i = maps.get(anuncio.getId());

		maps.put(anuncio.getId(), i + 1);

		cesta.setMapaAnunciosCantidad(maps);
		cestaService.save(cesta);
		FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");
	}

	public Double totalPrecioProducto(Anuncio anuncio) {

		Double total = 0.0;

		Cesta c = this.clienteLogado.getCesta();

		if (c != null) {
			Map<Integer, Integer> maps = c.getMapaAnunciosCantidad();

			Integer cant = maps.get(anuncio.getId());
			total = cant * anuncio.getPrecio();

		}

		return total;
	}

	public void eliminarProductoDelCarrito(Anuncio anuncio) throws IOException {

		Cesta c = this.clienteLogado.getCesta();
		Map<Integer, Integer> maps = c.getMapaAnunciosCantidad();

		maps.remove(anuncio.getId());
		c.setMapaAnunciosCantidad(maps);

		cestaService.save(c);
		FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");
	}

	public Double getTotalProductos() {

		Double total = 0.0;
		Cesta c = this.clienteLogado.getCesta();

		if (c != null) {
			Map<Integer, Integer> maps = c.getMapaAnunciosCantidad();

			Set<Integer> setCesta = c.getMapaAnunciosCantidad().keySet();
			for (Integer anuncioId : setCesta) {
				Double precio = anuncioService.findById(anuncioId).get().getPrecio();
				Integer cantidad = maps.get(anuncioId);
				total = total + (precio * cantidad);
			}
		}

		return total;

	}

	public Double getImporteEnvio() {
		
		Configuracion config = configuracionService.findConfiguracion();
		
		if(config.getHayEnvioGratis() && this.getTotalProductos() >= config.getPrecioaPartirEnvioGratis()) {
			return 0.0;
		}else {
			return config.getPrecioEnvio();
		}
		
	}

	public Double getTotalPrecio() {
		return getTotalProductos() + getImporteEnvio();
	}

	public void seleccionarDireccion(DireccionPostal dp) throws IOException {

		this.direccionPostalSeleccionada = dp;

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("direccionPostalSeleccionada",
				this.direccionPostalSeleccionada.getId());

		FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");

	}

	public void cancelarCarrito() {

		Cesta cesta = this.clienteLogado.getCesta();

		this.clienteLogado.setCesta(null);
		clienteService.save(this.clienteLogado);

		cestaService.delete(cesta);

	}

	public void pagarCarrito() {

		// TODO

	}

	public void addAnuncioAlCarrito(Anuncio anuncio) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
			cestaService.addAnuncioAlCarrito(anuncio, this.clienteLogado);
		}
	}

	public Integer numeroProductosCarritoCliente() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
			User user = (User) auth.getPrincipal();
			String nombreUsuario = user.getUsername();
			this.clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);
			Cesta cesta = this.clienteLogado.getCesta();
			if (cesta == null) {
				return 0;
			} else {
				Collection<Integer> intList = cesta.getMapaAnunciosCantidad().values();
				Integer suma = 0;
				for (Integer i : intList) {
					suma = suma + i;
				}
				return suma;
			}
		}
		return 0;
	}

	public boolean renderModificarCantidadManualmente(Anuncio anuncio) {
		return anuncio.getId() == this.anuncioIdParaEditar;
	}
	
	public boolean renderDireccionSeleccionada(DireccionPostal dp) {
		return dp == this.direccionPostalSeleccionada;
	}

	public void verCarritoCompraCliente() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
			User user = (User) auth.getPrincipal();
			String nombreUsuario = user.getUsername();
			this.clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);

			context.getExternalContext().getSessionMap().put("cestaClienteLogado", this.clienteLogado.getCesta());
			FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");

		} else {
			FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml?faces-redirect=true");
		}
	}

	public Cliente getClienteLogado() {
		return clienteLogado;
	}

	public void setClienteLogado(Cliente clienteLogado) {
		this.clienteLogado = clienteLogado;
	}

	public List<Anuncio> getListaProductosCesta() {
		return listaProductosCesta;
	}

	public void setListaProductosCesta(List<Anuncio> listaProductosCesta) {
		this.listaProductosCesta = listaProductosCesta;
	}

	public List<DireccionPostal> getListaDireccionesUsuario() {
		return listaDireccionesUsuario;
	}

	public void setListaDireccionesUsuario(List<DireccionPostal> listaDireccionesUsuario) {
		this.listaDireccionesUsuario = listaDireccionesUsuario;
	}

	public DireccionPostal getDireccionPostalSeleccionada() {
		return direccionPostalSeleccionada;
	}

	public void setDireccionPostalSeleccionada(DireccionPostal direccionPostalSeleccionada) {
		this.direccionPostalSeleccionada = direccionPostalSeleccionada;
	}

	public Integer getAnuncioIdParaEditar() {
		return anuncioIdParaEditar;
	}

	public void setAnuncioIdParaEditar(Integer anuncioIdParaEditar) {
		this.anuncioIdParaEditar = anuncioIdParaEditar;
	}

	public Integer getValorCantidadEnEdicion() {
		return valorCantidadEnEdicion;
	}

	public void setValorCantidadEnEdicion(Integer valorCantidadEnEdicion) {
		this.valorCantidadEnEdicion = valorCantidadEnEdicion;
	}

}
