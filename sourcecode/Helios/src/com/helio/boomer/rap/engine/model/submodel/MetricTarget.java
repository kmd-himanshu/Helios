package com.helio.boomer.rap.engine.model.submodel;

import java.util.ArrayList;
import java.util.List;

import com.helio.app.boomer.common.dal.model.BoomerMetric;
import com.helio.boomer.rap.engine.model.enumeration.PhysicalLocationType;

public class MetricTarget {

	protected List<BoomerMetric> targetMetricList = new ArrayList<BoomerMetric>();
	
	protected PhysicalLocationType locationType;
	
	public MetricTarget(PhysicalLocationType locationType) {
		this.locationType = locationType;
	}
	
}
