package com.helio.app.serviceapi.kpi;

public class HelioInvalidKPIException extends Exception {
	private static final long serialVersionUID = 1L;
	public HelioInvalidKPIException (){super();}
	public HelioInvalidKPIException(String msg){super (msg);}
	public HelioInvalidKPIException(String msg, Throwable e){super (msg, e);}
	public HelioInvalidKPIException(Throwable e){super (e);}
}
