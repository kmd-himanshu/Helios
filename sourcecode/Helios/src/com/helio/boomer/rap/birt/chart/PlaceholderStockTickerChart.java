package com.helio.boomer.rap.birt.chart;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.StockDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;

import com.helio.boomer.rap.dataobject.KPIStockData;
import com.helio.boomer.rap.engine.servicedata.EnterpriseReportDAO;
import com.helio.boomer.rap.preference.PreferenceConstants;
import com.ibm.icu.util.Calendar;

public class PlaceholderStockTickerChart extends BoomerBirtChart {

//	private Color defaultStickColor;
	
	public PlaceholderStockTickerChart(String chartTitle) {
		super(chartTitle);
		this.setXAxisLabel("Hour of the Day");
		this.setYAxisLabel("kWh");
		initPreferences();
	}

	//////////////////////////////////////////////////////////////////////////////////
	// PRIVATE CLASSES //
	//////////////////////////////////////////////////////////////////////////////////
	
	private void adjustFont( FontDefinition font ) {
		font.setSize( 6 );
		font.setName( "Verdana" );
	}
	
	private DateTimeDataSet getTimeValues() {
		return DateTimeDataSetImpl.create( new Calendar[]{
				new CDateTime( 2011, 3, 30, 1, 0 ),
				new CDateTime( 2011, 3, 30, 2, 0 ),
				new CDateTime( 2011, 3, 30, 3, 0 ),
				new CDateTime( 2011, 3, 30, 4, 0 ),
				new CDateTime( 2011, 3, 30, 5, 0 ),
				new CDateTime( 2011, 3, 30, 6, 0 ),
				new CDateTime( 2011, 3, 30, 7, 0 ),
				new CDateTime( 2011, 3, 30, 8, 0 ),
				new CDateTime( 2011, 3, 30, 9, 0 ),
				new CDateTime( 2011, 3, 30, 10, 0 ),
				new CDateTime( 2011, 3, 30, 11, 0 ),
				new CDateTime( 2011, 3, 30, 12, 0 ),
				new CDateTime( 2011, 3, 30, 13, 0 ),
				new CDateTime( 2011, 3, 30, 14, 0 ),
				new CDateTime( 2011, 3, 30, 15, 0 ),
				new CDateTime( 2011, 3, 30, 16, 0 ),
				new CDateTime( 2011, 3, 30, 17, 0 ),
				new CDateTime( 2011, 3, 30, 18, 0 ),
				new CDateTime( 2011, 3, 30, 19, 0 ),
				new CDateTime( 2011, 3, 30, 20, 0 ),
				new CDateTime( 2011, 3, 30, 21, 0 ),
				new CDateTime( 2011, 3, 30, 22, 0 ),
				new CDateTime( 2011, 3, 30, 23, 0 ),
				new CDateTime( 2011, 3, 30, 24, 0 )
		} );
	}
	
	private StockDataSet getStockValues() {
		KPIStockData stockData = new KPIStockData(
				EnterpriseReportDAO.getDeviationForSlicesInPeriod());
		return StockDataSetImpl.create(stockData.getStockEntryList());
//		return StockDataSetImpl.create( new StockEntry[]{
//				new StockEntry( 127.15, 125.28, 128.01, 127.16 ),
//				new StockEntry( 127.22, 124.80, 128.07, 127.11 ),
//				new StockEntry( 127.00, 125.17, 127.94, 127.07 ),
//				new StockEntry( 126.87, 125.15, 127.83, 127.01 ),
//				new StockEntry( 127.01, 127.00, 128.42, 127.85 ),
//				new StockEntry( 126.87, 125.15, 127.83, 127.01 ),
//				new StockEntry( 126.84, 126.00, 127.78, 126.97 ),
//				new StockEntry( 127.00, 125.17, 127.94, 127.07 ),
//				new StockEntry( 126.01, 125.15, 128.39, 126.95 ),
//				new StockEntry( 127.00, 124.76, 127.80, 126.96 ),
//				new StockEntry( 127.15, 125.28, 128.01, 127.16 ),
//				new StockEntry( 127.22, 124.80, 128.07, 127.11 ),
//				new StockEntry( 127.00, 125.17, 127.94, 127.07 ),
//				new StockEntry( 126.87, 125.15, 127.83, 127.01 ),
//				new StockEntry( 127.01, 127.00, 128.42, 127.85 ),
//				new StockEntry( 126.87, 125.15, 127.83, 127.01 ),
//				new StockEntry( 126.84, 126.00, 127.78, 126.97 ),
//				new StockEntry( 127.00, 125.17, 127.94, 127.07 ),
//				new StockEntry( 126.01, 125.15, 128.39, 126.95 ),
//				new StockEntry( 127.00, 125.17, 127.94, 127.07 ),
//				new StockEntry( 127.15, 125.28, 128.01, 127.16 ),
//				new StockEntry( 127.22, 124.80, 128.07, 127.11 ),
//				new StockEntry( 127.00, 125.17, 127.94, 127.07 ),
//				new StockEntry( 126.87, 125.15, 127.83, 127.01 )
//		} );	
	}

	//////////////////////////////////////////////////////////////////////////////////
	// PUBLIC CLASSES //
	//////////////////////////////////////////////////////////////////////////////////

	public Chart createChart() {

		ChartWithAxes cwaStock = ChartWithAxesImpl.create( );
		cwaStock.setType( "Stock Chart" );	
		cwaStock.setSubType( "Standard Stock Chart" );

		// Title
		cwaStock.getTitle( ).getLabel( ).getCaption( ).setValue( "Energy Level Consumption (kWh)" );

		// Plot
		cwaStock.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		cwaStock.getPlot( ).getClientArea( ).setBackground( ColorDefinitionImpl.WHITE( ) );

		// Legend
		cwaStock.getLegend( ).setVisible( false );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaStock ).getPrimaryBaseAxes( )[0];
		xAxisPrimary.setLabelPosition( Position.BELOW_LITERAL );
		xAxisPrimary.setType( AxisType.DATE_TIME_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
		xAxisPrimary.setCategoryAxis( true );
		xAxisPrimary.setFormatSpecifier( JavaDateFormatSpecifierImpl.create("hh aa") );
		xAxisPrimary.getLabel().getCaption().getFont().setRotation(90.0);

		// Y-Axis
		Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaStock ).getPrimaryOrthogonalAxis( xAxisPrimary );

		yAxisPrimary.getScale( ).setMin( NumberDataElementImpl.create( 0.0 ) );
		yAxisPrimary.getScale( ).setMax( NumberDataElementImpl.create( 10.0 ) );
		yAxisPrimary.getScale( ).setStep( 10.0 );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );

		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );

		// Data Set
		DateTimeDataSet dsTimeValues = getTimeValues();
		StockDataSet dsStockValues = getStockValues();
		
		// X-Series
		Series seBase = SeriesImpl.create( );

		seBase.setDataSet(dsTimeValues);
		
		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seBase );

		// Y-Series
		StockSeries ss = (StockSeries) StockSeriesImpl.create( );
		ss.getLineAttributes( ).setColor( ColorDefinitionImpl.create(180, 0, 0) );
		ss.getLineAttributes().setThickness(2);
		ss.setDataSet( dsStockValues );
		ss.setTranslucent( true );
		ss.setCurveFitting( CurveFittingImpl.create( ) );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).shift( -1 );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( ss );

		adjustFont(xAxisPrimary.getTitle().getCaption().getFont());
		adjustFont(yAxisPrimary.getTitle().getCaption().getFont());

		return cwaStock;
	}

	///////////////////////////////////////////////////////////////////////////////////////////	
	//	PREFERENCES AREA
	///////////////////////////////////////////////////////////////////////////////////////////

	private void initPreferences() {
		IPreferenceStore preferences = PlatformUI.getPreferenceStore();
		// Remember, the node method creates the preference node if it does not already exist
		preferences.addPropertyChangeListener((org.eclipse.jface.util.IPropertyChangeListener) propertyChangeListener);
	}

	/////////////////////////////////////////////////////////////////////
	//	Private classes supporting the view							   //	
	/////////////////////////////////////////////////////////////////////

	private final IPropertyChangeListener propertyChangeListener = new IPropertyChangeListener(){
		@Override
		public void propertyChange(PropertyChangeEvent event) {
			IPreferenceStore preferences = PlatformUI.getPreferenceStore();
			if (event.getProperty().equals(
					PreferenceConstants.P_STOCKTICKERCOLOR)) {
				@SuppressWarnings("unused")
				Object rgbColor = PreferenceConverter.getColor(preferences, PreferenceConstants.P_STOCKTICKERCOLOR);
						// preferences.getBoolean(PreferenceConstants.P_STOCKTICKERCOLOR); 
//				birtChart.getLegend().setVisible(showLegend);
			}
		}
	};

}