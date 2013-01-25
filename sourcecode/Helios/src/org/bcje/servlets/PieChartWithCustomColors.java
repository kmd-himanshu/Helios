package org.bcje.servlets;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import org.helios.chart.PieChart.PieRenderer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieToolTipGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class PieChartWithCustomColors extends ApplicationFrame
{ 
	public PieChartWithCustomColors(String title){

	  super(title);

	  final JFreeChart chart = createChart();
	  final ChartPanel chartPanel = new ChartPanel(chart);
	  chartPanel.setPreferredSize(
	   new java.awt.Dimension(600, 450));
	  setContentPane(chartPanel);
	}
	  
    private JFreeChart createChart() {
    	// create the dataset...
    			DefaultPieDataset dataset = new DefaultPieDataset();

    			dataset.setValue("Produce", 21.0);
    			dataset.setValue("Grocery", 47.0);
    			

    			final JFreeChart chart= ChartFactory.createPieChart3D("d", dataset, true, true, false);
    	        PiePlot3D plot = (PiePlot3D) chart.getPlot();
    	        plot.setForegroundAlpha(.6f);
    	        plot.setCircular(true);    	       
    	        plot.setLabelGenerator(null);
    	        //chart.getLegend().setPosition(RectangleEdge.RIGHT);
    	        plot.setBackgroundPaint(Color.white);
    	        // Specify the colors here 
    	        Color[] colors = {Color.red, Color.blue, Color.green,Color.gray,Color.yellow}; 
    	        plot.setToolTipGenerator(new StandardPieToolTipGenerator());      
    	        // OPTIONAL CUSTOMISATION COMPLETED.
    	        Font font= new Font("Verdana", Font.PLAIN, 10)  ;      
    	        chart.getTitle().setFont(font);           
    	        
    	        chart.getLegend().setItemFont(font);

    			return chart;
	}

	public static void main(String[] args) 
    { 
		final String title = "Score Bord";
		  final PieChartWithCustomColors chart = new PieChartWithCustomColors(title);
		  chart.pack();
		  RefineryUtilities.centerFrameOnScreen(chart);
		  chart.setVisible(true);
    } 
    
    
    /* 
     * A simple renderer for setting custom colors 
     * for a pie chart. 
     */ 
    
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