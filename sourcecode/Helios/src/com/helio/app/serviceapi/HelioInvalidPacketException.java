package com.helio.app.serviceapi;

public class HelioInvalidPacketException extends Exception {
	private static final long serialVersionUID = 1L;
	public HelioInvalidPacketException (){super();}
	public HelioInvalidPacketException(String msg){super (msg);}
	public HelioInvalidPacketException(String msg, Throwable e){super (msg, e);}
	public HelioInvalidPacketException(Throwable e){super (e);}
}
