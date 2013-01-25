package com.helio.app.boomer.common.dal;

public class DAOInsufficientRightsException extends DAOException {
	private static final long serialVersionUID = 1L;
	public DAOInsufficientRightsException (){super();}
	public DAOInsufficientRightsException(String msg){super (msg);}
	public DAOInsufficientRightsException(String msg, Throwable e){super (msg, e);}
	public DAOInsufficientRightsException(Throwable e){super (e);}

}
