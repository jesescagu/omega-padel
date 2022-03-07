package com.omegapadel.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.EstadoPedido;
import com.omegapadel.model.Pedido;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.EstadoPedidoService;
import com.omegapadel.service.PedidoService;

import sis.redsys.api.ApiMacSha256;

@Named("pedidoController")
@ViewScoped
public class PedidoController implements Serializable {

	private static final long serialVersionUID = -4101551350656645590L;

	@Inject
	private ClienteService clienteService;
	@Inject
	private PedidoService pedidoService;
	@Inject
	private EstadoPedidoService estadoPedidoService;
	@Inject
	private AnuncioService anuncioService;

	private Cliente clienteLogado;
	private List<Pedido> listaPedidos;

	private List<Pedido> listaPedidosParaTramitar;
	private String estadoSeleccionado;

	private Pedido pedidoParaMostrar;

	private String ds_SignatureVersion;
	private String ds_MerchantParameters;
	private String ds_Signature;

	private String textoMotivoAbrirDisputa;

	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (estaEmpleadoLogado()) {
			this.estadoSeleccionado = "PENDIENTE_ENVIO";
			this.listaPedidosParaTramitar = pedidoService.getPedidoPorUltimoEstado(this.estadoSeleccionado);

			Object pedidoParaMostrar = context.getExternalContext().getSessionMap().get("pedidoParaVerDetalles");
			if (pedidoParaMostrar != null && pedidoParaMostrar instanceof Pedido) {
				this.pedidoParaMostrar = (Pedido) pedidoParaMostrar;
			}
		}

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
			this.listaPedidos = pedidoService.getPedidosDeClienteConId(this.clienteLogado.getId());

			Object pedidoParaMostrar = context.getExternalContext().getSessionMap().get("pedidoParaVerDetalles");
			if (pedidoParaMostrar != null && pedidoParaMostrar instanceof Pedido) {
				this.pedidoParaMostrar = (Pedido) pedidoParaMostrar;
			}
		}
	}

	public void getListaPedidosParaTramitarPorEstado() {

		this.listaPedidosParaTramitar = pedidoService.getPedidoPorUltimoEstado(this.estadoSeleccionado);

	}

	public Anuncio getAnuncioPorId(Integer id) {

		return anuncioService.findById(id).get();

	}

	public void abrirHistorialPedidos() throws IOException {
		if (this.clienteLogado != null) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaPedidos.xhtml");
		}
	}

	public void abrirPedidosParaTramitar() throws IOException {

		if (estaEmpleadoLogado()) {

			FacesContext.getCurrentInstance().getExternalContext().redirect("listaPedidosParaTramitar.xhtml");
		}
	}

	public boolean filtrarFechaCreacion(Object value, Object filter, Locale locale) {

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

	public boolean filtrarTablaPorProducto(Object value, Object filter, Locale locale) {

		Set<Integer> listaProductos = new HashSet<Integer>();
		String textoFiltro = null;
		if (value instanceof Integer) {
			Integer idPed = (Integer) value;
			Pedido pedido = pedidoService.findById(idPed).get();

			listaProductos = pedido.getMapaAnunciosCantidad().keySet();
		}

		if (filter instanceof String) {
			textoFiltro = (String) filter;
			textoFiltro = textoFiltro.trim().toUpperCase();
		}

		for (Integer i : listaProductos) {
			Anuncio anuncio = anuncioService.findById(i).get();
			String prod = anuncio.getTitulo().trim().toUpperCase();
			if (prod.contains(textoFiltro)) {
				return true;
			}
		}

		return false;

	}

	public void abrirDetallesPedido(Pedido pedido) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("pedidoParaVerDetalles", pedido);
		FacesContext.getCurrentInstance().getExternalContext().redirect("verPedido.xhtml");

	}

	public void abrirDisputaPedido(Pedido pedido) {

		if (this.clienteLogado != null
				&& (pedido.getUltimoEstado().equals("ENVIADO") || pedido.getUltimoEstado().equals("ENTREGADO"))) {

			EstadoPedido nuevoEstado = estadoPedidoService.createEstadoConDisputaAbierta(this.textoMotivoAbrirDisputa);
			pedido.addEstadoPedidoNuevo(nuevoEstado);

			pedidoService.save(pedido);

		}

	}

	public void cancelarPedido(Pedido pedido) {

		if (this.clienteLogado != null && (pedido.getUltimoEstado().equals("PENDIENTE_ENVIO"))) {

			EstadoPedido nuevoEstado = estadoPedidoService.createEstadoPendienteDeReembolso();
			pedido.addEstadoPedidoNuevo(nuevoEstado);

			pedidoService.save(pedido);

		}
	}

	public void eliminarPedidoSinPago(Pedido pedido) {

		if (this.clienteLogado != null && (pedido.getUltimoEstado().equals("PAGO_PENDIENTE"))) {

			pedidoService.delete(pedido);

		}
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

		String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pedidoId");
		Integer id = new Integer(value);
		Pedido pedido = pedidoService.findById(id).get();

		Integer precio = (int) ((pedido.getPrecioTotalProductos() + pedido.getPrecioEnvio()) * 100);
		String amount = precio.toString();

		pedido.setReferenciaPedido(pedidoService.getReferenciaPedidoUnicoGenerado());
		Pedido pedidoN = pedidoService.save(pedido);

		String numeroPedido = pedidoN.getReferenciaPedido();
		System.out.println("NUMERO_PEDIDO: " + numeroPedido);

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

	}

	public void realizarDevolucionRedsys()
			throws IOException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		Properties propiedades = new Properties();
		propiedades.load(this.getClass().getResourceAsStream("/redsys.properties"));

		String dsMerchantcode = propiedades.getProperty("DS_MERCHANT_MERCHANTCODE");
		String dsMerchantcurrency = propiedades.getProperty("DS_MERCHANT_CURRENCY");
		String dsMerchantTerminal = propiedades.getProperty("DS_MERCHANT_TERMINAL");
		String dsMerchantUrl = propiedades.getProperty("DS_MERCHANT_MERCHANTURL");
		String dsMerchantUrlOk = propiedades.getProperty("DS_MERCHANT_URLOK");
		String dsMerchantUrlKo = propiedades.getProperty("DS_MERCHANT_URLKO");
		ApiMacSha256 apiMacSha256 = new ApiMacSha256();

		String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("pedidoId");
		Integer id = new Integer(value);
		Pedido pedido = pedidoService.findById(id).get();

		Integer precio = (int) ((pedido.getPrecioTotalProductos() + pedido.getPrecioEnvio()) * 100);
		String amount = precio.toString();

		String numeroPedido = pedido.getReferenciaPedido();
		System.out.println("NUMERO_PEDIDO: " + numeroPedido);

		apiMacSha256.setParameter("DS_MERCHANT_AMOUNT", amount);
		apiMacSha256.setParameter("DS_MERCHANT_ORDER", numeroPedido);
		apiMacSha256.setParameter("DS_MERCHANT_MERCHANTCODE", dsMerchantcode);
		apiMacSha256.setParameter("DS_MERCHANT_CURRENCY", dsMerchantcurrency);
		apiMacSha256.setParameter("DS_MERCHANT_TRANSACTIONTYPE", "3"); // Devolucion
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

	}

	public Boolean estaEmpleadoLogado() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return auth != null && (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("empleado"))
				|| auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin")));

	}

	public void realizarEnvio(Pedido pedido) {

		if (estaEmpleadoLogado() && (pedido.getUltimoEstado().equals("PENDIENTE_ENVIO"))) {

			EstadoPedido nuevoEstado = estadoPedidoService.createEstadoEnviado();
			pedido.addEstadoPedidoNuevo(nuevoEstado);

			pedidoService.save(pedido);

		}
	}

	public void realizarReembolso(Pedido pedido)
			throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {

		if (estaEmpleadoLogado() && (pedido.getUltimoEstado().equals("PENDIENTE_REEMBOlSO"))) {

			realizarDevolucionRedsys();

			EstadoPedido nuevoEstado = estadoPedidoService.createEstadoReembolsado();
			pedido.addEstadoPedidoNuevo(nuevoEstado);

			pedidoService.save(pedido);

		}
	}

	public void aceptarDisputaPedido(Pedido pedido) {

		if (estaEmpleadoLogado() && (pedido.getUltimoEstado().equals("CON_DISPUTA"))) {

			EstadoPedido nuevoEstado = estadoPedidoService.createEstadoPendienteDeReembolso();
			pedido.addEstadoPedidoNuevo(nuevoEstado);

			pedidoService.save(pedido);

		}
	}

	public void denegarDisputaPedido(Pedido pedido) {

		if (estaEmpleadoLogado() && (pedido.getUltimoEstado().equals("CON_DISPUTA"))) {

			// TODO

			EstadoPedido nuevoEstado = estadoPedidoService.createEstadoDisputaDenegada(this.textoMotivoAbrirDisputa);
			pedido.addEstadoPedidoNuevo(nuevoEstado);

			pedidoService.save(pedido);

		}
	}

	public Boolean renderBotonPagarYEliminar(Pedido pedido) {

		return pedido.getUltimoEstado().equals("PAGO_PENDIENTE");

	}

	public Boolean renderBotonCancelarPedido(Pedido pedido) {

		return pedido.getUltimoEstado().equals("PENDIENTE_ENVIO");

	}

	public Boolean renderBotonAbrirDisputaPedido(Pedido pedido) {

		return pedido.getUltimoEstado().equals("ENVIADO") || pedido.getUltimoEstado().equals("ENTREGADO");

	}

	public Boolean renderBotonRealizarReembolso(Pedido pedido) {

		return pedido.getUltimoEstado().equals("PENDIENTE_REEMBOlSO");

	}

	public Boolean renderBotonResolucionDisputa(Pedido pedido) {

		return pedido.getUltimoEstado().equals("CON_DISPUTA");

	}

	public Boolean renderBotonRealizarEnvio(Pedido pedido) {

		return pedido.getUltimoEstado().equals("PENDIENTE_ENVIO");

	}

	public List<Pedido> getListaPedidos() {
		return listaPedidos;
	}

	public void setListaPedidos(List<Pedido> listaPedidos) {
		this.listaPedidos = listaPedidos;
	}

	public Cliente getClienteLogado() {
		return clienteLogado;
	}

	public void setClienteLogado(Cliente clienteLogado) {
		this.clienteLogado = clienteLogado;
	}

	public Pedido getPedidoParaMostrar() {
		return pedidoParaMostrar;
	}

	public void setPedidoParaMostrar(Pedido pedidoParaMostrar) {
		this.pedidoParaMostrar = pedidoParaMostrar;
	}

	public List<Pedido> getListaPedidosParaTramitar() {
		return listaPedidosParaTramitar;
	}

	public void setListaPedidosParaTramitar(List<Pedido> listaPedidosParaTramitar) {
		this.listaPedidosParaTramitar = listaPedidosParaTramitar;
	}

	public String getEstadoSeleccionado() {
		return estadoSeleccionado;
	}

	public void setEstadoSeleccionado(String estadoSeleccionado) {
		this.estadoSeleccionado = estadoSeleccionado;
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

	public String getTextoMotivoAbrirDisputa() {
		return textoMotivoAbrirDisputa;
	}

	public void setTextoMotivoAbrirDisputa(String textoMotivoAbrirDisputa) {
		this.textoMotivoAbrirDisputa = textoMotivoAbrirDisputa;
	}

}
