package org.bcje.servlets;


import java.util.ArrayList;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;

public class DataChart
{

    public static final Chart createMinSliceChart( )
    {
        ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create( );
        cwoaPie.getBlock( ).setBackground( ColorDefinitionImpl.PINK( ) );

        // Plot
Plot p = cwoaPie.getPlot( );
        p.getClientArea( ).setBackground( ColorDefinitionImpl.PINK( ) );
        p.getClientArea( ).getOutline( ).setVisible( false );
        p.getOutline( ).setVisible( false );

        // Legend
Legend lg = cwoaPie.getLegend( );
        lg.setItemType( LegendItemType.CATEGORIES_LITERAL );
        lg.getClientArea( ).getOutline( ).setVisible( true );
        lg.getTitle( ).setVisible( false );

        // Title
cwoaPie.getTitle( )
                .getLabel( )
                .getCaption( )
                .setValue( "Explosion & Min Slice" ); //$NON-NLS-1$
cwoaPie.getTitle( ).getOutline( ).setVisible( false );

        // Data Set
TextDataSet categoryValues = TextDataSetImpl.create( new String []{
                "New York", "Boston", "Chicago", "San Francisco", "Dallas", "Miami"//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$//$NON-NLS-6$
} );
        NumberDataSet seriesOneValues = NumberDataSetImpl.create( new double[]{
                24, 9, 30, 36, 8, 51
        } );

        // Base Series
SeriesDefinition sd = SeriesDefinitionImpl.create( );
        cwoaPie.getSeriesDefinitions( ).add( sd );

        Series seCategory = (Series) SeriesImpl.create( );

        final Fill[] fiaBase = {
                ColorDefinitionImpl.ORANGE( ),
                GradientImpl.create( ColorDefinitionImpl.create( 225, 225, 255 ),
                        ColorDefinitionImpl.create( 255, 255, 225 ),
                        -35,
                        false ),
                ColorDefinitionImpl.CREAM( ),
                ColorDefinitionImpl.RED( ),
                 ColorDefinitionImpl.GREEN( ),
                 ColorDefinitionImpl.BLUE( ).brighter( ),
                 ColorDefinitionImpl.CYAN( ).darker( ),
         };
         sd.getSeriesPalette( ).getEntries( ).clear( );
         for ( int i = 0; i < fiaBase.length; i++ )
         {
             sd.getSeriesPalette( ).getEntries( ).add( fiaBase[i] );
         }
 
         seCategory.setDataSet( categoryValues );
         sd.getSeries( ).add( seCategory );
 
         // Orthogonal Series
 SeriesDefinition sdCity = SeriesDefinitionImpl.create( );
         sd.getSeriesDefinitions( ).add( sdCity );
 
         PieSeries sePie = (PieSeries) PieSeriesImpl.create( );
         sePie.setDataSet( seriesOneValues );
         sePie.setLabelPosition( Position.INSIDE_LITERAL );
         sePie.setSeriesIdentifier( "Cities" ); //$NON-NLS-1$
 
         // Explosion
 sePie.setExplosion( 30 );
         sePie.setExplosionExpression( "valueData<20 ||valueData>50" );//$NON-NLS-1$
 
         sdCity.getSeries( ).add( sePie );
 
         // Min Slice
 cwoaPie.setMinSlice( 10 );
         cwoaPie.setMinSlicePercent( false );
         cwoaPie.setMinSliceLabel( "Others" );//$NON-NLS-1$
 
         return cwoaPie;
     }
 
     public static final Chart createMultiYAxisChart( )
     {
         ChartWithAxes cwaBar = ChartWithAxesImpl.create( );
 
         // Plot
 cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
         Plot p = cwaBar.getPlot( );
         p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 255,
                 245,
                 255 ) );
 
         // Title
 cwaBar.getTitle( )
                 .getLabel( )
                 .getCaption( )
                 .setValue( "Line Chart with Multiple Y Axis" );//$NON-NLS-1$
 
         // Legend
 Legend lg = cwaBar.getLegend( );
         LineAttributes lia = lg.getOutline( );
         lg.getText( ).getFont( ).setSize( 16 );
         lia.setStyle( LineStyle.SOLID_LITERAL );
         lg.getInsets( ).set( 10, 5, 0, 0 );
         lg.getOutline( ).setVisible( false );
         lg.setAnchor( Anchor.NORTH_LITERAL );
 
         // X-Axis
 Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];
         xAxisPrimary.setType( AxisType.TEXT_LITERAL );
         xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
         xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
         xAxisPrimary.getTitle( ).setVisible( false );
 
         // Y-Axis
 Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
         yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
         yAxisPrimary.getTitle( )
                 .getCaption( )
                 .setValue( "Sales Growth ($Million)" );//$NON-NLS-1$
 
         // Y-Axis (2)
 Axis yAxis = AxisImpl.create( Axis.ORTHOGONAL );
         yAxis.setType( AxisType.LINEAR_LITERAL );
         yAxis.getMajorGrid( ).setTickStyle( TickStyle.RIGHT_LITERAL );
         yAxis.setLabelPosition( Position.RIGHT_LITERAL );
         xAxisPrimary.getAssociatedAxes( ).add( yAxis );
 
         // Data Set
 TextDataSet categoryValues = TextDataSetImpl.create( new String []{
                 "March", "April", "May", "June", "July"} );//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
 NumberDataSet orthoValues1 = NumberDataSetImpl.create( new double[]{
                 12.5, 19.6, 18.3, 13.2, 26.5
         } );
         NumberDataSet orthoValues2 = NumberDataSetImpl.create( new double[]{
                 22.7, 23.6, 38.3, 43.2, 40.5
         } );
 
         // X-Series
 Series seCategory = SeriesImpl.create( );
         seCategory.setDataSet( categoryValues );
 
         SeriesDefinition sdX = SeriesDefinitionImpl.create( );
         xAxisPrimary.getSeriesDefinitions( ).add( sdX );
         sdX.getSeries( ).add( seCategory );
 
         // Y-Series (1)
 LineSeries ls1 = (LineSeries) LineSeriesImpl.create( );
         ls1.setSeriesIdentifier( "A Corp." );//$NON-NLS-1$
 ls1.setDataSet( orthoValues1 );
         ls1.getLineAttributes( ).setColor( ColorDefinitionImpl.CREAM( ) );
         for ( int i = 0; i < ls1.getMarkers( ).size( ); i++ )
         {
             ( (Marker) ls1.getMarkers( ).get( i ) ).setType( MarkerType.TRIANGLE_LITERAL );
             ( (Marker) ls1.getMarkers( ).get( i ) ).setSize( 10 );
         }
         ls1.getLabel( ).setVisible( true );
 
         SeriesDefinition sdY1 = SeriesDefinitionImpl.create( );
         sdY1.getSeriesPalette( ).update( -2 );
         yAxisPrimary.getSeriesDefinitions( ).add( sdY1 );
         sdY1.getSeries( ).add( ls1 );
 
         // Y-Serires (2)
 LineSeries ls2 = (LineSeries) LineSeriesImpl.create( );
         ls2.setSeriesIdentifier( "B Corp." );//$NON-NLS-1$
 ls2.setDataSet( orthoValues2 );
         ls2.getLineAttributes( ).setColor( ColorDefinitionImpl.CREAM( ) );
         for ( int i = 0; i < ls2.getMarkers( ).size( ); i++ )
         {
             ( (Marker) ls2.getMarkers( ).get( i ) ).setType( MarkerType.CIRCLE_LITERAL );
             ( (Marker) ls2.getMarkers( ).get( i ) ).setSize( 10 );
         }
         ls2.getLabel( ).setVisible( true );
 
         SeriesDefinition sdY2 = SeriesDefinitionImpl.create( );
         sdY2.getSeriesPalette( ).update( -3 );
         yAxis.getSeriesDefinitions( ).add( sdY2 );
         sdY2.getSeries( ).add( ls2 );
 
         return cwaBar;
     }
 
     public static final Chart createMulitYSeriesChart( )
     {
         ChartWithAxes cwaBar = ChartWithAxesImpl.create( );
 
         // Plot
 cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
         Plot p = cwaBar.getPlot( );
         p.getClientArea( )
                 .setBackground( GradientImpl.create( ColorDefinitionImpl.create( 225,
                         225,
                         255 ),
                         ColorDefinitionImpl.create( 255, 255, 225 ),
                         -35,
                         false ) );
         p.getOutline( ).setVisible( true );
 
         // Title
 cwaBar.getTitle( )
                 .getLabel( )
                 .getCaption( )
                 .setValue( "Bar Chart with Multiple Y Series" );//$NON-NLS-1$
 
         // Legend
 Legend lg = cwaBar.getLegend( );
         lg.getText( ).getFont( ).setSize( 16 );
         lg.getInsets( ).set( 10, 5, 0, 0 );
         lg.setAnchor( Anchor.NORTH_LITERAL );
 
         // X-Axis
 Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];
         xAxisPrimary.setType( AxisType.TEXT_LITERAL );
         xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
         xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
         xAxisPrimary.getTitle( ).getCaption( ).setValue( "Regional Markets" ); //$NON-NLS-1$ 
 xAxisPrimary.setLabelPosition( Position.BELOW_LITERAL );
         xAxisPrimary.setTitlePosition( Position.BELOW_LITERAL );
 
         // Y-Axis
 Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
         yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
         yAxisPrimary.getTitle( )
                 .getCaption( )
                 .setValue( "Sales vs. Net Profit ($Million)" );//$NON-NLS-1$
 
         // Data Set
 TextDataSet categoryValues = TextDataSetImpl.create( new String []{
                 "Europe", "Asia", "North America"} );//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
 NumberDataSet orthoValues1 = NumberDataSetImpl.create( new double[]{
                 26.17, 34.21, 21.5
         } );
         NumberDataSet orthoValues2 = NumberDataSetImpl.create( new double[]{
                 4.81, 3.55, -5.26
         } );
 
         // X-Series
 Series seCategory = SeriesImpl.create( );
         seCategory.setDataSet( categoryValues );
 
         SeriesDefinition sdX = SeriesDefinitionImpl.create( );
         xAxisPrimary.getSeriesDefinitions( ).add( sdX );
         sdX.getSeries( ).add( seCategory );
 
         // Y-Series (1)
 BarSeries bs = (BarSeries) BarSeriesImpl.create( );
         bs.setSeriesIdentifier( "Sales" );//$NON-NLS-1$
 bs.setDataSet( orthoValues1 );
         bs.setRiserOutline( null );
         bs.getLabel( ).setVisible( true );
         bs.setLabelPosition( Position.INSIDE_LITERAL );
 
         SeriesDefinition sdY1 = SeriesDefinitionImpl.create( );
         sdY1.getSeriesPalette( ).update( -2 );
         yAxisPrimary.getSeriesDefinitions( ).add( sdY1 );
         sdY1.getSeries( ).add( bs );
 
         // Y-Series (2)
 BarSeries bs2 = (BarSeries) BarSeriesImpl.create( );
         bs2.setSeriesIdentifier( "Net Profit" );//$NON-NLS-1$
 bs2.setDataSet( orthoValues2 );
         bs2.setRiserOutline( null );
         bs2.getLabel( ).setVisible( true );
         bs2.setLabelPosition( Position.INSIDE_LITERAL );
 
         SeriesDefinition sdY2 = SeriesDefinitionImpl.create( );
         sdY2.getSeriesPalette( ).update( -3 );
         yAxisPrimary.getSeriesDefinitions( ).add( sdY2 );
         sdY2.getSeries( ).add( bs2 );
         setChartScripts(cwaBar);
         return cwaBar;
     }
     private static void setChartScripts(Chart chart) {
 		chart.setScript((
 				getBeforeGenerationScript()
// 				getBeforeDrawSeriesScript()
// 					.append("\n")
// 					.append(getDataPointFillScript())
// 					.append("\n")
// 					.append(getDataPointLabelScript())
 					).toString());
 	}
 	
 	public static StringBuffer getBeforeGenerationScript() {
 		StringBuffer sb = new StringBuffer("function beforeGeneration( chart, icsc )");
 		sb.append("{");
 		sb.append("importPackage (Packages.org.eclipse.birt.chart.model.attribute);");
 		sb.append("chart.setDimension(ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL);");
 		//sb.append("var yAxisPrimary = xAxis.getAssociatedAxes().get(0);");
 		sb.append("");
 		//sb.append("yAxisPrimary.setSideBySide(true);");
 		sb.append("");
 		sb.append("}");
 		return sb;
 	}
 	
 	public final static Chart createStockChart( )

 	{

 	ChartWithAxes cwaStock = ChartWithAxesImpl.create( );
 	cwaStock.getLegend( ).setVisible(false);
 	cwaStock.getTitle( ).getLabel( ).getCaption( ).setValue( "Stock Chart" );//$NON-NLS-1$
 	// X-Axis
 	Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaStock ).getPrimaryBaseAxes( )[0];
 	xAxisPrimary.getTitle( ).getCaption( ).setValue( "Date" );//$NON-NLS-1$
 	xAxisPrimary.setType( AxisType.DATE_TIME_LITERAL );
 	xAxisPrimary.setCategoryAxis( true );
 	xAxisPrimary.getTitle().setVisible(false);
 	xAxisPrimary.getLabel().getCaption().getFont().setRotation(65);


 	// Y-Axis (1)
 	Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaStock ).getPrimaryOrthogonalAxis( xAxisPrimary );
 	yAxisPrimary.getTitle().getCaption( ).setValue( "Price Axis" );//$NON-NLS-1$
 	yAxisPrimary.getTitle().setVisible(true);//$NON-NLS-1$
 	yAxisPrimary.getScale( ).setMax( NumberDataElementImpl.create( 180000000 ) );
 	yAxisPrimary.getScale( ).setMin( NumberDataElementImpl.create( 20000000 ) );

 	yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
 	yAxisPrimary.setTitlePosition( Position.LEFT_LITERAL );
 	yAxisPrimary.getTitle().setVisible(false);


 	// Y-Axis (2)
 	Axis yAxisOverlay = AxisImpl.create( Axis.ORTHOGONAL );
 	yAxisOverlay.getTitle( ).getCaption( ).setValue( "Volume" );//$NON-NLS-1$
 	//yAxisOverlay.getTitle( ).setVisible( true );
 	yAxisOverlay.setType( AxisType.LINEAR_LITERAL );
 	yAxisOverlay.getScale( ).setMin( NumberDataElementImpl.create( 20.5 ) );
 	yAxisOverlay.getScale( ).setMax( NumberDataElementImpl.create( 30.0 ) );
 	yAxisOverlay.getScale( ).setStep( 0.5 );


 	xAxisPrimary.getAssociatedAxes( ).add( yAxisOverlay );



 	// X-Series

 	Series seBase = SeriesImpl.create( );
 	//seBase.setDataSet( dsDateValues );
 	Query dateq = QueryImpl.create("date");
 	seBase.getDataDefinition().add(dateq);

 	SeriesDefinition sdX = SeriesDefinitionImpl.create( );
 	sdX.getSeriesPalette( ).shift( -1 );
 	xAxisPrimary.getSeriesDefinitions( ).add( sdX );
 	sdX.getSeries( ).add( seBase );


 	// Y-Series
 	BarSeries bs = (BarSeries) BarSeriesImpl.create( );
 	//bs.setRiserOutline( null );
 	//bs.setDataSet( dsStockVolume );
 	Query volumeq = QueryImpl.create("volume");

 	bs.getDataDefinition().add(volumeq);

 	StockSeries ss = (StockSeries) StockSeriesImpl.create( );
 	ss.setSeriesIdentifier( "Stock Price" );//$NON-NLS-1$
 	ss.getLineAttributes( ).setColor( ColorDefinitionImpl.BLUE( ) );
 	//ss.setDataSet( dsStockValues );

 	Query q1 = QueryImpl.create("high");
 	Query q2 = QueryImpl.create("low");
 	Query q3 = QueryImpl.create("open");
 	Query q4 = QueryImpl.create("close");
 	ArrayList<Query> list = new ArrayList<Query>();
 	list.add(q1);
 	list.add(q2);
 	list.add(q3);
 	list.add(q4);

 	ss.getDataDefinition().addAll(list);

 	SeriesDefinition sdY1 = SeriesDefinitionImpl.create( );
 	sdY1.getSeriesPalette( ).update( ColorDefinitionImpl.CYAN( ) );
 	SeriesDefinition sdY2 = SeriesDefinitionImpl.create( );
 	sdY2.getSeriesPalette( ).update( ColorDefinitionImpl.GREEN( ) );


 	yAxisPrimary.getSeriesDefinitions( ).add( sdY2 );

 	sdY1.getSeries( ).add( ss );


 	yAxisOverlay.getSeriesDefinitions( ).add( sdY1 );
 	sdY2.getSeries( ).add( bs );


 	return cwaStock;

 	} 

 }


