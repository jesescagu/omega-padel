package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.MapUtils;
import org.primefaces.model.file.UploadedFile;

import com.omegapadel.model.Configuracion;
import com.omegapadel.model.Imagen;
import com.omegapadel.service.ConfiguracionService;
import com.omegapadel.service.ImagenService;

@Named("configuracionController")
@ViewScoped
public class ConfiguracionController implements Serializable {

	private static final long serialVersionUID = -3402576359517974738L;

	@Inject
	private ConfiguracionService configuracionService;
	@Inject
	private ImagenService imagenService;

	private Configuracion configuracionCreada;
	private Double precioEnvio;
	private Boolean hayEnvioGratis;
	private Double precioaPartirEnvioGratis;
	private Integer limiteStockBajo;
	private Boolean mostrarBanners;
	private Map<String, String> mapaBannersConRedireccion;
	private Set<String> listaImagenesBanners;

	private UploadedFile imagenBannerNueva;
	private String urlBanner;

	@PostConstruct
	public void init() {

		this.configuracionCreada = configuracionService.findConfiguracion();

		this.precioEnvio = this.configuracionCreada.getPrecioEnvio();
		this.hayEnvioGratis = this.configuracionCreada.getHayEnvioGratis();
		this.precioaPartirEnvioGratis = this.configuracionCreada.getPrecioaPartirEnvioGratis();
		this.mostrarBanners = this.configuracionCreada.getMostrarBanners();
		this.mapaBannersConRedireccion = this.configuracionCreada.getMapaBannersConRedireccion();
		this.limiteStockBajo = this.configuracionCreada.getLimiteStockBajo();

		this.urlBanner = "";

		populaListaImagenesBanners();

	}

	public void editarConfiguracion() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("editarConfiguracion.xhtml");
	}

	public void guardarEdicionConfiguracion() {

		Boolean esCorrecto = true;

		if (this.precioEnvio == null) {
			this.precioEnvio = 0.0;
		}

		if (this.limiteStockBajo == null) {
			this.limiteStockBajo = 0;
		}

		if (this.hayEnvioGratis == true) {
			if (this.precioaPartirEnvioGratis == null) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir un precio a partir el cual el envio es gratis si marca el check.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				esCorrecto = false;
			}
		}
		if (this.mostrarBanners == true) {
			if (MapUtils.isEmpty(this.mapaBannersConRedireccion)) {
				FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe introducir algun banner si marca el check para mostrarse en la web.", null);
				FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
				esCorrecto = false;
			}
		}

		if (esCorrecto) {
			Configuracion config = this.configuracionCreada;
			config.setHayEnvioGratis(this.hayEnvioGratis);
			config.setMostrarBanners(this.mostrarBanners);
			config.setLimiteStockBajo(this.limiteStockBajo);
			config.setPrecioEnvio(this.precioEnvio);
			
			config.setMapaBannersConRedireccion(this.mapaBannersConRedireccion);
			config.setPrecioaPartirEnvioGratis(this.precioaPartirEnvioGratis);
			
			this.configuracionCreada = configuracionService.save(config);
			
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Se ha modificado la configuración correctamente.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
		}
	}

	public void populaListaImagenesBanners() {

		this.listaImagenesBanners = this.mapaBannersConRedireccion.keySet();

	}

	public void nuevaImagen() {

		try {
			if (this.imagenBannerNueva != null && this.imagenBannerNueva.getSize() != 0) {

				Imagen imagen = imagenService.create(this.imagenBannerNueva.getFileName(),
						this.imagenBannerNueva.getContent());

				Imagen saved = imagenService.save(imagen);

				this.mapaBannersConRedireccion.put(saved.getNombre(), this.urlBanner);
				this.urlBanner = "";

				populaListaImagenesBanners();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void eliminaImagen(String elem) {

		Imagen imagen = imagenService.getImagenPorNombre(elem).get();

		imagenService.delete(imagen);
		this.mapaBannersConRedireccion.remove(elem);

		populaListaImagenesBanners();

	}

	public String getBytesDeImagen(String elemNombre) {
		try {

			Imagen elem = imagenService.getImagenPorNombre(elemNombre).get();

			byte[] photo = elem.getContent().getBytes(1, (int) elem.getContent().length());
			String bphoto = Base64.getEncoder().encodeToString(photo);
			return bphoto;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Configuracion getConfiguracionCreada() {
		return configuracionCreada;
	}

	public void setConfiguracionCreada(Configuracion configuracionCreada) {
		this.configuracionCreada = configuracionCreada;
	}

	public Double getPrecioEnvio() {
		return precioEnvio;
	}

	public void setPrecioEnvio(Double precioEnvio) {
		this.precioEnvio = precioEnvio;
	}

	public Boolean getHayEnvioGratis() {
		return hayEnvioGratis;
	}

	public void setHayEnvioGratis(Boolean hayEnvioGratis) {
		this.hayEnvioGratis = hayEnvioGratis;
	}

	public Double getPrecioaPartirEnvioGratis() {
		return precioaPartirEnvioGratis;
	}

	public void setPrecioaPartirEnvioGratis(Double precioaPartirEnvioGratis) {
		this.precioaPartirEnvioGratis = precioaPartirEnvioGratis;
	}

	public Boolean getMostrarBanners() {
		return mostrarBanners;
	}

	public void setMostrarBanners(Boolean mostrarBanners) {
		this.mostrarBanners = mostrarBanners;
	}

	public Map<String, String> getMapaBannersConRedireccion() {
		return mapaBannersConRedireccion;
	}

	public void setMapaBannersConRedireccion(Map<String, String> mapaBannersConRedireccion) {
		this.mapaBannersConRedireccion = mapaBannersConRedireccion;
	}

	public UploadedFile getImagenBannerNueva() {
		return imagenBannerNueva;
	}

	public void setImagenBannerNueva(UploadedFile imagenBannerNueva) {
		this.imagenBannerNueva = imagenBannerNueva;
	}

	public Set<String> getListaImagenesBanners() {
		return listaImagenesBanners;
	}

	public void setListaImagenesBanners(Set<String> listaImagenesBanners) {
		this.listaImagenesBanners = listaImagenesBanners;
	}

	public String getUrlBanner() {
		return urlBanner;
	}

	public void setUrlBanner(String urlBanner) {
		this.urlBanner = urlBanner;
	}

	public Integer getLimiteStockBajo() {
		return limiteStockBajo;
	}

	public void setLimiteStockBajo(Integer limiteStockBajo) {
		this.limiteStockBajo = limiteStockBajo;
	}

}
