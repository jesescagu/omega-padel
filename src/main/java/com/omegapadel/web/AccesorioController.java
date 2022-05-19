package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.primefaces.model.file.UploadedFile;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Imagen;
import com.omegapadel.model.Marca;
import com.omegapadel.model.Accesorio;
import com.omegapadel.model.TipoAccesorio;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.ImagenService;
import com.omegapadel.service.MarcaService;
import com.omegapadel.service.AccesorioService;
import com.omegapadel.service.TipoAccesorioService;

@Named("accesorioController")
@ViewScoped
public class AccesorioController implements Serializable {

	private static final long serialVersionUID = -6186738665220092482L;

	@Inject
	private AnuncioService anuncioService;
	@Inject
	private MarcaService marcaService;
	@Inject
	private AccesorioService accesorioService;
	@Inject
	private ImagenService imagenService;
	@Inject
	private TipoAccesorioService tipoAccesorioService;

	private List<Anuncio> listaAnuncios;
	private Accesorio nuevoAccesorio;
	private UploadedFile imagenAccesorioNuevo;

	private List<Accesorio> listaAccesorio;
	private Collection<Marca> listaMarcas;

	private String marcaEscogida;
	private Integer stockNuevoAccesorio;
	private String modeloNuevoAccesorio;
	private String referenciaNuevoAccesorio;
	private Map<String, Integer> mapaTallaStockNuevoAccesorio;
	private Set<String> listaKeysTallas;
	private String tipoNuevoAccesorio;

	private String tallaNuevoAccesorio;
	private Integer stockNuevoTallaAccesorio;

	private List<Imagen> imagenesAccesorioNuevo;
	private Boolean validacionImagenes;

	private String breadcrumb;

	public static final String TEXTO_ERROR_IMAGENES_VACIAS = "Debe existir alguna imagen del producto.";

	@PostConstruct
	public void init() {

		this.imagenAccesorioNuevo = null;
		this.validacionImagenes = false;
		this.listaMarcas = marcaService.findAll();
		this.listaAccesorio = accesorioService.findAll();

		FacesContext context = FacesContext.getCurrentInstance();
		Object accesorioParaEditar = context.getExternalContext().getSessionMap().get("accesorioParaEditar");
		if (accesorioParaEditar != null && accesorioParaEditar instanceof Accesorio) {
			this.nuevoAccesorio = (Accesorio) accesorioParaEditar;
		}

		if (this.nuevoAccesorio == null) {

			this.imagenesAccesorioNuevo = new ArrayList<>();
			this.stockNuevoAccesorio = null;
			this.modeloNuevoAccesorio = "";
			this.referenciaNuevoAccesorio = "";
			this.marcaEscogida = "";
			this.mapaTallaStockNuevoAccesorio = new HashMap<String, Integer>();
			this.tipoNuevoAccesorio = "";
			this.listaKeysTallas = new HashSet<String>();
			this.tipoNuevoAccesorio = "";

			this.tallaNuevoAccesorio = null;
			this.stockNuevoTallaAccesorio = null;

		} else {

			this.imagenesAccesorioNuevo = imagenService.getImagenesDelProducto(this.nuevoAccesorio.getId());
			this.stockNuevoAccesorio = this.nuevoAccesorio.getStock();
			this.modeloNuevoAccesorio = this.nuevoAccesorio.getModelo();
			this.referenciaNuevoAccesorio = this.nuevoAccesorio.getReferencia();
			this.marcaEscogida = this.nuevoAccesorio.getMarca().getNombre();
			this.mapaTallaStockNuevoAccesorio = this.nuevoAccesorio.getMapaTallaStock();
			this.tipoNuevoAccesorio = this.nuevoAccesorio.getTipo().getNombre();
			this.listaKeysTallas = this.mapaTallaStockNuevoAccesorio.keySet();
			this.tipoNuevoAccesorio = this.nuevoAccesorio.getTipo().getNombre();

			this.tallaNuevoAccesorio = null;
			this.stockNuevoTallaAccesorio = null;

			Object breadcrumbObject = context.getExternalContext().getSessionMap().get("breadcrumb");
			this.breadcrumb = (String) breadcrumbObject;

		}

		Object tipoAccesorioDeAnunciosObject = context.getExternalContext().getSessionMap()
				.get("tipoAccesorioDeAnuncios");

		if (tipoAccesorioDeAnunciosObject != null) {
			String tipoAccesorioDeAnuncios = (String) tipoAccesorioDeAnunciosObject;
			this.tipoNuevoAccesorio = tipoAccesorioDeAnuncios;
			this.listaAnuncios = anuncioService.getAnunciosPorTipoAccesorio(tipoAccesorioDeAnuncios);
		} else {
			this.tipoNuevoAccesorio = "";
		}

		Object verAnunciosTodoObject = context.getExternalContext().getSessionMap().get("verTodoAccesorio");
		if (verAnunciosTodoObject != null) {
			this.listaAnuncios = anuncioService.getAnunciosPorTipo("Accesorio");
		}
	}

	public Boolean renderTipoAccesorio() {
		return this.tipoNuevoAccesorio != null && !this.tipoNuevoAccesorio.equals("");
	}

	public Boolean renderTallasDeAccesorio() {

		TipoAccesorio tipo = tipoAccesorioService.getTipoAccesorioPorNombre(this.tipoNuevoAccesorio);

		if (tipo != null) {
			Boolean res = !tipo.getTipoTalla().equals("UNICA");
			return res;
		} else {
			return false;
		}
	}

	public Boolean renderColumnaTallasDeAccesorio() {

		List<TipoAccesorio> tipoAccesorios = tipoAccesorioService.findAll();
		Boolean res = false;
		for (TipoAccesorio ta : tipoAccesorios) {
			if (!ta.getTipoTalla().equals("UNICA")) {
				res = accesorioService.countAccesoriosPorTipoAccesorio(ta.getId()) != 0;
			}
		}
		return res;
	}

	public void addTallaAAccesorio() {

		if (this.tallaNuevoAccesorio == null || this.tallaNuevoAccesorio.equals("")
				|| this.stockNuevoTallaAccesorio == null || this.stockNuevoTallaAccesorio.equals(0)) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Introduce datos de talla y stock para añadir la talla de la accesorio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
		} else {
			this.mapaTallaStockNuevoAccesorio.put(this.tallaNuevoAccesorio, this.stockNuevoTallaAccesorio);
		}
	}

	public void eliminarTallaDeAccesorio(String talla) {
		this.mapaTallaStockNuevoAccesorio.remove(talla);
	}

	public Integer getStockDeTalla(Accesorio accesorio, String talla) {

		if (accesorio != null && !CollectionUtils.isEmpty(accesorio.getMapaTallaStock().keySet())) {
			return accesorio.getMapaTallaStock().get(talla);
		} else {
			return 0;
		}
	}

	public void verAccesoriosDelTipo(String tipo) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("tipoAccesorioDeAnuncios", tipo);

		context.getExternalContext().getSessionMap().remove("verTodoAccesorio");

		FacesContext.getCurrentInstance().getExternalContext().redirect("accesorios.xhtml");
	}

	public void verAccesorioTodas() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("verTodoAccesorio", true);

		context.getExternalContext().getSessionMap().remove("tipoAccesorioDeAnuncios");

		FacesContext.getCurrentInstance().getExternalContext().redirect("accesorios.xhtml");
	}

	public String guardarAccesorio() throws IOException {

		Boolean validacionErronea = false;
		validacionImagenes();
		if (!this.validacionImagenes) {
			FacesMessage facesMsgImagenes = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe añadir al menos una imagen.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgImagenes);
			validacionErronea = true;
		}
		if (this.modeloNuevoAccesorio == null || this.modeloNuevoAccesorio.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El modelo del accesorio no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.referenciaNuevoAccesorio == null || this.referenciaNuevoAccesorio.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"La referencia del accesorio no puede estar vacia.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (this.nuevoAccesorio == null && accesorioService.existeReferencia(this.referenciaNuevoAccesorio)) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La referencia ya se encuentra en el sistema.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}

		if (this.tipoNuevoAccesorio == null || this.tipoNuevoAccesorio.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El tipo del accesorio no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			TipoAccesorio ta = tipoAccesorioService.getTipoAccesorioPorNombre(this.tipoNuevoAccesorio);

			if (ta.getTipoTalla().equals("UNICA")) {

				if (this.stockNuevoAccesorio == null) {
					FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"El stock del accesorio no puede estar vacio.", null);
					FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
					validacionErronea = true;
				}

			} else {

				if (MapUtils.isEmpty(this.mapaTallaStockNuevoAccesorio)) {
					FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Debe añadir alguna talla del producto.", null);
					FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
					validacionErronea = true;
				}

			}

		}

		if (validacionErronea) {
			return "";
		}

		if (this.validacionImagenes && (this.modeloNuevoAccesorio != null && !this.modeloNuevoAccesorio.equals(""))) {

			Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
			TipoAccesorio tipo = tipoAccesorioService.getTipoAccesorioPorNombre(this.tipoNuevoAccesorio);

			if (!tipo.getTipoTalla().equals("UNICA")) {
				Collection<Integer> iColl = this.mapaTallaStockNuevoAccesorio.values();
				Integer res = 0;
				for (Integer i : iColl) {
					res = res + i;
				}
				this.stockNuevoAccesorio = res;
			}

			if (this.nuevoAccesorio == null) {
				Accesorio accesorio = accesorioService.create(tipo, marca, this.modeloNuevoAccesorio,
						this.stockNuevoAccesorio, this.mapaTallaStockNuevoAccesorio, this.referenciaNuevoAccesorio);
				Accesorio saved = accesorioService.save(accesorio);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesAccesorioNuevo) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}
			} else {
				Accesorio p = this.nuevoAccesorio;

				p.setMarca(marca);
				p.setModelo(this.modeloNuevoAccesorio);
				p.setReferencia(this.referenciaNuevoAccesorio);
				p.setStock(this.stockNuevoAccesorio);
				p.setMapaTallaStock(this.mapaTallaStockNuevoAccesorio);
				p.setTipo(tipo);
				Accesorio saved = accesorioService.save(p);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesAccesorioNuevo) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}

			}
		}
		this.nuevoAccesorio = null;

		if (this.breadcrumb.equals("bajoStock")) {
			return "listaProductosBajoStock.xhtml";
		} else if (this.breadcrumb.equals("desactivado")) {
			return "listaProductosDesactivados.xhtml";
		} else {
			return "listaAccesorio.xhtml";
		}

	}

	public Boolean renderTallaTexto() {

		TipoAccesorio tipo = tipoAccesorioService.getTipoAccesorioPorNombre(this.tipoNuevoAccesorio);
		return tipo.getTipoTalla().equals("LETRAS");

	}

	public List<TipoAccesorio> getTipoAccesorioCreados() {

		return tipoAccesorioService.findAll();

	}

	public List<Imagen> getImagenesDeAccesorio(Integer accesorioId) {
		List<Imagen> imagenes = imagenService.getImagenesDelProducto(accesorioId);
		if (!CollectionUtils.isEmpty(imagenes)) {
			return imagenes;
		} else {
			return new ArrayList<>();
		}

	}

	public void nuevaImagen() throws IOException, SerialException, SQLException {

		if (this.imagenAccesorioNuevo != null && this.imagenAccesorioNuevo.getSize() != 0) {

			Imagen imagen = imagenService.create(this.imagenAccesorioNuevo.getFileName(),
					this.imagenAccesorioNuevo.getContent());

			Boolean yaEsta = false;
			for (Imagen im : this.imagenesAccesorioNuevo) {
				if (imagen.getContent().equals(new SerialBlob(im.getContent()))) {
					yaEsta = true;
					break;
				}
			}

			if (!yaEsta) {
				Imagen saved = imagenService.save(imagen);
				this.imagenesAccesorioNuevo.add(saved);
			} else {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La imagen ya esta adjuntada.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			}

		}
		this.imagenAccesorioNuevo = null;
		validacionImagenes();
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
		if (CollectionUtils.isEmpty(this.imagenesAccesorioNuevo)) {
			this.validacionImagenes = false;
		} else {
			this.validacionImagenes = true;
		}
	}

	public void eliminaImagen(Imagen elem) {

		imagenService.delete(elem);
		this.imagenesAccesorioNuevo.remove(elem);
		validacionImagenes();

	}

	public String eliminarAccesorio(Accesorio elem) {

		Integer count = anuncioService.countAnunciosDeProducto(elem.getId());

		if (count == 0) {
			List<Imagen> imagenes = imagenService.getImagenesDelProducto(elem.getId());
			for (Imagen i : imagenes) {
				imagenService.delete(i);
			}
			accesorioService.delete(elem);
			this.listaAccesorio = accesorioService.findAll();
			return "";
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"No se puede eliminar mientras exista anuncios con este producto.", ""));
			return null;
		}
	}

	public void verNuevoAccesorio() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("accesorioParaEditar");
		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoAccesorio.xhtml");

	}

	public void verEditarAccesorio(Accesorio accesorio, String breadcrumb) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("accesorioParaEditar", accesorio);

		context.getExternalContext().getSessionMap().put("breadcrumb", breadcrumb);

		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoAccesorio.xhtml");

	}

	public void verListaAccesorio() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaAccesorio.xhtml");
	}

	public List<Anuncio> getListaAnuncios() {
		return listaAnuncios;
	}

	public void setListaAnuncios(List<Anuncio> listaAnuncios) {
		this.listaAnuncios = listaAnuncios;
	}

	public Accesorio getNuevoAccesorio() {
		return nuevoAccesorio;
	}

	public void setNuevoAccesorio(Accesorio nuevoAccesorio) {
		this.nuevoAccesorio = nuevoAccesorio;
	}

	public UploadedFile getImagenAccesorioNuevo() {
		return imagenAccesorioNuevo;
	}

	public void setImagenAccesorioNuevo(UploadedFile imagenAccesorioNuevo) {
		this.imagenAccesorioNuevo = imagenAccesorioNuevo;
	}

	public List<Accesorio> getListaAccesorio() {
		return listaAccesorio;
	}

	public void setListaAccesorio(List<Accesorio> listaAccesorio) {
		this.listaAccesorio = listaAccesorio;
	}

	public Collection<Marca> getListaMarcas() {
		return listaMarcas;
	}

	public void setListaMarcas(Collection<Marca> listaMarcas) {
		this.listaMarcas = listaMarcas;
	}

	public String getMarcaEscogida() {
		return marcaEscogida;
	}

	public void setMarcaEscogida(String marcaEscogida) {
		this.marcaEscogida = marcaEscogida;
	}

	public Integer getStockNuevoAccesorio() {
		return stockNuevoAccesorio;
	}

	public void setStockNuevoAccesorio(Integer stockNuevoAccesorio) {
		this.stockNuevoAccesorio = stockNuevoAccesorio;
	}

	public String getModeloNuevoAccesorio() {
		return modeloNuevoAccesorio;
	}

	public void setModeloNuevoAccesorio(String modeloNuevoAccesorio) {
		this.modeloNuevoAccesorio = modeloNuevoAccesorio;
	}

	public Map<String, Integer> getMapaTallaStockNuevoAccesorio() {
		return mapaTallaStockNuevoAccesorio;
	}

	public void setMapaTallaStockNuevoAccesorio(Map<String, Integer> mapaTallaStockNuevoAccesorio) {
		this.mapaTallaStockNuevoAccesorio = mapaTallaStockNuevoAccesorio;
	}

	public List<Imagen> getImagenesAccesorioNuevo() {
		return imagenesAccesorioNuevo;
	}

	public void setImagenesAccesorioNuevo(List<Imagen> imagenesAccesorioNuevo) {
		this.imagenesAccesorioNuevo = imagenesAccesorioNuevo;
	}

	public Boolean getValidacionImagenes() {
		return validacionImagenes;
	}

	public void setValidacionImagenes(Boolean validacionImagenes) {
		this.validacionImagenes = validacionImagenes;
	}

	public Set<String> getListaKeysTallas() {
		return listaKeysTallas;
	}

	public void setListaKeysTallas(Set<String> listaKeysTallas) {
		this.listaKeysTallas = listaKeysTallas;
	}

	public String getTallaNuevoAccesorio() {
		return tallaNuevoAccesorio;
	}

	public void setTallaNuevoAccesorio(String tallaNuevoAccesorio) {
		this.tallaNuevoAccesorio = tallaNuevoAccesorio;
	}

	public Integer getStockNuevoTallaAccesorio() {
		return stockNuevoTallaAccesorio;
	}

	public void setStockNuevoTallaAccesorio(Integer stockNuevoTallaAccesorio) {
		this.stockNuevoTallaAccesorio = stockNuevoTallaAccesorio;
	}

	public String getReferenciaNuevoAccesorio() {
		return referenciaNuevoAccesorio;
	}

	public void setReferenciaNuevoAccesorio(String referenciaNuevoAccesorio) {
		this.referenciaNuevoAccesorio = referenciaNuevoAccesorio;
	}

	public String getTipoNuevoAccesorio() {
		return tipoNuevoAccesorio;
	}

	public void setTipoNuevoAccesorio(String tipoNuevoAccesorio) {
		this.tipoNuevoAccesorio = tipoNuevoAccesorio;
	}

	public String getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(String breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

}
