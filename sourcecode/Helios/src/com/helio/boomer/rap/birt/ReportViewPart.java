package com.helio.boomer.rap.birt;

//import java.io.InputStream;
//import java.net.URL;
//
//import org.eclipse.birt.core.framework.Platform;
//import org.eclipse.birt.report.engine.api.EngineConfig;
//import org.eclipse.birt.report.engine.api.EngineException;
//import org.eclipse.birt.report.engine.api.IReportEngine;
//import org.eclipse.birt.report.engine.api.IReportEngineFactory;
//import org.eclipse.birt.report.engine.api.IReportRunnable;
//import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
//import org.eclipse.birt.report.engine.api.RenderOption;
//import org.eclipse.core.runtime.FileLocator;
//import org.eclipse.core.runtime.Path;
//import org.eclipse.rwt.RWT;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.browser.Browser;
//import org.eclipse.swt.layout.FillLayout;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.ui.part.ViewPart;
//import org.osgi.framework.Bundle;
//
//import com.helio.boomer.rap.Activator;

public class ReportViewPart {

//public abstract class ReportViewPart extends ViewPart {
//
//	  private static final String REPORT_NAME = "TopNPercent.rptdesign";
//
//	  private static final String REPORT_DIRECTORY = "reports/";
//	  
//	  private IReportEngine engine = null;
//	  private EngineConfig config = null;
//	  private IReportRunnable design = null;
//	  private IRunAndRenderTask task;
//	  private Browser browser;
//
//	  protected void runReport( RenderOption renderOptions ) {
//	    task.setRenderOption( renderOptions );
//	    try {
//	      task.run();
//	    } catch( EngineException e ) {
//	      e.printStackTrace();
//	    }
//	    task.close();
//	    engine.destroy();
//	  }
//
//	  protected void createReport() throws Exception {
//	    config = new EngineConfig();
//	    IReportEngineFactory factory = ( IReportEngineFactory )Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
//	    engine = factory.createReportEngine( config );
//	    InputStream fs = null;
//	    try {
//	      Bundle bundle = org.eclipse.core.runtime.Platform.getBundle( Activator.PLUGIN_ID );
//	      URL resourceurl = FileLocator.find( bundle, new Path( "/" ), null );
//	      String myresourcepath = FileLocator.toFileURL( resourceurl ).getPath();
//	      config.setResourcePath( myresourcepath );
//	      URL url = Activator.getDefault().getBundle().getResource( REPORT_DIRECTORY
//	                                                                  + REPORT_NAME );
//	      fs = url.openStream();
//	      design = engine.openReportDesign( fs );
//	    } finally {
//	      if(fs != null) {
//	        fs.close();
//	      }
//	    }
//	    task = engine.createRunAndRenderTask( design );
////	    task.setLocale( RWT.getLocale() );
//	  }
//
//	  public abstract void doReport() throws Exception;
//
//	  public void createPartControl( Composite parent ) {
//	    parent.setLayout( new FillLayout() );
//	    browser = new Browser( parent, SWT.NONE );
//	    try {
//	      createReport();
//	      doReport();
//	    } catch( Exception e ) {
//	      e.printStackTrace();
//	    }
//	  }
//
//	  public void setFocus() {
//	    browser.setFocus();
//	  }
//
//
//	  protected Browser getBrowser() {
//	    return browser;
//	  }

}
