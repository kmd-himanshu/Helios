package com.helio.app.serviceapi;

public class HelioMissingDeviceMonitorException extends Exception {
	private static final long serialVersionUID = 1L;
	public HelioMissingDeviceMonitorException (){super();}
	public HelioMissingDeviceMonitorException(String msg){super (msg);}
	public HelioMissingDeviceMonitorException(String msg, Throwable e){super (msg, e);}
	public HelioMissingDeviceMonitorException(Throwable e){super (e);}
}
