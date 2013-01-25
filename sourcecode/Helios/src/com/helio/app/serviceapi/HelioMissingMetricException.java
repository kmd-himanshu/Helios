package com.helio.app.serviceapi;

public class HelioMissingMetricException extends Exception {
	private static final long serialVersionUID = 1L;
	public HelioMissingMetricException (){super();}
	public HelioMissingMetricException(String msg){super (msg);}
	public HelioMissingMetricException(String msg, Throwable e){super (msg, e);}
	public HelioMissingMetricException(Throwable e){super (e);}
}
