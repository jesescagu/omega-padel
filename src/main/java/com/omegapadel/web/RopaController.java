package com.omegapadel.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Ropa;
import com.omegapadel.service.AnuncioService;

@Named("ropaController")
@SessionScoped
public class RopaController implements Serializable {

	private static final long serialVersionUID = -6186738665220092482L;

	@Inject
	private AnuncioService anuncioService;

	private List<Anuncio> listaAnunciosPorTipo;
	private Ropa nuevaRopa;
	private UploadedFile imagenRopaNueva;

	@PostConstruct
	public void init() {
		List<Anuncio> listaAnuncios = this.listaAnunciosPorTipo;
	}

	public void verRopaDelTipo(String tipo) throws IOException {

		this.listaAnunciosPorTipo = anuncioService.getAnunciosPorTipoRopa(tipo);
		FacesContext.getCurrentInstance().getExternalContext().redirect("ropa.xhtml");
	}

	public void subidaImagen(FileUploadEvent event) {

		try {
			this.imagenRopaNueva = null;
			UploadedFile file = event.getFile();
			if (file != null && file.getContent() != null && file.getContent().length > 0
					&& file.getFileName() != null) {
				this.imagenRopaNueva = file;

				Boolean b =copyFile(file.getFileName(), file.getInputStream(), "/resource/images/");
				if(!b) {
					FacesMessage msg = new FacesMessage("Error", "La imagen no se ha guardado");
					FacesContext.getCurrentInstance().addMessage(null, msg);
				}
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error", "La imagen no se ha guardado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public List<Anuncio> getListaAnunciosPorMarca() {
		return listaAnunciosPorTipo;
	}

	public void setListaAnunciosPorMarca(List<Anuncio> listaAnunciosPorMarca) {
		this.listaAnunciosPorTipo = listaAnunciosPorMarca;
	}

	public Ropa getNuevaRopa() {
		return nuevaRopa;
	}

	public void setNuevaRopa(Ropa nuevaRopa) {
		this.nuevaRopa = nuevaRopa;
	}

//	/*
//	 * devuelve el path de la aplicacion
//	 */
//	public static String getPath() {
//		try {
//			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//			return ctx.getRealPath("/");
//
//		} catch (Exception e) {
//
//			addErrorMessage("getPath() " + e.getLocalizedMessage());
//		}
//		return null;
//
//	}
//
//	/*
//	 * devuelve un hashmap con la ruta de fotos clinicas y el url para las imagenes
//	 */
//	public static HashMap<String, String> getMapPathFotosClinica() {
//		try {
//			HashMap<String, String> map = new HashMap<String, String>();
//
//			String path = getPath() + "resources/fotos/clinicas/";
//			map.put("path", path);
//			map.put("url", "/resources/fotos/clinicas/");
//			return map;
//		} catch (Exception e) {
//
//			addErrorMessage(" getMapPathFotosClinica() " + e.getLocalizedMessage());
//		}
//		return null;
//
//	}
//
//	/*
//	 * devuelve un hashmap con la ruta de fotos clinicas y el url para las imagenes
//	 */
//	public static String getPathFotosClinica() {
//		try {
//
//			String path = getPath() + "resources/fotos/clinicas/";
//			return path;
//		} catch (Exception e) {
//
//			addErrorMessage("getPathFotosClinica() " + e.getLocalizedMessage());
//		}
//		return null;
//
//	}

	/*
	 * copia un archivo generalmente cuando se usa el fileupload fileName: nombre
	 * del archivo a copiar in: Es el InputStream destination: ruta donde se
	 * guardara el archivo
	 * 
	 */
	public static Boolean copyFile(String fileName, InputStream in, String destination) {
		try {

			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(new File(destination + fileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			return true;
		} catch (IOException e) {
		}
		return false;
	}

}
