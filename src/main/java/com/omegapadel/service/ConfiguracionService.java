package com.omegapadel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.omegapadel.model.Configuracion;
import com.omegapadel.repository.ConfiguracionRepository;

@Service
@Transactional
public class ConfiguracionService {

	@Inject
	private ConfiguracionRepository configuracionRepository;

	public <S extends Configuracion> S save(S entity) {
		return configuracionRepository.save(entity);
	}

	public List<Configuracion> findAll() {
		return (List<Configuracion>) configuracionRepository.findAll();
	}

	public Configuracion findConfiguracion() {
		return findAll().get(0);
	}
	
	public Configuracion creaConfiguracionEjemploInicial() {
		
		Configuracion config = new Configuracion();
		config.setHayEnvioGratis(true);
		config.setPrecioaPartirEnvioGratis(50.00);
		config.setPrecioEnvio(5.00);
		
		config.setMostrarBanners(false);
		Map<String, String> mapaBannersConRedireccion = new HashMap<String, String>();
		config.setMapaBannersConRedireccion(mapaBannersConRedireccion);
		
		return config;
	}
	
	public Double getImporteEnvio(Double precioProductos) {

		Configuracion config = findConfiguracion();

		if (config.getHayEnvioGratis() && precioProductos >= config.getPrecioaPartirEnvioGratis()) {
			return 0.0;
		} else {
			return config.getPrecioEnvio();
		}

	}
	
}
