package org.helios.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Paint;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

public class BarChartDemo extends ApplicationFrame{
	final String series1 = "Previous Year";
    final String series2 = "Current Year";
	public BarChartDemo(String titel){

		  super(titel);

	DefaultCategoryDataset	  dataset = new DefaultCategoryDataset();
			dataset.setValue(0.19, series1, "Tracy");
			dataset.setValue(2.0, series2, "Tracy");
			dataset.setValue(3.0, series1, "Tracy1");
			dataset.setValue(2.0, series2, "Tracy1");
			dataset.setValue(3.0, series1, "Tracy2");
			dataset.setValue(2.0, series2, "Tracy2");
			dataset.setValue(3.0, series1, "Tracy3");
			dataset.setValue(2.0, series2, "Tracy3");

			
		  
		  final JFreeChart chart = createChart("Cases Handled Per Building", "Location",
					"Cases Handled", dataset);
		  final ChartPanel chartPanel = new ChartPanel(chart);
		  chartPanel.setPreferredSize(
		   new java.awt.Dimension(250, 250));
		  setContentPane(chartPanel);
		  
	}
	
	public JFreeChart createChart(String chartTitle,String domainAxisLabel,String rangeAxisLabel,CategoryDataset dataset ) {
		JFreeChart chart=null;
	      // get ImageMap
			 
			
		   chart = ChartFactory.createBarChart3D(
				   chartTitle,       // chart title
				   domainAxisLabel,               // domain axis label
				   rangeAxisLabel,                  // range axis label
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
	        plot.setBackgroundPaint(Color.white);
	        plot.setDomainGridlinePaint(Color.white);
	        plot.setRangeGridlinePaint(Color.white);
	        
	       /* final IntervalMarker target = new IntervalMarker(4.5, 7.5);
	        target.setLabel("Target Range");
	        target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
	        target.setLabelAnchor(RectangleAnchor.LEFT);
	        target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
	        target.setPaint(new Color(222, 222, 255, 128));
	        plot.addRangeMarker(target, Layer.BACKGROUND);*/
	        
	        // set the range axis to display integers only...
	        
	        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	        rangeAxis.setAutoRange(true);	        
	        rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());
	        /*// disable bar outlines...
	        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
	        renderer.setDrawBarOutline(true);
	        //renderer.setAutoPopulateSeriesFillPaint(true);
           renderer.setBaseOutlinePaint(Color.black);
           //renderer.setBaseSeriesVisible(true,true);
           renderer.setItemMargin(0.0f);
           plot.setForegroundAlpha(1.0f);
	        
	        // set up gradient paints for series...
	        final GradientPaint gp0 = new GradientPaint(
	            0.0f, 0.0f, Color.red, 
	            0.0f, 0.0f, Color.orange
	        );
	        final GradientPaint gp1 = new GradientPaint(
		            0.0f, 0.0f, Color.blue, 
		            0.0f, 0.0f, Color.green
		        );
	        
	        renderer.setSeriesPaint(0, gp0);
	        renderer.setSeriesPaint(1, gp1);*/
	        final CategoryItemRenderer r = plot.getRenderer();
	        BarRenderer renderer = (BarRenderer) r;
	        renderer.setMaximumBarWidth(0.1);
	        plot.setForegroundAlpha(1.0f);
	        renderer.setItemMargin(0.0f);
	        
	        
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
	        plot.getDomainAxis().setTickLabelFont(font);
	        
	        //use it for transpartent display 
	        plot.setForegroundAlpha(1.0f);
	        
	        
	
	        
	        
	        return chart;
			        
			        
	}
	
	
	public static void main(final String[] args) {

		  final String title = "Score Bord";
		  final BarChartDemo chart = new BarChartDemo(title);
		  chart.pack();
		  RefineryUtilities.centerFrameOnScreen(chart);
		  chart.setVisible(true);
		  }
	

}


