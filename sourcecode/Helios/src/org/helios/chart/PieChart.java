package org.helios.chart;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.bcje.servlets.PieChartWithCustomColors.PieRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RefineryUtilities;

public class PieChart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	public JFreeChart createChart(String chartTitle,DefaultPieDataset dataset ) {

		
		JFreeChart chart=null;

		chart= ChartFactory.createPieChart3D(chartTitle, dataset, true, true, false);
        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        //plot.setForegroundAlpha(0.6f);
        plot.setForegroundAlpha(1.0f);
        plot.setCircular(true);
        plot.setLabelGenerator(null);
        //chart.getLegend().setPosition(RectangleEdge.RIGHT);
        plot.setBackgroundPaint(Color.white);
        // Specify the colors here 
		Color[] colors = { new Color(80, 166, 218, 255),
				new Color(242, 88, 106, 255), new Color(232, 172, 57, 255),
				new Color(64, 128, 128, 255), new Color(151, 166, 33, 255),
				new Color(128, 255, 128, 255), new Color(89, 89, 134, 255) };
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
	public static class PieRenderer 
    { 
        private Color[] color; 
        
        public PieRenderer(Color[] color) 
        { 
            this.color = color; 
        }        
        
        public void setColor(PiePlot plot, DefaultPieDataset dataset) 
        { 
            List <Comparable> keys = dataset.getKeys(); 
            int aInt; 
            
            for (int i = 0; i < keys.size(); i++) 
            { 
                aInt = i % this.color.length; 
                plot.setSectionPaint(keys.get(i), this.color[aInt]); 
            } 
        } 
    } 
	
}