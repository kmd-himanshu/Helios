package org.bcje.servlets;
import java.awt.Color;
import java.awt.GradientPaint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

public class MultipleAxis extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String series1 = "Previous Year";
	final String series2 = "Current Year";

	public MultipleAxis(String titel) {
		super(titel);

		final JFreeChart chart = createChart();
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(600, 450));
		setContentPane(chartPanel);
	}

	private JFreeChart createChart() {

		// row keys...
		final String series1 = "Previous Year";
		final String series2 = "Current Year";

		// create the dataset...
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		dataset = new DefaultCategoryDataset();
		dataset.setValue(0.56, series1, "Grocery");
		dataset.setValue(1.13, series2, "Grocery");

		dataset.setValue(0.39, series1, "Produce");
		dataset.setValue(0.86, series2, "Produce");


		//final JFreeChart chart = ChartFactory.createBarChart3D(
		
		
		// create the chart...
		final JFreeChart chart = ChartFactory.createBarChart3D(
				"MultiAxis Bar Chart", // chart title
				"Category", // domain axis label
				"Value", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips?
				false // URLs?
				);

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		final CategoryPlot plot = chart.getCategoryPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		
	    CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
		
		ValueAxis axis2 = new NumberAxis("Cost($) / SqFt");
		axis2.setRange(getAxis2Range());
        plot.setRangeAxis(1, axis2);
        
		final BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderColor(renderer);
		
		renderer.setItemLabelsVisible(true);
        final ItemLabelPosition p = new ItemLabelPosition(
            ItemLabelAnchor.INSIDE12, TextAnchor.CENTER_RIGHT, 
            TextAnchor.CENTER_RIGHT, -Math.PI / 2.0
        );
        renderer.setPositiveItemLabelPosition(p);

        final ItemLabelPosition p2 = new ItemLabelPosition(
            ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT, 
            TextAnchor.CENTER_LEFT, -Math.PI / 2.0
        );
        
		return chart;

	}

	private void renderColor(final BarRenderer renderer) {
		renderer.setDrawBarOutline(false);
		renderer.setItemMargin(0.10);

		// set up gradient paints for series...
		final GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.red,
				0.0f, 0.0f, Color.lightGray);
		final GradientPaint gp1 = new GradientPaint(0.0f, 0.0f, Color.green,
				0.0f, 0.0f, Color.yellow);
		renderer.setSeriesPaint(0, gp0);
		renderer.setSeriesPaint(1, gp1);
	}

	private Range getAxis2Range() {
		Range range = new Range(1.36,1.90);
		return range;
	}

	public static void main(final String[] args) {

		final String title = "Score Bord";
		final MultipleAxis chart = new MultipleAxis(title);
		chart.pack();
		RefineryUtilities.centerFrameOnScreen(chart);
		chart.setVisible(true);
	}
}