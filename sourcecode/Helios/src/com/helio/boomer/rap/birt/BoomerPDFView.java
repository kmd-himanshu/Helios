package com.helio.boomer.rap.birt;

//import java.io.DataInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletResponse;
//
//import org.eclipse.birt.report.engine.api.PDFRenderOption;
//import org.eclipse.rwt.RWT;
//import org.eclipse.rwt.service.IServiceHandler;

public class BoomerPDFView {

//public class BoomerPDFView extends ReportViewPart {

//	public static final String ID = "com.helio.boomer.rap.view.birtpdfview";
//
//	private static final String PDF_HANDLER_ID = PDFServiceHandler.class.getName();
//
//	private static class PDFServiceHandler implements IServiceHandler {
//
//		private String absolutePath;
//
//		public PDFServiceHandler( String absolutePath ) {
//			super();
//			this.absolutePath = absolutePath;
//		}
//
//		public void service() throws IOException, ServletException {
//			InputStream fileInputStream = null;
//			InputStream dataInputStream = null;
//			HttpServletResponse response = RWT.getResponse();
//			OutputStream outputStream = response.getOutputStream();
//			try {
//				File file = new File( absolutePath );
//				int fileSize = ( int )file.length();
//				fileInputStream = new FileInputStream( file );
//				response.setContentType( "application/pdf" );
//				response.setContentLength( fileSize );
//				response.setHeader( "Content-Disposition", "attachment; filename=\""
//						+ file.getName()
//						+ "\"" );
//				byte[] buffer = new byte[ fileSize ];
//				dataInputStream = new DataInputStream( new FileInputStream( file ) );
//				while( dataInputStream.read( buffer ) != -1 ) {
//					outputStream.write( buffer );
//				}
//			} catch( Exception e ) {
//				e.printStackTrace();
//			} finally {
//				if( fileInputStream != null ) {
//					fileInputStream.close();
//				}
//				if( dataInputStream != null ) {
//					dataInputStream.close();
//				}
//				outputStream.flush();
//				outputStream.close();
//			}
//		}
//	}
//
//	public void doReport() throws Exception {
//		String url = createPDFReport();
//		getBrowser().setUrl( url );
//	}
//
//	private String createPDFReport() throws Exception {
//		File tmpFile = File.createTempFile( "birt", ".pdf" );
//		String absolutePath = tmpFile.getAbsolutePath();
//		PDFRenderOption renderOptions = null;
//		renderOptions = new PDFRenderOption();
//		renderOptions.setOutputFormat( PDFRenderOption.OUTPUT_FORMAT_PDF );
//		renderOptions.setOutputFileName( absolutePath );
//		runReport( renderOptions );
//		RWT.getServiceManager()
//		.registerServiceHandler( PDF_HANDLER_ID,
//				new PDFServiceHandler( absolutePath ) );
//		StringBuffer url = new StringBuffer();
//		url.append( "?" );
//		url.append( IServiceHandler.REQUEST_PARAM );
//		url.append( "=" );
//		url.append( PDF_HANDLER_ID );
//		String encodedURL = RWT.getResponse().encodeURL( url.toString() );
//		return encodedURL;
//	}

}
