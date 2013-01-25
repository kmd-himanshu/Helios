package org.bcje.servlets;

import java.awt.Color;

import java.awt.Font;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedDomainCategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;





public class DualAxisChart {

	  final String team1 = "1st Team";
	  final String team2 = "2nd Team";

	  public DualAxisChart() {
	  	  final JFreeChart chart = createChart();
	  
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
	  dataset.addValue(run[i], team1+
	   " Run", "" + (i + 1));
	  }
	  return dataset;
	  }

	  private CategoryDataset createRunDataset2() {
	  final DefaultCategoryDataset dataset = 
	   new DefaultCategoryDataset();

	  double[] run = run()[1];

	  for (int i = 0; i < run.length; i++) {
	  dataset.addValue(run[i], team2+
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
	  team1+" Runrate", "" + (i + 1));
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
	  team2+" Runrate", "" + (i + 1));
	  }
	  return dataset;
	  }

	  public  JFreeChart createChart() {

	  final CategoryDataset dataset1 = createRunDataset1();
	  final NumberAxis rangeAxis1 = new NumberAxis("Run");
	  rangeAxis1.setStandardTickUnits(
	  NumberAxis.createIntegerTickUnits());
	  
	  final BarRenderer barRenderer1 = new BarRenderer();
	  barRenderer1.setSeriesPaint(0, Color.red);
	  barRenderer1.setBaseToolTipGenerator(
	   new StandardCategoryToolTipGenerator());
	  
	  
	  
	  final BarRenderer barRenderer2 
	   = new BarRenderer();
	  barRenderer2.setSeriesPaint(0, Color.blue);
	  barRenderer1.setBaseToolTipGenerator(
			   new StandardCategoryToolTipGenerator());

	  
	  final CategoryPlot subplot1 = 
			  new CategoryPlot(dataset1, null, 
			  rangeAxis1, barRenderer1);
			  subplot1.setDomainGridlinesVisible(true);

			  final CategoryDataset runrateDataset1 
			  = createRunRateDataset1();
			  final ValueAxis axis2 = new NumberAxis("Run Rate");
			  subplot1.setRangeAxis(1, axis2);
			  subplot1.setDataset(1, runrateDataset1);
			  subplot1.mapDatasetToRangeAxis(1, 1);
	  
	  subplot1.setForegroundAlpha(0.7f);
	  subplot1.setRenderer(0, barRenderer1);
	  subplot1.setRenderer(1, barRenderer2);


	  

	  final CategoryAxis domainAxis = new CategoryAxis("Over");
	  final CombinedDomainCategoryPlot plot = 
	  new CombinedDomainCategoryPlot(domainAxis);

	  plot.add(subplot1, 1);
	  
	  final JFreeChart chart = new JFreeChart("Score Bord", new Font("SansSerif", Font.BOLD, 12), plot, true);
	  
	  
	  return chart;
	  
	  }

	 
	}
