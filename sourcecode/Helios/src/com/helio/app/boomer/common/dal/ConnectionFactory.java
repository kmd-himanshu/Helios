package com.helio.app.boomer.common.dal;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionFactory {
	private static Log LOG = LogFactory.getLog("ConnectionFactory");
	private static Properties PROPS = new Properties();
	private DataSource ds = null;
	private static ConnectionFactory instance = null;
	private ConnectionPool connectionPool = null;
	
	private ConnectionFactory() {
		init();
	}

	public static ConnectionFactory getInstance()
	{
		if (instance == null) 
		{
			instance = new ConnectionFactory();
		}
		return instance;
	}
	
	private synchronized void init() {
		if (ds != null) {
			return;
		}
		LOG.info("Initalized ConnectionFactory");
		try {	
			getProperties();
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup((String)PROPS.get("initial.context"));
			ds = (DataSource) envCtx.lookup(PROPS.getProperty("jndi.data.source"));
		} 
		catch (NamingException ne)
		{
			LOG.error("ConnectionFactory NamingException error. Message: " + ne.getMessage());
		}
		catch (Exception ex) {
			LOG.error("ConnectionFactory error. Message: " + ex.getMessage());
		}
	}
	
	/*
	 * This 
	 */
	private Connection getConnection2() throws SQLException {
	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user",     (String)PROPS.get("dbuser"));
	    connectionProps.put("password", (String)PROPS.get("dbpswd"));
	    conn = DriverManager.getConnection((String)PROPS.get("mysql.jdbc.data.source"), connectionProps);
	    LOG.debug("Connected to database using JDBC");
	    return conn;
	}
	
	public Connection getConnection() {
		Connection connection = null;
		if (connectionPool != null) {
			try {
				LOG.debug("*** Connection obtained from connection pool ***");
				return connectionPool.checkout();
			}
			catch (SQLException ex) {
				connectionPool = null;
			}
		}
		try {
			if (ds == null) {
				init();
			}			
			connection = ds.getConnection();
		} catch (Exception e) {
			LOG.error("*** ConnectionFactory unable to get JNDI connection ***");
		}
		if (connection == null) {
			try {
				if (connectionPool == null) {
					connectionPool = new ConnectionPool(
							(String)PROPS.get("mysql.jdbc.data.source"),
							(String)PROPS.get("dbuser"), 
							(String)PROPS.get("dbpswd"));
				}
				connection = connectionPool.checkout();
				LOG.info("*** Connections obtained from local connection pool ***");
			}
			catch (Exception ex) {
				DAOException dbe = new DAOException("Unexpected exception while connecting", ex);
				LOG.error("*** ConnectionFactory unable to get connection from local connection pool ***", dbe);
			}
		}
		if (connection == null) {
			try {
				LOG.debug("*** ATTEMPTING TO CONNECT THROUGH JDBC ***");
				// If not using App server, try JDBC
				connection = getConnection2();
			}
			catch (Exception ex) {
				DAOException dbe = new DAOException("Unexpected exception while connecting", ex);
				LOG.error("*** ConnectionFactory unable to get JDBC connection ***", dbe);
			}
		}
		return connection;
	}

	/**
	 * Close any open DB resources 
	 * 
	 */
	public static final void closeAll(Connection conn, Statement stmt, ResultSet rs) 
	{
		LOG.debug(ConnectionFactory.class.getName() + ".closeAll()");

		try
		{
			if (rs != null)
			{
				try
				{
					rs.close();
				}
				catch (SQLException e)
				{
					DAOException dbe = new DAOException("Unable to close ResultSet", e);
					LOG.error("ConnectionFactory error: Unable to close ResultSet", dbe);
				}
			}

			if (stmt != null)
			{
				try
				{
					stmt.close();
				}
				catch (SQLException e)
				{
					DAOException dbe = new DAOException("Unable to close Statement", e);
					LOG.error("ConnectionFactory error: Unable to close Statement ", dbe);

				}
			}

			if (conn != null)
			{
				if (ConnectionFactory.getInstance().connectionPool != null) {
					ConnectionFactory.getInstance().connectionPool.checkin(conn);
					LOG.debug("*** Connection returned to connection pool ***");

				}
				else 
					try
					{
						if (!conn.isClosed())
						{
							conn.close();
						}
					}
					catch (SQLException e)
					{
						DAOException dbe = new DAOException("Unable to close Connection to database", e);
						LOG.error("ConnectionFactory error: Unable to close Connection to database: ", dbe);
					}
			}

		}
		finally
		{
			conn = null;
			stmt = null;
			rs = null;
		}
	}
	
	private synchronized void getProperties() 
	{
		
		try {
			PROPS.load( ConnectionFactory.class.getResource( "dal.properties" ).openStream() );
		} 
		catch (Exception e) {
			LOG.error("*** Exception while loading DAL properties *** " + e.toString());
			e.printStackTrace();
		}
	}
}
