package com.omegapadel.web;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
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

import com.omegapadel.model.EstadoPedido;
import com.omegapadel.model.Pedido;
import com.omegapadel.service.EstadoPedidoService;
import com.omegapadel.service.PedidoService;

import sis.redsys.api.ApiMacSha256;

@Named("respuestaRedsysController")
@ViewScoped
public class RespuestaRedsysController implements Serializable {

	private static final long serialVersionUID = -1801917740179216443L;

	@Inject
	private PedidoService pedidoService;
	@Inject
	private EstadoPedidoService estadopedidoService;

	private Boolean esCorrecto;

	@PostConstruct
	public void init()
			throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, IllegalStateException,
			NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// TODO
//		mostrar mensaje en la vista
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
			
			String [] arrayString = decodec.replace("{", "").replace("}", "").split(",");
			Map<String,String> mapaString = new HashMap<String, String>();
			for(String s : arrayString) {
				String [] arrayAux = s.split(":");
				
				mapaString.put(arrayAux[0].replace("\"", ""), arrayAux[1]);
			}
			
			String referencia = mapaString.get("Ds_Order").replace("\"", "");
			Pedido pedido = pedidoService.getPedidoPorReferencia(referencia).get();
			
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
