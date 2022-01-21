package com.omegapadel.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.event.FilesUploadEvent;
import org.primefaces.model.file.UploadedFile;

import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Marca;
import com.omegapadel.model.Pala;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.MarcaService;
import com.omegapadel.service.PalaService;

@Named("palaControllerBak")
@ViewScoped
public class PalaControllerBak implements Serializable {

	private static final long serialVersionUID = -6186738665220092482L;

	@Inject
	private AnuncioService anuncioService;

	@Inject
	private MarcaService marcaService;

	@Inject
	private PalaService palaService;

	//TODO Esto guardar en BBDD y que pueda ser modificable
	private static String rutaImagenes = "D:\\Descargas\\omegapadel\\src\\main\\webapp\\resources\\images\\";
	
	private List<Anuncio> listaAnunciosPorMarca;
	private UploadedFile imagenPalaNueva;
	private List<UploadedFile> imagenesPalaNueva;
	private List<String> nombresImagenesPalaNueva;
	private String marcaEscogida;
	private String textoErrorImagenesVacias;
	private Boolean validacionImagenes;
	private String modeloNuevaPala;
	private Integer temporadaEscogida;
	private Integer stockNuevaPala;

	@PostConstruct
	public void init() {
		List<Anuncio> listaAnuncios = this.listaAnunciosPorMarca;
		this.imagenPalaNueva = null;
		this.imagenesPalaNueva = new ArrayList<UploadedFile>();
		this.textoErrorImagenesVacias = "Debe existir alguna imagen del producto.";
		this.validacionImagenes = true;
		this.marcaEscogida = "";
		this.modeloNuevaPala = "";
		this.temporadaEscogida = getTemporadasPosibles().get(0);
		this.stockNuevaPala = null;
		this.nombresImagenesPalaNueva = new ArrayList<String>();
	}

	public void validacionImagenes() {
		if (CollectionUtils.isEmpty(this.imagenesPalaNueva)) {
			this.validacionImagenes = false;
		} else {
			this.validacionImagenes = true;
		}
	}

	public void nuevaImagen() {

		if (this.imagenPalaNueva != null && this.imagenPalaNueva.getSize() != 0) {
			this.imagenesPalaNueva.add(this.imagenPalaNueva);
		}
		this.imagenPalaNueva = null;
		validacionImagenes();

	}

	public void eliminaImagen(UploadedFile elem) {

		this.imagenesPalaNueva.remove(elem);
		this.imagenPalaNueva = null;
		validacionImagenes();

	}

	public String verPalasMarcas(String marca) {
		String nav = "palas";
//		this.listaPalasPorMarca = palaService.getPalasDeMarca(marca);
//		this.listaPalasPorMarca.stream().filter(f -> anuncio.getProductos.contains(f))

		this.listaAnunciosPorMarca = anuncioService.getAnunciosPorMarcaPala(marca);

		return nav;
	}

	public Collection<Marca> getListaMarcas() {
		return marcaService.findAll();
	}

	public String verNuevaPala() {
		String nav = "nuevaPala";
		return nav;
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

	public String guardarPala() {

		// TODO
		validacionImagenes();
		if (!this.validacionImagenes) {
			FacesMessage facesMsgImagenes = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Debes añadir al menos una imagen.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgImagenes);
		}
		if (this.modeloNuevaPala == null || this.modeloNuevaPala.equals("")) {
			FacesMessage facesMsgModelo = new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"El modelo de la pala no puede estar vacio.", null);
			FacesContext.getCurrentInstance().addMessage(null, facesMsgModelo);
		}

		if (this.validacionImagenes && (this.modeloNuevaPala != null && !this.modeloNuevaPala.equals(""))) {
			Marca marca = marcaService.getMarcaPorNombre(this.marcaEscogida);
			Pala pala = palaService.create(marca, modeloNuevaPala, this.stockNuevaPala, this.temporadaEscogida);
			for (UploadedFile uf : this.imagenesPalaNueva) {

				String nombreImagen = guardaImagen(uf);
				this.nombresImagenesPalaNueva.add(nombreImagen);
			}
//			pala.setImagen(this.nombresImagenesPalaNueva);
			palaService.save(pala);
		}
		return "";

	}

//	public void subidaImagen(FileUploadEvent event) {
//
//		this.imagenesPalaNueva.add(event.getFile());
//		
//	}		

	public void guardaImagenes(FilesUploadEvent event) {
		// allowTypes="/(\.|\/)(gif|jpe?g|png)$/"

		try {
			List<UploadedFile> files = event.getFiles().getFiles();

			for (UploadedFile file : files) {
				if (file != null && file.getContent() != null && file.getContent().length > 0
						&& file.getFileName() != null) {
//				this.imagenesPalaNueva = files;

					String contexto = getPath();
					Boolean b = copyFile(file.getFileName(), file.getInputStream(),
							contexto + "src\\main\\webapp\\resource\\images\\");
					if (!b) {
						FacesMessage msg = new FacesMessage("Error", "La imagen no se ha guardado");
						FacesContext.getCurrentInstance().addMessage(null, msg);
					}
				}
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error", "La imagen no se ha guardado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	public String guardaImagen(UploadedFile file) {
		// allowTypes="/(\.|\/)(gif|jpe?g|png)$/"

		try {
			if (file != null && file.getContent() != null && file.getContent().length > 0
					&& file.getFileName() != null) {

				String nombreUnicoGenerado = getNombreImagenUnicoGenerado(file.getFileName());

				String contexto = getPath();
				copyFile(nombreUnicoGenerado, file.getInputStream(),
						contexto + "src\\main\\webapp\\resource\\images\\");

				return file.getFileName();
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Error", "La imagen no se ha guardado");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			e.printStackTrace();
		}
		return null;
	}

	public String getNombreImagenUnicoGenerado(String nombre) {
		String cadenaGenerada = "";
		String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		Integer longitud = 10;
		String resultado = "";

		for (int x = 0; x < longitud; x++) {
			int indiceAleatorio = ThreadLocalRandom.current().nextInt(0, banco.length() - 1);
			char caracterAleatorio = banco.charAt(indiceAleatorio);
			cadenaGenerada += caracterAleatorio;
		}

		resultado = cadenaGenerada + "-" + nombre;
		
		File file = new File(rutaImagenes+resultado);
		if(file.exists()) {
			return getNombreImagenUnicoGenerado(nombre);
		}else {
			return resultado;
		}
		
	}

	public List<Anuncio> getListaAnunciosPorMarca() {
		return listaAnunciosPorMarca;
	}

	public void setListaAnunciosPorMarca(List<Anuncio> listaAnunciosPorMarca) {
		this.listaAnunciosPorMarca = listaAnunciosPorMarca;
	}

	/*
	 * devuelve el path de la aplicacion
	 */
	public static String getPath() {
		try {
			ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			return ctx.getRealPath("/");

		} catch (Exception e) {

//			addErrorMessage("getPath() " + e.getLocalizedMessage());
		}
		return null;

	}
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

			System.out.println(Pala.class.getProtectionDomain().getCodeSource().getLocation());

			File directory = new File(".");
			System.out.println(System.getProperty("user.dir"));

			System.out.println(directory.getAbsolutePath());

			// write the inputStream to a FileOutputStream
			OutputStream out = new FileOutputStream(
					new File(rutaImagenes + fileName));

//			Files.copy(in, target, options)

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
			e.printStackTrace();
		}
		return false;
	}

	public String getMarcaEscogida() {
		return marcaEscogida;
	}

	public void setMarcaEscogida(String marcaEscogida) {
		this.marcaEscogida = marcaEscogida;
	}

	public List<UploadedFile> getImagenesPalaNueva() {
		return imagenesPalaNueva;
	}

	public void setImagenesPalaNueva(List<UploadedFile> imagenesPalaNueva) {
		this.imagenesPalaNueva = imagenesPalaNueva;
	}

	public UploadedFile getImagenPalaNueva() {
		return imagenPalaNueva;
	}

	public void setImagenPalaNueva(UploadedFile imagenPalaNueva) {
		this.imagenPalaNueva = imagenPalaNueva;
	}

	public String getTextoErrorImagenesVacias() {
		return textoErrorImagenesVacias;
	}

	public void setTextoErrorImagenesVacias(String textoErrorImagenesVacias) {
		this.textoErrorImagenesVacias = textoErrorImagenesVacias;
	}

	public Boolean getValidacionImagenes() {
		return validacionImagenes;
	}

	public void setValidacionImagenes(Boolean validacionImagenes) {
		this.validacionImagenes = validacionImagenes;
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

	public Integer getStockNuevaPala() {
		return stockNuevaPala;
	}

	public void setStockNuevaPala(Integer stockNuevaPala) {
		this.stockNuevaPala = stockNuevaPala;
	}

}
