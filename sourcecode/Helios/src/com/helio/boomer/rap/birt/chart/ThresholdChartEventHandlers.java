package com.helio.boomer.rap.birt.chart;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.render.ISeriesRenderer;
import org.eclipse.birt.chart.script.ChartEventHandlerAdapter;
import org.eclipse.birt.chart.script.IChartScriptContext;

public class ThresholdChartEventHandlers extends ChartEventHandlerAdapter {

	@Override
	public void beforeDrawDataPoint(
			DataPointHints dph, Fill fill, IChartScriptContext icsc) {
		Number val = (Number) dph.getOrthogonalValue();
		if (fill instanceof ColorDefinition) {
			ColorDefinition cd = (ColorDefinition) fill.copyInstance();
			if (val.floatValue() < 2.0) {
				((ColorDefinition) fill).set(cd.darker().getRed(), cd.darker().getGreen(), cd.darker().getBlue());
			} else {
				((ColorDefinition) fill).set(cd.brighter().getRed(), cd.brighter().getGreen(), cd.brighter().getBlue());
			}
		}
		super.beforeDrawDataPoint(dph, fill, icsc);
	}
	
	@Override
	public void beforeDrawDataPointLabel(DataPointHints dph, Label label,
			IChartScriptContext icsc) {
		label.getCaption().setValue("Testing");
		dph.getSeriesDisplayValue();
		super.beforeDrawDataPointLabel(dph, label, icsc);
	}

	@Override
	public void beforeDrawSeries(Series series, ISeriesRenderer isr,
			IChartScriptContext icsc) {
		// TODO Auto-generated method stub
		super.beforeDrawSeries(series, isr, icsc);
	}
	
}
