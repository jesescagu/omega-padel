package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.collections4.CollectionUtils;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.omegapadel.model.Administrador;
import com.omegapadel.model.Anuncio;
import com.omegapadel.model.Cliente;
import com.omegapadel.model.Configuracion;
import com.omegapadel.model.DireccionPostal;
import com.omegapadel.model.Empleado;
import com.omegapadel.model.Imagen;
import com.omegapadel.model.Marca;
import com.omegapadel.model.Municipio;
import com.omegapadel.model.PuestoTrabajo;
import com.omegapadel.model.Rol;
import com.omegapadel.model.TipoAccesorio;
import com.omegapadel.model.TipoRopa;
import com.omegapadel.model.Usuario;
import com.omegapadel.service.AccesorioService;
import com.omegapadel.service.AdministratorService;
import com.omegapadel.service.AnuncioService;
import com.omegapadel.service.CestaService;
import com.omegapadel.service.ClienteService;
import com.omegapadel.service.ComentarioService;
import com.omegapadel.service.ConfiguracionService;
import com.omegapadel.service.DireccionPostalService;
import com.omegapadel.service.EmpleadoService;
import com.omegapadel.service.ImagenService;
import com.omegapadel.service.MarcaService;
import com.omegapadel.service.MunicipioService;
import com.omegapadel.service.PalaService;
import com.omegapadel.service.PaleteroService;
import com.omegapadel.service.PedidoService;
import com.omegapadel.service.PelotaService;
import com.omegapadel.service.ProductoService;
import com.omegapadel.service.PuestoTrabajoService;
import com.omegapadel.service.RolService;
import com.omegapadel.service.RopaService;
import com.omegapadel.service.TipoAccesorioService;
import com.omegapadel.service.TipoRopaService;
import com.omegapadel.service.UsuarioService;
import com.omegapadel.service.ZapatillaService;

@Named("templateController")
@ViewScoped
public class TemplateController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BCryptPasswordEncoder encoderService;

	@Inject
	private MarcaService marcaService;

	@Inject
	private TipoRopaService tipoRopaService;

	@Inject
	private AnuncioService anuncioService;

	@Inject
	private MunicipioService municipioService;

	@Inject
	private UsuarioService usuarioService;

	@Inject
	private PaleteroService paleteroService;

	@Inject
	private DireccionPostalService direccionPostalService;

	@Inject
	private RopaService ropaService;

	@Inject
	private ZapatillaService zapatillaService;

	@Inject
	private AccesorioService accesorioService;

	@Inject
	private AdministratorService administratorService;

	@Inject
	private CestaService cestaService;

	@Inject
	private ConfiguracionService configuracionService;

	@Inject
	private ClienteService clienteService;

	@Inject
	private ComentarioService comentarioService;

	@Inject
	private EmpleadoService empleadoService;

	@Inject
	private PalaService palaService;

	@Inject
	private PedidoService pedidoService;

	@Inject
	private PelotaService pelotaService;

	@Inject
	private ProductoService productoService;

	@Inject
	private PuestoTrabajoService puestoTrabajoService;

	@Inject
	private RolService rolService;

	@Inject
	private TipoAccesorioService tipoAccesorioService;

	@Inject
	private ImagenService imagenService;
	
	@Inject
	private AnuncioController anuncioController;

	private MenuModel model;
	public List<String> listaMarcasPalas = new ArrayList<>();
	public List<String> listaMarcasPaleteros = new ArrayList<>();
	public List<String> listaMarcasRopa = new ArrayList<>();
	public List<String> listaMarcasZapatillas = new ArrayList<>();
	public List<String> listaMarcasPelotas = new ArrayList<>();
	public List<String> listaMarcasPacks = new ArrayList<>();
	public List<Marca> listaMarcasTodas = new ArrayList<>();

	public List<TipoAccesorio> listaTiposAccesorios = new ArrayList<>();
	public List<TipoRopa> listaTiposRopa = new ArrayList<>();

	private Boolean mostrarBanners;
	private Map<String, String> mapaBannersConRedireccion;
	private Set<String> listaImagenesBanners;

	public void abrirLogin() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
	}

	private void marcasPalas() {
		if (CollectionUtils.isEmpty(listaMarcasPalas)) {
			this.listaMarcasPalas = marcaService.getTodasMarcasPalas();
		}
	}

	private void marcasTodas() {
		if (CollectionUtils.isEmpty(listaMarcasTodas)) {
			this.listaMarcasTodas = marcaService.findAll();
		}
	}

	private void marcasPacks() {
		if (CollectionUtils.isEmpty(listaMarcasPacks)) {
			this.listaMarcasPacks = marcaService.getTodasMarcasPacks();
		}
	}

	private void marcasPaleteros() {
		if (CollectionUtils.isEmpty(listaMarcasPaleteros)) {
			this.listaMarcasPaleteros = marcaService.getTodasMarcasPaletero();
		}
	}

	private void marcasRopa() {
		if (CollectionUtils.isEmpty(listaMarcasRopa)) {
			this.listaMarcasRopa = marcaService.getTodasMarcasRopa();
		}
	}

	private void marcasZapatillas() {
		if (CollectionUtils.isEmpty(listaMarcasZapatillas)) {
			this.listaMarcasZapatillas = marcaService.getTodasMarcasZapatillas();
		}
	}

	private void marcasPelotas() {
		if (CollectionUtils.isEmpty(listaMarcasPelotas)) {
			this.listaMarcasPelotas = marcaService.getTodasMarcasPelotas();
		}
	}

	private void listaTiposAccesorios() {
		if (CollectionUtils.isEmpty(listaTiposAccesorios)) {
			this.listaTiposAccesorios = tipoAccesorioService.findAll();
		}
	}

	private void cargaDatos() {

		String passAdmin1 = encoderService.encode("admin1");
		String passCliente1 = encoderService.encode("cliente1");
		String passCliente2 = encoderService.encode("cliente2");
		String passEmpleado1 = encoderService.encode("empleado1");
		String passEmpleado2 = encoderService.encode("empleado2");

		Usuario u1 = usuarioService.create("admin1", passAdmin1);
		Usuario u2 = usuarioService.create("cliente1", passCliente1);
		Usuario u3 = usuarioService.create("cliente2", passCliente2);
		Usuario u4 = usuarioService.create("empleado1", passEmpleado1);
		Usuario u5 = usuarioService.create("empleado2", passEmpleado2);

		Rol r1 = rolService.create("admin1", "admin");
		rolService.saveRol(r1);
		Rol r2 = rolService.create("cliente1", "cliente");
		rolService.saveRol(r2);
		Rol r3 = rolService.create("cliente2", "cliente");
		rolService.saveRol(r3);
		Rol r4 = rolService.create("empleado1", "empleado");
		rolService.saveRol(r4);
		Rol r5 = rolService.create("empleado2", "empleado");
		rolService.saveRol(r5);

		Configuracion config = configuracionService.creaConfiguracionEjemploInicial();
		configuracionService.save(config);

		Administrador a1 = administratorService.create(u1, null);
		administratorService.save(a1);

		Cliente cl1 = clienteService.create("Pepe", "Amador Jerez", "paj@google.es", new ArrayList<DireccionPostal>(),
				u2, new ArrayList<Anuncio>());
		Cliente cli1Saved = clienteService.save(cl1);
		Cliente cl2 = clienteService.create("Juan", "Perez Lopez", "jpl@google.es", new ArrayList<DireccionPostal>(),
				u3, new ArrayList<Anuncio>());
		Cliente cli2Saved = clienteService.save(cl2);

		PuestoTrabajo pt = puestoTrabajoService.create("trabajador");
		puestoTrabajoService.save(pt);

		PuestoTrabajo pt1 = puestoTrabajoService.create("ventas");
		puestoTrabajoService.save(pt1);

		PuestoTrabajo pt2 = puestoTrabajoService.create("repartidor");
		puestoTrabajoService.save(pt2);

		Empleado e1 = empleadoService.create("11111111C", "Paco", "Soria Aguilar", "psa@google.es", "954332233",
				new ArrayList<DireccionPostal>(), u4, pt);
		Empleado emp1Saved = empleadoService.save(e1);
		Empleado e2 = empleadoService.create("11111111D", "Luis", "Lopez Escobar", "lle@google.es", "675454545",
				new ArrayList<DireccionPostal>(), u5, pt);
		Empleado emp2Saved = empleadoService.save(e2);

		Marca marca1 = marcaService.create("Head");
		marcaService.save(marca1);
		Marca marca2 = marcaService.create("Adidas");
		marcaService.save(marca2);
		Marca marca3 = marcaService.create("BullPadel");
		marcaService.save(marca3);
		Marca marca4 = marcaService.create("Asics");
		marcaService.save(marca4);
		Marca marca5 = marcaService.create("Nox");
		marcaService.save(marca5);
		Marca marca6 = marcaService.create("Siux");
		marcaService.save(marca6);
		Marca marca7 = marcaService.create("Nike");
		marcaService.save(marca7);

//		Pala pa1 = palaService.create(marca1, "alpha", 1000, 2020);
//		palaService.save(pa1);
//		Pala pa2 = palaService.create(marca2, "k90", 99, 2021);
//		palaService.save(pa2);
//		Pala pa3 = palaService.create(marca3, "shot", 3, 2019);
//		palaService.save(pa3);
//		Pala pa4 = palaService.create(marca4, "run", 500, 2021);
//		palaService.save(pa4);
//		Pala pa5 = palaService.create(marca5, "silver", 50, 2020);
//		palaService.save(pa5);

//		Paletero pale1 = paleteroService.create(marca1, "team padel", 1000);
//		paleteroService.save(pale1);
//		Paletero pale2 = paleteroService.create(marca2, "RH team", 99);
//		paleteroService.save(pale2);
//		Paletero pale3 = paleteroService.create(marca3, "perf lite", 3);
//		paleteroService.save(pale3);
//		Paletero pale4 = paleteroService.create(marca4, "maleta", 500);
//		paleteroService.save(pale4);
//		Paletero pale5 = paleteroService.create(marca5, "tm padel", 50);
//		paleteroService.save(pale5);

//		Map<Integer, Integer> mapStockTalla1 = new HashMap<Integer, Integer>();
//		mapStockTalla1.put(36, 800);
//		mapStockTalla1.put(37, 900);
//		mapStockTalla1.put(38, 900);
//		mapStockTalla1.put(39, 1000);
//		mapStockTalla1.put(40, 1000);
//		mapStockTalla1.put(41, 1000);
//		mapStockTalla1.put(42, 1000);
//		mapStockTalla1.put(43, 1000);
//		mapStockTalla1.put(44, 1000);
//		mapStockTalla1.put(45, 900);

//		Zapatilla za1 = zapatillaService.create(marca1, "sprint ", 1000, "HOMBRE", mapStockTalla1);
//		zapatillaService.save(za1);
//		Zapatilla za2 = zapatillaService.create(marca2, "extreme sprint", 99, "MUJER", mapStockTalla1);
//		zapatillaService.save(za2);
//		Zapatilla za3 = zapatillaService.create(marca3, "grip", 3, "JUNIOR", mapStockTalla1);
//		zapatillaService.save(za3);
//		Zapatilla za4 = zapatillaService.create(marca4, "revolt", 500, "HOMBRE", mapStockTalla1);
//		zapatillaService.save(za4);
//		Zapatilla za5 = zapatillaService.create(marca5, "brazer", 50, "MUJER", mapStockTalla1);
//		zapatillaService.save(za5);

		TipoRopa tp1 = tipoRopaService.create("Camisetas", "LETRAS");
		TipoRopa tp1Saved = tipoRopaService.save(tp1);
		TipoRopa tp2 = tipoRopaService.create("Calzonas", "LETRAS");
		TipoRopa tp2Saved = tipoRopaService.save(tp2);
		TipoRopa tp3 = tipoRopaService.create("Calcetines", "NUMERICO");
		tipoRopaService.save(tp3);
		TipoRopa tp4 = tipoRopaService.create("Gorras", "UNICA");
		tipoRopaService.save(tp4);
		TipoRopa tp5 = tipoRopaService.create("Chandales", "LETRAS");
		tipoRopaService.save(tp5);

//		Ropa ro1 = ropaService.create(marca1, "Camiseta Sanyo", 1000, "M", "HOMBRE", tp1Saved);
//		ropaService.save(ro1);
//		Ropa ro2 = ropaService.create(marca2, "Calzonas k90", 99, "L", "HOMBRE", tp2Saved);
//		ropaService.save(ro2);

//		Pelota pe1 = pelotaService.create(marca1, "normal", 3, 2000);
//		pelotaService.save(pe1);
//		Pelota pe2 = pelotaService.create(marca6, "power", 3, 53);
//		pelotaService.save(pe2);
//		Pelota pe3 = pelotaService.create(marca5, "control", 9, 100);
//		pelotaService.save(pe3);

//		TipoAccesorio ta1 = tipoAccesorioService.create("overgrip");
//		tipoAccesorioService.save(ta1);
//		TipoAccesorio ta2 = tipoAccesorioService.create("muñequera");
//		tipoAccesorioService.save(ta2);
//		TipoAccesorio ta3 = tipoAccesorioService.create("calcetines");
//		tipoAccesorioService.save(ta3);
//
//		Accesorio acc1 = accesorioService.create(ta1, marca1, "sanyo", 150);
//		accesorioService.save(acc1);
//		Accesorio acc2 = accesorioService.create(ta3, marca7, "cortez", 30);
//		accesorioService.save(acc2);
//		Accesorio acc3 = accesorioService.create(ta1, marca2, "leo", 69);
//		accesorioService.save(acc3);
//		Accesorio acc4 = accesorioService.create(ta2, marca3, "adipower", 73);
//		accesorioService.save(acc4);

//		List<Producto> prods1 = new ArrayList<>();
//		prods1.add(pa1);
//		prods1.add(pale1);
////		prods1.add(za1);
////		prods1.add(ro1);
//		prods1.add(acc1);
////		Anuncio an1 = anuncioService.create("Productos head", "varios productos head", 100.0, e1, prods1);
////		Anuncio an1Saved = anuncioService.save(an1);
//
//		List<Producto> prods2 = new ArrayList<>();
//		prods2.add(pa2);
//		prods2.add(pe1);
//		prods1.add(pale2);
////		prods1.add(za2);
////		prods1.add(ro2);
//		prods2.add(acc1);
////		Anuncio an2 = anuncioService.create("Productos variados", "varios productos", 150.0, e2, prods2);
////		Anuncio an2Saved = anuncioService.save(an2);
//
//		List<Producto> prods3 = new ArrayList<>();
//		prods2.add(pa3);
////		Anuncio an3 = anuncioService.create("Pala Nox Silver", "Buena pala de iniciación con una buena rebaja", 75.0,
////				e1, prods3);
////		Anuncio an3Saved = anuncioService.save(an3);

		municipioService.createAndSave("02", "Albacete");
		municipioService.createAndSave("03", "Alicante/Alacant");
		municipioService.createAndSave("04", "Almería");
		municipioService.createAndSave("01", "Araba/Álava");
		municipioService.createAndSave("33", "Asturias");
		municipioService.createAndSave("05", "Ávila");
		municipioService.createAndSave("06", "Badajoz");
		municipioService.createAndSave("07", "Balears, Illes");
		municipioService.createAndSave("08", "Barcelona");
		municipioService.createAndSave("48", "Bizkaia");
		municipioService.createAndSave("09", "Burgos");
		municipioService.createAndSave("10", "Cáceres");
		municipioService.createAndSave("11", "Cádiz");
		municipioService.createAndSave("39", "Cantabria");
		municipioService.createAndSave("12", "Castellón/Castelló");
		municipioService.createAndSave("13", "Ciudad Real");
		municipioService.createAndSave("14", "Córdoba");
		municipioService.createAndSave("15", "Coruña, A");
		municipioService.createAndSave("16", "Cuenca");
		municipioService.createAndSave("20", "Gipuzkoa");
		municipioService.createAndSave("17", "Girona");
		municipioService.createAndSave("18", "Granada");
		municipioService.createAndSave("19", "Guadalajara");
		municipioService.createAndSave("21", "Huelva");
		municipioService.createAndSave("22", "Huesca");
		municipioService.createAndSave("23", "Jaén");
		municipioService.createAndSave("24", "León");
		municipioService.createAndSave("25", "Lleida");
		municipioService.createAndSave("27", "Lugo");
		municipioService.createAndSave("28", "Madrid");
		municipioService.createAndSave("29", "Málaga");
		municipioService.createAndSave("30", "Murcia");
		municipioService.createAndSave("31", "Navarra");
		municipioService.createAndSave("32", "Ourense");
		municipioService.createAndSave("34", "Palencia");
		municipioService.createAndSave("35", "Palmas, Las");
		municipioService.createAndSave("36", "Pontevedra");
		municipioService.createAndSave("26", "Rioja, La");
		municipioService.createAndSave("37", "Salamanca");
		municipioService.createAndSave("38", "Santa Cruz de Tenerife");
		municipioService.createAndSave("40", "Segovia");
		Municipio sev = municipioService.createAndSave("41", "Sevilla");
		municipioService.createAndSave("42", "Soria");
		municipioService.createAndSave("43", "Tarragona");
		municipioService.createAndSave("44", "Teruel");
		municipioService.createAndSave("45", "Toledo");
		municipioService.createAndSave("46", "Valencia/València");
		municipioService.createAndSave("47", "Valladolid");
		municipioService.createAndSave("49", "Zamora");
		municipioService.createAndSave("50", "Zaragoza");
		municipioService.createAndSave("51", "Ceuta");
		municipioService.createAndSave("52", "Melilla");

		DireccionPostal d1 = direccionPostalService.create("Arfe, 23", "41019", sev, cli1Saved);
		DireccionPostal d1Saved = direccionPostalService.save(d1);
		DireccionPostal d2 = direccionPostalService.create("Pastor y Landero, 18", "41019", sev, cli2Saved);
		DireccionPostal d2Saved = direccionPostalService.save(d2);
		DireccionPostal d3 = direccionPostalService.create("Torrecuadrada, 2", "41016", sev, emp1Saved);
		DireccionPostal d3Saved = direccionPostalService.save(d3);
		DireccionPostal d4 = direccionPostalService.create("Golondrina, 15", "41021", sev, emp1Saved);
		DireccionPostal d4Saved = direccionPostalService.save(d4);
		DireccionPostal d5 = direccionPostalService.create("Chipiona, 3A", "41166", sev, emp2Saved);
		DireccionPostal d5Saved = direccionPostalService.save(d5);

//		Map<Integer, Integer> anuncios = new HashMap<Integer, Integer>();
////		anuncios.put(an1Saved.getId(), 2);
////		Pedido pedido1 = pedidoService.create(cl1, d1Saved, anuncios, cestaService.getReferenciaPedidoUnicoGenerado());
////		Pedido pedido1Saved = pedidoService.save(pedido1);
//
//		List<Pedido> pedidosPrueba = new ArrayList<Pedido>();
////		pedidosPrueba.add(pedido1Saved);
//		d1Saved.setPedidos(pedidosPrueba);
//		direccionPostalService.save(d1Saved);
//
//		List<DireccionPostal> dps1 = new ArrayList<DireccionPostal>();
//		dps1.add(d1Saved);
//		cli1Saved.setDireccionesPostales(dps1);
//		clienteService.save(cli1Saved);
//
//		List<DireccionPostal> dps2 = new ArrayList<DireccionPostal>();
//		dps2.add(d2Saved);
//		cli2Saved.setDireccionesPostales(dps2);
//		clienteService.save(cli2Saved);
//
//		List<DireccionPostal> dps3 = new ArrayList<DireccionPostal>();
//		dps3.add(d3Saved);
//		dps3.add(d4Saved);
//		emp1Saved.setDireccionesPostales(dps3);
//		empleadoService.save(emp1Saved);
//
//		List<DireccionPostal> dps5 = new ArrayList<DireccionPostal>();
//		dps5.add(d5Saved);
//		emp2Saved.setDireccionesPostales(dps5);
//		empleadoService.save(emp2Saved);

	}

	public String getBytesDeImagen(String elemCodigo) {
		try {
			Imagen elem = imagenService.getImagenPorNombre(elemCodigo).get();
			byte[] photo = elem.getContent().getBytes(1, (int) elem.getContent().length());
			String bphoto = Base64.getEncoder().encodeToString(photo);
			return bphoto;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void navegaUrlBanner(String elem) {

		try {
			String url = this.mapaBannersConRedireccion.get(elem);
			if (url != null && !"".equals(url)) {
				FacesContext.getCurrentInstance().getExternalContext().redirect(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostConstruct
	public void init() {

		if (usuarioService.findAll().isEmpty()) {
			cargaDatos();
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		Configuracion config = configuracionService.findConfiguracion();
		this.mostrarBanners = config.getMostrarBanners();

		if (this.mostrarBanners == true) {
			this.mapaBannersConRedireccion = config.getMapaBannersConRedireccion();
			this.listaImagenesBanners = this.mapaBannersConRedireccion.keySet();
		}

		marcasPalas();
		marcasPaleteros();
		marcasRopa();
		marcasZapatillas();
		marcasPelotas();
		marcasPacks();
		marcasTodas();
		listaTiposAccesorios();

		model = new DefaultMenuModel();

		DefaultMenuItem itemIndex = DefaultMenuItem.builder().icon("pi pi-home").ajax(false)
				.command("#{inicioController.verPaginaInicio()}").build();
		model.getElements().add(itemIndex);

		DefaultSubMenu submenuPacks = DefaultSubMenu.builder().label("Packs de padel").build();
		DefaultMenuItem itemTodoPack = DefaultMenuItem.builder().value("Todo").ajax(false)
				.command("#{anuncioController.verTodosPacks()}").build();
		submenuPacks.getElements().add(itemTodoPack);
		if (!CollectionUtils.isEmpty(this.listaMarcasPacks)) {
			for (String m : this.listaMarcasPacks) {
				DefaultMenuItem item = DefaultMenuItem.builder().value(m).ajax(false)
						.command("#{anuncioController.verPacksMarcas(" + m + ")}").build();
				submenuPacks.getElements().add(item);
			}
		}
		model.getElements().add(submenuPacks);

		DefaultSubMenu submenuPalas = DefaultSubMenu.builder().label("Palas").build();
		DefaultMenuItem itemTodoPala = DefaultMenuItem.builder().value("Todo").ajax(false)
				.command("#{palaController.verPalasTodas()}").build();
		submenuPalas.getElements().add(itemTodoPala);
		if (!CollectionUtils.isEmpty(this.listaMarcasPalas)) {
			for (String m : this.listaMarcasPalas) {
				DefaultMenuItem item = DefaultMenuItem.builder().value(m).ajax(false)
						.command("#{palaController.verPalasMarcas(" + m + ")}").build();
				submenuPalas.getElements().add(item);
			}
		}
		model.getElements().add(submenuPalas);

		DefaultSubMenu submenuPaleteros = DefaultSubMenu.builder().label("Paleteros").build();
		DefaultMenuItem itemTodoPaletero = DefaultMenuItem.builder().value("Todo").ajax(false)
				.command("#{paleteroController.verPaleterosTodos()}").build();
		submenuPaleteros.getElements().add(itemTodoPaletero);
		if (!CollectionUtils.isEmpty(this.listaMarcasPaleteros)) {
			for (String m : this.listaMarcasPaleteros) {
				DefaultMenuItem item = DefaultMenuItem.builder().value(m).ajax(false)
						.command("#{paleteroController.verPaleterosMarcas(" + m + ")}").build();
				submenuPaleteros.getElements().add(item);
			}
		}
		model.getElements().add(submenuPaleteros);

		DefaultSubMenu submenuZapatillas = DefaultSubMenu.builder().label("Zapatillas").build();
		DefaultMenuItem itemTodoZapa = DefaultMenuItem.builder().value("Todo").ajax(false)
				.command("#{zapatillaController.verZapatillasTodas()}").build();
		submenuZapatillas.getElements().add(itemTodoZapa);
		if (!CollectionUtils.isEmpty(this.listaMarcasZapatillas)) {
			for (String m : this.listaMarcasZapatillas) {
				DefaultMenuItem item = DefaultMenuItem.builder().value(m).ajax(false)
						.command("#{zapatillaController.verZapatillasMarcas(" + m + ")}").build();
				submenuZapatillas.getElements().add(item);
			}
		}
		model.getElements().add(submenuZapatillas);

		DefaultSubMenu submenuRopa = DefaultSubMenu.builder().label("Ropa").build();
		DefaultMenuItem itemTodoRopa = DefaultMenuItem.builder().value("Todo").ajax(false)
				.command("#{ropaController.verRopaTodas()}").build();
		submenuRopa.getElements().add(itemTodoRopa);
		// TODO Filtrar por tipo de ropa tambien.

//		if (!CollectionUtils.isEmpty(this.listaTiposRopa)) {
//			for (TipoRopa m : this.listaTiposRopa) {
//				DefaultMenuItem item = DefaultMenuItem.builder().value(m.getTipoRopa()).ajax(false)
//						.command("#{ropaController.verRopaDelTipo(" + m.getTipoRopa() + ")}").build();
//				submenuRopa.getElements().add(item);
//			}
//		}
		if (!CollectionUtils.isEmpty(this.listaMarcasRopa)) {
			for (String m : this.listaMarcasRopa) {
				DefaultMenuItem item = DefaultMenuItem.builder().value(m).ajax(false)
						.command("#{ropaController.verRopaMarcas(" + m + ")}").build();
				submenuRopa.getElements().add(item);
			}
		}

		model.getElements().add(submenuRopa);

		DefaultSubMenu submenuPelotas = DefaultSubMenu.builder().label("Pelotas").build();
		DefaultMenuItem itemTodoPelota = DefaultMenuItem.builder().value("Todo").ajax(false)
				.command("#{pelotaController.verPelotasTodas()}").build();
		submenuPelotas.getElements().add(itemTodoPelota);
		if (!CollectionUtils.isEmpty(this.listaMarcasPelotas)) {
			for (String m : this.listaMarcasPelotas) {
				DefaultMenuItem item = DefaultMenuItem.builder().value(m).ajax(false)
						.command("#{pelotaController.verPelotasMarcas(" + m + ")}").build();
				submenuPelotas.getElements().add(item);
			}
		}
		model.getElements().add(submenuPelotas);

		DefaultSubMenu submenuAccs = DefaultSubMenu.builder().label("Accesorios").build();
		DefaultMenuItem itemTodoAccs = DefaultMenuItem.builder().value("Todo").ajax(false)
				.command("#{accesorioController.verAccesorioTodas()}").build();
		submenuAccs.getElements().add(itemTodoAccs);
		if (!CollectionUtils.isEmpty(this.listaTiposAccesorios)) {
			for (TipoAccesorio m : this.listaTiposAccesorios) {
				String nombreTipo = m.getNombre();
				DefaultMenuItem item = DefaultMenuItem.builder().value(nombreTipo).ajax(false)
						.command("#{accesorioController.verAccesoriosDelTipo(" + nombreTipo + ")}").build();
				submenuAccs.getElements().add(item);
			}
		}
		model.getElements().add(submenuAccs);

		if (auth != null && auth.getAuthorities().stream()
				.anyMatch(a -> (a.getAuthority().equals("empleado") || a.getAuthority().equals("admin")))) {

			DefaultSubMenu submenuEmpleado = DefaultSubMenu.builder().label("Menu Empleado").build();

			DefaultMenuItem pedidosParaTramitar = DefaultMenuItem.builder().value("Lista de pedidos para tramitar")
					.ajax(false).command("#{pedidoController.abrirPedidosParaTramitar()}").build();
			submenuEmpleado.getElements().add(pedidosParaTramitar);

			DefaultMenuItem itemAnuncio = DefaultMenuItem.builder().value("Nuevo Anuncio").ajax(false)
					.command("#{anuncioController.verNuevoAnuncio()}").build();
			submenuEmpleado.getElements().add(itemAnuncio);

			DefaultMenuItem itemListaAnuncios = DefaultMenuItem.builder().value("Lista anuncios desactivados")
					.ajax(false).command("#{anuncioController.verListaAnunciosDesactivados()}").build();
			submenuEmpleado.getElements().add(itemListaAnuncios);

			DefaultMenuItem itemRopa = DefaultMenuItem.builder().value("Lista prendas de ropa").ajax(false)
					.command("#{ropaController.verListaRopa()}").build();
			submenuEmpleado.getElements().add(itemRopa);

			DefaultMenuItem item = DefaultMenuItem.builder().value("Lista Palas").ajax(false)
					.command("#{palaController.verListaPalas()}").build();
			submenuEmpleado.getElements().add(item);

			DefaultMenuItem itemPaletero = DefaultMenuItem.builder().value("Lista Paleteros").ajax(false)
					.command("#{paleteroController.verListaPaleteros()}").build();
			submenuEmpleado.getElements().add(itemPaletero);

			DefaultMenuItem itemZapatilla = DefaultMenuItem.builder().value("Lista Zapatillas").ajax(false)
					.command("#{zapatillaController.verListaZapatillas()}").build();
			submenuEmpleado.getElements().add(itemZapatilla);

			DefaultMenuItem itemListaPelota = DefaultMenuItem.builder().value("Lista Pelotas").ajax(false)
					.command("#{pelotaController.verListaPelotas()}").build();
			submenuEmpleado.getElements().add(itemListaPelota);

			DefaultMenuItem itemAccs = DefaultMenuItem.builder().value("Lista Accesorios").ajax(false)
					.command("#{accesorioController.verListaAccesorio()}").build();
			submenuEmpleado.getElements().add(itemAccs);

			DefaultMenuItem marcas = DefaultMenuItem.builder().value("Lista Marcas").ajax(false)
					.command("#{marcaController.verMarcas()}").build();
			submenuEmpleado.getElements().add(marcas);

			DefaultMenuItem itemTA = DefaultMenuItem.builder().value("Lista de tipos de accesorio").ajax(false)
					.command("#{tipoAccesorioController.verTipoAccesorios()}").build();
			submenuEmpleado.getElements().add(itemTA);

			DefaultMenuItem itemTR = DefaultMenuItem.builder().value("Listas de tipos de ropa").ajax(false)
					.command("#{tipoRopaController.verTipoRopa()}").build();
			submenuEmpleado.getElements().add(itemTR);

			if (configuracionService.findConfiguracion().getLimiteStockBajo() > 0) {
				DefaultMenuItem itemStockBajo = DefaultMenuItem.builder().value("Lista de productos con stock bajo")
						.ajax(false).command("#{productoController.verListaProductosBajoStock()}").build();
				submenuEmpleado.getElements().add(itemStockBajo);
				DefaultMenuItem itemDes = DefaultMenuItem.builder().value("Lista de productos desactivados").ajax(false)
						.command("#{productoController.verListaProductosDesactivados()}").build();
				submenuEmpleado.getElements().add(itemDes);
			}

			model.getElements().add(submenuEmpleado);
		}

		if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("admin"))) {

			DefaultSubMenu submenuAdministrador = DefaultSubMenu.builder().label("Menu Administrador").build();

			DefaultMenuItem itemDB = DefaultMenuItem.builder().value("Dashboard").ajax(false)
					.command("#{dashboardController.mostrarDashboard()}").build();
			submenuAdministrador.getElements().add(itemDB);
			
			DefaultMenuItem itemConfiguracion = DefaultMenuItem.builder().value("Configuracion").ajax(false)
					.command("#{configuracionController.editarConfiguracion()}").build();
			submenuAdministrador.getElements().add(itemConfiguracion);

			DefaultMenuItem itemEmpleado = DefaultMenuItem.builder().value("Lista empleados").ajax(false)
					.command("#{empleadoController.verListaEmpleados()}").build();
			submenuAdministrador.getElements().add(itemEmpleado);

			DefaultMenuItem itemPT = DefaultMenuItem.builder().value("Puestos de trabajo").ajax(false)
					.command("#{puestoTrabajoController.verListaPuestoTrabajo()}").build();
			submenuAdministrador.getElements().add(itemPT);

			model.getElements().add(submenuAdministrador);
		}

		if (auth == null || auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS"))) {

			DefaultSubMenu subMenuUsuario = DefaultSubMenu.builder().label("Usuario").style("float:right;width:150px;")
					.build();

			DefaultMenuItem login = DefaultMenuItem.builder().value("Registrarse").ajax(false)
					.command("#{clienteController.abrirNuevoCliente()}").build();
			subMenuUsuario.getElements().add(login);

			DefaultMenuItem iniciarSesión = DefaultMenuItem.builder().value("Iniciar sesión").ajax(false)
					.command("#{templateController.abrirLogin()}").build();
			subMenuUsuario.getElements().add(iniciarSesión);

			model.getElements().add(subMenuUsuario);

		} else {

			String nombreUsuario = null;
			Object princ = auth.getPrincipal();
			if (princ instanceof User) {
				User user = (User) princ;
				nombreUsuario = user.getUsername();
			} else {
				nombreUsuario = (String) auth.getPrincipal();
			}

//			User user = (User) auth.getPrincipal();
//			String nombreUsuario = user.getUsername();

			DefaultSubMenu subMenuUsuario = DefaultSubMenu.builder().label(nombreUsuario)
					.style("float:right;width:150px;").build();

			if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("cliente"))) {

				DefaultMenuItem editarCliente = DefaultMenuItem.builder().value("Editar perfil").ajax(false)
						.command("#{clienteController.abrirEditarCliente()}").build();
				subMenuUsuario.getElements().add(editarCliente);

				DefaultMenuItem misPedidos = DefaultMenuItem.builder().value("Mis pedidos").ajax(false)
						.command("#{pedidoController.abrirHistorialPedidos()}").build();
				subMenuUsuario.getElements().add(misPedidos);

				DefaultMenuItem misDirecciones = DefaultMenuItem.builder().value("Mis direcciones").ajax(false)
						.command("#{direccionPostalController.abrirListaDireccionesActor()}").build();
				subMenuUsuario.getElements().add(misDirecciones);

			}

			if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("empleado"))) {

				DefaultMenuItem editarEmpleado = DefaultMenuItem.builder().value("Editar perfil").ajax(false)
						.command("#{empleadoController.abrirEditarEmpleado()}").build();
				subMenuUsuario.getElements().add(editarEmpleado);

				DefaultMenuItem misDirecciones = DefaultMenuItem.builder().value("Mis direcciones").ajax(false)
						.command("#{direccionPostalController.abrirListaDireccionesActor()}").build();
				subMenuUsuario.getElements().add(misDirecciones);

			}

			DefaultMenuItem iniciarSesión = DefaultMenuItem.builder().value("Cerrar sesión").ajax(false)
					.command("#{templateController.logout()}").build();
			subMenuUsuario.getElements().add(iniciarSesión);

			model.getElements().add(subMenuUsuario);

		}
	}
	
	public String logout() {
		SecurityContextHolder.clearContext();
		return "index.xhtml";
	}

	public MenuModel getModel() {
		return model; 
	}

	public List<String> getListaMarcasPalas() {
		return listaMarcasPalas;
	}

	public void setListaMarcasPalas(List<String> listaMarcasPalas) {
		this.listaMarcasPalas = listaMarcasPalas;
	}

	public List<String> getListaMarcasPaleteros() {
		return listaMarcasPaleteros;
	}

	public void setListaMarcasPaleteros(List<String> listaMarcasPaleteros) {
		this.listaMarcasPaleteros = listaMarcasPaleteros;
	}

	public List<String> getListaMarcasRopa() {
		return listaMarcasRopa;
	}

	public void setListaMarcasRopa(List<String> listaMarcasRopa) {
		this.listaMarcasRopa = listaMarcasRopa;
	}

	public List<String> getListaMarcasZapatillas() {
		return listaMarcasZapatillas;
	}

	public void setListaMarcasZapatillas(List<String> listaMarcasZapatillas) {
		this.listaMarcasZapatillas = listaMarcasZapatillas;
	}

	public List<String> getListaMarcasPelotas() {
		return listaMarcasPelotas;
	}

	public void setListaMarcasPelotas(List<String> listaMarcasPelotas) {
		this.listaMarcasPelotas = listaMarcasPelotas;
	}

	public List<String> getListaMarcasPacks() {
		return listaMarcasPacks;
	}

	public void setListaMarcasPacks(List<String> listaMarcasPacks) {
		this.listaMarcasPacks = listaMarcasPacks;
	}

	public List<Marca> getListaMarcasTodas() {
		return listaMarcasTodas;
	}

	public void setListaMarcasTodas(List<Marca> listaMarcasTodas) {
		this.listaMarcasTodas = listaMarcasTodas;
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

	public Set<String> getListaImagenesBanners() {
		return listaImagenesBanners;
	}

	public void setListaImagenesBanners(Set<String> listaImagenesBanners) {
		this.listaImagenesBanners = listaImagenesBanners;
	}

	public List<TipoAccesorio> getListaTiposAccesorios() {
		return listaTiposAccesorios;
	}

	public void setListaTiposAccesorios(List<TipoAccesorio> listaTiposAccesorios) {
		this.listaTiposAccesorios = listaTiposAccesorios;
	}

	public List<TipoRopa> getListaTiposRopa() {
		return listaTiposRopa;
	}

	public void setListaTiposRopa(List<TipoRopa> listaTiposRopa) {
		this.listaTiposRopa = listaTiposRopa;
	}

}
