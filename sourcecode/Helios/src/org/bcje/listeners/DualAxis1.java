package org.bcje.listeners;

import java.awt.Color;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis3D;
import org.jfree.chart.axis.NumberAxis3D;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.DefaultKeyedValue;
import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.KeyedValues;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultKeyedValueDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import com.sun.org.apache.xml.internal.security.keys.content.KeyValue;

public class DualAxis1 extends ApplicationFrame {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String team1 = "1st Team";
	  final String team2 = "2nd Team";

	  public DualAxis1(String titel) {
	  super(titel);

	  final JFreeChart chart = createChart();
	  final ChartPanel chartPanel = new ChartPanel(chart);
	  chartPanel.setPreferredSize(
	   new java.awt.Dimension(600, 450));
	  setContentPane(chartPanel);
	  }

	 

	  private JFreeChart createChart() {

	   
		  final double[][] data = new double[][]{
				  {210, 300, 320, 265, 299},
				  {200, 304, 201, 201, 340}
				  };
		  DefaultKeyedValues keyValue=new DefaultKeyedValues();
		  keyValue.setValue("girls", 10);
		  keyValue.setValue("boys", 12);
		  
		  DefaultKeyedValues keyValue1=new DefaultKeyedValues();
		  keyValue.setValue("girls", 12);
		  keyValue.setValue("boys", 14);
		  		  
				  final CategoryDataset dataset = 
				    DatasetUtilities.createCategoryDataset("Team",  (KeyedValues) keyValue);
				  final CategoryDataset dataset1 = 
						    DatasetUtilities.createCategoryDataset("Team",  (KeyedValues) keyValue1);

				  JFreeChart chart = null;
				  BarRenderer renderer3D = null;
				  CategoryPlot plot = null;


				  final CategoryAxis3D categoryAxis = new CategoryAxis3D("Match");
				  final ValueAxis valueAxis = new NumberAxis3D("Run");
				  renderer3D = new BarRenderer();

				  plot = new CategoryPlot(dataset, categoryAxis, valueAxis, renderer3D);
				  plot.setOrientation(PlotOrientation.VERTICAL);
				  plot.setDataset(1,dataset1);
				  plot.setRenderer(1,renderer3D);
				  chart = new JFreeChart("Srore Bord", JFreeChart.DEFAULT_TITLE_FONT, 
				   plot, true);
				  
				  chart.setBackgroundPaint(new Color(249, 231, 236));

 return chart;
	  }

	  public static void main(final String[] args) {

	  final String title = "Score Bord";
	  final DualAxis1 chart = new DualAxis1(title);
	  chart.pack();
	  RefineryUtilities.centerFrameOnScreen(chart);
	  chart.setVisible(true);
	  }
	}