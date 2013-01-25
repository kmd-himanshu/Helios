package com.helio.app.boomer.common.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConnectionPool implements Runnable
{    
	private static Log LOG = LogFactory.getLog("ConnectionPool");
	
    // Number of initial connections to make.
    private int m_InitialConnectionCount = 15;    
    // A list of available connections for use.
    private Vector<Connection> m_AvailableConnections = new Vector<Connection>();
    // A list of connections being used currently.
    private Vector<Connection> m_UsedConnections = new Vector<Connection>();
    // The URL string used to connect to the database
    private String m_URLString = null;
    // The username used to connect to the database
    private String m_UserName = null;    
    // The password used to connect to the database
    private String m_Password = null;    
    // The cleanup thread
    private Thread m_CleanupThread = null;
    // The keep-alive SQL statement
    private String keepAliveSQL = "select 1 from dual";
                                             
    //Constructor
    public ConnectionPool(String urlString, String user, String passwd) throws SQLException
    {
        // Initialize the required parameters
        m_URLString = urlString;
        m_UserName = user;
        m_Password = passwd;

        for(int cnt=0; cnt<m_InitialConnectionCount; cnt++)
        {
            // Add a new connection to the available list.
            m_AvailableConnections.addElement(getConnection());
        }
        LOG.info("CREATED " + m_AvailableConnections.size() + " CONNECTIONS");

        
        // Create the cleanup thread
        m_CleanupThread = new Thread(this);
        m_CleanupThread.start();
    }    
    
    private Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(m_URLString, m_UserName, m_Password);
    }
    
    /**
     * Attempts to get a connection. If for whatever reason the 
     * attempt failed, it tries again. This helps get past errors
     * where MySQL can timeout a connection.
     * @return Connection
     * @throws SQLException if connection not valid or not found. 
     */
    public synchronized Connection checkout() throws SQLException
    {
        Connection conn = null;
    	try {
    		conn = tryCheckout();
    		return conn;
    	}
    	catch (SQLException ex) {
    		/*
    		 * If connection timed out then try one more time
    		 */
    		conn = tryCheckout();
    	}
    	return conn;
    }
    private Connection tryCheckout() throws SQLException
    {
        Connection newConnxn = null;
        
        if(m_AvailableConnections.size() == 0)
        {
            // Im out of connections. Create one more.
             newConnxn = getConnection();
            // Add this connection to the "Used" list.
             m_UsedConnections.addElement(newConnxn);
            // We dont have to do anything else since this is
            // a new connection.
        }
        else
        {
            // Connections exist !
            // Get a connection object
            newConnxn = (Connection)m_AvailableConnections.lastElement();
            // Remove it from the available list.
            m_AvailableConnections.removeElement(newConnxn);
            // Add it to the used list.
            m_UsedConnections.addElement(newConnxn);            
        }        
        newConnxn.isValid(2); // Validate the connection
        //System.out.println("RETURNING A CONNECTION : Available Connections : " + availableCount());

        // Either way, we should have a connection object now.
        return newConnxn;
    }
    

    public synchronized void checkin(Connection c)
    {
        if(c != null)
        {
            // Remove from used list.
            m_UsedConnections.removeElement(c);
            // Add to the available list
            m_AvailableConnections.addElement(c);        
        }
    }            
    
    public int availableCount()
    {
        return m_AvailableConnections.size();
    }
    
    public void run()
    {
        try
        {
            while(true)
            {
                synchronized(this)
                {
                    while(m_AvailableConnections.size() > m_InitialConnectionCount)
                    {
                        // Clean up extra available connections.
                        Connection c = (Connection)m_AvailableConnections.lastElement();
                        m_AvailableConnections.removeElement(c);
                        
                        // Close the connection to the database.
                        c.close();
                    }
                    
                    // Clean up is done
                }
                
                LOG.info("CLEANUP : Available Connections : " + availableCount());
                // Now do something to keep the connections from timing out
                keepAliveConnections();
                // Now sleep for 10 minutes
                Thread.sleep(60000 * 10);
            }    
        }
        catch(SQLException sqle)
        {
            sqle.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    private void keepAliveConnections()
    {
        synchronized(this)
        {
        	for (int i=0; i<m_AvailableConnections.size(); i++)
            {
        		try {
                    // Clean up extra available connections.
            		Connection conn = (Connection)m_AvailableConnections.get(i);
            		PreparedStatement stmt = conn.prepareStatement(keepAliveSQL);
                    stmt.executeQuery();
        		}
        		catch (Exception ex) {
        			LOG.error("ERROR IN CONNECTION POOL KEEP ALIVE FUNCTION");
        			ex.printStackTrace();
        		}
            }
        	LOG.info("KEEP ALIVE PERFORMED ON " + m_AvailableConnections.size() + " CONNECTIONS");
        }

    }
}
