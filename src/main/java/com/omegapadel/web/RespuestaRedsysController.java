package com.omegapadel.web;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import com.omegapadel.model.Accesorio;
import com.omegapadel.model.AnuncioCantidad;
import com.omegapadel.model.EstadoPedido;
import com.omegapadel.model.Pala;
import com.omegapadel.model.Paletero;
import com.omegapadel.model.Pedido;
import com.omegapadel.model.Pelota;
import com.omegapadel.model.Producto;
import com.omegapadel.model.ProductoTalla;
import com.omegapadel.model.Ropa;
import com.omegapadel.model.Zapatilla;
import com.omegapadel.service.AccesorioService;
import com.omegapadel.service.AnuncioCantidadService;
import com.omegapadel.service.EstadoPedidoService;
import com.omegapadel.service.PedidoService;
import com.omegapadel.service.ProductoTallaService;
import com.omegapadel.service.RopaService;
import com.omegapadel.service.ZapatillaService;

import sis.redsys.api.ApiMacSha256;

@Named("respuestaRedsysController")
@ViewScoped
public class RespuestaRedsysController implements Serializable {

	private static final long serialVersionUID = -1801917740179216443L;

	@Inject
	private PedidoService pedidoService;
	@Inject
	private EstadoPedidoService estadopedidoService;
	@Inject
	private AnuncioCantidadService anuncioCantidadService;
	@Inject
	private RopaService ropaService;
	@Inject
	private AccesorioService accesorioService;
	@Inject
	private ZapatillaService zapatillaService;
	@Inject
	private ProductoTallaService productoTallaService;

	private Boolean esCorrecto;

	@PostConstruct
	public void init()
			throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalStateException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		this.esCorrecto = recuperaDatosPedidoRetornoNavegacion();
	}

	public Boolean recuperaDatosPedidoRetornoNavegacion()
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		ApiMacSha256 apiMacSha256 = new ApiMacSha256();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		String version = request.getParameter("Ds_SignatureVersion");
		String params = request.getParameter("Ds_MerchantParameters");
		String signatureRecibida = request.getParameter("Ds_Signature");

		String decodec = apiMacSha256.decodeMerchantParameters(params);

		String codigoRespuesta = apiMacSha256.getParameter("Ds_Response");

		String claveModuloAdmin = "sq7HjrUOBfKmC576ILgskD5srU870gJ7";
		String signatureCalculada = apiMacSha256.createMerchantSignatureNotif(claveModuloAdmin, params);

		if (signatureCalculada.equals(signatureRecibida)) {
			System.out.println("FIRMA OK. Realizar tareas en el servidor");
			// Recoger referencia pedido, y avanzar al estado PAGO_ACEPTADO

			String[] arrayString = decodec.replace("{", "").replace("}", "").split(",");
			Map<String, String> mapaString = new HashMap<String, String>();
			for (String s : arrayString) {
				String[] arrayAux = s.split(":");

				mapaString.put(arrayAux[0].replace("\"", ""), arrayAux[1]);
			}

			String referencia = mapaString.get("Ds_Order").replace("\"", "");
			Pedido pedido = pedidoService.getPedidoPorReferencia(referencia).get();

			List<AnuncioCantidad> lac = anuncioCantidadService.getAnunciosCantidadDePedido(pedido.getId());
			for (AnuncioCantidad ac : lac) {
				Integer cantidad = ac.getCantidad();
				List<Producto> lps = ac.getAnuncio().getProductos();
				for (Producto p : lps) {
					if (p instanceof Pala || p instanceof Paletero || p instanceof Pelota) {
						Integer stock = p.getStock();
						p.setStock(stock - cantidad);
					} else if (p instanceof Zapatilla) {

						ProductoTalla pt = productoTallaService.getProductoTallaDeAnuncioCantidadYProducto(p.getId(),
								ac.getId());
						String i = pt.getTalla();

						Zapatilla zapa = (Zapatilla) p;
						Map<String, Integer> map = zapa.getMapaTallaStock();
						Integer stock = map.get(i);
						map.put(i, stock - cantidad);
						zapa.setMapaTallaStock(map);

						Collection<Integer> iColl = map.values();
						Integer res = 0;
						for (Integer index : iColl) {
							res = res + index;
						}
						zapa.setStock(res);

						zapatillaService.save(zapa);

					} else if (p instanceof Ropa) {

						ProductoTalla pt = productoTallaService.getProductoTallaDeAnuncioCantidadYProducto(p.getId(),
								ac.getId());
						String i = pt.getTalla();

						Ropa ropa = (Ropa) p;
						Map<String, Integer> map = ropa.getMapaTallaStock();
						Integer stock = map.get(i);
						map.put(i, stock - cantidad);
						ropa.setMapaTallaStock(map);

						Collection<Integer> iColl = map.values();
						Integer res = 0;
						for (Integer index : iColl) {
							res = res + index;
						}
						ropa.setStock(res);

						ropaService.save(ropa);

					} else if (p instanceof Accesorio) {

						Accesorio acc = (Accesorio) p;
						if (acc.getTipo().getTipoTalla().equals("UNICA")) {

							Integer stock = p.getStock();
							p.setStock(stock - cantidad);

						} else {

							ProductoTalla pt = productoTallaService
									.getProductoTallaDeAnuncioCantidadYProducto(p.getId(), ac.getId());
							String i = pt.getTalla();

							Map<String, Integer> map = acc.getMapaTallaStock();
							Integer stock = map.get(i);
							map.put(i, stock - cantidad);
							acc.setMapaTallaStock(map);

							Collection<Integer> iColl = map.values();
							Integer res = 0;
							for (Integer index : iColl) {
								res = res + index;
							}
							acc.setStock(res);

							accesorioService.save(acc);

						}
					}
				}
			}

			EstadoPedido estadoNuevo = estadopedidoService.createEstadoPagoAceptado();
			pedido.addEstadoPedidoNuevo(estadoNuevo);
			EstadoPedido estadoNuevo2 = estadopedidoService.createEstadoPendienteEnvio();
			pedido.addEstadoPedidoNuevo(estadoNuevo2);
			pedidoService.save(pedido);

			return true;
		} else {
			System.out.println("FIRMA KO. Error, firma inválida");
			return false;
		}

	}

	public void recuperaDatosPedidoNotificacionOnline()
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, IllegalStateException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		ApiMacSha256 apiMacSha256 = new ApiMacSha256();

		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();

		String version = request.getParameter("Ds_SignatureVersion");
		String params = request.getParameter("Ds_MerchantParameters");
		String signatureRecibida = request.getParameter("Ds_Signature");

		String decodec = apiMacSha256.decodeMerchantParameters(params);

		String codigoRespuesta = apiMacSha256.getParameter("Ds_Response");

		String claveModuloAdmin = "sq7HjrUOBfKmC576ILgskD5srU870gJ7";
		String signatureCalculada = apiMacSha256.createMerchantSignatureNotif(claveModuloAdmin, params);

		if (signatureCalculada.equals(signatureRecibida)) {
			System.out.println("FIRMA OK. Realizar tareas en el servidor");

		} else {
			System.out.println("FIRMA KO. Error, firma inválida");
		}
	}

	public Boolean getEsCorrecto() {
		return esCorrecto;
	}

	public void setEsCorrecto(Boolean esCorrecto) {
		this.esCorrecto = esCorrecto;
	}

}
