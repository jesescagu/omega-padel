package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.time.LocalDate;
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
import com.omegapadel.model.Pala;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.ImagenService;
import com.omegapadel.service.MarcaService;
import com.omegapadel.service.PalaService;

@Named("palaController")
@ViewScoped
public class PalaController implements Serializable {

	private static final long serialVersionUID = 8919849616921030506L;

	@Inject
	private AnuncioService anuncioService;
	@Inject
	private ImagenService imagenService;
	@Inject
	private PalaService palaService;
	@Inject
	private MarcaService marcaService;

	private List<Pala> listaPalasCreadas;

	private List<Anuncio> listaAnunciosPorMarca;
	private Pala nuevaPala;
	private UploadedFile imagenPalaNueva;
	private List<Imagen> imagenesPalaNueva;
	private String marcaEscogida;
	private Integer stockNuevaPala;
	private String modeloNuevaPala;
	private Integer temporadaEscogida;
	private Boolean validacionImagenes;
	private Collection<Marca> listaMarcas;

	public static final String TEXTO_ERROR_IMAGENES_VACIAS = "Debe existir alguna imagen del producto.";

	@PostConstruct
	public void init() {

		this.imagenPalaNueva = null;
		this.validacionImagenes = false;
		this.listaMarcas = marcaService.findAll();
		this.listaPalasCreadas = palaService.findAll();

		FacesContext context = FacesContext.getCurrentInstance();
		Object palaParaEditar = context.getExternalContext().getSessionMap().get("palaParaEditar");

		if (palaParaEditar != null && palaParaEditar instanceof Pala) {
			this.nuevaPala = (Pala) palaParaEditar;
		}

		if (this.nuevaPala == null) {

			this.imagenesPalaNueva = new ArrayList<>();
			this.stockNuevaPala = null;
			this.modeloNuevaPala = "";
			this.marcaEscogida = "";
			this.temporadaEscogida = null;

		} else {

			this.imagenesPalaNueva = imagenService.getImagenesDelProducto(this.nuevaPala.getId());
			this.stockNuevaPala = this.nuevaPala.getStock();
			this.modeloNuevaPala = this.nuevaPala.getModelo();
			this.marcaEscogida = this.nuevaPala.getMarca().getNombre();
			this.temporadaEscogida = this.nuevaPala.getTemporada();

		}

		Object marcaDeAnunciosObject = context.getExternalContext().getSessionMap().get("marcaDeAnuncios");
		if (marcaDeAnunciosObject != null) {

			String marcaDeAnuncios = (String) marcaDeAnunciosObject;
			this.listaAnunciosPorMarca = anuncioService.getAnunciosPorMarcaPala(marcaDeAnuncios);
		}
	}

	public List<Integer> getTemporadasPosibles() {
		List<Integer> listaAnyos = new ArrayList<Integer>();

		LocalDate d = LocalDate.now();
		Integer anyo = d.getYear();

		listaAnyos.add(anyo + 1);
		listaAnyos.add(anyo);
		listaAnyos.add(anyo - 1);
		listaAnyos.add(anyo - 2);
		listaAnyos.add(anyo - 3);
		listaAnyos.add(anyo - 4);
		listaAnyos.add(anyo - 5);
		listaAnyos.add(anyo - 6);
		listaAnyos.add(anyo - 7);
		listaAnyos.add(anyo - 8);
		listaAnyos.add(anyo - 9);
		listaAnyos.add(anyo - 10);

		return listaAnyos;
	}

	public String guardarPala() throws IOException {

		Boolean validacionErronea = false;
		validacionImagenes();
		if (!this.validacionImagenes) {
			FacesMessage facesMsgImagenes = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe añadir al menos una imagen.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgImagenes);
			validacionErronea = true;
		}
		if (this.modeloNuevaPala == null || this.modeloNuevaPala.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El modelo de la pala no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.stockNuevaPala == null) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El stock de la pala no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.temporadaEscogida == null) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe marcar una temporada no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (validacionErronea) {
			return "";
		}

		if (this.validacionImagenes && (this.modeloNuevaPala != null && !this.modeloNuevaPala.equals(""))) {

			if (this.nuevaPala == null) {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				Pala pala = palaService.create(marca, modeloNuevaPala, this.stockNuevaPala, this.temporadaEscogida);
				Pala saved = palaService.save(pala);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesPalaNueva) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}
			} else {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				Pala p = this.nuevaPala;

				p.setMarca(marca);
				p.setModelo(this.modeloNuevaPala);
				p.setStock(this.stockNuevaPala);
				p.setTemporada(this.temporadaEscogida);
				Pala saved = palaService.save(p);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesPalaNueva) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}

			}
		}
		this.nuevaPala = null;
		return "listaPalasCreadas";

	}

	public List<Imagen> getImagenesDePala(Integer palaId) {
		List<Imagen> imagenes = imagenService.getImagenesDelProducto(palaId);
		if (!CollectionUtils.isEmpty(imagenes)) {
			return imagenes;
		} else {
			return new ArrayList<>();
		}

	}

	public void nuevaImagen() throws IOException, SerialException, SQLException {

		if (this.imagenPalaNueva != null && this.imagenPalaNueva.getSize() != 0) {

			Imagen imagen = imagenService.create(this.imagenPalaNueva.getFileName(), this.imagenPalaNueva.getContent());

			Boolean yaEsta = false;
			for (Imagen im : this.imagenesPalaNueva) {
				if (imagen.getContent().equals(new SerialBlob(im.getContent()))) {
					yaEsta = true;
					break;
				}
			}

			if (!yaEsta) {
				Imagen saved = imagenService.save(imagen);
				this.imagenesPalaNueva.add(saved);
			} else {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La imagen ya esta adjuntada.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			}

		}
		this.imagenPalaNueva = null;
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
		if (CollectionUtils.isEmpty(this.imagenesPalaNueva)) {
			this.validacionImagenes = false;
		} else {
			this.validacionImagenes = true;
		}
	}

	public void eliminaImagen(Imagen elem) {

		imagenService.delete(elem);
		this.imagenesPalaNueva.remove(elem);
		validacionImagenes();

	}

	public String eliminarPala(Pala elem) {

		Integer count = anuncioService.countAnunciosDeProducto(elem.getId());

		if (count == 0) {
			List<Imagen> imagenes = imagenService.getImagenesDelProducto(elem.getId());
			for (Imagen i : imagenes) {
				imagenService.delete(i);
			}
			palaService.delete(elem);
			this.listaPalasCreadas = palaService.findAll();
			return "";
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "No se puede eliminar mientras exista anuncios con este producto.", ""));
			return null;
		}
	}

	public void verNuevaPala() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("palaParaEditar");
		
		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevaPala.xhtml");

	}

	public void verEditarPala(Pala pala) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("palaParaEditar", pala);

		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevaPala.xhtml");

	}

	public void verListaPalas() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaPalasCreadas.xhtml");

	}

	public void verPalasMarcas(String marca) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("marcaDeAnuncios", marca);

		FacesContext.getCurrentInstance().getExternalContext().redirect("palas.xhtml");

	}

	public List<Anuncio> getListaAnunciosPorMarca() {
		return listaAnunciosPorMarca;
	}

	public void setListaAnunciosPorMarca(List<Anuncio> listaAnunciosPorMarca) {
		this.listaAnunciosPorMarca = listaAnunciosPorMarca;
	}

	public Pala getNuevaPala() {
		return nuevaPala;
	}

	public void setNuevaPala(Pala nuevaPala) {
		this.nuevaPala = nuevaPala;
	}

	public UploadedFile getImagenPalaNueva() {
		return imagenPalaNueva;
	}

	public void setImagenPalaNueva(UploadedFile imagenPalaNueva) {
		this.imagenPalaNueva = imagenPalaNueva;
	}

	public List<Imagen> getImagenesPalaNueva() {
		return imagenesPalaNueva;
	}

	public void setImagenesPalaNueva(List<Imagen> imagenesPalaNueva) {
		this.imagenesPalaNueva = imagenesPalaNueva;
	}

	public String getMarcaEscogida() {
		return marcaEscogida;
	}

	public void setMarcaEscogida(String marcaEscogida) {
		this.marcaEscogida = marcaEscogida;
	}

	public Integer getStockNuevaPala() {
		return stockNuevaPala;
	}

	public void setStockNuevaPala(Integer stockNuevaPala) {
		this.stockNuevaPala = stockNuevaPala;
	}

	public String getModeloNuevaPala() {
		return modeloNuevaPala;
	}

	public void setModeloNuevaPala(String modeloNuevaPala) {
		this.modeloNuevaPala = modeloNuevaPala;
	}

	public Integer getTemporadaEscogida() {
		return temporadaEscogida;
	}

	public void setTemporadaEscogida(Integer temporadaEscogida) {
		this.temporadaEscogida = temporadaEscogida;
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

	public List<Pala> getListaPalasCreadas() {
		return listaPalasCreadas;
	}

	public void setListaPalasCreadas(List<Pala> listaPalasCreadas) {
		this.listaPalasCreadas = listaPalasCreadas;
	}

}
