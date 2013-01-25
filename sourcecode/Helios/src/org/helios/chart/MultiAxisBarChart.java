package org.helios.chart;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bcje.model.ChartModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.TextAnchor;

import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.servicedata.BusinessUnitReportDAO;

public class MultiAxisBarChart {

	
	public JFreeChart createChart(String chartTitle,String domainAxisLabel,String rangeAxisLabel,String rangeAxisLabel2,CategoryDataset dataset1 ,CategoryDataset dataset2) {
		JFreeChart chart=null;
	      // get ImageMap
			 
			
		   chart = ChartFactory.createBarChart3D(
				   chartTitle,       // chart title
				   domainAxisLabel,               // domain axis label
				   rangeAxisLabel,                  // range axis label
	            dataset1,                  // data
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
	        //rangeAxis.setAutoRangeIncludesZero(true);
	        //rangeAxis.setStandardTickUnits(NumberAxis.createStandardTickUnits());



	     // set up gradient paints for series...
	        
	       
	        
	        final GradientPaint gp0 = new GradientPaint(
	            0.0f, 0.0f, Color.green, 
	            0.0f, 0.0f, Color.green
	        );
	       
	        final GradientPaint gp1 = new GradientPaint(
		            0.0f, 0.0f, /*new Color(169, 61, 74, 255)*/new Color(255, 128, 0, 255), 
		            0.0f, 0.0f, new Color(255, 128, 0, 255)
		        );
	        
	        final ItemLabelPosition p1 = new ItemLabelPosition(
		            ItemLabelAnchor.INSIDE12, TextAnchor.CENTER_RIGHT, 
		            TextAnchor.CENTER_RIGHT, -Math.PI / 2.0
		        );
	        final ItemLabelPosition p2 = new ItemLabelPosition(
		            ItemLabelAnchor.OUTSIDE12, TextAnchor.CENTER_LEFT, 
		            TextAnchor.CENTER_LEFT, -Math.PI / 2.0
		        );
	        
	        
	        
	     // disable bar outlines...
	        final CategoryItemRenderer r = plot.getRenderer();
	        BarRenderer renderer1 = (BarRenderer) r;
	        renderer1.setMaximumBarWidth(0.1);
	        plot.setForegroundAlpha(1.0f);
	        renderer1.setItemMargin(0.0f);
	  //      renderer.setLabelGenerator(new BarChartDemo7.LabelGenerator());
	        renderer1.setItemLabelsVisible(true);
	        
	        renderer1.setPositiveItemLabelPosition(p1);
	        renderer1.setSeriesPaint(1, gp1);
	        
	        renderer1.setPositiveItemLabelPositionFallback(p2);
	        
	        final CategoryAxis domainAxis = plot.getDomainAxis();
	        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
	        	        
	        
		 	 ValueAxis axis2 = new NumberAxis(rangeAxisLabel2);
		 	
		 	 axis2.setAutoRange(true);		 
		 	 axis2.setStandardTickUnits(NumberAxis.createStandardTickUnits());
		 	 plot.setRangeAxis(1, axis2);  	
		 	
		 	LineAndShapeRenderer renderer2 = null;
	        
		 	if(dataset2!=null){
		 		
		 		plot.setDataset(1, dataset2);
			 	plot.mapDatasetToRangeAxis(1, 1); 
			 	
		 		// disable bar outlines...
		 		renderer2 = new LineAndShapeRenderer();
		        renderer2.setToolTipGenerator(new StandardCategoryToolTipGenerator());
		        renderer2.setDrawOutlines(false);
		        renderer2.setItemMargin(0.10);
		        renderer2.setSeriesPaint(1, gp1);
		         // renderer.setLabelGenerator(new BarChartDemo7.LabelGenerator());
		        renderer2.setItemLabelsVisible(true);		        
		        renderer2.setPositiveItemLabelPosition(p1);		        
		        plot.setRenderer(1, renderer2);
		        plot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);     
		        
		        
		 	}
		 	
		 	 
	        
	        // OPTIONAL CUSTOMISATION COMPLETED.
	        Font font= new Font("Verdana", Font.PLAIN, 10)  ;      
	        chart.getTitle().setFont(font);
	        renderer1.setItemLabelFont(font);
	        chart.getLegend().setItemFont(font);
	        plot.getDomainAxis().setLabelFont(font);
	        chart.getLegend().setItemFont(font);
	        plot.getRangeAxis().setLabelFont(font);
	        plot.getRangeAxis(1).setLabelFont(font);
	        plot.getRangeAxis(1).setTickLabelFont(font);
	        plot.getDomainAxis().setTickLabelFont(font);
	        
	        if(dataset2!=null)
	        {
	        renderer2.setItemLabelFont(font);
	        axis2.setLabelFont(font);
	        axis2.setTickLabelFont(font);
	        
	        }
	        
	       //use it for transpartent display 
	        plot.setForegroundAlpha(1.0f);
	        
	        return chart;
			        
			        
	}

	

}
