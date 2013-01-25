package com.helio.app.serviceapi;

public class HelioInvalidIdException extends Exception {
	private static final long serialVersionUID = 1L;
	public HelioInvalidIdException (){super();}
	public HelioInvalidIdException(String msg){super (msg);}
	public HelioInvalidIdException(String msg, Throwable e){super (msg, e);}
	public HelioInvalidIdException(Throwable e){super (e);}

}
