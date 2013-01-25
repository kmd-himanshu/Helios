package org.bcje.model;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;


public class ChartBean implements Serializable {

	/**
    * 
    */
	private static final long serialVersionUID = 1L;
	private CartesianChartModel categoryModel;

	private PieChartModel pieModel;

	public PieChartModel getPieModel() {
		return pieModel;
	}

	public ChartBean() {
	}

	public CartesianChartModel getCategoryModel() {
		return categoryModel;
	}

	
	public void init() {
		categoryModel = new CartesianChartModel();

		categoryModel.addSeries(getBoysSeries());
		categoryModel.addSeries(getGirsSeries());

		createPieChart();
	}

	private void createPieChart() {
		pieModel = new PieChartModel();

		pieModel.set("Brand 1", 540);
		pieModel.set("Brand 2", 325);
		pieModel.set("Brand 3", 702);
		pieModel.set("Brand 4", 421);
	}

	private ChartSeries getGirsSeries() {
		ChartSeries girls = new ChartSeries();
		girls.setLabel("Girls");

		girls.set("2004", 52);
		girls.set("2005", 60);
		girls.set("2006", 110);
		girls.set("2007", 135);
		girls.set("2008", 120);
		return girls;
	}

	private ChartSeries getBoysSeries() {
		ChartSeries boys = new ChartSeries();
		boys.setLabel("Boys");

		boys.set("2004", 120);
		boys.set("2005", 100);
		boys.set("2006", 44);
		boys.set("2007", 150);
		boys.set("2008", 25);
		return boys;
	}

}