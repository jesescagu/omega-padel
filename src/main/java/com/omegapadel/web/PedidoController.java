package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.Pedido;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.PedidoService;

@Named("pedidoController")
@ViewScoped
public class PedidoController implements Serializable{

	private static final long serialVersionUID = -4101551350656645590L;
	
	@Inject
	private ClienteService clienteService;
	
	@Inject
	private PedidoService pedidoService;
	
	private Cliente clienteLogado;
	private List<Pedido> listaPedidos;

	
	@PostConstruct
	public void init() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null
				&& auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
			User user = (User) auth.getPrincipal();
			String nombreUsuario = user.getUsername();

			this.clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);

			this.listaPedidos = pedidoService.getPedidosDeClienteConId(this.clienteLogado.getId());
		}
	}
	
	public void abrirHistorialPedidos() throws IOException{
		if (this.clienteLogado != null) {
			FacesContext.getCurrentInstance().getExternalContext().redirect("listaPedidos.xhtml");
		}
	}
	
	public List<Pedido> getListaPedidos() {
		return listaPedidos;
	}

	public void setListaPedidos(List<Pedido> listaPedidos) {
		this.listaPedidos = listaPedidos;
	}
	
}
