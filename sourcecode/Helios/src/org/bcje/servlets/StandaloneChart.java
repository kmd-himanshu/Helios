package org.bcje.servlets;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.device.EmptyUpdateNotifier;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.device.IImageMapEmitter;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.IGenerator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.core.framework.PlatformConfig;



/**
* Test decription:
* 


* Chart script: BeforeDrawLegendItem()
* 


*/

public class StandaloneChart
{

private static String OUTPUT = "d:/output/Standalone.png"; //$NON-NLS-1$
private static String OUTPUT_HTML = "d:/output/Standalone.html"; //$NON-NLS-1$


/**
* Comment for serialVersionUID
*/
private static final long serialVersionUID = 1L;

/**
* A chart model instance
*/
private Chart cm = null;

/**
* The swing rendering device
*/
private IDeviceRenderer dRenderer = null;
private IDisplayServer dServer = null;


private GeneratedChartState gcs = null;

/**
* execute application
* 
* @param args
*/
public static void main( String[] args )
{
new StandaloneChart( );
System.out.println("Finished");
}

public String getIm() {
	return im;
}

public void setIm(String im) {
	this.im = im;
}

/**
* Constructor
*/
String im =null;
public StandaloneChart(String outputI)
{
	OUTPUT=outputI;	
}
public StandaloneChart( )
{
PlatformConfig pf = new PlatformConfig();
pf.setProperty("STANDALONE", true);

//Returns a singleton instance of the Chart Engine
ChartEngine ce = ChartEngine.instance( pf);
//Returns a singleton instance of the Generator
IGenerator gr = ce.getGenerator();

try
{
//device renderers for dv.SWT, dv.PNG, dv.JPG
//dv.PDF, dv.SVG, dv.SWING, dv.PNG24, div.BMP
dRenderer = ce.getRenderer("dv.PNG");
dServer =dRenderer.getDisplayServer( ); 
}
catch( Exception ex)
{
ex.printStackTrace();
}

//cm = new AfterDatasetFilled().GetChartModel();
cm = StandaloneChart.createColoredByCategoryChart2();
//cm=DataChart.createMinSliceChart();



BufferedImage img = new BufferedImage(
600,
600,
BufferedImage.TYPE_INT_ARGB );
Graphics g = img.getGraphics( );

Graphics2D g2d = (Graphics2D) g;
//Look at IDeviceRenderer.java for all properties
//like DPI_RESOLUTION
//FILE_IDENTIFIER
//FORMAT_IDENTIFIER
//UPDATE_NOTIFIER
dRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
dRenderer.setProperty( IDeviceRenderer.FILE_IDENTIFIER, OUTPUT ); //$NON-NLS-1$

//Set the bounds for the entire chart
Bounds bo = BoundsImpl.create( 0, 0, 600, 600 );
bo.scale( 72d / dRenderer.getDisplayServer( ).getDpiResolution( ) );


try
{
gcs = gr.build(
dServer,
cm,
bo,
null,
null,
null );
dRenderer.setProperty(IDeviceRenderer.UPDATE_NOTIFIER, new EmptyUpdateNotifier( cm, gcs.getChartModel()));

gr.render( dRenderer, gcs );
im= ((IImageMapEmitter)dRenderer).getImageMap();

BufferedWriter out = new BufferedWriter( new FileWriter(OUTPUT_HTML));

out.write("<html>");
out.newLine();
out.write("<body>");
out.newLine();
out.write("");
out.newLine();
out.write("");
out.write(im);
out.write("");
out.newLine();
out.write( "");
out.newLine();
out.write("");
out.newLine(); 
out.write("");
out.newLine();
out.write("");
out.newLine();
out.close();

System.out.println(im);

}
catch ( Exception e )
{
// TODO Auto-generated catch block
e.printStackTrace( );
}
}

protected static final Chart createColoredByCategoryChart( )
{
//Create chart with or without axes
//Configure blocks
//Configure axis
//Create DataSets
//Create Series and Series Definitions
//Tie the Series Definitions to the axis

ChartWithAxes cwaBar = ChartWithAxesImpl.create( );

// Plot
cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
cwaBar.getBlock( ).getOutline( ).setVisible( true );
Plot p = cwaBar.getPlot( );
p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 255,
255,
225 ) );
p.getOutline( ).setVisible( false );
cwaBar.getTitle( )
.getLabel( )
.getCaption( )
.setValue( "Bar Chart Colored by Category" );//$NON-NLS-1$

// Legend
Legend lg = cwaBar.getLegend( );
lg.getText( ).getFont( ).setSize( 16 );
lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

// X-Axis
Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];
//LINEAR_LITERAL
//LOGARITHMIC_LITERAL
//TEXT_LITERAL
//DATE_TIME_LITERAL
xAxisPrimary.setType( AxisType.TEXT_LITERAL );
xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
xAxisPrimary.getTitle( ).getCaption( ).setValue( "Products" );
xAxisPrimary.getTitle( ).setVisible( true );

// Y-Axis
Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 45 );
yAxisPrimary.getTitle( ).getCaption( ).setValue( "Sales" );//$NON-NLS-1$
yAxisPrimary.getTitle( ).setVisible( true );

//To add another axis use AxisImpl.create( Axis.ORTHOGONAL )
//Then add it to the associated primary base axis like
//.getAssociatedAxes().add( youraxis )


// Data Set
//BubbleDataSetImpl
//DateTimeDataSetImpl
//DifferenceDataSetImpl
//GanttDataSetImpl
//NumberDataSetImpl
//StockDataSetImpl
//TextDataSetImpl
TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
"Item 1", "Item 2", "Item 3"} );
NumberDataSet orthoValues = NumberDataSetImpl.create( new double[]{
14.3, 20.9, -7.6
} );

// X-Series
// Use SeriesImpl for base series
Series seCategory = SeriesImpl.create( );
seCategory.setDataSet( categoryValues );
//Tie series to a series definition and tie sd to the axis
//Setup palette, grouping,sorting, and formatspecifier
SeriesDefinition sdX = SeriesDefinitionImpl.create( );
sdX.getSeriesPalette( ).shift( 0 );
xAxisPrimary.getSeriesDefinitions( ).add( sdX );
sdX.getSeries( ).add( seCategory );

// Y-Series
//Use specific series type
//AreaSeriesImpl
//BarSeriesImpl
//BubbleSeriesImpl
//DialSeriesImpl
//DifferenceSeriesImpl
//GanntSeriesImpl
//LineSeriesImpl
//PieSeriesImpl
//ScatterSeriesImpl
//StockSeriesImpl
BarSeries bs = (BarSeries) BarSeriesImpl.create( );
bs.setDataSet( orthoValues );
bs.setRiserOutline( null );
bs.getLabel( ).setVisible( true );
bs.setLabelPosition( Position.INSIDE_LITERAL );


Trigger tr = TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL, ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null)));

bs.getTriggers().add(tr);

//Setup palette, grouping,sorting, and formatspecifier
SeriesDefinition sdY = SeriesDefinitionImpl.create( );
yAxisPrimary.getSeriesDefinitions( ).add( sdY );
sdY.getSeries( ).add( bs );

return cwaBar;
} 
protected static final Chart createColoredByCategoryChart1( )
{
//Create chart with or without axes
//Configure blocks
//Configure axis
//Create DataSets
//Create Series and Series Definitions
//Tie the Series Definitions to the axis

ChartWithAxes cwaBar = ChartWithAxesImpl.create( );

// Plot
cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
cwaBar.getBlock( ).getOutline( ).setVisible( true );
Plot p = cwaBar.getPlot( );
p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 255,
255,
225 ) );
p.getOutline( ).setVisible( false );
cwaBar.getTitle( )
.getLabel( )
.getCaption( )
.setValue( "Bar Chart Colored by Category" );//$NON-NLS-1$

// Legend
Legend lg = cwaBar.getLegend( );
lg.getText( ).getFont( ).setSize( 16 );
lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

// X-Axis
Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];
//LINEAR_LITERAL
//LOGARITHMIC_LITERAL
//TEXT_LITERAL
//DATE_TIME_LITERAL
xAxisPrimary.setType( AxisType.TEXT_LITERAL );
xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
xAxisPrimary.getTitle( ).getCaption( ).setValue( "Products" );
xAxisPrimary.getTitle( ).setVisible( true );

// Y-Axis
Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 45 );
yAxisPrimary.getTitle( ).getCaption( ).setValue( "Sales" );//$NON-NLS-1$
yAxisPrimary.getTitle( ).setVisible( true );

//To add another axis use AxisImpl.create( Axis.ORTHOGONAL )
//Then add it to the associated primary base axis like
//.getAssociatedAxes().add( youraxis )


// Data Set
//BubbleDataSetImpl
//DateTimeDataSetImpl
//DifferenceDataSetImpl
//GanttDataSetImpl
//NumberDataSetImpl
//StockDataSetImpl
//TextDataSetImpl
TextDataSet categoryValues = TextDataSetImpl.create( new String []{
        "Europe", "Asia", "North America"} );//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
NumberDataSet orthoValues1 = NumberDataSetImpl.create( new double[]{
        26.17, 34.21, 21.5
} );
NumberDataSet orthoValues2 = NumberDataSetImpl.create( new double[]{
        4.81, 3.55, 5.26
} );

// X-Series
// Use SeriesImpl for base series
Series seCategory = SeriesImpl.create( );
seCategory.setDataSet( categoryValues );
//Tie series to a series definition and tie sd to the axis
//Setup palette, grouping,sorting, and formatspecifier
SeriesDefinition sdX = SeriesDefinitionImpl.create( );
sdX.getSeriesPalette( ).shift( 0 );
xAxisPrimary.getSeriesDefinitions( ).add( sdX );
sdX.getSeries( ).add( seCategory );

// Y-Series
//Use specific series type
//AreaSeriesImpl
//BarSeriesImpl
//BubbleSeriesImpl
//DialSeriesImpl
//DifferenceSeriesImpl
//GanntSeriesImpl
//LineSeriesImpl
//PieSeriesImpl
//ScatterSeriesImpl
//StockSeriesImpl
BarSeries bs = (BarSeries) BarSeriesImpl.create( );
bs.setDataSet( orthoValues1 );
bs.setRiserOutline( null );
bs.getLabel( ).setVisible( true );
bs.setLabelPosition( Position.INSIDE_LITERAL );


Trigger tr = TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL, ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null)));

bs.getTriggers().add(tr);

//Setup palette, grouping,sorting, and formatspecifier
SeriesDefinition sdY = SeriesDefinitionImpl.create( );
yAxisPrimary.getSeriesDefinitions( ).add( sdY );
sdY.getSeries( ).add( bs );

BarSeries bs2 = (BarSeries) BarSeriesImpl.create( );
bs2.setSeriesIdentifier( "Net Profit" );//$NON-NLS-1$
bs2.setDataSet( orthoValues2 );
bs2.setRiserOutline( null );
bs2.getLabel( ).setVisible( true );
bs2.setLabelPosition( Position.INSIDE_LITERAL );

SeriesDefinition sdY2 = SeriesDefinitionImpl.create( );
yAxisPrimary.getSeriesDefinitions( ).add( sdY2 );
sdY2.getSeries( ).add( bs2 );

return cwaBar;
} 

protected static final Chart createColoredByCategoryChart2( )
{
//Create chart with or without axes
//Configure blocks
//Configure axis
//Create DataSets
//Create Series and Series Definitions
//Tie the Series Definitions to the axis

ChartWithAxes cwaBar = ChartWithAxesImpl.create( );

// Plot
cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
cwaBar.getBlock( ).getOutline( ).setVisible( true );
Plot p = cwaBar.getPlot( );
p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 255,
255,
225 ) );
p.getOutline( ).setVisible( false );
cwaBar.getTitle( )
.getLabel( )
.getCaption( )
.setValue( "Bar Chart Colored by Category" );//$NON-NLS-1$

// Legend
Legend lg = cwaBar.getLegend( );
lg.getText( ).getFont( ).setSize( 16 );
lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

//X-Axis
Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];
//LINEAR_LITERAL
//LOGARITHMIC_LITERAL
//TEXT_LITERAL
//DATE_TIME_LITERAL
xAxisPrimary.setType( AxisType.TEXT_LITERAL );
xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
xAxisPrimary.getTitle( ).getCaption( ).setValue( "Products" );
xAxisPrimary.getTitle( ).setVisible( true );

//Y-Axis
Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 45 );
yAxisPrimary.getTitle( ).getCaption( ).setValue( "Sales" );//$NON-NLS-1$
yAxisPrimary.getTitle( ).setVisible( true );


// Y-Axis (2)
Axis yAxis = AxisImpl.create( Axis.ORTHOGONAL );
yAxis.setType( AxisType.LINEAR_LITERAL );
yAxis.getMajorGrid( ).setTickStyle( TickStyle.RIGHT_LITERAL );
yAxis.setLabelPosition( Position.RIGHT_LITERAL );
xAxisPrimary.getAssociatedAxes( ).add( yAxis );

//To add another axis use AxisImpl.create( Axis.ORTHOGONAL )
//Then add it to the associated primary base axis like
//.getAssociatedAxes().add( youraxis )


// Data Set
//BubbleDataSetImpl
//DateTimeDataSetImpl
//DifferenceDataSetImpl
//GanttDataSetImpl
//NumberDataSetImpl
//StockDataSetImpl
//TextDataSetImpl
TextDataSet categoryValues = TextDataSetImpl.create( new String []{
        "Europe", "Asia", "North America"} );//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
NumberDataSet orthoValues1 = NumberDataSetImpl.create( new double[]{
        26.17, 34.21, 21.5
} );
NumberDataSet orthoValues2 = NumberDataSetImpl.create( new double[]{
        4.81, 3.55, 5.26
} );

// X-Series
// Use SeriesImpl for base series
Series seCategory = SeriesImpl.create( );
seCategory.setDataSet( categoryValues );
//Tie series to a series definition and tie sd to the axis
//Setup palette, grouping,sorting, and formatspecifier
SeriesDefinition sdX = SeriesDefinitionImpl.create( );
sdX.getSeriesPalette( ).shift( 0 );
xAxisPrimary.getSeriesDefinitions( ).add( sdX );
sdX.getSeries( ).add( seCategory );

// Y-Series
//Use specific series type
//AreaSeriesImpl
//BarSeriesImpl
//BubbleSeriesImpl
//DialSeriesImpl
//DifferenceSeriesImpl
//GanntSeriesImpl
//LineSeriesImpl
//PieSeriesImpl
//ScatterSeriesImpl
//StockSeriesImpl
BarSeries bs = (BarSeries) BarSeriesImpl.create( );
bs.setDataSet( orthoValues1 );
bs.setRiserOutline( null );
bs.getLabel( ).setVisible( true );
bs.setLabelPosition( Position.INSIDE_LITERAL );


Trigger tr = TriggerImpl.create(TriggerCondition.ONMOUSEOVER_LITERAL, ActionImpl.create(ActionType.SHOW_TOOLTIP_LITERAL, TooltipValueImpl.create(500, null)));

bs.getTriggers().add(tr);

BarSeries bs2 = (BarSeries) BarSeriesImpl.create( );
bs2.setSeriesIdentifier( "Net Profit" );//$NON-NLS-1$
bs2.setDataSet( orthoValues2 );
bs2.setRiserOutline( null );
bs2.getLabel( ).setVisible( true );
bs2.setLabelPosition( Position.INSIDE_LITERAL );




SeriesDefinition sdY1 = SeriesDefinitionImpl.create( );
//sdY1.getSeriesPalette( ).update( -2 );
yAxisPrimary.getSeriesDefinitions( ).add( sdY1 );
sdY1.getSeries( ).add( bs );


SeriesDefinition sdY2 = SeriesDefinitionImpl.create( );
sdY2.getSeriesPalette( ).update( -3 );
yAxis.getSeriesDefinitions( ).add( sdY2 );
sdY2.getSeries( ).add( bs2 );

return cwaBar;
} 


}

