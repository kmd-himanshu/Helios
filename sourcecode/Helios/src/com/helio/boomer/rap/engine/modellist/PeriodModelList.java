package com.helio.boomer.rap.engine.modellist;

import java.util.List;

import com.google.common.collect.Lists;
import com.helio.boomer.rap.engine.AbstractModelObject;
import com.helio.boomer.rap.engine.model.Period;

public class PeriodModelList extends AbstractModelObject {
	
	private List<Period> periodList;
	
	public PeriodModelList() {
		this.periodList = Lists.newArrayList();
	}
	
	public PeriodModelList(List<Period> periodList) {
		this.periodList = periodList;
	}
	
	public List<Period> getPeriodList() {
		return periodList;
	}
	
	public void addPeriod(Period period) {
		periodList.add(period);
		this.firePropertyChange("periodList", null, null);
	}
	
	public void removePeriod(Period period) {
		periodList.remove(period);
		this.firePropertyChange("periodList", null, null);
	}

}
