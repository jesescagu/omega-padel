package com.omegapadel.web;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.bar.BarChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;
import org.primefaces.model.charts.optionconfig.tooltip.Tooltip;

import com.omegapadel.service.AdministratorService;

@Named("dashboardController")
@ViewScoped
public class DashboardController implements Serializable {

	private static final long serialVersionUID = 2549283619245853247L;

	@Inject
	private AdministratorService administratorService;

	private BarChartModel tablaPedidosEntregados;
	private BarChartModel tablaPedidosReembolsados;
	private List<Integer> listaAnyosSelector;
	private Integer anyoSeleccionado;

	@PostConstruct
	public void init() {
		selectorAnyosConVenta();
		this.anyoSeleccionado = this.listaAnyosSelector.get(0);
		creaTablaPedidosEntregados();
		creaTablaPedidosReembolsados();
	}

	public void mostrarDashboard() throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect("dashboard.xhtml");
	}

	public void selectorAnyosConVenta() {
		this.listaAnyosSelector = administratorService.getAnyosConAlgunaVenta();
	}

	public void creaTablaPedidosEntregados() {

		int anyo = this.anyoSeleccionado;

		this.tablaPedidosEntregados = new BarChartModel();
		ChartData data = new ChartData();

		BarChartDataSet barDataSet = new BarChartDataSet();
		barDataSet.setLabel("Packs");
		barDataSet.setBackgroundColor("rgb(255, 99, 132)");
		List<Number> valores1 = getValoresPacks(anyo);
		barDataSet.setData(valores1);

		BarChartDataSet barDataSet2 = new BarChartDataSet();
		barDataSet2.setLabel("Palas");
		barDataSet2.setBackgroundColor("rgb(54, 162, 235)");
		List<Number> valores2 = getValoresPalas(anyo);
		barDataSet2.setData(valores2);

		BarChartDataSet barDataSet3 = new BarChartDataSet();
		barDataSet3.setLabel("Paleteros");
		barDataSet3.setBackgroundColor("rgb(255, 205, 86)");
		List<Number> valores3 = getValoresPaleteros(anyo);
		barDataSet3.setData(valores3);

		BarChartDataSet barDataSet4 = new BarChartDataSet();
		barDataSet4.setLabel("Zapatillas");
		barDataSet4.setBackgroundColor("rgb(204, 204, 0)");
		List<Number> valores4 = getValoresZapatillas(anyo);
		barDataSet4.setData(valores4);

		BarChartDataSet barDataSet5 = new BarChartDataSet();
		barDataSet5.setLabel("Ropa");
		barDataSet5.setBackgroundColor("rgb(255, 204, 204)");
		List<Number> valores5 = getValoresRopa(anyo);
		barDataSet5.setData(valores5);

		BarChartDataSet barDataSet6 = new BarChartDataSet();
		barDataSet6.setLabel("Pelotas");
		barDataSet6.setBackgroundColor("rgb(153, 153, 255)");
		List<Number> valores6 = getValoresPelotas(anyo);
		barDataSet6.setData(valores6);

		BarChartDataSet barDataSet7 = new BarChartDataSet();
		barDataSet7.setLabel("Accesorios");
		barDataSet7.setBackgroundColor("rgb(160, 160, 160)");
		List<Number> valores7 = getValoresAccesorios(anyo);
		barDataSet7.setData(valores7);

		data.addChartDataSet(barDataSet);
		data.addChartDataSet(barDataSet2);
		data.addChartDataSet(barDataSet3);
		data.addChartDataSet(barDataSet4);
		data.addChartDataSet(barDataSet5);
		data.addChartDataSet(barDataSet6);
		data.addChartDataSet(barDataSet7);

		List<String> labels = new ArrayList<>();
		Integer suma1 = (valores1.get(0).intValue() + valores2.get(0).intValue() + valores3.get(0).intValue()
				+ valores4.get(0).intValue() + valores5.get(0).intValue() + valores6.get(0).intValue()
				+ valores7.get(0).intValue());
		Integer suma2 = (valores1.get(1).intValue() + valores2.get(1).intValue() + valores3.get(1).intValue()
				+ valores4.get(1).intValue() + valores5.get(1).intValue() + valores6.get(1).intValue()
				+ valores7.get(1).intValue());
		Integer suma3 = (valores1.get(2).intValue() + valores2.get(2).intValue() + valores3.get(2).intValue()
				+ valores4.get(2).intValue() + valores5.get(2).intValue() + valores6.get(2).intValue()
				+ valores7.get(2).intValue());
		Integer suma4 = (valores1.get(3).intValue() + valores2.get(3).intValue() + valores3.get(3).intValue()
				+ valores4.get(3).intValue() + valores5.get(3).intValue() + valores6.get(3).intValue()
				+ valores7.get(3).intValue());
		Integer suma5 = (valores1.get(4).intValue() + valores2.get(4).intValue() + valores3.get(4).intValue()
				+ valores4.get(4).intValue() + valores5.get(4).intValue() + valores6.get(4).intValue()
				+ valores7.get(4).intValue());
		Integer suma6 = (valores1.get(5).intValue() + valores2.get(5).intValue() + valores3.get(5).intValue()
				+ valores4.get(5).intValue() + valores5.get(5).intValue() + valores6.get(5).intValue()
				+ valores7.get(5).intValue());
		Integer suma7 = (valores1.get(6).intValue() + valores2.get(6).intValue() + valores3.get(6).intValue()
				+ valores4.get(6).intValue() + valores5.get(6).intValue() + valores6.get(6).intValue()
				+ valores7.get(6).intValue());
		Integer suma8 = (valores1.get(7).intValue() + valores2.get(7).intValue() + valores3.get(7).intValue()
				+ valores4.get(7).intValue() + valores5.get(7).intValue() + valores6.get(7).intValue()
				+ valores7.get(7).intValue());
		Integer suma9 = (valores1.get(8).intValue() + valores2.get(8).intValue() + valores3.get(8).intValue()
				+ valores4.get(8).intValue() + valores5.get(8).intValue() + valores6.get(8).intValue()
				+ valores7.get(8).intValue());
		Integer suma10 = (valores1.get(9).intValue() + valores2.get(9).intValue() + valores3.get(9).intValue()
				+ valores4.get(9).intValue() + valores5.get(9).intValue() + valores6.get(9).intValue()
				+ valores7.get(9).intValue());
		Integer suma11 = (valores1.get(10).intValue() + valores2.get(10).intValue() + valores3.get(10).intValue()
				+ valores4.get(10).intValue() + valores5.get(10).intValue() + valores6.get(10).intValue()
				+ valores7.get(10).intValue());
		Integer suma12 = (valores1.get(11).intValue() + valores2.get(11).intValue() + valores3.get(11).intValue()
				+ valores4.get(11).intValue() + valores5.get(11).intValue() + valores6.get(11).intValue()
				+ valores7.get(11).intValue());

		labels.add("Enero - Total: ".concat(suma1.toString()));
		labels.add("Febrero - Total: ".concat(suma2.toString()));
		labels.add("Marzo - Total: ".concat(suma3.toString()));
		labels.add("Abril - Total: ".concat(suma4.toString()));
		labels.add("Mayo - Total: ".concat(suma5.toString()));
		labels.add("Junio - Total: ".concat(suma6.toString()));
		labels.add("Julio - Total: ".concat(suma7.toString()));
		labels.add("Agosto - Total: ".concat(suma8.toString()));
		labels.add("Septiembre - Total: ".concat(suma9.toString()));
		labels.add("Octubre - Total: ".concat(suma10.toString()));
		labels.add("Noviembre - Total: ".concat(suma11.toString()));
		labels.add("Diciembre - Total: ".concat(suma12.toString()));
		data.setLabels(labels);
		tablaPedidosEntregados.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		linearAxes.setStacked(true);
		linearAxes.setOffset(true);
		cScales.addXAxesData(linearAxes);
		cScales.addYAxesData(linearAxes);
		options.setScales(cScales);

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Ventas entegadas correctamente");
		options.setTitle(title);

		Tooltip tooltip = new Tooltip();
		tooltip.setMode("index");
		tooltip.setIntersect(false);
		options.setTooltip(tooltip);

		tablaPedidosEntregados.setOptions(options);
	}

	private List<Number> getValoresPacks(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosPacksPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresPalas(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosPalasPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresPaleteros(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosPaleterosPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresZapatillas(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosZapatillasPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresRopa(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosRopaPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresPelotas(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosPelotasPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresAccesorios(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosAccesoriosPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	public void creaTablaPedidosReembolsados() {

		int anyo = this.anyoSeleccionado;

		this.tablaPedidosReembolsados = new BarChartModel();
		ChartData data = new ChartData();

		BarChartDataSet barDataSet = new BarChartDataSet();
		barDataSet.setLabel("Packs");
		barDataSet.setBackgroundColor("rgb(255, 99, 132)");
		List<Number> valores1 = getValoresReembolsadosPacks(anyo);
		barDataSet.setData(valores1);

		BarChartDataSet barDataSet2 = new BarChartDataSet();
		barDataSet2.setLabel("Palas");
		barDataSet2.setBackgroundColor("rgb(54, 162, 235)");
		List<Number> valores2 = getValoresReembolsadosPalas(anyo);
		barDataSet2.setData(valores2);

		BarChartDataSet barDataSet3 = new BarChartDataSet();
		barDataSet3.setLabel("Paleteros");
		barDataSet3.setBackgroundColor("rgb(255, 205, 86)");
		List<Number> valores3 = getValoresReembolsadosPaleteros(anyo);
		barDataSet3.setData(valores3);

		BarChartDataSet barDataSet4 = new BarChartDataSet();
		barDataSet4.setLabel("Zapatillas");
		barDataSet4.setBackgroundColor("rgb(204, 204, 0)");
		List<Number> valores4 = getValoresReembolsadosZapatillas(anyo);
		barDataSet4.setData(valores4);

		BarChartDataSet barDataSet5 = new BarChartDataSet();
		barDataSet5.setLabel("Ropa");
		barDataSet5.setBackgroundColor("rgb(255, 204, 204)");
		List<Number> valores5 = getValoresReembolsadosRopa(anyo);
		barDataSet5.setData(valores5);

		BarChartDataSet barDataSet6 = new BarChartDataSet();
		barDataSet6.setLabel("Pelotas");
		barDataSet6.setBackgroundColor("rgb(153, 153, 255)");
		List<Number> valores6 = getValoresReembolsadosPelotas(anyo);
		barDataSet6.setData(valores6);

		BarChartDataSet barDataSet7 = new BarChartDataSet();
		barDataSet7.setLabel("Accesorios");
		barDataSet7.setBackgroundColor("rgb(160, 160, 160)");
		List<Number> valores7 = getValoresReembolsadosAccesorios(anyo);
		barDataSet7.setData(valores7);

		data.addChartDataSet(barDataSet);
		data.addChartDataSet(barDataSet2);
		data.addChartDataSet(barDataSet3);
		data.addChartDataSet(barDataSet4);
		data.addChartDataSet(barDataSet5);
		data.addChartDataSet(barDataSet6);
		data.addChartDataSet(barDataSet7);

		List<String> labels = new ArrayList<>();
		Integer suma1 = (valores1.get(0).intValue() + valores2.get(0).intValue() + valores3.get(0).intValue()
				+ valores4.get(0).intValue() + valores5.get(0).intValue() + valores6.get(0).intValue()
				+ valores7.get(0).intValue());
		Integer suma2 = (valores1.get(1).intValue() + valores2.get(1).intValue() + valores3.get(1).intValue()
				+ valores4.get(1).intValue() + valores5.get(1).intValue() + valores6.get(1).intValue()
				+ valores7.get(1).intValue());
		Integer suma3 = (valores1.get(2).intValue() + valores2.get(2).intValue() + valores3.get(2).intValue()
				+ valores4.get(2).intValue() + valores5.get(2).intValue() + valores6.get(2).intValue()
				+ valores7.get(2).intValue());
		Integer suma4 = (valores1.get(3).intValue() + valores2.get(3).intValue() + valores3.get(3).intValue()
				+ valores4.get(3).intValue() + valores5.get(3).intValue() + valores6.get(3).intValue()
				+ valores7.get(3).intValue());
		Integer suma5 = (valores1.get(4).intValue() + valores2.get(4).intValue() + valores3.get(4).intValue()
				+ valores4.get(4).intValue() + valores5.get(4).intValue() + valores6.get(4).intValue()
				+ valores7.get(4).intValue());
		Integer suma6 = (valores1.get(5).intValue() + valores2.get(5).intValue() + valores3.get(5).intValue()
				+ valores4.get(5).intValue() + valores5.get(5).intValue() + valores6.get(5).intValue()
				+ valores7.get(5).intValue());
		Integer suma7 = (valores1.get(6).intValue() + valores2.get(6).intValue() + valores3.get(6).intValue()
				+ valores4.get(6).intValue() + valores5.get(6).intValue() + valores6.get(6).intValue()
				+ valores7.get(6).intValue());
		Integer suma8 = (valores1.get(7).intValue() + valores2.get(7).intValue() + valores3.get(7).intValue()
				+ valores4.get(7).intValue() + valores5.get(7).intValue() + valores6.get(7).intValue()
				+ valores7.get(7).intValue());
		Integer suma9 = (valores1.get(8).intValue() + valores2.get(8).intValue() + valores3.get(8).intValue()
				+ valores4.get(8).intValue() + valores5.get(8).intValue() + valores6.get(8).intValue()
				+ valores7.get(8).intValue());
		Integer suma10 = (valores1.get(9).intValue() + valores2.get(9).intValue() + valores3.get(9).intValue()
				+ valores4.get(9).intValue() + valores5.get(9).intValue() + valores6.get(9).intValue()
				+ valores7.get(9).intValue());
		Integer suma11 = (valores1.get(10).intValue() + valores2.get(10).intValue() + valores3.get(10).intValue()
				+ valores4.get(10).intValue() + valores5.get(10).intValue() + valores6.get(10).intValue()
				+ valores7.get(10).intValue());
		Integer suma12 = (valores1.get(11).intValue() + valores2.get(11).intValue() + valores3.get(11).intValue()
				+ valores4.get(11).intValue() + valores5.get(11).intValue() + valores6.get(11).intValue()
				+ valores7.get(11).intValue());

		labels.add("Enero - Total: ".concat(suma1.toString()));
		labels.add("Febrero - Total: ".concat(suma2.toString()));
		labels.add("Marzo - Total: ".concat(suma3.toString()));
		labels.add("Abril - Total: ".concat(suma4.toString()));
		labels.add("Mayo - Total: ".concat(suma5.toString()));
		labels.add("Junio - Total: ".concat(suma6.toString()));
		labels.add("Julio - Total: ".concat(suma7.toString()));
		labels.add("Agosto - Total: ".concat(suma8.toString()));
		labels.add("Septiembre - Total: ".concat(suma9.toString()));
		labels.add("Octubre - Total: ".concat(suma10.toString()));
		labels.add("Noviembre - Total: ".concat(suma11.toString()));
		labels.add("Diciembre - Total: ".concat(suma12.toString()));
		data.setLabels(labels);
		tablaPedidosReembolsados.setData(data);

		// Options
		BarChartOptions options = new BarChartOptions();
		CartesianScales cScales = new CartesianScales();
		CartesianLinearAxes linearAxes = new CartesianLinearAxes();
		linearAxes.setStacked(true);
		linearAxes.setOffset(true);
		cScales.addXAxesData(linearAxes);
		cScales.addYAxesData(linearAxes);
		options.setScales(cScales);

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Disputas Reembolsadas");
		options.setTitle(title);

		Tooltip tooltip = new Tooltip();
		tooltip.setMode("index");
		tooltip.setIntersect(false);
		options.setTooltip(tooltip);

		tablaPedidosReembolsados.setOptions(options);
	}

	private List<Number> getValoresReembolsadosPacks(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosReembolsadosPacksPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresReembolsadosPalas(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosReembolsadosPalasPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresReembolsadosPaleteros(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosReembolsadosPaleterosPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresReembolsadosZapatillas(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosReembolsadosZapatillasPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresReembolsadosRopa(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosReembolsadosRopaPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresReembolsadosPelotas(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosReembolsadosPelotasPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	private List<Number> getValoresReembolsadosAccesorios(Integer anyo) {
		List<Number> valores = Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		List<Number[]> dataVal = administratorService.getPedidosReembolsadosAccesoriosPorAnyo(anyo);
		for (Number[] na : dataVal) {
			Integer index = na[0].intValue() - 1;
			if (na[1] != null) {
				valores.set(index, na[1]);
			}
		}
		return valores;
	}

	public void listenerChangeValue(ValueChangeEvent event) {

		this.anyoSeleccionado = (Integer) event.getNewValue();
		creaTablaPedidosEntregados();
		creaTablaPedidosReembolsados();

	}

	public List<Integer> getListaAnyosSelector() {
		return listaAnyosSelector;
	}

	public void setListaAnyosSelector(List<Integer> listaAnyosSelector) {
		this.listaAnyosSelector = listaAnyosSelector;
	}

	public Integer getAnyoSeleccionado() {
		return anyoSeleccionado;
	}

	public void setAnyoSeleccionado(Integer anyoSeleccionado) {
		this.anyoSeleccionado = anyoSeleccionado;
	}

	public BarChartModel getTablaPedidosEntregados() {
		return tablaPedidosEntregados;
	}

	public void setTablaPedidosEntregados(BarChartModel tablaPedidosEntregados) {
		this.tablaPedidosEntregados = tablaPedidosEntregados;
	}

	public BarChartModel getTablaPedidosReembolsados() {
		return tablaPedidosReembolsados;
	}

	public void setTablaPedidosReembolsados(BarChartModel tablaPedidosReembolsados) {
		this.tablaPedidosReembolsados = tablaPedidosReembolsados;
	}

}
