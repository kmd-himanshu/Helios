package org.bcje.listeners;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.urls.StandardCategoryURLGenerator;
import org.jfree.data.DefaultKeyedValue;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultKeyedValueDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;

public class BarChart1 extends ApplicationFrame {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String team1 = "1st Team";
	  final String team2 = "2nd Team";

	  public BarChart1(String titel) {
	  super(titel);

	  final JFreeChart chart = createChart();
	  final ChartPanel chartPanel = new ChartPanel(chart);
	  chartPanel.setPreferredSize(
	   new java.awt.Dimension(600, 450));
	  setContentPane(chartPanel);
	  }

	 

	  private JFreeChart createChart() {

JFreeChart chart=null;	   
		  DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			dataset.setValue(0.0, "Previous Year", "Tracy");
			dataset.setValue(0.0, "Current Year", "Tracy");

			dataset.setValue(0.0, "Previous Year", "El Monte");
			dataset.setValue(0.0, "Current Year", "El Monte");

			dataset.setValue(0.0, "Previous Year", "Santa Fe Springs");
			dataset.setValue(0.0, "Current Year", "Santa Fe Springs");

			chart = ChartFactory.createBarChart3D(
					"Comparison between Girls and Boys in Science, "
							+ "Economics and Language classes",
					"Students Comparisons", "No of Students", dataset,
					PlotOrientation.VERTICAL, true, true, false);
			chart.setBackgroundPaint(Color.white);

			// Set the background colour of the chart
			chart.getTitle().setPaint(Color.white);

			// Adjust the colour of the title
			CategoryPlot plot = chart.getCategoryPlot();

			// Get the Plot object for a bar graph

			plot.setBackgroundPaint(Color.white);
			plot.setRangeGridlinePaint(Color.white);
			CategoryItemRenderer renderer = plot.getRenderer();
			renderer.setSeriesPaint(0, Color.red);
			renderer.setSeriesPaint(1, Color.green);
			renderer.setItemURLGenerator(

			new StandardCategoryURLGenerator("index1.html", "series", "section"));
			renderer.setToolTipGenerator(new StandardCategoryToolTipGenerator());

 return chart;
	  }

	  public static void main(final String[] args) {

	  final String title = "Score Bord";
	  final BarChart1 chart = new BarChart1(title);
	  chart.pack();
	  RefineryUtilities.centerFrameOnScreen(chart);
	  chart.setVisible(true);
	  }
	}