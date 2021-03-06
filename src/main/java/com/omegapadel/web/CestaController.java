package com.omegapadel.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.omegapadel.model.AnuncioCantidad;
import com.omegapadel.model.Cesta;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.Configuracion;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.Pedido;
import com.omegapadel.model.ProductoTalla;
import com.omegapadel.service.AnuncioCantidadService;
import com.omegapadel.service.CestaService;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.ConfiguracionService;
import com.omegapadel.service.DireccionPostalService;
import com.omegapadel.service.PedidoService;
import com.omegapadel.service.ProductoService;
import com.omegapadel.service.ProductoTallaService;

import sis.redsys.api.ApiMacSha256;

@Named("cestaController")
@ViewScoped
public class CestaController implements Serializable {

	Logger logger = Logger.getLogger("CestaController");
	private static final long serialVersionUID = 982685617877081241L;

	@Inject
	private CestaService cestaService;
	@Inject
	private ClienteService clienteService;
	@Inject
	private DireccionPostalService direccionPostalService;
	@Inject
	private ConfiguracionService configuracionService;
	@Inject
	private PedidoService pedidoService;
	@Inject
	private AnuncioCantidadService anuncioCantidadService;
	@Inject
	private ProductoTallaService productoTallaService;
	@Inject
	private ProductoService productoService;

	private Cliente clienteLogado;

	public List<AnuncioCantidad> listaProductosCesta;
	private List<DireccionPostal> listaDireccionesUsuario;
	private DireccionPostal direccionPostalSeleccionada;

	private Integer anuncioIdParaEditar;
	private Integer valorCantidadEnEdicion;

	private String ds_SignatureVersion;
	private String ds_MerchantParameters;
	private String ds_Signature;

	private String valorAux;

	@PostConstruct
	public void init() throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			FileNotFoundException, IOException {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		this.anuncioIdParaEditar = null;
		this.valorCantidadEnEdicion = null;

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
//			User user = (User) auth.getPrincipal();
//			String nombreUsuario = user.getUsername();

			String nombreUsuario = null;
			Object princ = auth.getPrincipal();
			if (princ instanceof User) {
				User user = (User) princ;
				nombreUsuario = user.getUsername();
			} else {
				nombreUsuario = (String) auth.getPrincipal();
			}

			this.clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);

			FacesContext context = FacesContext.getCurrentInstance();
			Object anuncioParaeditarObject = context.getExternalContext().getSessionMap().get("anuncioIdParaEditar");
			Object valorCantidadEnEdicionObject = context.getExternalContext().getSessionMap()
					.get("valorCantidadEnEdicion");

			if (anuncioParaeditarObject != null && valorCantidadEnEdicionObject != null) {
				this.anuncioIdParaEditar = (Integer) anuncioParaeditarObject;
				this.valorCantidadEnEdicion = (Integer) valorCantidadEnEdicionObject;
			}

			Cesta c = this.clienteLogado.getCesta();
			if (c != null) {
				this.listaProductosCesta = anuncioCantidadService.getAnunciosCantidadDeCesta(c.getId());
			} else {
				Cesta cesta = cestaService.create();
				this.clienteLogado.setCesta(cestaService.save(cesta));
				clienteService.save(this.clienteLogado);
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

	public void cambiarCantidadManualmenteProductoDelCarrito(AnuncioCantidad anuncio) throws IOException {

		this.anuncioIdParaEditar = anuncio.getId();
		this.valorCantidadEnEdicion = anuncio.getCantidad();

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("anuncioIdParaEditar", this.anuncioIdParaEditar);
		context.getExternalContext().getSessionMap().put("valorCantidadEnEdicion", this.valorCantidadEnEdicion);

	}

	public void cambioCantidadProducto(AnuncioCantidad anuncio) throws IOException {

		Integer cantidad = this.valorCantidadEnEdicion;
		Boolean hayStock = productoService.hayStockDeProductos(anuncio, cantidad);

		if (hayStock) {
			anuncio.setCantidad(cantidad);
			anuncioCantidadService.save(anuncio);

			FacesContext context = FacesContext.getCurrentInstance();
			context.getExternalContext().getSessionMap().remove("anuncioIdParaEditar");
			context.getExternalContext().getSessionMap().remove("valorCantidadEnEdicion");

			FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"No existe esa cantidad de stock del producto, disculpe las molestias.", ""));
		}
	}

	public void quitarUnProductoDelCarrito(AnuncioCantidad anuncio) throws IOException {

		Integer cantidad = anuncio.getCantidad() - 1;
		anuncio.setCantidad(cantidad);
		anuncioCantidadService.save(anuncio);

		FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");
	}

	public void addUnProductoDelCarrito(AnuncioCantidad anuncio) throws IOException {

		Integer cantidad = anuncio.getCantidad() + 1;
		Boolean hayStock = productoService.hayStockDeProductos(anuncio, cantidad);

		if (hayStock) {

			anuncio.setCantidad(cantidad);
			anuncioCantidadService.save(anuncio);

			FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");

		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"No existe esa cantidad de stock del producto, disculpe las molestias.", ""));

		}
	}

	public Double totalPrecioProducto(AnuncioCantidad anuncio) {

		Double total = 0.0;
		Integer cant = anuncio.getCantidad();
		total = cant * anuncio.getAnuncio().getPrecio();

		return total;

	}

	public void eliminarProductoDelCarrito(AnuncioCantidad anuncio) throws IOException {

		List<ProductoTalla> pts = productoTallaService.getProductosTallaDeAnuncioCantidad(anuncio.getId());
		productoTallaService.deleteAll(pts);
		anuncioCantidadService.deleteById(anuncio.getId());

		FacesContext.getCurrentInstance().getExternalContext().redirect("cestaCliente.xhtml");
	}

	public List<ProductoTalla> getProductosTallaDeAnuncio(AnuncioCantidad ac) {
		List<ProductoTalla> lpt = productoTallaService.getProductosTallaDeAnuncioCantidad(ac.getId());
		return lpt;
	}

	public Double getTotalProductos() {

		Double total = 0.0;
		Cesta c = this.clienteLogado.getCesta();

		if (c != null) {

			List<AnuncioCantidad> listaAnuncios = anuncioCantidadService.getAnunciosCantidadDeCesta(c.getId());

			for (AnuncioCantidad a : listaAnuncios) {

				Integer cantidad = a.getCantidad();
				Double precio = a.getAnuncio().getPrecio();
				total = total + (precio * cantidad);
			}
		}
		return total;

	}

	public Double getImporteEnvio() {

		Configuracion config = configuracionService.findConfiguracion();

		if (config.getHayEnvioGratis() && this.getTotalProductos() >= config.getPrecioaPartirEnvioGratis()) {
			return 0.0;
		} else {
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

	public void cancelarCarrito() throws IOException {

		Cesta cesta = this.clienteLogado.getCesta();

		this.clienteLogado.setCesta(null);
		clienteService.save(this.clienteLogado);

		cestaService.delete(cesta);

		FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
	}

	public void inicializaVariablesRedsys() throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException,
			FileNotFoundException, IOException {

		Properties propiedades = new Properties();
		propiedades.load(this.getClass().getResourceAsStream("/redsys.properties"));

		String dsMerchantcode = propiedades.getProperty("DS_MERCHANT_MERCHANTCODE");
		String dsMerchantcurrency = propiedades.getProperty("DS_MERCHANT_CURRENCY");
		String dsMerchantTerminal = propiedades.getProperty("DS_MERCHANT_TERMINAL");
		String dsMerchantUrl = propiedades.getProperty("DS_MERCHANT_MERCHANTURL");
		String dsMerchantUrlOk = propiedades.getProperty("DS_MERCHANT_URLOK");
		String dsMerchantUrlKo = propiedades.getProperty("DS_MERCHANT_URLKO");
		ApiMacSha256 apiMacSha256 = new ApiMacSha256();

		Integer precio = (int) (getTotalPrecio() * 100);
		String amount = precio.toString();

		String numeroPedido = this.clienteLogado.getCesta().getReferenciaProvisional();
		logger.info("numero Pedido: " + numeroPedido);

		apiMacSha256.setParameter("DS_MERCHANT_AMOUNT", amount);
		apiMacSha256.setParameter("DS_MERCHANT_ORDER", numeroPedido);
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTCODE", dsMerchantcode);
		apiMacSha256.setParameter("DS_MERCHANT_CURRENCY", dsMerchantcurrency);
		apiMacSha256.setParameter("DS_MERCHANT_TRANSACTIONTYPE", "0"); // Autorizacion
		apiMacSha256.setParameter("DS_MERCHANT_TERMINAL", dsMerchantTerminal);
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTURL", dsMerchantUrl);
		apiMacSha256.setParameter("DS_MERCHANT_URLOK", dsMerchantUrlOk);
		apiMacSha256.setParameter("DS_MERCHANT_URLKO", dsMerchantUrlKo);

		String params = apiMacSha256.createMerchantParameters();

		String claveSHA256 = propiedades.getProperty("claveSHA256");
		String firma = apiMacSha256.createMerchantSignature(claveSHA256);

		this.ds_SignatureVersion = propiedades.getProperty("ds_SignatureVersion");
		this.ds_MerchantParameters = params;
		this.ds_Signature = firma;

		boolean res = renderHayStock();
		if (res == true) {
			this.valorAux = "true";
		} else {
			this.valorAux = "false";
		}
	}

	public Boolean renderHayStock() {

		for (AnuncioCantidad ac : this.listaProductosCesta) {
			Boolean hayStock = productoService.hayStockDeProductos(ac, ac.getCantidad());
			if (!hayStock) {
				return false;
			}
		}
		return true;
	}

	public Boolean pagarCarrito() {

		if (this.clienteLogado != null) {

			Cesta cesta = this.clienteLogado.getCesta();
			this.clienteLogado.setCesta(null);
			clienteService.save(this.clienteLogado);

			if (cesta != null) {

				List<AnuncioCantidad> listaAnuncios = anuncioCantidadService.getAnunciosCantidadDeCesta(cesta.getId());
				Pedido pedido = pedidoService.create(this.clienteLogado, this.direccionPostalSeleccionada,
						listaAnuncios, cesta.getReferenciaProvisional());
				Pedido pedidoSaved = pedidoService.save(pedido);

				for (AnuncioCantidad ac : listaAnuncios) {
					ac.setCesta(null);
					ac.setPedido(pedidoSaved);
					anuncioCantidadService.save(ac);
				}

				cestaService.delete(cesta);

				return true;
			}
		}
		return false;
	}

	public Boolean desactivarBotonCesta() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		return (auth == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente")));

	}

	public Integer numeroProductosCarritoCliente() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
//			User user = (User) auth.getPrincipal();
//			String nombreUsuario = user.getUsername();

			String nombreUsuario = null;
			Object princ = auth.getPrincipal();
			if (princ instanceof User) {
				User user = (User) princ;
				nombreUsuario = user.getUsername();
			} else {
				nombreUsuario = (String) auth.getPrincipal();
			}

			this.clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);
			Cesta cesta = this.clienteLogado.getCesta();
			if (cesta == null) {
				return 0;
			} else {
				List<AnuncioCantidad> intList = anuncioCantidadService.getAnunciosCantidadDeCesta(cesta.getId());
				Integer suma = 0;
				for (AnuncioCantidad a : intList) {
					Integer i = a.getCantidad();
					suma = suma + i;
				}
				return suma;
			}
		}
		return 0;
	}

	public boolean renderModificarCantidadManualmente(AnuncioCantidad anuncio) {
		if (this.anuncioIdParaEditar == null) {
			return false;
		}
		return anuncio.getId() == this.anuncioIdParaEditar;
	}

	public boolean renderDireccionSeleccionada(DireccionPostal dp) {
		return dp == this.direccionPostalSeleccionada;
	}

	public void verCarritoCompraCliente() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
//			User user = (User) auth.getPrincipal();
//			String nombreUsuario = user.getUsername();

			String nombreUsuario = null;
			Object princ = auth.getPrincipal();
			if (princ instanceof User) {
				User user = (User) princ;
				nombreUsuario = user.getUsername();
			} else {
				nombreUsuario = (String) auth.getPrincipal();
			}

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

	public List<AnuncioCantidad> getListaProductosCesta() {
		return listaProductosCesta;
	}

	public void setListaProductosCesta(List<AnuncioCantidad> listaProductosCesta) {
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

	public String getDs_SignatureVersion() {
		return ds_SignatureVersion;
	}

	public void setDs_SignatureVersion(String ds_SignatureVersion) {
		this.ds_SignatureVersion = ds_SignatureVersion;
	}

	public String getDs_MerchantParameters() {
		return ds_MerchantParameters;
	}

	public void setDs_MerchantParameters(String ds_MerchantParameters) {
		this.ds_MerchantParameters = ds_MerchantParameters;
	}

	public String getDs_Signature() {
		return ds_Signature;
	}

	public void setDs_Signature(String ds_Signature) {
		this.ds_Signature = ds_Signature;
	}

	public String getValorAux() {
		return valorAux;
	}

	public void setValorAux(String valorAux) {
		this.valorAux = valorAux;
	}

}
