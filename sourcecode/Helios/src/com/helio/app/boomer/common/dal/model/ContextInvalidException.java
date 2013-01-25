package com.helio.app.boomer.common.dal.model;


/**
 * Exception handling specific to Helio
 * @author RES
 *
 */
public class ContextInvalidException extends Exception {
	private static final long serialVersionUID = 1L;
	public ContextInvalidException (){super();}
	public ContextInvalidException(String msg){super (msg);}
	public ContextInvalidException(String msg, Throwable e){super (msg, e);}
	public ContextInvalidException(Throwable e){super (e);}

}
