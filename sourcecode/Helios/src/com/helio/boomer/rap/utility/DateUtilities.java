package com.helio.boomer.rap.utility;

import java.sql.Date;
import java.util.Calendar;

import org.eclipse.swt.widgets.DateTime;

public class DateUtilities {

	public static Date getDateFromWidget(DateTime dtWidget) throws IllegalArgumentException {
		return Date.valueOf(
					dtWidget.getYear() 							+ "-" 
				+	String.format("%02d", dtWidget.getMonth())	+ "-" 
				+	String.format("%02d", dtWidget.getDay()));
	}
	
	/*
	 * Note: can decrement by specifying negative values
	 */
	public static Date incrementDateByDay(Date sourceDate, int dayIncrement) {
		if (dayIncrement == 0) return sourceDate;
		Calendar cal = Calendar.getInstance();
		cal.setTime(sourceDate);
		cal.add(Calendar.DATE, dayIncrement);
		return new Date(cal.getTime().getTime());
	}

}
