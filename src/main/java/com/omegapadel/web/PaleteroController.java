package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.file.UploadedFile;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Imagen;
import com.omegapadel.model.Marca;
import com.omegapadel.model.Paletero;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.ImagenService;
import com.omegapadel.service.MarcaService;
import com.omegapadel.service.PaleteroService;

@Named("paleteroController")
@ViewScoped
public class PaleteroController implements Serializable {

	private static final long serialVersionUID = -6186738665220092482L;

	@Inject
	private AnuncioService anuncioService;
	@Inject
	private MarcaService marcaService;
	@Inject
	private PaleteroService paleteroService;
	@Inject
	private ImagenService imagenService;

	private List<Anuncio> listaAnunciosPorMarca;
	private Paletero nuevoPaletero;
	private UploadedFile imagenPaleteroNuevo;

	private List<Paletero> listaPaleteros;
	private Collection<Marca> listaMarcas;

	private String marcaEscogida;
	private Integer stockNuevoPaletero;
	private String modeloNuevoPaletero;
	private String referenciaNuevoPaletero;

	private List<Imagen> imagenesPaleteroNuevo;
	private Boolean validacionImagenes;

	private String breadcrumb;

	public static final String TEXTO_ERROR_IMAGENES_VACIAS = "Debe existir alguna imagen del producto.";

	@PostConstruct
	public void init() {

		this.imagenPaleteroNuevo = null;
		this.validacionImagenes = false;
		this.listaMarcas = marcaService.findAll();
		this.listaPaleteros = paleteroService.findAll();

		FacesContext context = FacesContext.getCurrentInstance();

		Object paleteroParaEditar = context.getExternalContext().getSessionMap().get("paleteroParaEditar");
		if (paleteroParaEditar != null && paleteroParaEditar instanceof Paletero) {
			this.nuevoPaletero = (Paletero) paleteroParaEditar;
		}

		if (this.nuevoPaletero == null) {

			this.imagenesPaleteroNuevo = new ArrayList<>();
			this.stockNuevoPaletero = null;
			this.modeloNuevoPaletero = "";
			this.referenciaNuevoPaletero = "";
			this.marcaEscogida = "";

		} else {

			this.imagenesPaleteroNuevo = imagenService.getImagenesDelProducto(this.nuevoPaletero.getId());
			this.stockNuevoPaletero = this.nuevoPaletero.getStock();
			this.modeloNuevoPaletero = this.nuevoPaletero.getModelo();
			this.referenciaNuevoPaletero = this.nuevoPaletero.getReferencia();
			this.marcaEscogida = this.nuevoPaletero.getMarca().getNombre();

			Object breadcrumbObject = context.getExternalContext().getSessionMap().get("breadcrumb");
			this.breadcrumb = (String) breadcrumbObject;

		}

		Object marcaDeAnunciosObject = context.getExternalContext().getSessionMap().get("marcaDeAnuncios");

		if (marcaDeAnunciosObject != null) {
			String marcaDeAnuncios = (String) marcaDeAnunciosObject;
			this.marcaEscogida = marcaDeAnuncios;
			this.listaAnunciosPorMarca = anuncioService.getAnunciosPorMarcaPaletero(marcaDeAnuncios);
		} else {
			this.marcaEscogida = "";
		}

		Object verAnunciosTodoObject = context.getExternalContext().getSessionMap().get("verTodoAnuncio");
		if (verAnunciosTodoObject != null) {
			this.listaAnunciosPorMarca = anuncioService.getAnunciosPorTipo("Paletero");
		}

	}

	public Boolean renderMarcaPaletero() {
		return this.marcaEscogida != null && !this.marcaEscogida.equals("");
	}

	public void verPaleterosMarcas(String marca) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("marcaDeAnuncios", marca);

		context.getExternalContext().getSessionMap().remove("verTodoAnuncio");

		FacesContext.getCurrentInstance().getExternalContext().redirect("paleteros.xhtml");
	}

	public void verPaleterosTodos() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("verTodoAnuncio", true);

		context.getExternalContext().getSessionMap().remove("marcaDeAnuncios");

		FacesContext.getCurrentInstance().getExternalContext().redirect("paleteros.xhtml");
	}

	public String guardarPaletero() throws IOException {

		Boolean validacionErronea = false;
		validacionImagenes();
		if (!this.validacionImagenes) {
			FacesMessage facesMsgImagenes = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe añadir al menos una imagen.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgImagenes);
			validacionErronea = true;
		}
		if (this.modeloNuevoPaletero == null || this.modeloNuevoPaletero.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El modelo del paletero no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.referenciaNuevoPaletero == null || this.referenciaNuevoPaletero.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"La referencia del paletero no puede estar vacia.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (this.nuevoPaletero == null && paleteroService.existeReferencia(this.referenciaNuevoPaletero)) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La referencia ya se encuentra en el sistema.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}

		if (this.stockNuevoPaletero == null) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El stock del paletero no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (validacionErronea) {
			return "";
		}

		if (this.validacionImagenes && (this.modeloNuevoPaletero != null && !this.modeloNuevoPaletero.equals(""))) {

			if (this.nuevoPaletero == null) {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				Paletero paletero = paleteroService.create(marca, this.modeloNuevoPaletero, this.stockNuevoPaletero,
						this.referenciaNuevoPaletero);
				Paletero saved = paleteroService.save(paletero);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesPaleteroNuevo) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}
			} else {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				Paletero p = this.nuevoPaletero;

				p.setMarca(marca);
				p.setModelo(this.modeloNuevoPaletero);
				p.setReferencia(this.referenciaNuevoPaletero);
				p.setStock(this.stockNuevoPaletero);
				Paletero saved = paleteroService.save(p);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesPaleteroNuevo) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}

			}
		}
		this.nuevoPaletero = null;

		if (this.breadcrumb.equals("bajoStock")) {
			return "listaProductosBajoStock.xhtml";
		} else if (this.breadcrumb.equals("desactivado")) {
			return "listaProductosDesactivados.xhtml";
		} else {
			return "listaPaleteros.xhtml";
		}
	}

	public List<Imagen> getImagenesDePaletero(Integer paleteroId) {
		List<Imagen> imagenes = imagenService.getImagenesDelProducto(paleteroId);
		if (!CollectionUtils.isEmpty(imagenes)) {
			return imagenes;
		} else {
			return new ArrayList<>();
		}

	}

	public void nuevaImagen() throws IOException, SerialException, SQLException {

		if (this.imagenPaleteroNuevo != null && this.imagenPaleteroNuevo.getSize() != 0) {

			Imagen imagen = imagenService.create(this.imagenPaleteroNuevo.getFileName(),
					this.imagenPaleteroNuevo.getContent());

			Boolean yaEsta = false;
			for (Imagen im : this.imagenesPaleteroNuevo) {
				if (imagen.getContent().equals(new SerialBlob(im.getContent()))) {
					yaEsta = true;
					break;
				}
			}

			if (!yaEsta) {
				Imagen saved = imagenService.save(imagen);
				this.imagenesPaleteroNuevo.add(saved);
			} else {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La imagen ya esta adjuntada.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			}

		}
		this.imagenPaleteroNuevo = null;
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
		if (CollectionUtils.isEmpty(this.imagenesPaleteroNuevo)) {
			this.validacionImagenes = false;
		} else {
			this.validacionImagenes = true;
		}
	}

	public void eliminaImagen(Imagen elem) {

		imagenService.delete(elem);
		this.imagenesPaleteroNuevo.remove(elem);
		validacionImagenes();

	}

	public String eliminarPaletero(Paletero elem) {

		Integer count = anuncioService.countAnunciosDeProducto(elem.getId());

		if (count == 0) {
			List<Imagen> imagenes = imagenService.getImagenesDelProducto(elem.getId());
			for (Imagen i : imagenes) {
				imagenService.delete(i);
			}
			paleteroService.delete(elem);
			this.listaPaleteros = paleteroService.findAll();
			return "";
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"No se puede eliminar mientras exista anuncios con este producto.", ""));
			return null;
		}
	}

	public void verNuevoPaletero() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("paleteroParaEditar");
		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoPaletero.xhtml");

	}

	public void verEditarPaletero(Paletero paletero, String breadcrumb) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("paleteroParaEditar", paletero);

		context.getExternalContext().getSessionMap().put("breadcrumb", breadcrumb);

		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevoPaletero.xhtml");

	}

	public void verListaPaleteros() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaPaleteros.xhtml");
	}

	public List<Anuncio> getListaAnunciosPorMarca() {
		return listaAnunciosPorMarca;
	}

	public void setListaAnunciosPorMarca(List<Anuncio> listaAnunciosPorMarca) {
		this.listaAnunciosPorMarca = listaAnunciosPorMarca;
	}

	public Paletero getNuevoPaletero() {
		return nuevoPaletero;
	}

	public void setNuevoPaletero(Paletero nuevoPaletero) {
		this.nuevoPaletero = nuevoPaletero;
	}

	public UploadedFile getImagenPaleteroNuevo() {
		return imagenPaleteroNuevo;
	}

	public void setImagenPaleteroNuevo(UploadedFile imagenPaleteroNuevo) {
		this.imagenPaleteroNuevo = imagenPaleteroNuevo;
	}

	public List<Paletero> getListaPaleteros() {
		return listaPaleteros;
	}

	public void setListaPaleteros(List<Paletero> listaPaleteros) {
		this.listaPaleteros = listaPaleteros;
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

	public Integer getStockNuevoPaletero() {
		return stockNuevoPaletero;
	}

	public void setStockNuevoPaletero(Integer stockNuevoPaletero) {
		this.stockNuevoPaletero = stockNuevoPaletero;
	}

	public String getModeloNuevoPaletero() {
		return modeloNuevoPaletero;
	}

	public void setModeloNuevoPaletero(String modeloNuevoPaletero) {
		this.modeloNuevoPaletero = modeloNuevoPaletero;
	}

	public List<Imagen> getImagenesPaleteroNuevo() {
		return imagenesPaleteroNuevo;
	}

	public void setImagenesPaleteroNuevo(List<Imagen> imagenesPaleteroNuevo) {
		this.imagenesPaleteroNuevo = imagenesPaleteroNuevo;
	}

	public String getReferenciaNuevoPaletero() {
		return referenciaNuevoPaletero;
	}

	public void setReferenciaNuevoPaletero(String referenciaNuevoPaletero) {
		this.referenciaNuevoPaletero = referenciaNuevoPaletero;
	}

	public String getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(String breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

}
