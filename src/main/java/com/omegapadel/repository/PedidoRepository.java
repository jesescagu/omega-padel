package com.omegapadel.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Pedido;
import com.omegapadel.model.Producto;

public interface PedidoRepository  extends CrudRepository<Pedido, Integer>{

	@Query("select p from Pedido p where p.cliente.id = ?1 order by p.fechaCreacion desc")
	public List<Pedido> getPedidosDeClienteConId(Integer idCliente);
	
	@Query("select p from Pedido p where p.referenciaPedido = ?1")
	public Optional<Pedido> getPedidoPorReferencia(String referencia);
	
	@Query("select p from Pedido p where p.ultimoEstado = ?1 order by p.fechaCreacion asc")
	public List<Pedido> getPedidoPorUltimoEstado(String ultimoEstado);
	
}
