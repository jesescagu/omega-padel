package com.omegapadel.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.omegapadel.model.Administrador;

public interface AdministradorRepository extends CrudRepository<Administrador, Integer> {

	@Query("select distinct YEAR(p.fechaCreacion) from Pedido p order by YEAR(p.fechaCreacion) desc")
	public List<Integer> getAnyosConAlgunaVenta();

	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac where ac.pedido.id = p.id and size(ac.anuncio.productos) > 1) from Pedido p where p.ultimoEstado like 'Entregado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosPacksPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Pala) from Pedido p where p.ultimoEstado like 'Entregado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosPalasPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Paletero) from Pedido p where p.ultimoEstado like 'Entregado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosPaleterosPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Zapatilla) from Pedido p where p.ultimoEstado like 'Entregado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosZapatillasPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Ropa) from Pedido p where p.ultimoEstado like 'Entregado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosRopaPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Pelota) from Pedido p where p.ultimoEstado like 'Entregado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosPelotasPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Accesorio) from Pedido p where p.ultimoEstado like 'Entregado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosAccesoriosPorAnyo(Integer anyo);

	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac where ac.pedido.id = p.id and size(ac.anuncio.productos) > 1) from Pedido p where p.ultimoEstado like 'Reembolsado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosReembolsadosPacksPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Pala) from Pedido p where p.ultimoEstado like 'Reembolsado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosReembolsadosPalasPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Paletero) from Pedido p where p.ultimoEstado like 'Reembolsado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosReembolsadosPaleterosPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Zapatilla) from Pedido p where p.ultimoEstado like 'Reembolsado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosReembolsadosZapatillasPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Ropa) from Pedido p where p.ultimoEstado like 'Reembolsado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosReembolsadosRopaPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Pelota) from Pedido p where p.ultimoEstado like 'Reembolsado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosReembolsadosPelotasPorAnyo(Integer anyo);
	
	@Query("select MONTH(p.fechaCreacion),(select SUM(ac.cantidad) from AnuncioCantidad ac join ac.anuncio.productos pl where ac.pedido.id = p.id and size(ac.anuncio.productos) = 1 and type(pl) = Accesorio) from Pedido p where p.ultimoEstado like 'Reembolsado' and YEAR(p.fechaCreacion) = ?1 group by MONTH(p.fechaCreacion)")
	public List<Number[]> getPedidosReembolsadosAccesoriosPorAnyo(Integer anyo);
	
	
	
	
	
	
	
}
