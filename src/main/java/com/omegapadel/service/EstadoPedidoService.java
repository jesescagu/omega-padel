package com.omegapadel.service;

import java.util.Date;
import java.util.Optional;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.EstadoPedido;
import com.omegapadel.repository.EstadoPedidoRepository;

@Service
@Transactional
public class EstadoPedidoService {

	@Inject
	private EstadoPedidoRepository estadoPedidoRepository;

	public Iterable<EstadoPedido> findAll() {
		return estadoPedidoRepository.findAll();
	}

	public Optional<EstadoPedido> findById(Integer arg0) {
		return estadoPedidoRepository.findById(arg0);
	}

	public <S extends EstadoPedido> S save(S arg0) {
		return estadoPedidoRepository.save(arg0);
	}

	public EstadoPedido createEstadoPendientePago() {

		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("PAGO_PENDIENTE");
		ep.setDescripcion("Pago pendiente");
		ep.setFecha(new Date());
		ep.setOrden(1);
		save(ep);
		return ep;
	}
	
	public EstadoPedido createEstadoPagoAceptado() {

		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("PAGO_ACEPTADO");
		ep.setDescripcion("Pago aceptado");
		ep.setFecha(new Date());
		ep.setOrden(2);
		save(ep);
		return ep;
	}
	
	public EstadoPedido createEstadoPendienteEnvio() {

		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("PENDIENTE_ENVIO");
		ep.setDescripcion("Pendiente de envío");
		ep.setFecha(new Date());
		ep.setOrden(3);
		save(ep);
		return ep;
	}

	public EstadoPedido createEstadoEnviado() {

		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("ENVIADO");
		ep.setDescripcion("Enviado");
		ep.setFecha(new Date());
		ep.setOrden(4);
		save(ep);
		return ep;
	}

	public EstadoPedido createEstadoEntregado() {

		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("ENTREGADO");
		ep.setDescripcion("Entregado");
		ep.setFecha(new Date());
		ep.setOrden(5);
		save(ep);
		return ep;
	}

	public EstadoPedido createEstadoConDisputaAbierta(String textoMotivo) {

		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("CON_DISPUTA");
		ep.setDescripcion("Disputa abierta");
		ep.setFecha(new Date());
		ep.setOrden(6);
		ep.setMensaje(textoMotivo);
		save(ep);
		return ep;
	}

	public EstadoPedido createEstadoDisputaDenegada(String textoMotivo) {

		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("DISPUTA_DENEGADA");
		ep.setDescripcion("Disputa denegada");
		ep.setFecha(new Date());
		ep.setOrden(7);
		ep.setMensaje(textoMotivo);
		save(ep);
		return ep;
	}

	public EstadoPedido createEstadoPendienteDeReembolso() {

		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("PENDIENTE_REEMBOlSO");
		ep.setDescripcion("Pendiente de reembolso");
		ep.setFecha(new Date());
		ep.setOrden(8);
		save(ep);
		return ep;
	}

	public EstadoPedido createEstadoReembolsado() {

		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("REEMBOLSADO");
		ep.setDescripcion("Reembolsado");
		ep.setFecha(new Date());
		ep.setOrden(9);
		save(ep);
		return ep;
	}

}
