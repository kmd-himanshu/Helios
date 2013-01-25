package org.bcje.listeners;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;



public class DualAxis extends ApplicationFrame {

	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	  final String series1 = "Previous Year";
      final String series2 = "Current Year";

	  public DualAxis(String titel) {
	  super(titel);

	  final JFreeChart chart = createChart();
	  final ChartPanel chartPanel = new ChartPanel(chart);
	  chartPanel.setPreferredSize(
	   new java.awt.Dimension(600, 450));
	  setContentPane(chartPanel);
	  }

	  public double[][] run() {
	  double[][] run = new double[][]{
	  {10, 6, 2, 4, 7, 2, 8, 12, 9, 4},
	  {2, 6, 3, 8, 1, 6, 4, 9, 2, 10}
	  };
	  return run;
	  }

	  private CategoryDataset createRunDataset1() {
	  final DefaultCategoryDataset dataset = 
	  new DefaultCategoryDataset();

	  double[] run = run()[0];

	  for (int i = 0; i < run.length; i++) {
	  dataset.addValue(run[i], series1+
	   " Run", "" + (i + 1));
	  }
	  return dataset;
	  }

	  private CategoryDataset createRunDataset2() {
	  final DefaultCategoryDataset dataset = 
	   new DefaultCategoryDataset();

	  double[] run = run()[1];

	  for (int i = 0; i < run.length; i++) {
	  dataset.addValue(run[i], series2+
	   " Run", "" + (i + 1));
	  }
	  return dataset;
	  }

	  private CategoryDataset createRunRateDataset1() {
	  final DefaultCategoryDataset dataset 
	   = new DefaultCategoryDataset();

	  double[] run = run()[0];
	  float num = 0;

	  for (int i = 0; i < run.length; i++) {
	  num += run[i];
	  dataset.addValue(num / (i + 1), 
			  series1+" Runrate", "" + (i + 1));
	  }
	  return dataset;
	  }

	  private CategoryDataset createRunRateDataset2() {
	  final DefaultCategoryDataset dataset =
	   new DefaultCategoryDataset();

	  double[] run = run()[1];
	  float num = 0;

	  for (int i = 0; i < run.length; i++) {
	  num += run[i];
	  dataset.addValue(num / (i + 1), 
			  series1+" Runrate", "" + (i + 1));
	  }
	  return dataset;
	  }

	  private JFreeChart createChart() {

		// row keys...
		  final String series1 = "Previous Year";
	      final String series2 = "Current Year";

	        // column keys...
	        final String category1 = "Category 1";
	        final String category2 = "Category 2";
	        final String category3 = "Category 3";
	        final String category4 = "Category 4";
	        final String category5 = "Category 5";

	        // create the dataset...
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

	        dataset = new DefaultCategoryDataset();
			dataset.setValue(30, series1, "Tracy");
			dataset.setValue(30, series2, "Tracy");

			dataset.setValue(10, series1, "El Monte");
			dataset.setValue(50, series2, "El Monte");

			dataset.setValue(5, series1, "Santa Fe Springs");
			dataset.setValue(55, series2, "Santa Fe Springs");

	       

		// create the chart...
	        final JFreeChart chart = ChartFactory.createBarChart3D(
	            "Bar Chart Demo 7",       // chart title
	            "Category",               // domain axis label
	            "Value",                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                    // include legend
	            true,                     // tooltips?
	            false                     // URLs?
	        );

	        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

	        // set the background color for the chart...
	        chart.setBackgroundPaint(Color.white);

	        // get a reference to the plot for further customisation...
	        final CategoryPlot plot = chart.getCategoryPlot();
	        plot.setBackgroundPaint(Color.lightGray);
	        plot.setDomainGridlinePaint(Color.white);
	        plot.setRangeGridlinePaint(Color.white);
	        
	        /*final IntervalMarker target = new IntervalMarker(4.5, 7.5);
	        target.setLabel("Target Range");
	        //target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
	        target.setLabelAnchor(RectangleAnchor.LEFT);
	        target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
	        target.setPaint(new Color(222, 222, 255, 128));
	        plot.addRangeMarker(target, Layer.BACKGROUND);*/
	        
	        // set the range axis to display integers only...
	        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

	        // disable bar outlines...
	        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
	        renderer.setDrawBarOutline(false);
	        renderer.setItemMargin(0.10);
	        
	        // set up gradient paints for series...
	        final GradientPaint gp0 = new GradientPaint(
	            0.0f, 0.0f, Color.red, 
	            0.0f, 0.0f, Color.lightGray
	        );
	        final GradientPaint gp1 = new GradientPaint(
	            0.0f, 0.0f, Color.green, 
	            0.0f, 0.0f, Color.yellow
	        );
	        final GradientPaint gp2 = new GradientPaint(
	            0.0f, 0.0f, Color.red, 
	            0.0f, 0.0f, Color.lightGray
	        );
	        renderer.setSeriesPaint(0, gp0);
	        renderer.setSeriesPaint(1, gp1);
	       	        
	  //      renderer.setLabelGenerator(new BarChartDemo7.LabelGenerator());
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
	        renderer.setPositiveItemLabelPositionFallback(p2);
	        final CategoryAxis domainAxis = plot.getDomainAxis();
	        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
	        // OPTIONAL CUSTOMISATION COMPLETED.
	        Font font= new Font("Verdana", Font.PLAIN, 10)  ;      
	        
	        chart.getTitle().setFont(font);
	        chart.getCategoryPlot().getRangeAxis().setLabelFont(font);
	        renderer.setItemLabelFont(font);
	        plot.getDomainAxis().setLabelFont(font);
	        chart.getLegend().setItemFont(font);
	        plot.getRangeAxis().setLabelFont(font);
	        
	        
			        chart.getTitle().setVisible(true);
					chart.getLegend().setVisible(false);
					font= new Font("Verdana", Font.PLAIN, 20)  ;      
					plot.getDomainAxis().setTickLabelFont(font);
	        return chart;

	  }

	  public static void main(final String[] args) {

	  final String title = "Score Bord";
	  final DualAxis chart = new DualAxis(title);
	  chart.pack();
	  RefineryUtilities.centerFrameOnScreen(chart);
	  chart.setVisible(true);
	  }
	}