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
import com.omegapadel.model.Ropa;
import com.omegapadel.model.TipoRopa;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.ImagenService;
import com.omegapadel.service.MarcaService;
import com.omegapadel.service.RopaService;
import com.omegapadel.service.TipoRopaService;

@Named("ropaController")
@ViewScoped
public class RopaController implements Serializable {

	private static final long serialVersionUID = -6186738665220092482L;

	@Inject
	private AnuncioService anuncioService;
	@Inject
	private MarcaService marcaService;
	@Inject
	private RopaService ropaService;
	@Inject
	private ImagenService imagenService;
	@Inject
	private TipoRopaService tipoRopaService;

	private List<Anuncio> listaAnunciosPorMarca;
	private Ropa nuevaRopa;
	private UploadedFile imagenRopaNueva;

	private List<Ropa> listaRopa;
	private Collection<Marca> listaMarcas;

	private String marcaEscogida;
	private Integer stockNuevaRopa;
	private String modeloNuevaRopa;
	private String referenciaNuevaRopa;
	private Map<String, Integer> mapaTallaStockNuevaRopa;
	private Set<String> listaKeysTallas;
	private String sexoNuevaRopa;
	private String tipoNuevaRopa;

	private String tallaNuevaRopa;
	private Integer stockNuevaTallaRopa;

	private List<Imagen> imagenesRopaNueva;
	private Boolean validacionImagenes;

	private String breadcrumb;

	public static final String TEXTO_ERROR_IMAGENES_VACIAS = "Debe existir alguna imagen del producto.";

	@PostConstruct
	public void init() {

		this.imagenRopaNueva = null;
		this.validacionImagenes = false;
		this.listaMarcas = marcaService.findAll();
		this.listaRopa = ropaService.findAll();

		FacesContext context = FacesContext.getCurrentInstance();
		Object ropaParaEditar = context.getExternalContext().getSessionMap().get("ropaParaEditar");
		if (ropaParaEditar != null && ropaParaEditar instanceof Ropa) {
			this.nuevaRopa = (Ropa) ropaParaEditar;
		}

		if (this.nuevaRopa == null) {

			this.imagenesRopaNueva = new ArrayList<>();
			this.stockNuevaRopa = null;
			this.modeloNuevaRopa = "";
			this.referenciaNuevaRopa = "";
			this.marcaEscogida = "";
			this.mapaTallaStockNuevaRopa = new HashMap<String, Integer>();
			this.sexoNuevaRopa = "";
			this.tipoNuevaRopa = "";
			this.listaKeysTallas = new HashSet<String>();

			this.tallaNuevaRopa = null;
			this.stockNuevaTallaRopa = null;

		} else {

			this.imagenesRopaNueva = imagenService.getImagenesDelProducto(this.nuevaRopa.getId());
			this.stockNuevaRopa = this.nuevaRopa.getStock();
			this.modeloNuevaRopa = this.nuevaRopa.getModelo();
			this.referenciaNuevaRopa = this.nuevaRopa.getReferencia();
			this.marcaEscogida = this.nuevaRopa.getMarca().getNombre();
			this.mapaTallaStockNuevaRopa = this.nuevaRopa.getMapaTallaStock();
			this.sexoNuevaRopa = this.nuevaRopa.getSexo();
			this.tipoNuevaRopa = this.nuevaRopa.getTipoRopa().getTipoRopa();
			this.listaKeysTallas = this.mapaTallaStockNuevaRopa.keySet();

			Object breadcrumbObject = context.getExternalContext().getSessionMap().get("breadcrumb");
			this.breadcrumb = (String) breadcrumbObject;

			this.tallaNuevaRopa = null;
			this.stockNuevaTallaRopa = null;

		}
		Object marcaDeAnunciosObject = context.getExternalContext().getSessionMap().get("marcaDeAnuncios");

		if (marcaDeAnunciosObject != null) {
			String marcaDeAnuncios = (String) marcaDeAnunciosObject;
			this.marcaEscogida = marcaDeAnuncios;
			this.listaAnunciosPorMarca = anuncioService.getAnunciosPorMarcaRopa(marcaDeAnuncios);
		} else {
			this.marcaEscogida = "";
		}

		Object verAnunciosTodoObject = context.getExternalContext().getSessionMap().get("verTodoRopa");
		if (verAnunciosTodoObject != null) {
			this.listaAnunciosPorMarca = anuncioService.getAnunciosPorTipo("Ropa");
		}
	}

	public void addTallaARopa() {

		if (this.tallaNuevaRopa == null || this.tallaNuevaRopa.equals("") || this.stockNuevaTallaRopa == null
				|| this.stockNuevaTallaRopa.equals(0)) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Introduce datos de talla y stock para añadir la talla de la ropa.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
		} else {
			this.mapaTallaStockNuevaRopa.put(this.tallaNuevaRopa, this.stockNuevaTallaRopa);
		}
	}

	public void eliminarTallaDeRopa(String talla) {
		this.mapaTallaStockNuevaRopa.remove(talla);
	}

	public Integer getStockDeTalla(Ropa ropa, String talla) {

		if (ropa != null && !CollectionUtils.isEmpty(ropa.getMapaTallaStock().keySet())) {
			return ropa.getMapaTallaStock().get(talla);
		} else {
			return 0;
		}
	}

	public Boolean renderMarcaRopa() {
		return this.marcaEscogida != null && !this.marcaEscogida.equals("");
	}

	public void verRopaMarcas(String marca) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("marcaDeAnuncios", marca);

		context.getExternalContext().getSessionMap().remove("verTodoRopa");

		FacesContext.getCurrentInstance().getExternalContext().redirect("ropa.xhtml");
	}

	public void verRopaTodas() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("verTodoRopa", true);

		context.getExternalContext().getSessionMap().remove("marcaDeAnuncios");

		FacesContext.getCurrentInstance().getExternalContext().redirect("ropa.xhtml");
	}

	public String guardarRopa() throws IOException {

		Boolean validacionErronea = false;
		validacionImagenes();
		if (!this.validacionImagenes) {
			FacesMessage facesMsgImagenes = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe añadir al menos una imagen.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgImagenes);
			validacionErronea = true;
		}
		if (this.modeloNuevaRopa == null || this.modeloNuevaRopa.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El modelo de la ropa no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}
		if (this.referenciaNuevaRopa == null || this.referenciaNuevaRopa.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"La referencia de la ropa no puede estar vacia.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		} else {

			if (this.nuevaRopa == null && ropaService.existeReferencia(this.referenciaNuevaRopa)) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La referencia ya se encuentra en el sistema.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				validacionErronea = true;
			}
		}

		if (MapUtils.isEmpty(this.mapaTallaStockNuevaRopa)) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debe añadir alguna talla del producto.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.sexoNuevaRopa == null || this.sexoNuevaRopa.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El sexo de la ropa no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (this.tipoNuevaRopa == null || this.tipoNuevaRopa.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El tipo de ropa no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			validacionErronea = true;
		}

		if (validacionErronea) {
			return "";
		}

		if (this.validacionImagenes && (this.modeloNuevaRopa != null && !this.modeloNuevaRopa.equals(""))) {

			Collection<Integer> iColl = this.mapaTallaStockNuevaRopa.values();
			Integer res = 0;
			for (Integer i : iColl) {
				res = res + i;
			}
			this.stockNuevaRopa = res;

			if (this.nuevaRopa == null) {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				TipoRopa tipo = tipoRopaService.getTipoRopaPorNombre(this.tipoNuevaRopa);
				Ropa ropa = ropaService.create(marca, this.modeloNuevaRopa, this.stockNuevaRopa, this.sexoNuevaRopa,
						tipo, this.mapaTallaStockNuevaRopa, this.referenciaNuevaRopa);
				Ropa saved = ropaService.save(ropa);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesRopaNueva) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}
			} else {
				Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
				TipoRopa tipo = tipoRopaService.getTipoRopaPorNombre(this.tipoNuevaRopa);
				Ropa p = this.nuevaRopa;

				p.setMarca(marca);
				p.setModelo(this.modeloNuevaRopa);
				p.setReferencia(this.referenciaNuevaRopa);
				p.setStock(this.stockNuevaRopa);
				p.setMapaTallaStock(this.mapaTallaStockNuevaRopa);
				p.setSexo(this.sexoNuevaRopa);
				p.setTipoRopa(tipo);
				Ropa saved = ropaService.save(p);

				List<Imagen> imagenesGuardadas = new ArrayList<>();
				for (Imagen im : this.imagenesRopaNueva) {
					im.setProducto(saved);
					Imagen savedI = imagenService.save(im);
					imagenesGuardadas.add(savedI);
				}

			}
		}
		this.nuevaRopa = null;

		if (this.breadcrumb.equals("bajoStock")) {
			return "listaProductosBajoStock.xhtml";
		} else if (this.breadcrumb.equals("desactivado")) {
			return "listaProductosDesactivados.xhtml";
		} else {
			return "listaRopa.xhtml";
		}

	}

	public String parseaTextoSexo(Ropa z) {

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

	public List<TipoRopa> getTipoRopaCreados() {

		return tipoRopaService.findAll();

	}

	public List<Imagen> getImagenesDeRopa(Integer ropaId) {
		List<Imagen> imagenes = imagenService.getImagenesDelProducto(ropaId);
		if (!CollectionUtils.isEmpty(imagenes)) {
			return imagenes;
		} else {
			return new ArrayList<>();
		}

	}

	public void nuevaImagen() throws IOException, SerialException, SQLException {

		if (this.imagenRopaNueva != null && this.imagenRopaNueva.getSize() != 0) {

			Imagen imagen = imagenService.create(this.imagenRopaNueva.getFileName(), this.imagenRopaNueva.getContent());

			Boolean yaEsta = false;
			for (Imagen im : this.imagenesRopaNueva) {
				if (imagen.getContent().equals(new SerialBlob(im.getContent()))) {
					yaEsta = true;
					break;
				}
			}

			if (!yaEsta) {
				Imagen saved = imagenService.save(imagen);
				this.imagenesRopaNueva.add(saved);
			} else {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"La imagen ya esta adjuntada.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
			}

		}
		this.imagenRopaNueva = null;
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
		if (CollectionUtils.isEmpty(this.imagenesRopaNueva)) {
			this.validacionImagenes = false;
		} else {
			this.validacionImagenes = true;
		}
	}

	public void eliminaImagen(Imagen elem) {

		imagenService.delete(elem);
		this.imagenesRopaNueva.remove(elem);
		validacionImagenes();

	}

	public String eliminarRopa(Ropa elem) {

		Integer count = anuncioService.countAnunciosDeProducto(elem.getId());

		if (count == 0) {
			List<Imagen> imagenes = imagenService.getImagenesDelProducto(elem.getId());
			for (Imagen i : imagenes) {
				imagenService.delete(i);
			}
			ropaService.delete(elem);
			this.listaRopa = ropaService.findAll();
			return "";
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
					"No se puede eliminar mientras exista anuncios con este producto.", ""));
			return null;
		}
	}

	public void verNuevaRopa() throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().remove("ropaParaEditar");
		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevaRopa.xhtml");

	}

	public void verEditarRopa(Ropa ropa, String breadcrumb) throws IOException {

		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().getSessionMap().put("ropaParaEditar", ropa);

		context.getExternalContext().getSessionMap().put("breadcrumb", breadcrumb);

		FacesContext.getCurrentInstance().getExternalContext().redirect("nuevaRopa.xhtml");

	}

	public void verListaRopa() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("listaRopa.xhtml");
	}

	public List<Anuncio> getListaAnunciosPorMarca() {
		return listaAnunciosPorMarca;
	}

	public void setListaAnunciosPorMarca(List<Anuncio> listaAnunciosPorMarca) {
		this.listaAnunciosPorMarca = listaAnunciosPorMarca;
	}

	public Ropa getNuevaRopa() {
		return nuevaRopa;
	}

	public void setNuevaRopa(Ropa nuevaRopa) {
		this.nuevaRopa = nuevaRopa;
	}

	public UploadedFile getImagenRopaNueva() {
		return imagenRopaNueva;
	}

	public void setImagenRopaNueva(UploadedFile imagenRopaNueva) {
		this.imagenRopaNueva = imagenRopaNueva;
	}

	public List<Ropa> getListaRopa() {
		return listaRopa;
	}

	public void setListaRopa(List<Ropa> listaRopa) {
		this.listaRopa = listaRopa;
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

	public Integer getStockNuevaRopa() {
		return stockNuevaRopa;
	}

	public void setStockNuevaRopa(Integer stockNuevaRopa) {
		this.stockNuevaRopa = stockNuevaRopa;
	}

	public String getModeloNuevaRopa() {
		return modeloNuevaRopa;
	}

	public void setModeloNuevaRopa(String modeloNuevaRopa) {
		this.modeloNuevaRopa = modeloNuevaRopa;
	}

	public Map<String, Integer> getMapaTallaStockNuevaRopa() {
		return mapaTallaStockNuevaRopa;
	}

	public void setMapaTallaStockNuevaRopa(Map<String, Integer> mapaTallaStockNuevaRopa) {
		this.mapaTallaStockNuevaRopa = mapaTallaStockNuevaRopa;
	}

	public String getSexoNuevaRopa() {
		return sexoNuevaRopa;
	}

	public void setSexoNuevaRopa(String sexoNuevaRopa) {
		this.sexoNuevaRopa = sexoNuevaRopa;
	}

	public List<Imagen> getImagenesRopaNueva() {
		return imagenesRopaNueva;
	}

	public void setImagenesRopaNueva(List<Imagen> imagenesRopaNueva) {
		this.imagenesRopaNueva = imagenesRopaNueva;
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

	public String getTallaNuevaRopa() {
		return tallaNuevaRopa;
	}

	public void setTallaNuevaRopa(String tallaNuevaRopa) {
		this.tallaNuevaRopa = tallaNuevaRopa;
	}

	public Integer getStockNuevaTallaRopa() {
		return stockNuevaTallaRopa;
	}

	public void setStockNuevaTallaRopa(Integer stockNuevaTallaRopa) {
		this.stockNuevaTallaRopa = stockNuevaTallaRopa;
	}

	public String getReferenciaNuevaRopa() {
		return referenciaNuevaRopa;
	}

	public void setReferenciaNuevaRopa(String referenciaNuevaRopa) {
		this.referenciaNuevaRopa = referenciaNuevaRopa;
	}

	public String getTipoNuevaRopa() {
		return tipoNuevaRopa;
	}

	public void setTipoNuevaRopa(String tipoNuevaRopa) {
		this.tipoNuevaRopa = tipoNuevaRopa;
	}

	public String getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(String breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

}
