package com.helio.boomer.rap.birt;

//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//import org.eclipse.birt.report.engine.api.HTMLRenderOption;
//import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
//import org.eclipse.birt.report.engine.api.IImage;
//import org.eclipse.birt.report.engine.api.script.IReportContext;
//import org.eclipse.rwt.graphics.Graphics;

public class BoomerHTMLView {

//public class BoomerHTMLView extends ReportViewPart {
//	
//	public static final String ID = "com.helio.boomer.rap.view.birthtmlview";
//
//	private String createHTMLReport() {
//		ByteArrayOutputStream htmlOutputStream = new ByteArrayOutputStream();
//		HTMLRenderOption renderOptions = new HTMLRenderOption();
//		renderOptions.setOutputFormat( HTMLRenderOption.HTML );
//		renderOptions.setOutputStream( htmlOutputStream );
//		renderOptions.setImageDirectory( System.getProperty( "java.io.tmpdir" ) );
//		renderOptions.setSupportedImageFormats( "PNG" );
//		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler() {
//
//			private String registerImage( IImage image, Object context ) {
//				byte[] imageData = image.getImageData();
//				ByteArrayInputStream imageDatainputStream = new ByteArrayInputStream( imageData );
//				String fileName = image.getID();
//				Graphics.getImage( fileName, imageDatainputStream );
//				try {
//					imageDatainputStream.close();
//				} catch( IOException e ) {
//					e.printStackTrace();
//				}
//				return fileName;
//			}
//
//			public String onDocImage( IImage image, IReportContext context ) {
//				super.onDocImage( image, context );
//				return "/" + registerImage( image, context );
//			}
//
//			public String onDesignImage( IImage image, IReportContext context ) {
//				super.onDocImage( image, context );
//				return "/" + registerImage( image, context );
//			}
//
//			public String onCustomImage( IImage image, Object context ) {
//				return registerImage( image, context );
//			}
//		};
//		renderOptions.setImageHandler( imageHandler );
//		runReport( renderOptions );
//		return htmlOutputStream.toString();
//	}
//
//	public void doReport() {
//		String content = createHTMLReport();
//		getBrowser().setText( content );
//	}

}
