package com.omegapadel.model;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "configuracion")
public class Configuracion extends EntidadBase {

	private static final long serialVersionUID = 4422974669600864829L;

	private Boolean mostrarBanners;

	@ElementCollection
	private Map<String, String> mapaBannersConRedireccion;

	private Double precioEnvio;

	private Boolean hayEnvioGratis;

	private Double precioaPartirEnvioGratis;

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

}
