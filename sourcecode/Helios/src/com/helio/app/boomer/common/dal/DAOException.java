package com.helio.app.boomer.common.dal;


/**
 * Exception handling specific to Helio
 * @author RES
 *
 */
public class DAOException extends Exception {
	private static final long serialVersionUID = 1L;
	public DAOException (){super();}
	public DAOException(String msg){super (msg);}
	public DAOException(String msg, Throwable e){super (msg, e);}
	public DAOException(Throwable e){super (e);}

}
