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
import com.omegapadel.model.Pelota;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.ImagenService;
import com.omegapadel.service.MarcaService;
import com.omegapadel.service.PelotaService;

@Named("pelotaController")
@ViewScoped
public class PelotaController implements Serializable {

	private static final long serialVersionUID = 8919849616921030506L;

	@Inject
	private AnuncioService anuncioService;
	@Inject
	private ImagenService imagenService;
	@Inject
	private PelotaService pelotaService;
	@Inject
	private MarcaService marcaService;

	private List<Pelota> listaPelotasCreadas;

	private List<Anuncio> listaAnunciosPorMarca;
	private Pelota nuevaPelota;
	private UploadedFile imagenPelotaNueva;
	private List<Imagen> imagenesPelotaNueva;
	private String marcaEscogida;
	private Integer stockNuevaPelota;
	private String modeloNuevaPelota;
	private String referenciaNuevaPelota;
	private Integer numeroPackNuevaPelota;
	private Boolean validacionImagenes;
	private Collection<Marca> listaMarcas;

	private String breadcrumb;

	public static final String TEXTO_ERROR_IMAGENES_VACIAS = "Debe existir alguna imagen del producto.";

	@PostConstruct
	public void init() {

		this.imagenPelotaNueva = null;
		this.validacionImagenes = false;
		this.listaMarcas = marcaService.findAll();
		this.listaPelotasCreadas = pelotaService.findAll();

		FacesContext context = FacesContext.getCurrentInstance();
		Object pelotaParaEditar = context.getExternalContext().getSessionMap().get("pelotaParaEditar");

		if (pelotaParaEditar != null && pelotaParaEditar instanceof Pelota) {
			this.nuevaPelota = (Pelota) pelotaParaEditar;
		}

		if (this.nuevaPelota == null) {

			this.imagenesPelotaNueva = new ArrayList<>();
			this.stockNuevaPelota = null;
			this.modeloNuevaPelota = "";
			this.referenciaNuevaPelota = "";
			this.marcaEscogida = "";
			this.numeroPackNuevaPelota = null;

		} else {

			this.imagenesPelotaNueva = imagenService.getImagenesDelProducto(this.nuevaPelota.getId());
			this.stockNuevaPelota = this.nuevaPelota.getStock();
			this.modeloNuevaPelota = this.nuevaPelota.getModelo();
			this.referenciaNuevaPelota = this.nuevaPelota.getReferencia();
			this.marcaEscogida = this.nuevaPelota.getMarca().getNombre();
			this.numeroPackNuevaPelota = this.nuevaPelota.getNumero();

			Object breadcrumbObject = context.getExternalContext().getSessionMap().get("breadcrumb");
			this.breadcrumb = (String) breadcrumbObject;
		}

		Object marcaDeAnunciosObject = context.getExternalContext().getSessionMap().get("marcaDeAnuncios");
		if (marcaDeAnunciosObject != null) {

			String marcaDeAnuncios = (String) marcaDeAnunciosObject;
			this.marcaEscogida = marcaDeAnuncios;
			this.listaAnunciosPorMarca = anuncioService.getAnunciosPorMarcaPelota(marcaDeAnuncios);
		} else {
			this.marcaEscogida = "";
		}

		Object verTodasPelotasObject = context.getExternalContext().getSessionMap().get("verTodasPelotas");
		if (verTodasPelotasObject != null) {

			this.listaAnunciosPorMarca = anuncioService.getAnunciosPorTipo("Pelota");
		}
	}

	public String guardarPelota() throws IOException {

		Boolean validacionErronea = false;
		validacionImagenes();
		if (!this.validacionImagenes) {
			FacesMessage facesMsgImagenes = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debes añadir al menos una imagen.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgImagenes);
			validacionErronea = true;
		}
		if (this.referenciaNuevaPelota == null || this.modeloNuevaPelota.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El modelo de la pelota no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (this.nuevaPelota == null && pelotaService.existeReferencia(this.referenciaNuevaPelota)) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La referencia ya se encuentra en el sistema.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}

		if (this.referenciaNuevaPelota == null || this.referenciaNuevaPelota.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"La referencia de la pelota no puede estar vacia.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.stockNuevaPelota == null) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El stock de la pelota no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.numeroPackNuevaPelota == null) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El numero de pelotas del pack no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (validacionErronea) {
			return "";
		}

		if (this.validacionImagenes && (this.modeloNuevaPelota != null && !this.modeloNuevaPelota.equals(""))) {

			if (this.nuevaPelota == null) {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				Pelota pelota = pelotaService.create(marca, modeloNuevaPelota, this.numeroPackNuevaPelota,
						this.stockNuevaPelota, this.referenciaNuevaPelota);
				Pelota saved = pelotaService.save(pelota);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesPelotaNueva) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}
			} else {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				Pelota p = this.nuevaPelota;
				p.setMarca(marca);
				p.setModelo(this.modeloNuevaPelota);
				p.setReferencia(this.referenciaNuevaPelota);
				p.setNumero(this.numeroPackNuevaPelota);
				p.setStock(this.stockNuevaPelota);

				Pelota saved = pelotaService.save(p);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesPelotaNueva) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}
			}

		}
		this.nuevaPelota = null;

		if (this.breadcrumb.equals("bajoStock")) {
			return "listaProductosBajoStock.xhtml";
		} else if (this.breadcrumb.equals("desactivado")) {
			return "listaProductosDesactivados.xhtml";
		} else {
			return "listaPelotasCreadas.xhtml";
		}
	}

	public List<Imagen> getImagenesDePelota(Integer pelotaId) {
		List<Imagen> imagenes = imagenService.getImagenesDelProducto(pelotaId);
		if (!CollectionUtils.isEmpty(imagenes)) {
			return imagenes;
		} else {
			return new ArrayList<>();
		}

	}

	public Boolean renderMarcaPelota() {
		return this.marcaEscogida != null && !this.marcaEscogida.equals("");
	}

	public void nuevaImagen() throws IOException, SerialException, SQLException {

		if (this.imagenPelotaNueva != null && this.imagenPelotaNueva.getSize() != 0) {

			Imagen imagen = imagenService.create(this.imagenPelotaNueva.getFileName(),
					this.imagenPelotaNueva.getContent());

			Boolean yaEsta = false;
			for (Imagen im : this.imagenesPelotaNueva) {
				if (imagen.getContent().equals(new SerialBlob(im.getContent()))) {
					yaEsta = true;
					break;
				}
			}

			if (!yaEsta) {
				Imagen saved = imagenService.save(imagen);
				this.imagenesPelotaNueva.add(saved);
			} else {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La imagen ya esta adjuntada.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			}

		}
		this.imagenPelotaNueva = null;
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
		if (CollectionUtils.isEmpty(this.imagenesPelotaNueva)) {
			this.validacionImagenes = false;
		} else {
			this.validacionImagenes = true;
		}
	}

	public void eliminaImagen(Imagen elem) {

		imagenService.delete(elem);
		this.imagenesPelotaNueva.remove(elem);
		validacionImagenes();

	}

	public String eliminarPelota(Pelota elem) {

		Integer count = anuncioService.countAnunciosDeProducto(elem.getId());

		if (count == 0) {
			List<Imagen> imagenes = imagenService.getImagenesDelProducto(elem.getId());
			for (Imagen i : imagenes) {
				imagenService.delete(i);
			}
			pelotaService.delete(elem);
			this.listaPelotasCreadas = pelotaService.findAll();
			return "";
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"No se puede eliminar mientras exista anuncios con este producto.", ""));
			return null;
		}
	}

	public void verNuevoPackPelota() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("pelotaParaEditar");
		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevasPackPelotas.xhtml");
	}

	public void verEditarPelota(Pelota pelota, String breadcrumb) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("pelotaParaEditar", pelota);

		context.getExternalContext().getSessionMap().put("breadcrumb", breadcrumb);

		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevasPackPelotas.xhtml");
	}

	public void verListaPelotas() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaPelotasCreadas.xhtml");
	}

	public void verPelotasMarcas(String marca) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("marcaDeAnuncios", marca);

		context.getExternalContext().getSessionMap().remove("verTodasPelotas");

		FacesContext.getCurrentInstance().getExternalContext().redirect("pelotas.xhtml");
	}

	public void verPelotasTodas() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("verTodasPelotas", true);

		context.getExternalContext().getSessionMap().remove("marcaDeAnuncios");

		FacesContext.getCurrentInstance().getExternalContext().redirect("pelotas.xhtml");

	}

	public List<Anuncio> getListaAnunciosPorMarca() {
		return listaAnunciosPorMarca;
	}

	public void setListaAnunciosPorMarca(List<Anuncio> listaAnunciosPorMarca) {
		this.listaAnunciosPorMarca = listaAnunciosPorMarca;
	}

	public Pelota getNuevaPelota() {
		return nuevaPelota;
	}

	public void setNuevaPelota(Pelota nuevaPelota) {
		this.nuevaPelota = nuevaPelota;
	}

	public UploadedFile getImagenPelotaNueva() {
		return imagenPelotaNueva;
	}

	public void setImagenPelotaNueva(UploadedFile imagenPelotaNueva) {
		this.imagenPelotaNueva = imagenPelotaNueva;
	}

	public List<Imagen> getImagenesPelotaNueva() {
		return imagenesPelotaNueva;
	}

	public void setImagenesPelotaNueva(List<Imagen> imagenesPelotaNueva) {
		this.imagenesPelotaNueva = imagenesPelotaNueva;
	}

	public String getMarcaEscogida() {
		return marcaEscogida;
	}

	public void setMarcaEscogida(String marcaEscogida) {
		this.marcaEscogida = marcaEscogida;
	}

	public Integer getStockNuevaPelota() {
		return stockNuevaPelota;
	}

	public void setStockNuevaPelota(Integer stockNuevaPelota) {
		this.stockNuevaPelota = stockNuevaPelota;
	}

	public String getModeloNuevaPelota() {
		return modeloNuevaPelota;
	}

	public void setModeloNuevaPelota(String modeloNuevaPelota) {
		this.modeloNuevaPelota = modeloNuevaPelota;
	}

	public Integer getNumeroPackNuevaPelota() {
		return numeroPackNuevaPelota;
	}

	public void setNumeroPackNuevaPelota(Integer numeroPackNuevaPelota) {
		this.numeroPackNuevaPelota = numeroPackNuevaPelota;
	}

	public Boolean getValidacionImagenes() {
		return validacionImagenes;
	}

	public void setValidacionImagenes(Boolean validacionImagenes) {
		this.validacionImagenes = validacionImagenes;
	}

	public static String getTextoErrorImagenesVacias() {
		return TEXTO_ERROR_IMAGENES_VACIAS;
	}

	public void setListaMarcas(Collection<Marca> listaMarcas) {
		this.listaMarcas = listaMarcas;
	}

	public Collection<Marca> getListaMarcas() {
		return listaMarcas;
	}

	public List<Pelota> getListaPelotasCreadas() {
		return listaPelotasCreadas;
	}

	public void setListaPelotasCreadas(List<Pelota> listaPelotasCreadas) {
		this.listaPelotasCreadas = listaPelotasCreadas;
	}

	public String getReferenciaNuevaPelota() {
		return referenciaNuevaPelota;
	}

	public void setReferenciaNuevaPelota(String referenciaNuevaPelota) {
		this.referenciaNuevaPelota = referenciaNuevaPelota;
	}

	public String getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(String breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

}
