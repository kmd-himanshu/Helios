package com.helio.app.boomer.common.dal;

public class DAODuplicateNotAllowedException extends DAOException {
	private static final long serialVersionUID = 1L;
	public DAODuplicateNotAllowedException (){super();}
	public DAODuplicateNotAllowedException(String msg){super (msg);}
	public DAODuplicateNotAllowedException(String msg, Throwable e){super (msg, e);}
	public DAODuplicateNotAllowedException(Throwable e){super (e);}

}
