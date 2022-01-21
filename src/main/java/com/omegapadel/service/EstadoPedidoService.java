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

	public EstadoPedido createEstadoRealizado() {
		
		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("Realizado");
		ep.setFecha(new Date());
		save(ep);
		return ep;
	}
	
	public EstadoPedido createEstadoPagoAceptado() {
		
		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("Pago aceptado");
		ep.setFecha(new Date());
		save(ep);
		return ep;
	}
	
	public EstadoPedido createEstadoReembolso() {
		
		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("Reembolso");
		ep.setFecha(new Date());
		save(ep);
		return ep;
	}
	
	public EstadoPedido createEstadoEnviado() {
		
		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("Enviado");
		ep.setFecha(new Date());
		save(ep);
		return ep;
	}
	
	public EstadoPedido createEstadoEntregado() {
		
		EstadoPedido ep = new EstadoPedido();
		ep.setEstado("Entregado");
		ep.setFecha(new Date());
		save(ep);
		return ep;
	}
	
}
