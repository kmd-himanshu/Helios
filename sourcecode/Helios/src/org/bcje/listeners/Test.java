package org.bcje.listeners;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;

import com.helio.boomer.rap.engine.PeriodListController;
import com.helio.boomer.rap.engine.model.Period;
import com.helio.boomer.rap.engine.servicedata.BusinessUnitReportDAO;

public class Test {
	
	/*public static void main(String[] args) {
		
		String[] array=new String[2];
		//array[0]="a";
		//array[1]="b";
		
		String value="";
		if(array!=null)
		{
			value=Arrays.toString(array);
			
			value=value.replaceAll("\\[","");
			value=value.replaceAll("\\]","");
			System.out.println(value);
		}
		
		
		Map<String, Double> valueMap =new HashMap<String, Double>();
		List<Long> buildingIds=new ArrayList<Long>();
		buildingIds.add(1901l);
		buildingIds.add(1801l);
		List<Period> periods=Arrays.asList(PeriodListController.getInstance().getPeriodModelListAsArray());
		Period period=null;
		
		
		try {
			for(int i=0;i<periods.size();i++)
			{
				period=periods.get(i);
				period.setStartDt(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2011-08-14").getTime()));
				period.setEndDt(new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2011-09-10").getTime()));
				period.setPeriodName("Period 9 2011");
				//periods.add(period);
			}
		
		List<Long> longs=new ArrayList<Long>();
		longs.add(new Long(1801));
		
		      Map<String, Double> currentValueMap = BusinessUnitReportDAO
				.getEnergyPerBuildingPerSquareFoot(longs,
						periods, false);
		
			//valueMap = BusinessUnitReportDAO.getEnergyPerBuildingPerSquareFoot(chart.getLocationIds(),chart.getPeriods(), false);
			TextDataSet categoryValues = TextDataSetImpl.create( currentValueMap.keySet() );
			System.out.println(categoryValues);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}*/
	
	public static void main(String[] args) {
		String str="1000|NULL|NULL|NULL|DIVISION";
		String[] strArr=str.split("\\|");
		System.out.println(strArr[0]);
	}

}
