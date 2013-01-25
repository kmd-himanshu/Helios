package org.bcje.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.JFreeChart;

import com.keypoint.PngEncoder;

public class CopyOfChartViewer extends HttpServlet 
{ 
   public void doGet( HttpServletRequest request, HttpServletResponse response )
      throws ServletException, IOException
   {
      // get the chart from storage
      JFreeChart  chart = (JFreeChart) request.getSession().getAttribute(request.getParameter("page")+"chart" ); 
      // set the content type so the browser can see this as it is
      response.setContentType( "image/png" );
   
      // send the picture
      BufferedImage buf = chart.createBufferedImage(350, 300, null); 
      PngEncoder encoder = new PngEncoder( buf, false, 0, 9 );
      response.getOutputStream().write( encoder.pngEncode() );
   }
}

