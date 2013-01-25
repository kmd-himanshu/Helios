package org.bcje.servlets;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.keypoint.PngEncoder;

public class ChartViewer extends HttpServlet 
{ 
   /*public void doGet( HttpServletRequest request, HttpServletResponse response )
      throws ServletException, IOException
   {
      // get the chart from storage
      JFreeChart  chart = (JFreeChart) request.getSession().getAttribute(request.getParameter("page")+"chart" ); 
      // set the content type so the browser can see this as it is
      response.setContentType( "image/png" );
      response.getOutputStream().flush();
      // send the picture
      //BufferedImage buf = chart.createBufferedImage(350, 300, null);
      BufferedImage buf = chart.createBufferedImage(Integer.parseInt(request.getParameter("width")),Integer.parseInt(request.getParameter("height")), null);
      PngEncoder encoder = new PngEncoder( buf, false, 0, 9 );
      response.getOutputStream().write( encoder.pngEncode() );
   }*/
	
	public void init() throws ServletException { 
	} 

	//Process the HTTP Get request 
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException { 

	// get the chart from session 
	HttpSession session = request.getSession(); 
	BufferedImage chartImage = (BufferedImage) session.getAttribute(request.getParameter("page")+"chartImage"); 

	// set the content type so the browser can see this as a picture 
	response.setContentType("image/png"); 

	// send the picture 
	PngEncoder encoder = new PngEncoder(chartImage, false, 0, 9); 
	response.getOutputStream().flush();
	response.getOutputStream().write(encoder.pngEncode()); 

	} 

	//Process the HTTP Post request 
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException { 
	doGet(request, response); 
	} 

	//Process the HTTP Put request 
	public void doPut(HttpServletRequest request, HttpServletResponse response) 
	throws ServletException, IOException { 
	} 

	//Clean up resources 
	public void destroy() { 
	} 


}

