package com.helio.app.boomer.common.dal;

public class DAONotFoundException extends DAOException {
	private static final long serialVersionUID = 1L;
	public DAONotFoundException (){super();}
	public DAONotFoundException(String msg){super (msg);}
	public DAONotFoundException(String msg, Throwable e){super (msg, e);}
	public DAONotFoundException(Throwable e){super (e);}

}
