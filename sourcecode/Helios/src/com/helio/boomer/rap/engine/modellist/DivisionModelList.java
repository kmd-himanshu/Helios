package com.helio.boomer.rap.engine.modellist;

import java.util.List;

import com.google.common.collect.Lists;
import com.helio.boomer.rap.engine.AbstractModelObject;
import com.helio.boomer.rap.engine.model.Division;

public class DivisionModelList extends AbstractModelObject {
	
	private List<Division> divisionList;
	
	public DivisionModelList() {
		this.divisionList = Lists.newArrayList();
	}
	
	public DivisionModelList(List<Division> divisionList) {
		this.divisionList = divisionList;
	}
	
	public List<Division> getDivisionList() {
		return divisionList;
	}
	
	public void addDivision(Division division) {
		divisionList.add(division);
		this.firePropertyChange("divisionList", null, null);
	}
	
	public void removeDivision(Division division) {
		divisionList.remove(division);
		this.firePropertyChange("divisionList", null, null);
	}

}