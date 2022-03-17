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
import org.primefaces.model.file.UploadedFile;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Imagen;
import com.omegapadel.model.Marca;
import com.omegapadel.model.Zapatilla;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.ImagenService;
import com.omegapadel.service.MarcaService;
import com.omegapadel.service.ZapatillaService;

@Named("zapatillaController")
@ViewScoped
public class ZapatillaController implements Serializable {

	private static final long serialVersionUID = -6186738665220092482L;

	@Inject
	private AnuncioService anuncioService;
	@Inject
	private MarcaService marcaService;
	@Inject
	private ZapatillaService zapatillaService;
	@Inject
	private ImagenService imagenService;

	private List<Anuncio> listaAnunciosPorMarca;
	private Zapatilla nuevaZapatilla;
	private UploadedFile imagenZapatillaNueva;

	private List<Zapatilla> listaZapatillas;
	private Collection<Marca> listaMarcas;

	private String marcaEscogida;
	private Integer stockNuevaZapatilla;
	private String modeloNuevaZapatilla;
	private Map<String, Integer> mapaTallaStockNuevaZapatilla;
	private Set<String> listaKeysTallas;
	private String sexoNuevaZapatilla;

	private Integer tallaNuevaZapatilla;
	private Integer stockNuevaTallaZapatilla;

	private List<Imagen> imagenesZapatillaNueva;
	private Boolean validacionImagenes;

	public static final String TEXTO_ERROR_IMAGENES_VACIAS = "Debe existir alguna imagen del producto.";

	@PostConstruct
	public void init() {

		this.imagenZapatillaNueva = null;
		this.validacionImagenes = false;
		this.listaMarcas = marcaService.findAll();
		this.listaZapatillas = zapatillaService.findAll();

		FacesContext context = FacesContext.getCurrentInstance();
		Object marcaDeAnunciosObject = context.getExternalContext().getSessionMap().get("marcaDeAnuncios");

		if (marcaDeAnunciosObject != null) {
			String marcaDeAnuncios = (String) marcaDeAnunciosObject;
			this.listaAnunciosPorMarca = anuncioService.getAnunciosPorMarcaZapatilla(marcaDeAnuncios);
		}

		Object zapatillaParaEditar = context.getExternalContext().getSessionMap().get("zapatillaParaEditar");
		if (zapatillaParaEditar != null && zapatillaParaEditar instanceof Zapatilla) {
			this.nuevaZapatilla = (Zapatilla) zapatillaParaEditar;
		}

		if (this.nuevaZapatilla == null) {

			this.imagenesZapatillaNueva = new ArrayList<>();
			this.stockNuevaZapatilla = null;
			this.modeloNuevaZapatilla = "";
			this.marcaEscogida = "";
			this.mapaTallaStockNuevaZapatilla = new HashMap<String, Integer>();
			this.sexoNuevaZapatilla = "";
			this.listaKeysTallas = new HashSet<String>();

			this.tallaNuevaZapatilla = null;
			this.stockNuevaTallaZapatilla = null;

		} else {

			this.imagenesZapatillaNueva = imagenService.getImagenesDelProducto(this.nuevaZapatilla.getId());
			this.stockNuevaZapatilla = this.nuevaZapatilla.getStock();
			this.modeloNuevaZapatilla = this.nuevaZapatilla.getModelo();
			this.marcaEscogida = this.nuevaZapatilla.getMarca().getNombre();
			this.mapaTallaStockNuevaZapatilla = this.nuevaZapatilla.getMapaTallaStock();
			this.sexoNuevaZapatilla = this.nuevaZapatilla.getSexo();
			this.listaKeysTallas = this.mapaTallaStockNuevaZapatilla.keySet();

			this.tallaNuevaZapatilla = null;
			this.stockNuevaTallaZapatilla = null;

		}
	}

	public void addTallaAZapatilla() {

		if (this.tallaNuevaZapatilla == null || this.tallaNuevaZapatilla.equals(0)
				|| this.stockNuevaTallaZapatilla == null || this.stockNuevaTallaZapatilla.equals(0)) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Introduce datos de talla y stock para añadir la talla de la zapatilla.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
		}

		this.mapaTallaStockNuevaZapatilla.put(this.tallaNuevaZapatilla.toString(), this.stockNuevaTallaZapatilla);
	}

	public void eliminarTallaDeZapatilla(Integer talla) {
		this.mapaTallaStockNuevaZapatilla.remove(talla.toString());
	}

	public Integer getStockDeTalla(Zapatilla zapa, Integer talla) {

		if (zapa != null && !CollectionUtils.isEmpty(zapa.getMapaTallaStock().keySet())) {
			return zapa.getMapaTallaStock().get(talla.toString());
		} else {
			return 0;
		}
	}

	public void verZapatillasMarcas(String marca) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("marcaDeAnuncios", marca);

		FacesContext.getCurrentInstance().getExternalContext().redirect("zapatillas.xhtml");
	}

	public String guardarZapatilla() throws IOException {

		Boolean validacionErronea = false;
		validacionImagenes();
		if (!this.validacionImagenes) {
			FacesMessage facesMsgImagenes = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe añadir al menos una imagen.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgImagenes);
			validacionErronea = true;
		}
		if (this.modeloNuevaZapatilla == null || this.modeloNuevaZapatilla.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El modelo de la zapatilla no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.stockNuevaZapatilla == null) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El stock de la zapatilla no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

//		if (this.tallaNuevaZapatilla == null) {
//			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
//					"La talla de la zapatilla no puede estar vacio.", null);
//			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
//			validacionErronea = true;
//		} else {
//			if (this.tallaNuevaZapatilla < 01 || this.tallaNuevaZapatilla > 99) {
//				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
//						"La talla de la zapatilla no es correcta.", null);
//				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
//				validacionErronea = true;
//			}
//		}

		if (this.sexoNuevaZapatilla == null || this.sexoNuevaZapatilla.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El sexo de la zapatilla no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (validacionErronea) {
			return "";
		}

		if (this.validacionImagenes && (this.modeloNuevaZapatilla != null && !this.modeloNuevaZapatilla.equals(""))) {

			if (this.nuevaZapatilla == null) {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				Zapatilla zapatilla = zapatillaService.create(marca, this.modeloNuevaZapatilla,
						this.stockNuevaZapatilla, this.sexoNuevaZapatilla, this.mapaTallaStockNuevaZapatilla);
				Zapatilla saved = zapatillaService.save(zapatilla);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesZapatillaNueva) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}
			} else {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				Zapatilla p = this.nuevaZapatilla;

				p.setMarca(marca);
				p.setModelo(this.modeloNuevaZapatilla);
				p.setStock(this.stockNuevaZapatilla);
//				p.setTalla(this.tallaNuevaZapatilla);
				p.setMapaTallaStock(this.mapaTallaStockNuevaZapatilla);
				p.setSexo(this.sexoNuevaZapatilla);
				Zapatilla saved = zapatillaService.save(p);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesZapatillaNueva) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}

			}
		}
		this.nuevaZapatilla = null;
		return "listaZapatillas.xhtml";

	}

	public String parseaTextoSexo(Zapatilla z) {

		String sexo = z.getSexo();
		switch (sexo) {
		case "HOMBRE":
			return "Hombre";
		case "MUJER":
			return "Mujer";
		case "JUNIOR":
			return "Junior";
		default:
			return "";
		}

	}

	public List<Imagen> getImagenesDeZapatilla(Integer zapatillaId) {
		List<Imagen> imagenes = imagenService.getImagenesDelProducto(zapatillaId);
		if (!CollectionUtils.isEmpty(imagenes)) {
			return imagenes;
		} else {
			return new ArrayList<>();
		}

	}

	public void nuevaImagen() throws IOException, SerialException, SQLException {

		if (this.imagenZapatillaNueva != null && this.imagenZapatillaNueva.getSize() != 0) {

			Imagen imagen = imagenService.create(this.imagenZapatillaNueva.getFileName(),
					this.imagenZapatillaNueva.getContent());

			Boolean yaEsta = false;
			for (Imagen im : this.imagenesZapatillaNueva) {
				if (imagen.getContent().equals(new SerialBlob(im.getContent()))) {
					yaEsta = true;
					break;
				}
			}

			if (!yaEsta) {
				Imagen saved = imagenService.save(imagen);
				this.imagenesZapatillaNueva.add(saved);
			} else {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La imagen ya esta adjuntada.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			}

		}
		this.imagenZapatillaNueva = null;
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
		if (CollectionUtils.isEmpty(this.imagenesZapatillaNueva)) {
			this.validacionImagenes = false;
		} else {
			this.validacionImagenes = true;
		}
	}

	public void eliminaImagen(Imagen elem) {

		imagenService.delete(elem);
		this.imagenesZapatillaNueva.remove(elem);
		validacionImagenes();

	}

	public String eliminarZapatilla(Zapatilla elem) {

		Integer count = anuncioService.countAnunciosDeProducto(elem.getId());

		if (count == 0) {
			List<Imagen> imagenes = imagenService.getImagenesDelProducto(elem.getId());
			for (Imagen i : imagenes) {
				imagenService.delete(i);
			}
			zapatillaService.delete(elem);
			this.listaZapatillas = zapatillaService.findAll();
			return "";
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"No se puede eliminar mientras exista anuncios con este producto.", ""));
			return null;
		}
	}

	public void verNuevaZapatilla() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("zapatillaParaEditar");
		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevaZapatilla.xhtml");

	}

	public void verEditarZapatilla(Zapatilla zapatilla) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("zapatillaParaEditar", zapatilla);

		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevaZapatilla.xhtml");

	}

	public void verListaZapatillas() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaZapatillas.xhtml");
	}

	public List<Anuncio> getListaAnunciosPorMarca() {
		return listaAnunciosPorMarca;
	}

	public void setListaAnunciosPorMarca(List<Anuncio> listaAnunciosPorMarca) {
		this.listaAnunciosPorMarca = listaAnunciosPorMarca;
	}

	public Zapatilla getNuevaZapatilla() {
		return nuevaZapatilla;
	}

	public void setNuevaZapatilla(Zapatilla nuevaZapatilla) {
		this.nuevaZapatilla = nuevaZapatilla;
	}

	public UploadedFile getImagenZapatillaNueva() {
		return imagenZapatillaNueva;
	}

	public void setImagenZapatillaNueva(UploadedFile imagenZapatillaNueva) {
		this.imagenZapatillaNueva = imagenZapatillaNueva;
	}

	public List<Zapatilla> getListaZapatillas() {
		return listaZapatillas;
	}

	public void setListaZapatillas(List<Zapatilla> listaZapatillas) {
		this.listaZapatillas = listaZapatillas;
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

	public Integer getStockNuevaZapatilla() {
		return stockNuevaZapatilla;
	}

	public void setStockNuevaZapatilla(Integer stockNuevaZapatilla) {
		this.stockNuevaZapatilla = stockNuevaZapatilla;
	}

	public String getModeloNuevaZapatilla() {
		return modeloNuevaZapatilla;
	}

	public void setModeloNuevaZapatilla(String modeloNuevaZapatilla) {
		this.modeloNuevaZapatilla = modeloNuevaZapatilla;
	}

	public Map<String, Integer> getMapaTallaStockNuevaZapatilla() {
		return mapaTallaStockNuevaZapatilla;
	}

	public void setMapaTallaStockNuevaZapatilla(Map<String, Integer> mapaTallaStockNuevaZapatilla) {
		this.mapaTallaStockNuevaZapatilla = mapaTallaStockNuevaZapatilla;
	}

	public String getSexoNuevaZapatilla() {
		return sexoNuevaZapatilla;
	}

	public void setSexoNuevaZapatilla(String sexoNuevaZapatilla) {
		this.sexoNuevaZapatilla = sexoNuevaZapatilla;
	}

	public List<Imagen> getImagenesZapatillaNueva() {
		return imagenesZapatillaNueva;
	}

	public void setImagenesZapatillaNueva(List<Imagen> imagenesZapatillaNueva) {
		this.imagenesZapatillaNueva = imagenesZapatillaNueva;
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

	public Integer getTallaNuevaZapatilla() {
		return tallaNuevaZapatilla;
	}

	public void setTallaNuevaZapatilla(Integer tallaNuevaZapatilla) {
		this.tallaNuevaZapatilla = tallaNuevaZapatilla;
	}

	public Integer getStockNuevaTallaZapatilla() {
		return stockNuevaTallaZapatilla;
	}

	public void setStockNuevaTallaZapatilla(Integer stockNuevaTallaZapatilla) {
		this.stockNuevaTallaZapatilla = stockNuevaTallaZapatilla;
	}

}
