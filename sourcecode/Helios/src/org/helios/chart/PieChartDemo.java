package org.helios.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bcje.listeners.DualAxis;
import org.bcje.model.ChartModel;
import org.helios.chart.PieChart.PieRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.ui.TextAnchor;

import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.servicedata.BusinessUnitReportDAO;

public class PieChartDemo extends ApplicationFrame{
	final String series1 = "Previous Year";
    final String series2 = "Current Year";
	public PieChartDemo(String titel){

		  super(titel);

	DefaultPieDataset	  dataset = new DefaultPieDataset();
			dataset.setValue( "Grocery",2.0 );
	dataset.setValue( "Product",1.0 );
	dataset.setValue( "Tracy1",1.0 );
	dataset.setValue( "Tracy2",1.0 );
	dataset.setValue( "Tracy3",1.0 );
	dataset.setValue( "Tracy4",3.0 );
	dataset.setValue( "Tracy5",2.0 );
		  
		  final JFreeChart chart = createChart("Cases Handled Per Building", dataset);
		  final ChartPanel chartPanel = new ChartPanel(chart);
		  chartPanel.setPreferredSize(
		   new java.awt.Dimension(600, 450));
		  setContentPane(chartPanel);
		  
	}
	
	public JFreeChart createChart(String chartTitle,DefaultPieDataset dataset ) {
		JFreeChart chart=null;

		chart= ChartFactory.createPieChart3D(chartTitle, dataset, true, true, false);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setForegroundAlpha(1.0f);
        plot.setCircular(true);
        plot.setLabelGenerator(null);
        //chart.getLegend().setPosition(RectangleEdge.RIGHT);
        plot.setBackgroundPaint(Color.white);
        plot.setAutoPopulateSectionOutlinePaint(true);
        // Specify the colors here 
        Color[] colors = {new Color(80, 166, 218, 255), new Color(242, 88, 106, 255), new Color(232, 172, 57, 255), new Color(64,128, 128, 255),new Color(151,166, 33, 255),new Color(128,255,128, 255),new Color(89,89,134, 255)}; 
        PieRenderer renderer = new PieRenderer(colors); 
        renderer.setColor(plot, dataset); 
        //plot.setToolTipGenerator(new StandardPieToolTipGenerator());      
         // OPTIONAL CUSTOMISATION COMPLETED.
        Font font= new Font("Verdana", Font.PLAIN, 10)  ;      
        chart.getTitle().setFont(font);           
        //plot.setLabelGenerator(new StandardPieSectionLabelGenerator());
        chart.getLegend().setItemFont(font);
        chart.setBorderPaint(Color.white);
        		return chart;
	        
	        
	
			        
	}
	public static void main(final String[] args) {

		  final String title = "Score Bord";
		  final PieChartDemo chart = new PieChartDemo(title);
		  chart.pack();
		  RefineryUtilities.centerFrameOnScreen(chart);
		  chart.setVisible(true);
		  }
	

}
