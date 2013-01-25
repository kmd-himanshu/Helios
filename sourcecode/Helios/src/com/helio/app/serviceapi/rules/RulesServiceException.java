package com.helio.app.serviceapi.rules;


/**
 * Exception handling specific to Helio Rules Service
 * @author RES
 *
 */
public class RulesServiceException extends Exception {
	private static final long serialVersionUID = 1L;
	public RulesServiceException (){super();}
	public RulesServiceException(String msg){super (msg);}
	public RulesServiceException(String msg, Throwable e){super (msg, e);}
	public RulesServiceException(Throwable e){super (e);}

}
