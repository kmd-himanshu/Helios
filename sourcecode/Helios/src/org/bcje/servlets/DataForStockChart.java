package org.bcje.servlets;


import org.eclipse.birt.chart.datafeed.StockEntry;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;

public class DataForStockChart {

public static IDataRowExpressionEvaluator getChartData(){


final Calendar[] dsDateValues = new Calendar[]{

new CDateTime( 2004, 12, 27 ),

new CDateTime( 2004, 12, 23 ),

new CDateTime( 2004, 12, 22 ),

new CDateTime( 2004, 12, 21 ),

new CDateTime( 2004, 12, 20 ),

new CDateTime( 2004, 12, 17 ),

new CDateTime( 2004, 12, 16 ),

new CDateTime( 2004, 12, 15 )

};



final StockEntry[] dsStockValues = new StockEntry[]{


new StockEntry( 27.01, 26.82, 28.02, 26.85 ),

new StockEntry( 26.87, 26.15, 28.83, 27.01 ),

new StockEntry( 26.84, 26.15, 28.78, 26.97 ),

new StockEntry( 27.00, 26.17, 29.94, 27.07 ),

new StockEntry( 27.01, 26.15, 28.89, 26.95 ),

new StockEntry( 27.00, 26.32, 29.80, 26.96 ),

new StockEntry( 27.15, 26.28, 28.01, 27.16 ),

new StockEntry( 27.22, 26.40, 29.07, 27.11 )

};


final double[] dsStockVolume = new double[]{

55958500,

65801900,

63651900,

94646096,

85552800,

126184400,

88997504,

106303904

} ;


//return new SimpleDataRowExpressionEvaluator( set, data);

return new IDataRowExpressionEvaluator( ) {

int idx = 0;

public void close( )
{
}

public Object evaluate( String expression )
{
if ( "date".equals( expression ) )
{
return dsDateValues[idx];
}
else if ( "volume".equals( expression ) )
{
return dsStockVolume[idx];
}
else if ( "high".equals( expression ) )
{
return dsStockValues[idx].getHigh();
} 
else if ( "low".equals( expression ) )
{
return dsStockValues[idx].getLow();
} 
else if ( "open".equals( expression ) )
{
return dsStockValues[idx].getOpen();
} 
else if ( "close".equals( expression ) )
{
return dsStockValues[idx].getClose();
}
return expression;
}

public Object evaluateGlobal( String expression )
{
return evaluate( expression );
}

public boolean first( )
{
idx = 0;
return true;
}

public boolean next( )
{
idx++;
return ( idx < 8 );
}
};



}
}


