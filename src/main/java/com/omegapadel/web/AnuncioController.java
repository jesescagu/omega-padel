package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.file.UploadedFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.Comentario;
import com.omegapadel.model.Empleado;
import com.omegapadel.model.Imagen;
import com.omegapadel.model.Pelota;
import com.omegapadel.model.Producto;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.ComentarioService;
import com.omegapadel.service.EmpleadoService;
import com.omegapadel.service.ImagenService;
import com.omegapadel.service.ProductoService;

@Named("anuncioController")
@ViewScoped
public class AnuncioController implements Serializable {

	private static final long serialVersionUID = 8919849616921030506L;

	@Inject
	private AnuncioService anuncioService;
	@Inject
	private ImagenService imagenService;
	@Inject
	private ProductoService productoService;
	@Inject
	private EmpleadoService empleadoService;
	@Inject
	private ComentarioService comentarioService;
	@Inject
	private ClienteService clienteService;

	private int activeIndex = 0;

	private Anuncio anuncioSeleccionado;
	private List<Anuncio> listaAnunciosCreados;
	private Anuncio nuevoAnuncio;
	private UploadedFile imagenAnuncioNuevo;
	private List<Imagen> imagenesAnuncioNuevo;
	private Boolean validacionImagenes;

	List<String> imagenesAMostrarDelAnuncio;

	private String tituloNuevoAnuncio;
	private String descripcionNuevoAnuncio;
	private Double precioNuevoAnuncio;

	private String tipoProductoEscogido;

	private List<Producto> listaProductosParaAnadir;
	private List<? extends Producto> listaProductos;

	private String nuevoTituloComentario;
	private String nuevaDescripcionComentario;
	private Integer nuevaCalificacionComentario;

	private Boolean estaClienteLogado;
	private Boolean estaEmpleadoLogado;
	private List<Comentario> listaComentariosAnuncio;

	private List<Anuncio> listaAnunciosDesactivados;

	private List<Anuncio> listaAnunciosPacksPorMarca;

	public static final String TEXTO_ERROR_IMAGENES_VACIAS = "Debe existir alguna imagen del anuncio.";

	public void popularListaProductos() {
		this.listaProductos = productoService.getProductosDeUnTipo(this.tipoProductoEscogido,
				this.listaProductosParaAnadir);
	}

	@PostConstruct
	public void init() {

		FacesContext context = FacesContext.getCurrentInstance();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null && auth.getAuthorities().stream()
				.anyMatch(a -> (a.getAuthority().equals("empleado") || a.getAuthority().equals("admin")))) {
			this.estaEmpleadoLogado = true;
			this.listaAnunciosDesactivados = anuncioService.getAnunciosDesactivados();
		} else {
			this.estaEmpleadoLogado = false;
		}

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {
			this.estaClienteLogado = true;
		} else {
			this.estaClienteLogado = false;
		}

		this.tipoProductoEscogido = "Pala";
		this.listaProductosParaAnadir = new ArrayList<>();

		this.validacionImagenes = false;

		Object anuncioParaEditar = context.getExternalContext().getSessionMap().get("anuncioParaEditar");
		Object anuncioParaMostrar = context.getExternalContext().getSessionMap().get("anuncioParaMostrar");

		if (anuncioParaMostrar != null && anuncioParaMostrar instanceof Anuncio) {
			this.anuncioSeleccionado = (Anuncio) anuncioParaMostrar;
			context.getExternalContext().getSessionMap().remove("anuncioParaMostrar");
			this.listaComentariosAnuncio = comentarioService.getComentariosDeAnuncio(this.anuncioSeleccionado.getId());
		}

		if (anuncioParaEditar != null && anuncioParaEditar instanceof Anuncio) {
			this.nuevoAnuncio = (Anuncio) anuncioParaEditar;
			context.getExternalContext().getSessionMap().remove("anuncioParaEditar");
		}

		if (this.nuevoAnuncio == null) {

			this.imagenesAnuncioNuevo = new ArrayList<>();
			this.descripcionNuevoAnuncio = null;
			this.tituloNuevoAnuncio = null;
		} else {

			this.imagenesAnuncioNuevo = imagenService.getImagenesDelAnuncio(this.nuevoAnuncio.getId());
			this.descripcionNuevoAnuncio = this.nuevoAnuncio.getDescripcion();
			this.tituloNuevoAnuncio = this.nuevoAnuncio.getTitulo();
			this.precioNuevoAnuncio = this.nuevoAnuncio.getPrecio();
			this.listaProductosParaAnadir = this.nuevoAnuncio.getProductos();

		}

		if (this.anuncioSeleccionado != null) {
			this.imagenesAMostrarDelAnuncio = new ArrayList<String>();
			List<Imagen> imagenesAux = imagenService.getImagenesDelAnuncio(this.anuncioSeleccionado.getId());
			for (Imagen i : imagenesAux) {
				this.imagenesAMostrarDelAnuncio.add(getBytesDeImagen(i));
			}
		}

		Object marcaDeAnunciosObject = context.getExternalContext().getSessionMap().get("marcaDeAnuncios");
		if (marcaDeAnunciosObject != null) {

			String marcaDeAnuncios = (String) marcaDeAnunciosObject;
			this.listaAnunciosPacksPorMarca = anuncioService.getAnunciosPorMarcaPack(marcaDeAnuncios);
			context.getExternalContext().getSessionMap().remove("marcaDeAnuncios");
		}

		popularListaProductos();
	}

	public void mostrarAnuncio(Anuncio anuncio) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("anuncioParaMostrar", anuncio);

		FacesContext.getCurrentInstance().getExternalContext().redirect("verAnuncio.xhtml");
	}

	public void editarAnuncio(Anuncio anuncio) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("anuncioParaEditar", anuncio);

		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoAnuncio.xhtml");
	}

	public String guardarAnuncio() throws IOException {

		Boolean validacionErronea = false;
		validacionImagenes();
		if (!this.validacionImagenes) {
			FacesMessage facesMsgImagenes = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debes añadir al menos una imagen.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgImagenes);
			validacionErronea = true;
		}

		if (this.tituloNuevoAnuncio == null || this.tituloNuevoAnuncio.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe indicar un titulo para el anuncio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.descripcionNuevoAnuncio == null || this.descripcionNuevoAnuncio.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe indicar una descripción para el anuncio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.precioNuevoAnuncio == null) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe indicar un precio para el anuncio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (CollectionUtils.isEmpty(this.listaProductosParaAnadir)) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe indicar al menos un producto para el anuncio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (validacionErronea) {
			return "";
		}

		if (this.validacionImagenes) {

			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			User user = (User) auth.getPrincipal();
			String nombreUsuario = user.getUsername();
			Empleado empleadoLogado = empleadoService.buscaEmpleadoPorNombreUsuario(nombreUsuario);

			Anuncio anuncio = null;
			if (this.nuevoAnuncio == null) {
				anuncio = anuncioService.create(tituloNuevoAnuncio, descripcionNuevoAnuncio, precioNuevoAnuncio,
						empleadoLogado, listaProductosParaAnadir);
			} else {
				anuncio = this.nuevoAnuncio;
				anuncio.setDescripcion(descripcionNuevoAnuncio);
				anuncio.setPrecio(precioNuevoAnuncio);
				anuncio.setProductos(listaProductosParaAnadir);
				anuncio.setTitulo(tituloNuevoAnuncio);
				anuncio.setEmpleado(empleadoLogado);
			}
			Anuncio saved = anuncioService.save(anuncio);

			List<Imagen> imagenesGuardadas = new ArrayList<>();
			for (Imagen im : this.imagenesAnuncioNuevo) {
				im.setAnuncio(saved);
				Imagen savedI = imagenService.save(im);
				imagenesGuardadas.add(savedI);
			}
		}
		this.nuevoAnuncio = null;
		return "index";

	}

	public void guardarComentario(Anuncio anuncio) throws IOException {

		Boolean hayError = false;

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {

			if (this.nuevoTituloComentario == null || this.nuevoTituloComentario.equals("")) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe indicar un titulo para el comentario.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				hayError = true;
			}

			if (this.nuevaDescripcionComentario == null || this.nuevaDescripcionComentario.equals("")) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe indicar una descripción para el comentario.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				hayError = true;
			}

			if (this.nuevaCalificacionComentario == null) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe indicar una calificación para el comentario.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				hayError = true;
			}

			if (!hayError) {
				User user = (User) auth.getPrincipal();
				String nombreUsuario = user.getUsername();
				Cliente clienteLogado = clienteService.buscaClientePorNombreUsuario(nombreUsuario);

				Comentario comentario = comentarioService.create(nuevoTituloComentario, nuevaDescripcionComentario,
						nuevaCalificacionComentario, clienteLogado);
				comentario.setActor(clienteLogado);
				comentario.setAnuncio(anuncio);
				comentarioService.save(comentario);
				mostrarAnuncio(anuncio);
			}
		}
	}

	public void eliminarComentario(Comentario comentario) throws IOException {

		if (this.estaEmpleadoLogado) {
			comentarioService.delete(comentario);
		}
		mostrarAnuncio(anuncioSeleccionado);

	}

	public void desactivarAnuncio(Anuncio anuncio) throws IOException {

		if (this.estaEmpleadoLogado) {
			anuncio.setActivo(false);
			Anuncio anuncioSaved = anuncioService.save(anuncio);
			mostrarAnuncio(anuncioSaved);
		}
	}

	public void activarAnuncio(Anuncio anuncio) throws IOException {

		if (this.estaEmpleadoLogado) {
			anuncio.setActivo(true);
			Anuncio anuncioSaved = anuncioService.save(anuncio);
			mostrarAnuncio(anuncioSaved);
		}
	}

	public List<Imagen> getImagenesDeProducto(Integer prodId) {
		List<Imagen> imagenes = imagenService.getImagenesDelProducto(prodId);
		if (!CollectionUtils.isEmpty(imagenes)) {
			return imagenes;
		} else {
			return new ArrayList<>();
		}

	}

	public List<Imagen> getImagenesDelAnuncio(Integer anunId) {
		List<Imagen> imagenes = imagenService.getImagenesDelAnuncio(anunId);
		if (!CollectionUtils.isEmpty(imagenes)) {
			return imagenes;
		} else {
			return new ArrayList<>();
		}

	}

	public Imagen getPrimeraImagenDelAnuncio(Integer anunId) {
		List<Imagen> imagenes = getImagenesDelAnuncio(anunId);
		if (!CollectionUtils.isEmpty(imagenes)) {
			return imagenes.get(0);
		} else {
			return null;
		}
	}

	public void addProductoDeLaLista(Integer prodId) {

		Optional<Producto> prodOpt = productoService.findById(prodId);
		if (prodOpt.isPresent()) {

			listaProductosParaAnadir.add(prodOpt.get());
			this.listaProductos = productoService.getProductosDeUnTipo(this.tipoProductoEscogido,
					this.listaProductosParaAnadir);

		}

	}

	public void eliminaProductoDeLaLista(Producto prod) {

		listaProductosParaAnadir.remove(prod);
		this.listaProductos = productoService.getProductosDeUnTipo(this.tipoProductoEscogido,
				this.listaProductosParaAnadir);

	}

	public Integer notaMedia(Anuncio anuncio) {

		return comentarioService.getNotaMediaAnuncio(anuncio);

	}

	public void nuevaImagen() {

		try {
			if (this.imagenAnuncioNuevo != null && this.imagenAnuncioNuevo.getSize() != 0) {

				Imagen imagen = imagenService.create(this.imagenAnuncioNuevo.getFileName(),
						this.imagenAnuncioNuevo.getContent());

				Boolean yaEsta = false;
				for (Imagen im : this.imagenesAnuncioNuevo) {
					if (imagen.getContent().equals(new SerialBlob(im.getContent()))) {
						yaEsta = true;
						break;
					}
				}

				if (!yaEsta) {
					Imagen saved = imagenService.save(imagen);
					this.imagenesAnuncioNuevo.add(saved);
				} else {
					FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"La imagen ya esta adjuntada.", null);
					FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				}

			}
			this.imagenAnuncioNuevo = null;
			validacionImagenes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getBytesDeImagen(Imagen elem) {
		try {
			byte[] photo = elem.getContent().getBytes(1, (int) elem.getContent().length());
			String bphoto = Base64.getEncoder().encodeToString(photo);
			return bphoto;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void validacionImagenes() {
		if (CollectionUtils.isEmpty(this.imagenesAnuncioNuevo)) {
			this.validacionImagenes = false;
		} else {
			this.validacionImagenes = true;
		}
	}

	public void eliminaImagen(Imagen elem) {

		imagenService.delete(elem);
		this.imagenesAnuncioNuevo.remove(elem);
		validacionImagenes();

	}

	public String eliminarAnuncio(Anuncio elem) {

		List<Imagen> imagenes = imagenService.getImagenesDelProducto(elem.getId());
		for (Imagen i : imagenes) {
			imagenService.delete(i);
		}
		anuncioService.delete(elem);
		this.listaAnunciosCreados = anuncioService.findAll();
		return "";
	}

	public void verNuevoAnuncio() throws IOException {

		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoAnuncio.xhtml");
	}

	public void verListaAnunciosDesactivados() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaAnunciosDesactivados.xhtml");
	}

	public void verPacksMarcas(String marca) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("marcaDeAnuncios", marca);

		FacesContext.getCurrentInstance().getExternalContext().redirect("packs.xhtml");

	}

	public Anuncio getAnuncioSeleccionado() {
		return anuncioSeleccionado;
	}

	public void setAnuncioSeleccionado(Anuncio anuncioSeleccionado) {
		this.anuncioSeleccionado = anuncioSeleccionado;
	}

	public List<Anuncio> getListaAnunciosCreados() {
		return listaAnunciosCreados;
	}

	public void setListaAnunciosCreados(List<Anuncio> listaAnunciosCreados) {
		this.listaAnunciosCreados = listaAnunciosCreados;
	}

	public Anuncio getNuevoAnuncio() {
		return nuevoAnuncio;
	}

	public void setNuevoAnuncio(Anuncio nuevoAnuncio) {
		this.nuevoAnuncio = nuevoAnuncio;
	}

	public UploadedFile getImagenAnuncioNuevo() {
		return imagenAnuncioNuevo;
	}

	public void setImagenAnuncioNuevo(UploadedFile imagenAnuncioNuevo) {
		this.imagenAnuncioNuevo = imagenAnuncioNuevo;
	}

	public List<Imagen> getImagenesAnuncioNuevo() {
		return imagenesAnuncioNuevo;
	}

	public void setImagenesAnuncioNuevo(List<Imagen> imagenesAnuncioNuevo) {
		this.imagenesAnuncioNuevo = imagenesAnuncioNuevo;
	}

	public Boolean getValidacionImagenes() {
		return validacionImagenes;
	}

	public void setValidacionImagenes(Boolean validacionImagenes) {
		this.validacionImagenes = validacionImagenes;
	}

	public String getTituloNuevoAnuncio() {
		return tituloNuevoAnuncio;
	}

	public void setTituloNuevoAnuncio(String tituloNuevoAnuncio) {
		this.tituloNuevoAnuncio = tituloNuevoAnuncio;
	}

	public String getDescripcionNuevoAnuncio() {
		return descripcionNuevoAnuncio;
	}

	public void setDescripcionNuevoAnuncio(String descripcionNuevoAnuncio) {
		this.descripcionNuevoAnuncio = descripcionNuevoAnuncio;
	}

	public Double getPrecioNuevoAnuncio() {
		return precioNuevoAnuncio;
	}

	public void setPrecioNuevoAnuncio(Double precioNuevoAnuncio) {
		this.precioNuevoAnuncio = precioNuevoAnuncio;
	}

	public String getTipoProductoEscogido() {
		return tipoProductoEscogido;
	}

	public void setTipoProductoEscogido(String tipoProductoEscogido) {
		this.tipoProductoEscogido = tipoProductoEscogido;
	}

	public List<Producto> getListaProductosParaAnadir() {
		return listaProductosParaAnadir;
	}

	public void setListaProductosParaAnadir(List<Producto> listaProductosParaAnadir) {
		this.listaProductosParaAnadir = listaProductosParaAnadir;
	}

	public List<? extends Producto> getListaProductos() {
		return listaProductos;
	}

	public void setListaProductos(List<? extends Producto> listaProductos) {
		this.listaProductos = listaProductos;
	}

	public int getActiveIndex() {
		return activeIndex;
	}

	public void setActiveIndex(int activeIndex) {
		this.activeIndex = activeIndex;
	}

	public List<String> getImagenesAMostrarDelAnuncio() {
		return imagenesAMostrarDelAnuncio;
	}

	public void setImagenesAMostrarDelAnuncio(List<String> imagenesAMostrarDelAnuncio) {
		this.imagenesAMostrarDelAnuncio = imagenesAMostrarDelAnuncio;
	}

	public Boolean getEstaEmpleadoLogado() {
		return estaEmpleadoLogado;
	}

	public void setEstaEmpleadoLogado(Boolean estaEmpleadoLogado) {
		this.estaEmpleadoLogado = estaEmpleadoLogado;
	}

	public List<Comentario> getListaComentariosAnuncio() {
		return listaComentariosAnuncio;
	}

	public void setListaComentariosAnuncio(List<Comentario> listaComentariosAnuncio) {
		this.listaComentariosAnuncio = listaComentariosAnuncio;
	}

	public String getNuevoTituloComentario() {
		return nuevoTituloComentario;
	}

	public void setNuevoTituloComentario(String nuevoTituloComentario) {
		this.nuevoTituloComentario = nuevoTituloComentario;
	}

	public String getNuevaDescripcionComentario() {
		return nuevaDescripcionComentario;
	}

	public void setNuevaDescripcionComentario(String nuevaDescripcionComentario) {
		this.nuevaDescripcionComentario = nuevaDescripcionComentario;
	}

	public Integer getNuevaCalificacionComentario() {
		return nuevaCalificacionComentario;
	}

	public void setNuevaCalificacionComentario(Integer nuevaCalificacionComentario) {
		this.nuevaCalificacionComentario = nuevaCalificacionComentario;
	}

	public Boolean getEstaClienteLogado() {
		return estaClienteLogado;
	}

	public void setEstaClienteLogado(Boolean estaClienteLogado) {
		this.estaClienteLogado = estaClienteLogado;
	}

	public List<Anuncio> getListaAnunciosDesactivados() {
		return listaAnunciosDesactivados;
	}

	public void setListaAnunciosDesactivados(List<Anuncio> listaAnunciosDesactivados) {
		this.listaAnunciosDesactivados = listaAnunciosDesactivados;
	}

	public List<Anuncio> getListaAnunciosPacksPorMarca() {
		return listaAnunciosPacksPorMarca;
	}

	public void setListaAnunciosPacksPorMarca(List<Anuncio> listaAnunciosPacksPorMarca) {
		this.listaAnunciosPacksPorMarca = listaAnunciosPacksPorMarca;
	}

}
