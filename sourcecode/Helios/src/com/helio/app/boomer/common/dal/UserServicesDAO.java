package com.helio.app.boomer.common.dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.helio.app.boomer.common.dal.model.Access;
import com.helio.app.boomer.common.dal.model.AccessType;
import com.helio.app.boomer.common.dal.model.Client;
import com.helio.app.boomer.common.dal.model.Division;
import com.helio.app.boomer.common.dal.model.Location;
import com.helio.app.boomer.common.dal.model.Role;
import com.helio.app.boomer.common.dal.model.User;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * Data Access Object for Core Services
 * This is a singleton. 
 * 
 * @author rickschwartz
 *
 */
public class UserServicesDAO extends DataAccessObject {

	/*
	 * singleton instance of this object
	 */
	private static UserServicesDAO instance = null;
	/*
	 * Cache store for values retrieved by this DAO
	 */
	private static HashMap<String, Object> cache = new HashMap<String, Object>();
	/*
	 * Commons logging 
	 */
	private static Log LOG = LogFactory.getLog("UserServicesDAO");

	/*
	 * enforces singleton
	 */
	private UserServicesDAO() {
		super();
	}

	/**
	 * Gets a singleton of the DAO
	 * @return singleton instance of this DAO
	 */
	public synchronized static UserServicesDAO getInstance() 
	{
		if (instance == null) {
			instance = new UserServicesDAO();
		}
		return instance;
	}

	/**
	 * Adds a user 
	 * @param pUser: populated User object
	 * @return User object with id containing DB key  
	 * @throws DAOException when cannot save the User
 	 */
	public User addUser(User pUser) 
		throws DAODuplicateNotAllowedException, DAOException 
	{
		LOG.debug("addUser " + pUser.getFirstName() + " " + pUser.getLastName());
		
		long seq = getNextSequence();		// get the next unique ID
		PreparedStatement prepareStmt = null;
        Connection conn = null;
        String sql = getProperty("insertPEUserSQL");
        
        try {
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(sql);
			prepareStmt.setLong(1, seq);								// PE_USER.id
			prepareStmt.setString(2, pUser.getFirstName());
			prepareStmt.setString(3, pUser.getLastName());
			prepareStmt.setString(4, pUser.getUserName());
			prepareStmt.setString(5, pUser.getEmailAddr());
			prepareStmt.setString(6, pUser.getPassword());
			prepareStmt.setBoolean(7, pUser.isGlobalAdmin());
			prepareStmt.setBoolean(8, pUser.isClientAdmin());
			prepareStmt.setDate(9, pUser.getExpire());
			prepareStmt.executeUpdate();  								// insert a PE_USER
			prepareStmt = conn.prepareCall(getProperty("insertUserActionInsert1SQL"));
			prepareStmt.setLong(1, seq);								// PE_USER.id
			prepareStmt.executeUpdate();  								// insert a USER_ACTION
			prepareStmt = conn.prepareCall(getProperty("insertUserActionInsert2SQL"));
			prepareStmt.setLong(1, seq);								// PE_USER.id
			prepareStmt.executeUpdate();  								// insert a USER_ACTION
			prepareStmt = conn.prepareCall(getProperty("insertUserActionInsert3SQL"));
			prepareStmt.setLong(1, seq);								// PE_USER.id
			prepareStmt.executeUpdate();  								// insert a USER_ACTION
			prepareStmt = conn.prepareCall(getProperty("insertUserActionInsert4SQL"));
			prepareStmt.setLong(1, seq);								// PE_USER.id
			prepareStmt.executeUpdate();  								// insert a USER_ACTION
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (MySQLIntegrityConstraintViolationException ex) {
        	LOG.error("ERROR while inserting PE_USER: " + pUser.getUserName() 
        			+ ": " +  ex.getMessage());
        	throw new DAODuplicateNotAllowedException(ex);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while inserting PE_USER: " + pUser.getUserName() 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }
        pUser.setId(seq);

        /*
         * CLIENT AND LOCATION ARE HANDLED DIFFERENT METHODS
         */
        if (pUser.getClient() != null) {
        	updateUser(pUser);
        }        
        if (pUser.getLocations() != null &&  pUser.getLocations().size() > 0) {
        	addUserLocations(pUser);
        }
		return pUser;
	}
	/**
	 * Adds a list of Locations for the User
	 * @param pUser: user object containing a list of locations
	 * @throws DAOException when cannot save the locations
 	 */
	private User addUserLocations(User pUser) 
		throws DAOException 
	{
		LOG.debug("addUserLocations for " + pUser.getFirstName() + " " + pUser.getLastName());
		
		List<Location>  locations  = pUser.getLocations();
		if (locations == null || locations.size() < 1) {
			return pUser;
		}
		
		PreparedStatement prepareStmt = null;
		try {
	        Connection conn = ConnectionFactory.getInstance().getConnection();
	        String sql = null;        
	        Iterator<Location> itrLoc = locations.iterator();
	        while (itrLoc.hasNext()){
	        	Location location = itrLoc.next();
				sql = getProperty("insertUserLocationSQL");
				prepareStmt = conn.prepareCall(sql);
				prepareStmt.setLong(1, getNextSequence());	// USER_LOCATION.id
				prepareStmt.setLong(2, location.getId());
				prepareStmt.setLong(3, pUser.getId());
				prepareStmt.executeUpdate();  				// insert a USER_LOCATION
	        }
		}
        catch (SQLException ex) {
        	LOG.error("ERROR while inserting USER_LOCATION for user: " + pUser.getUserName() 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }
        finally {
    		ConnectionFactory.closeAll(null, prepareStmt, null);
        }
		return pUser;
	}


	/**
	 * Deletes a user 
	 * @param pUserId: Id of user to delete
	 * @throws DAOException when cannot delete the User
 	 */
	public void deleteUser(Long pUserId) 
		throws DAOException 
	{
		LOG.debug("deleteUser:" + pUserId);
		
		/*
		 * FIRST DELETE ANY USER_LOCATIONS
		 */
		deleteUserLocations(pUserId); 
		
		PreparedStatement prepareStmt = null;
        Connection conn = null;
        
        try {
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(getProperty("deletePEUserWithIdSQL"));
			prepareStmt.setLong(1, pUserId);							// PE_USER.id
			prepareStmt.executeUpdate();  								// delete a PE_USER
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while deleting PE_USER: " + pUserId 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }

		return;
	}
	/**
	 * Deletes UserLocations by User 
	 * @param pUserId: Id of user to delete
	 * @throws DAOException when cannot delete the User
 	 */
	private void deleteUserLocations(Long pUserId) 
		throws DAOException 
	{
		LOG.debug("deleteUserLocations for userid:" + pUserId);
		
		PreparedStatement prepareStmt = null;
        Connection conn = null;
        
        try {
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(getProperty("deleteUserLocationsByUserSQL"));
			prepareStmt.setLong(1, pUserId);							// USER_LOCATION.PE_USER_id
			prepareStmt.executeUpdate();  								// deletes all USER_LOCATION
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while deleting USER_LOCATION rows for userid:" + pUserId 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }

		return;
	}
	/**
	 * Retrieves a user and it's associated model
	 * @param pEmailAddr: unique identifier for User
	 * @return User object 
	 * @throws DAONotFoundException when User not found in database
	 * @throws DAOException when cannot retrieve the User
 	 */
	public User getUserByEmailAddr(String pEmailAddr ) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getUserByEmailAddr: " + pEmailAddr);
		User user = getUser(" EMAIL_ADDR = '" + pEmailAddr + "'"); 
        if (user == null) {
    		throw new DAONotFoundException("User not retreived for EmailAddr=" + pEmailAddr );
        }
		return user;
	}
	/**
	 * Retrieves a user and it's associated model
	 * @param pUserName: unique identifier for user
	 * @return User object 
	 * @throws DAONotFoundException when User not found in database
	 * @throws DAOException when cannot retrieve the User
 	 */
	public User getUserByUserName(String pUserName ) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getUserByUserName: " + pUserName);
		User user = getUser(" USERNAME = '" + pUserName + "'"); 
        if (user == null) {
    		throw new DAONotFoundException("User not retreived for USERNAME=" + pUserName );
        }
		return user;
	}
	/**
	 * Retrieves a user and it's associated model
	 * @param pUserId: unique identifier for user
	 * @return User object 
	 * @throws DAONotFoundException when User not found in database
	 * @throws DAOException when cannot retrieve the User
 	 */
	public User getUserById(long pUserId ) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getUserById: " + pUserId);
		User user = getUser(" id = " + pUserId); 
        if (user == null) {
    		throw new DAONotFoundException("User not retreived for Id=" + pUserId );
        }
		return user;
	}
	private User getUser(String pSQLFragment) 
	throws DAOException 
	{
		LOG.debug("getUser " + pSQLFragment);
		
		User 				user 			= null;
	    Connection 			conn 			= null;
	    PreparedStatement 	stmt 			= null;
		ResultSet 			rs 				= null;
		
	    try {
	    	/*
	    	 * Get the user
	    	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("getUserByColumnSQL") + " " + pSQLFragment;
	        stmt = conn.prepareStatement(sqlStr);
	        rs = stmt.executeQuery();
	        if (rs != null) {
	        	if (rs.next()) {
	        		user = getUserFromResultSet(rs);
	        	}
	    	}
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
	    finally {
	        ConnectionFactory.closeAll(conn, stmt, rs);
	    }
		return user;
	}
	
	/**
	 * Retrieves all users for a particular client
	 * @param pClient: Client object for which all users are requested
	 * @return Client object populated with a <List>User; all users for the passed client 
	 * @throws DAONotFoundException when no users found for the passed client
	 * @throws DAOException when cannot retrieve the objects
 	 */
	public Client getUsersForClient(Client pClient ) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getUsersForClient: " + pClient.getName());
		List<User> users = getUsers(" CLIENT_id = " + pClient.getId()); 
        if (users.size() < 1) {
			throw new DAONotFoundException("PE_USERS DO NOT EXIST FOR CLIENT: " + pClient.getId());
		}
        pClient.setUsers(users);
		return pClient;
	}
	private List<User> getUsers(String pSQLFragment) 
	throws DAOException 
	{
		LOG.debug("getUsers " + pSQLFragment);

		List<User>			users			= new ArrayList<User>();
	    Connection 			conn 			= null;
	    PreparedStatement 	stmt 			= null;
		ResultSet 			rs 				= null;
		
	    try {
	    	/*
	    	 * Get the user
	    	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("getUserByColumnSQL") + " " + pSQLFragment;
	        stmt = conn.prepareStatement(sqlStr);
	        rs = stmt.executeQuery();
	        if (rs != null) {
	        	while (rs.next()) {
	        		users.add(getUserFromResultSet(rs));
	        	}
	    	}
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
	    finally {
	        ConnectionFactory.closeAll(conn, stmt, rs);
	    }

		return users;
	}
	
	/**
	 * Retrieves all users for a particular role
	 * @param pRole: Role object for which all users are requested
	 * @return Role object populated with a <List>User; all users for the passed Role 
	 * @throws DAONotFoundException when no users found for the passed Role
	 * @throws DAOException when cannot retrieve the objects
 	 */
	public List<User> getUsersByRole(Role pRole) 
	throws DAONotFoundException, DAOException 
	{
		LOG.debug("getUsersByRole:" + pRole.getName());

		List<User>			users			= new ArrayList<User>();
	    Connection 			conn 			= null;
	    PreparedStatement 	stmt 			= null;
		ResultSet 			rs 				= null;
		
	    try {
	    	/*
	    	 * Get the user
	    	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("getUsersByRoleSQL");
	        stmt = conn.prepareStatement(sqlStr);
	        stmt.setLong(1, pRole.getId());
	        rs = stmt.executeQuery();
	        if (rs != null) {
	        	while (rs.next()) {
	        		users.add(getUserFromResultSet(rs));
	        	}
	    	}
	    }
	    catch (Exception ex) {
	    	LOG.error(ex.getMessage());
	    	throw new DAOException(ex);
	    } 
	    finally {
	        ConnectionFactory.closeAll(conn, stmt, rs);
	    }

        if (users.size() < 1) {
			throw new DAONotFoundException("PE_USERS DO NOT EXIST FOR ROLE: " + pRole.getName());
		}
        
        pRole.setUsers(users);
        
		return users;
	}
	/**
	 * Updates a User 
	 * @param pUser: populated User object
	 * @throws DAOException when cannot save the User
 	 */
	public void updateUser(User pUser) 
	throws DAODuplicateNotAllowedException, DAOException 
	{
		LOG.debug("updateUser " + pUser.getFirstName() + " " + pUser.getLastName());
		
		PreparedStatement prepareStmt = null;
        Connection conn = null;
        
        try {
			conn = ConnectionFactory.getInstance().getConnection();
			String sql = getProperty("updatePEUserSQL");
			
			/*
			 *  CLIENT_id is optional so we need to set it to null when it doesn't exist.
			 */
			sql = sql + ", CLIENT_id="   + (pUser.getClient()   == null ? "null" : pUser.getClient().getId());
			sql = sql + " where id=" + pUser.getId();
			
			prepareStmt = conn.prepareCall(sql);
			prepareStmt.setString(1, pUser.getFirstName());
			prepareStmt.setString(2, pUser.getLastName());
			prepareStmt.setString(3, pUser.getUserName());
			prepareStmt.setString(4, pUser.getEmailAddr());
			prepareStmt.setString(5, pUser.getPassword());
			prepareStmt.setBoolean(6, pUser.isGlobalAdmin());
			prepareStmt.setBoolean(7, pUser.isClientAdmin());
			prepareStmt.setDate(8, pUser.getExpire());
			prepareStmt.executeUpdate();  								// update a PE_USER
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (MySQLIntegrityConstraintViolationException ex) {
        	LOG.error("ERROR while updating PE_USER: " + pUser.getUserName() 
        			+ ": " +  ex.getMessage());
        	throw new DAODuplicateNotAllowedException(ex);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while updating PE_USER: " + pUser.getUserName() 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }
		
		return;
	}
	
	/**
	 * This takes a result set and converts it to a User.
	 * @param pResultSet: contains the user info
	 * @return a fully populated User. 
	 * @throws Exception
	 */
	private User getUserFromResultSet(
			ResultSet pResultSet)  throws Exception
	{		
		User user = new User();
		
		user.setId(pResultSet.getLong(1));
		/*
		 * CLIENT IS OPTIONAL
		 */
		Client client = null;
		long clientId = pResultSet.getLong(2);
		if (clientId > 0) { // In case of a global administrator
			client = getClientById(clientId);
		}
		user.setClient(client);
		
		user.setFirstName(pResultSet.getString(3));
		user.setLastName(pResultSet.getString(4));
		user.setUserName(pResultSet.getString(5));
		user.setEmailAddr(pResultSet.getString(6));
		user.setPassword(pResultSet.getString(7));
		user.setGlobalAdmin(pResultSet.getBoolean(8));
		user.setClientAdmin(pResultSet.getBoolean(9));
		user.setExpire(pResultSet.getDate(10));
		
		/*
		 * LOCATIONS ARE OPTIONAL
		 */
		List<Location>  locations = null;
		try {
			locations = getLocationsForUser(user);
		}
		catch (DAONotFoundException ex) {}
		user.setLocations(locations);
		
		/*
		 * ROLES ARE OPTIONAL 
		 */
		List<Role>  roles = null;
		try {
			roles = getRolesForUser(user);
		}
		catch (DAONotFoundException ex) {}
		user.setRoles(roles);
		

        return user;
	}
	
	/**
	 * Retrieves a list of all Location object for a Client
	 * @param pClient: Client
	 * @return List of Location objects 
	 * @throws DAOException when cannot retrieve the Locations
 	 */
	public List<Location> getLocationsForClient(Client pClient) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getLocationsForClient:" + pClient.getName());
		
		Client client = getClientById(pClient.getId(), true);
		List<Location> locations = new ArrayList<Location>();

		if (client.getDivisions() == null || client.getDivisions().size() < 1 ) {
			throw new DAONotFoundException("LOCATIONS DO NOT EXIST FOR CLIENT: " + client.getName());
		}
		List<Division> divisions = client.getDivisions();
		Iterator<Division> divItr = divisions.iterator();
		while (divItr.hasNext()) {
			Division division = divItr.next();
			if (division.getLocations() != null && division.getLocations().size() > 0 ) {
				locations.addAll(division.getLocations());
			}
		}
		if (locations.size() < 1 ) {
			throw new DAONotFoundException("LOCATIONS DO NOT EXIST FOR CLIENT: " + client.getName());
		}
		
		return locations;
	}
	
	/**
	 * Retrieves a list of all UserLocation objects for a User
	 * @param pUser: User object
	 * @return List of UserLocation objects 
	 * @throws DAOException when cannot retrieve the UserLocations
 	 */
	private List<Location> getLocationsForUser(User pUser) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getLocationsForUser:" + pUser.getId() );
		
		List<Location>  userLocations   	= new ArrayList<Location>();
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the client
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("getUserLocationByUserSQL");
            stmt = conn.prepareStatement(sqlStr);
            stmt.setLong(1, pUser.getId());
            rs = stmt.executeQuery();
            CoreServicesDAO coreDAO = CoreServicesDAO.getInstance();
            if (rs != null) {
            	while (rs.next()) {
            		long locationId = rs.getLong(2);
            		Location location = coreDAO.getLocationFromId(locationId);
            		userLocations.add(location);
            	}
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
        if (userLocations.size() < 1) {
    		throw new DAONotFoundException("No locations for user " + pUser.getId());
        }
		
		return userLocations;
	}
	
	private boolean anyLocationsChanged(User pUser)
	{
		List<Location>  changedLocations   	= pUser.getLocations();
		List<Location>  storedLocations   	= null;
		
		try {
			storedLocations = getLocationsForUser(pUser);
		} catch (Exception ex){}
		
		if (storedLocations == null && changedLocations == null) {
			return false;
		}
		if (storedLocations != null && changedLocations == null) {
			return true;
		}
		if (storedLocations == null && changedLocations != null) {
			return true;
		}
		if (storedLocations.size() != changedLocations.size()) {
			return true;
		}
        Iterator<Location> itrChangedLocs = changedLocations.iterator();
        while (itrChangedLocs.hasNext()) {
        	Location changedLoc = itrChangedLocs.next();
            Iterator<Location> itrStoredLocs = storedLocations.iterator();
            boolean matchFound = false;
        	while (itrStoredLocs.hasNext()){
            	Location storedLoc = itrStoredLocs.next();
            	if (storedLoc.getId() == changedLoc.getId()) {
            		matchFound = true;
            	}
        	}
        	if (matchFound == false) {
        		return true;
        	}
        }
		
		return false;
	}
	
	
	/**
	 * Retrieves all Client objects
	 * @return List of Client objects 
	 * @throws DAOException when cannot retrieve list of Client objects
 	 */
	public List<Client> getAllClients() 
		throws DAOException 
	{
		LOG.debug("getAllClients");
		
		List<Client>		clients			= new ArrayList<Client>();
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the client
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectAllClientsSQL");
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		clients.add(getClientFromResultSet(rs, false));
            	}
        	}
        	else {
        		throw new DAOException("ERROR: Clients not retreived" );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
		return clients;
	}
	
	/**
	 * Retrieves a user and it's associated model
	 * @param pClientId: unique identifier for Client
	 * @return Client object 
	 * @throws DAOException when cannot retrieve Client
 	 */
	public Client getClientById(long pClientId) 
		throws DAONotFoundException, DAOException 
	{
		return getClientById(pClientId, false);
	}
	
	/**
	 * Retrieves a user and it's associated model
	 * @param pClientId: unique identifier for Client
	 * @param pDeep: indicates whether or not to get
	 * 	full object graph of child objects
	 * @return Client object 
	 * @throws DAOException when cannot retrieve Client
 	 */
	protected Client getClientById(long pClientId, boolean pDeep) 
			throws DAONotFoundException, DAOException 
		{
		LOG.debug("getClientById " + pClientId);
		
		Client 				client 			= null;
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the client
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectClientByIdSQL");
            stmt = conn.prepareStatement(sqlStr);
            stmt.setLong(1, pClientId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	if (rs.next()) {
            		client = getClientFromResultSet(rs, pDeep);
            	}
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
        if (client == null) {
    		throw new DAONotFoundException("Client not retreived for Id=" + pClientId );
        }
        
		return client;
	}
	
	/**
	 * This takes a result set and converts it to a Client.
	 * @param pResultSet: contains the user info
	 * @return a fully populated User. 
	 * @throws Exception
	 */
	private Client getClientFromResultSet(ResultSet pResultSet, boolean deep)  
		throws DAOException
	{		
		Client client = new Client();
		try {
			client.setId(pResultSet.getLong(1));
			client.setName(pResultSet.getString(2));
			client.setAbbreviation(pResultSet.getString(3));
			if (deep) {
				client.setDivisions(CoreServicesDAO.getInstance().getDivisionsByClient(client));
			}
		}
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
		
        return client;
	}

	/**
	 * Retrieves all AccessType objects
	 * @return List of AccessType objects 
	 * @throws DAOException when cannot retrieve list of AccessType objects
 	 */
	public List<AccessType> getAllAccessTypes() 
		throws DAOException 
	{
		LOG.debug("getAllAccessType");
		
		List<AccessType>	accessTypes		= new ArrayList<AccessType>();
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the access types
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectAllAccessTypesSQL");
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		AccessType accessType = new AccessType();
            		accessType.setId(rs.getLong(1));
            		accessType.setName(rs.getString(2));
            		accessType.setAbbreviation(rs.getString(3));
            		accessTypes.add(accessType);
            	}
        	}
        	else {
        		throw new DAOException("ERROR: All AccessTypes not retreived" );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
		return accessTypes;
	}

	/**
	 * Retrieves a single AccessType object
	 * @param id of AccessType
	 * @return an AccessType object 
	 * @throws DAONotFoundException when AccessType not found
	 * @throws DAOException when cannot retrieve the AccessType object
 	 */
	public AccessType getAccessType(Long pAccessTypeId) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getAccessType");
		
		/*
		 * Check cache first to reduce I/O
		 */
		Object accessTypeObj = (Object) cache.get("AccessType-Persist"+pAccessTypeId);
		if (!(accessTypeObj==null)) {
			return (AccessType)accessTypeObj;
		}
		
		AccessType			accessType		= null;
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the access type
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectAccessTypeByIDSQL");
            stmt = conn.prepareStatement(sqlStr);
            stmt.setLong(1, pAccessTypeId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	if (rs.next()) {
            		accessType = new AccessType();
            		accessType.setId(rs.getLong(1));
            		accessType.setName(rs.getString(2));
            		accessType.setAbbreviation(rs.getString(3));
            	}
        	}
        	else {
        		throw new DAOException("ERROR: AccessType not retreived:" + pAccessTypeId );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
        if (accessType == null) {
        	throw new DAONotFoundException("ERROR: AccessType not found:" + pAccessTypeId );
        }
        /*
         * save results to cache so next call will not use DB
         */
    	cache.put("AccessType-Persist"+pAccessTypeId, accessType);
		return accessType;
	}

	/**
	 * This takes a result set and converts it to an Access object.
	 * @param pResultSet: contains the Access info
	 * @return a fully populated Access. 
	 * @throws Exception
	 */
	private Access getAccessFromResultSet(
			ResultSet pResultSet)  throws Exception
	{		
		AccessType accessType = getAccessType(pResultSet.getLong(2));
		Access access = new Access(accessType);
		access.setId(pResultSet.getLong(1));
		access.setCreate(pResultSet.getBoolean(3));
		access.setRead(pResultSet.getBoolean(4));
		access.setUpdate(pResultSet.getBoolean(5));
		access.setDelete(pResultSet.getBoolean(6));

        return access;
	}
	

	/**
	 * Retrieves a single Access object
	 * @param id of Access
	 * @return an Access object 
	 * @throws DAONotFoundException when Access not found
	 * @throws DAOException when cannot retrieve the Access object
 	 */
	public Access getAccess(Long pAccessId) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getAccess:" + pAccessId);
		
		Access				access			= null;
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the access
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectAccessByIdSQL");
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            if (rs != null) {
            	if (rs.next()) {
            		access = getAccessFromResultSet(rs);
            	}
        	}
        	else {
        		throw new DAOException("ERROR: All AccessTypes not retreived" );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
        if (access == null) {
        	throw new DAONotFoundException("ERROR: Access not found:" + pAccessId );
        }
		return access;
	}
	/**
	 * Retrieves all Access objects for a Role
	 * @param Role for which all Access object are required
	 * @return List of Access objects 
	 * @throws DAONotFoundException when Access not found
	 * @throws DAOException when cannot retrieve the Access object
 	 */
	private List<Access> getAccessListForRole(Role pRole) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getAccessListForRole:" + pRole.getId());
		
		List<Access>		accessList		= new ArrayList<Access>();
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the access
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectAccessListByRoleSQL");
            stmt = conn.prepareStatement(sqlStr);
            stmt.setLong(1, pRole.getId());
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		accessList.add(getAccessFromResultSet(rs));
            	}
        	}
        	else {
        		throw new DAOException("ERROR: All AccessTypes not retreived" );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
        if (accessList.size() == 0) {
        	throw new DAONotFoundException("Access object not found for Role:" + pRole.getId() );
        }
		return accessList;
	}
	/**
	 * Updates an Access object
	 * @param Access object
	 * @throws DAOException when cannot retrieve the Access object
 	 */
	public void updateAccess(Access pAccess) 
		throws DAOException 
	{
		LOG.debug("updateAccess:" + pAccess.getId());
		
        Connection 			conn 			= null;
		PreparedStatement 	prepareStmt 	= null;
    	
        try {
        	/*
        	 * Update Access
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("updateAccessSQL");
			prepareStmt = conn.prepareCall(sqlStr);
			prepareStmt.setBoolean(1, pAccess.isCreate());
			prepareStmt.setBoolean(2, pAccess.isUpdate());
			prepareStmt.setBoolean(3, pAccess.isRead());
			prepareStmt.setBoolean(4, pAccess.isDelete());
			prepareStmt.executeUpdate();  						// update ACCESS
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while updating ACCESS: " + pAccess.getId() 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }
            
		return;
	}
	/**
	 * Inserts an Access object
	 * @param Access object
	 * @return inserted Access object with id containing DB key 
	 * @throws DAOException when cannot store the Access object
 	 */
	public Access addAccess(Access pAccess) 
		throws DAOException 
	{
		LOG.debug("insertAccess:" + pAccess.getName());
		
		long 				seq 			= getNextSequence();		// get the next unique ID
        Connection 			conn 			= null;
		PreparedStatement 	prepareStmt 	= null;
        String 				sqlStr 			= getProperty("insertAccessSQL");
        
        try {
        	/*
        	 * Update Access
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(sqlStr);
			prepareStmt.setLong(1, seq);
			prepareStmt.setLong(2, pAccess.getAccessTypeId());
			prepareStmt.setBoolean(3, pAccess.isCreate());
			prepareStmt.setBoolean(4, pAccess.isUpdate());
			prepareStmt.setBoolean(5, pAccess.isRead());
			prepareStmt.setBoolean(6, pAccess.isDelete());
			prepareStmt.executeUpdate();  						// update ACCESS
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while inserting ACCESS: " + pAccess.getId() 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }
            
        pAccess.setId(seq);
        
		return pAccess;
	}
	/**
	 * Deletes an Access object
	 * @param Long ID of Access object
	 * @throws DAOException when cannot delete the Access object
 	 */
	public void deleteAccess(Long pAccessId) 
		throws DAOException 
	{
		LOG.debug("deleteAccess:" + pAccessId);
		
        Connection 			conn 			= null;
		PreparedStatement 	prepareStmt 	= null;
    	
        try {
        	/*
        	 * Delete Access
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("deleteAccessSQL");
			prepareStmt = conn.prepareCall(sqlStr);
			prepareStmt.setLong(1, pAccessId);
			prepareStmt.executeUpdate();  						// delete ACCESS
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while deleting ACCESS: " + pAccessId 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }
            
		return;
	}
	
	/**
	 * This takes a result set and converts it to a Role object.
	 * @param pResultSet: contains the Role info
	 * @return a fully populated Role. 
	 * @throws Exception
	 */
	private Role getRoleFromResultSet(
			ResultSet pResultSet)  throws Exception
	{		
		Role role = new Role();
		role.setId(pResultSet.getLong(1));
		role.setName(pResultSet.getString(2));
		role.setAbbreviation(pResultSet.getString(3));
		role.setClientId(pResultSet.getLong(4));
		
		/*
		 * ACCESS LIST IS NULL IF NONE FOUND
		 */
		try {
			role.setAccessList(getAccessListForRole(role));
		}
		catch (Exception ex) {}
		
        return role;
	}
	
	/**
	 * Retrieves all Role objects. Must be a Global Admin
	 * @param A Global Admin User object
	 * @return List of Role objects 
	 * @throws DAOInsufficientRightsException if user doesn't have global access
	 * @throws DAONotFoundException no Role objects exist
	 * @throws DAOException when cannot retrieve list of Role objects
 	 */
	public List<Role> getAllRoles(User pUser) 
		throws DAOInsufficientRightsException, DAONotFoundException, DAOException 
	{
		LOG.debug("getAllRoles");
		
		if (!pUser.isGlobalAdmin()) {
        	throw new DAOInsufficientRightsException("USER "+ pUser.getId() + " MUST BE GLOBAL ADMIN TO GET ALL ROLES");
		}
		
		List<Role>			roles			= new ArrayList<Role>();
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the role types
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectAllRolesSQL");
            stmt = conn.prepareStatement(sqlStr);
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		roles.add(getRoleFromResultSet(rs));
            	}
        	}
        	else {
        		throw new DAOException("ERROR: All Roles not retreived" );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        if (roles.size() == 0) {
        	throw new DAONotFoundException("NO ROLES FOUND");
        }

		return roles;
	}
	
	
	/**
	 * Retrieves all Role objects for a given Client ID
	 * @param A Client ID for which all Roles are requested
	 * @param A Global Admin or Client Admin user object
	 * @return List of Role objects 
	 * @throws DAOInsufficientRightsException if user doesn't have client or global admin access
	 * @throws DAONotFoundException no Role objects exist for passed Client ID
	 * @throws DAOException when cannot retrieve list of Role objects
 	 */
	public List<Role> getAllRolesByClientId(Long pClientId, User pUser) 
		throws DAOInsufficientRightsException, DAONotFoundException, DAOException 
	{
		LOG.debug("getAllRolesByClientId:" + pClientId);

		if (!pUser.isGlobalAdmin() && !pUser.isClientAdmin()) {
        	throw new DAOInsufficientRightsException("USER "+ pUser.getId() + " MUST BE GLOBAL OR CLIENT ADMIN TO GET CLIENT ROLES");
		}
		
		List<Role>			roles			= new ArrayList<Role>();
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the role types
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectRolesByClientIdSQL");
            stmt = conn.prepareStatement(sqlStr);
            stmt.setLong(1, pClientId);
            rs = stmt.executeQuery();
            
            if (rs != null) {
            	while (rs.next()) {
            		roles.add(getRoleFromResultSet(rs));
            	}
        	}
        	else {
        		throw new DAOException("ERROR: All Roles not retreived" );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        if (roles.size() == 0) {
        	throw new DAONotFoundException("NO ROLES FOUND FOR CLIENT:" + pClientId);
        }
		return roles;
	}
	/**
	 * Retrieves all Role objects for a User
	 * @param User for which all Role objects are required
	 * @return List of Role objects 
	 * @throws DAONotFoundException when Role not found
	 * @throws DAOException when cannot retrieve the Role object
 	 */
	private List<Role> getRolesForUser(User pUser) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getRolesForUser:" + pUser.getId());
		
		List<Role>			roles			= new ArrayList<Role>();
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the access
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectRolesByUserSQL");
            stmt = conn.prepareStatement(sqlStr);
            stmt.setLong(1, pUser.getId());
            rs = stmt.executeQuery();
            if (rs != null) {
            	while (rs.next()) {
            		roles.add(getRoleFromResultSet(rs));
            	}
        	}
        	else {
        		throw new DAOException("ERROR: All RoleTypes not retreived" );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
        if (roles.size() == 0) {
        	throw new DAONotFoundException("Role object not found for Role:" + pUser.getId() );
        }
		return roles;
	}
	/**
	 * Retrieves a single Role object
	 * @param id of Role
	 * @return an Role object 
	 * @throws DAONotFoundException when Role not found
	 * @throws DAOException when cannot retrieve the Role object
 	 */
	public Role getRole(Long pRoleId) 
		throws DAONotFoundException, DAOException 
	{
		LOG.debug("getRole:" + pRoleId);
		
		Role				role		= null;
        Connection 			conn 			= null;
        PreparedStatement 	stmt 			= null;
    	ResultSet 			rs 				= null;
    	
        try {
        	/*
        	 * Get the access type
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			String sqlStr = getProperty("selectAccessTypeByIDSQL");
            stmt = conn.prepareStatement(sqlStr);
            stmt.setLong(1, pRoleId);
            rs = stmt.executeQuery();
            if (rs != null) {
            	if (rs.next()) {
            		role = getRoleFromResultSet(rs);
            	}
        	}
        	else {
        		throw new DAOException("ERROR: Role not retreived:" + pRoleId );
        	}
        }
        catch (Exception ex) {
        	LOG.error(ex.getMessage());
        	throw new DAOException(ex);
        } 
        finally {
            ConnectionFactory.closeAll(conn, stmt, rs);
        }
        
        if (role == null) {
        	throw new DAONotFoundException("ERROR: Role not found:" + pRoleId );
        }
		return role;
	}
	/**
	 * NOTE: ONLY USE THIS METHOD IF GLOBAL ADMIN
	 * Inserts an Role object
	 * @param Role object
	 * @param A Global Admin User object
	 * @return inserted Role object with id containing DB key 
	 * @throws DAOInsufficientRightsException if user doesn't have global admin access
	 * @throws DAOException when cannot store the Role object
 	 */
	public Role addRole(Role pRole, User pUser) 
		throws DAOInsufficientRightsException, DAOException 
	{
		LOG.debug("insertRole:" + pRole.getName() + " for Client:" +  pRole.getClientId());
		
		if (!pUser.isGlobalAdmin()) {
        	throw new DAOInsufficientRightsException("USER "+ pUser.getId() + " MUST BE GLOBAL ADMIN TO ADD ROLES");
		}
		
		long 				seq 			= getNextSequence();		// get the next unique ID
        Connection 			conn 			= null;
		PreparedStatement 	prepareStmt 	= null;
        String 				sqlStr 			= getProperty("insertRoleSQL");
        
        try {
        	/*
        	 * Add Role
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(sqlStr);
			prepareStmt.setLong(1, seq);
			prepareStmt.setString(2, pRole.getName());
			prepareStmt.setString(3, pRole.getAbbreviation());
			prepareStmt.setLong(4, pRole.getClientId());
			prepareStmt.executeUpdate();  						// add Role
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while inserting Role: " + pRole.getName() + " for Client:" +  pRole.getClientId()
        			+ "  : " +  ex.getMessage());
        	throw new DAOException(ex);
        }
            
        pRole.setId(seq);
        
		return pRole;
	}

	/**
	 * NOTE: ONLY USE THIS METHOD IF GLOBAL ADMIN
	 * Updates a Role object
	 * @param Role object
	 * @param A Global Admin User object
	 * @throws DAOInsufficientRightsException if user doesn't have global admin access
	 * @throws DAOException when cannot store the Role object
 	 */
	public void updateRole(Role pRole, User pUser) 
		throws DAOInsufficientRightsException, DAOException 
	{
		LOG.debug("updateRole:" + pRole.getId());
		
		if (!pUser.isGlobalAdmin()) {
        	throw new DAOInsufficientRightsException("USER "+ pUser.getId() + " MUST BE GLOBAL ADMIN TO UPDATE ROLES");
		}
		
        Connection 			conn 			= null;
		PreparedStatement 	prepareStmt 	= null;
        String 				sqlStr 			= getProperty("updateRoleSQL");
        
        try {
        	/*
        	 * Update Role
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(sqlStr);
			prepareStmt.setLong(1, pRole.getId());
			prepareStmt.setString(2, pRole.getName());
			prepareStmt.setString(3, pRole.getAbbreviation());
			prepareStmt.setLong(4, pRole.getClientId());
			prepareStmt.executeUpdate();  						// update Role
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while updating Role: " + pRole.getId()
        			+ "  : " +  ex.getMessage());
        	throw new DAOException(ex);
        }
            
		return;
	}


	/**
	 * NOTE: ONLY USE THIS METHOD IF GLOBAL ADMIN
	 * Delete a Role object
	 * @param Role object
	 * @param A Global Admin User object
	 * @throws DAOInsufficientRightsException if user doesn't have global admin access
	 * @throws DAOException when cannot store the Role object
 	 */
	public void deleteRole(Long pRoleId, User pUser) 
		throws DAOInsufficientRightsException, DAOException 
	{
		LOG.debug("updateRole:" + pRoleId);
		
		if (!pUser.isGlobalAdmin()) {
        	throw new DAOInsufficientRightsException("USER "+ pUser.getId() + " MUST BE GLOBAL ADMIN TO DELETE ROLES");
		}
		
		/*
		 * FIRST DELETE ROLE_ACCESS and USER_ROLE OBJECTS
		 */
		deleteRoleAccess(pRoleId); 
		deleteRoleUsers(pRoleId);
		
        Connection 			conn 			= null;
		PreparedStatement 	prepareStmt 	= null;
        String 				sqlStr 			= getProperty("deleteRoleWithIdSQL");
        
        try {
        	/*
        	 * Delete Role
        	 */
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(sqlStr);
			prepareStmt.setLong(1, pRoleId);
			prepareStmt.executeUpdate();  						// delete Role
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while deleting Role: " + pRoleId
        			+ "  : " +  ex.getMessage());
        	throw new DAOException(ex);
        }
            
		return;
	}

	/**
	 * NOTE: ONLY USE THIS METHOD IF GLOBAL ADMIN
	 * Updates the List of Access objects for a Role 
	 * @param Role object
	 * @param A Global Admin User object
	 * @throws DAOInsufficientRightsException if user doesn't have global admin access
	 * @throws DAOException when cannot store the Role object
 	 */
	public void updateRoleAccess(Role pRole, User pUser) 
		throws DAOInsufficientRightsException, DAOException 
	{
		LOG.debug("updateRoleAccess:" + pRole.getId());
		
		if (!pUser.isGlobalAdmin()) {
        	throw new DAOInsufficientRightsException("USER "+ pUser.getId() + " MUST BE GLOBAL ADMIN TO UPDATE ACCESS OBJECTS FOR A ROLE");
		}
		
        /*
         * NOW DETERMINE IF ANY ACCESS OBJECTS WERE CHANGED
         */
        if (accessListChanged(pRole)) {
        	deleteRoleAccess(pRole.getId());
        	if (pRole.getAccessList() != null && pRole.getAccessList().size() > 0) {
            	addRoleAccessList(pRole);
        	}
        	else {
            	pUser.setLocations(null);
        	}
        }
            
		return;
	}
	/**
	 * NOTE: ONLY USE THIS METHOD IF CLIENT OR GLOBAL ADMIN
	 * Updates the List of Location objects for a User 
	 * @param User object
	 * @param A Global Admin User object
	 * @throws DAOInsufficientRightsException if user doesn't have client or global admin access
	 * @throws DAOException when cannot update the Location objects
 	 */
	public void updateUserLocations(User pUser, User pAdmin) 
		throws DAOInsufficientRightsException, DAOException 
	{
		LOG.debug("updateUserLocations for user:" + pUser.getId());
		
		if (!pAdmin.isClientAdmin() && !pAdmin.isGlobalAdmin()) {
        	throw new DAOInsufficientRightsException("USER "+ pAdmin.getId() + " MUST BE GLOBAL OR CLIENT ADMIN TO UPDATE ROLES FOR USER");
		}
		
        /*
         * NOW DETERMINE IF ANY LOCATIONS WERE CHANGED
         */
        if (anyLocationsChanged(pUser)) {
        	deleteUserLocations(pUser.getId());
        	if (pUser.getLocations() != null && pUser.getLocations().size() > 0) {
            	addUserLocations(pUser);
        	}
        	else {
            	pUser.setLocations(null);
        	}
        }
        
		return;
	}
	/**
	 * NOTE: ONLY USE THIS METHOD IF CLIENT OR GLOBAL ADMIN
	 * Updates the List of Role objects for a User 
	 * @param User object
	 * @param A Global Admin User object
	 * @throws DAOInsufficientRightsException if user doesn't have client or global admin access
	 * @throws DAOException when cannot update the Role objects
 	 */
	public void updateUserRoles(User pUser, User pAdmin) 
		throws DAOInsufficientRightsException, DAOException 
	{
		LOG.debug("updateRoleAccess for user:" + pUser.getId());
		
		if (!pAdmin.isClientAdmin() && !pAdmin.isGlobalAdmin()) {
        	throw new DAOInsufficientRightsException("USER "+ pAdmin.getId() + " MUST BE GLOBAL OR CLIENT ADMIN TO UPDATE ROLES FOR USER");
		}
		
        /*
         * NOW DETERMINE IF ANY ROLES WERE CHANGED
         */
        if (rolesChanged(pUser)) {
        	deleteUserRoles(pUser.getId());
        	if (pUser.getRoles() != null && pUser.getRoles().size() > 0) {
            	addUserRoles(pUser);
        	}
        	else {
            	pUser.setRoles(null);
        	}
        }
		return;
	}
	private boolean accessListChanged(Role pRole)
	{
		List<Access>  changedAccessList   	= pRole.getAccessList();
		List<Access>  storedAccessList   	= null;
		
		try {
			storedAccessList = getAccessListForRole(pRole);
		} catch (Exception ex){}
		
		if (storedAccessList == null && changedAccessList == null) {
			return false;
		}
		if (storedAccessList != null && changedAccessList == null) {
			return true;
		}
		if (storedAccessList == null && changedAccessList != null) {
			return true;
		}
		if (storedAccessList.size() != changedAccessList.size()) {
			return true;
		}
        Iterator<Access> itrChangedList = changedAccessList.iterator();
        while (itrChangedList.hasNext()) {
        	Access changedLoc = itrChangedList.next();
            Iterator<Access> itrStoredList = storedAccessList.iterator();
            boolean matchFound = false;
        	while (itrStoredList.hasNext()){
            	Access storedLoc = itrStoredList.next();
            	if (storedLoc.getId() == changedLoc.getId()) {
            		matchFound = true;
            	}
        	}
        	if (matchFound == false) {
        		return true;
        	}
        }
		
		return false;
	}
	/**
	 * Deletes the Access List by Role
	 * @param pRoleId: Id of role for which RoleAccess objects are deleted
	 * @throws DAOException when cannot delete the RoleAccess
 	 */
	private void deleteRoleAccess(Long pRoleId) 
		throws DAOException 
	{
		LOG.debug("deleteRoleAccess for RoleId:" + pRoleId);
		
		PreparedStatement prepareStmt = null;
        Connection conn = null;
        
        try {
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(getProperty("deleteAccessListFromRoleSQL"));
			prepareStmt.setLong(1, pRoleId);							// ROLE_ACCESS.PE_ROLE_id
			prepareStmt.executeUpdate();  								// deletes ROLE_ACCESS for id
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while deleting ROLE_ACCESS rows for Role Id:" + pRoleId 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }

		return;
	}
	/**
	 * Deletes the UserRoles by Role
	 * @param pRoleId: Id of role for which UserRole objects are deleted
	 * @throws DAOException when cannot delete the UserRole
 	 */
	private void deleteRoleUsers(Long pRoleId) 
		throws DAOException 
	{
		LOG.debug("deleteRoleUsers for RoleId:" + pRoleId);
		
		PreparedStatement prepareStmt = null;
        Connection conn = null;
        
        try {
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(getProperty("deleteUserRolesFromRoleSQL"));
			prepareStmt.setLong(1, pRoleId);							// USER_ROLE.PE_ROLE_id
			prepareStmt.executeUpdate();  								// deletes USER_ROLE for PE_ROLE_id
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while deleting USER_ROLE rows for Role Id:" + pRoleId 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }

		return;
	}
	/**
	 * Adds a list of Access objects for the Role
	 * @param pRole: role object containing a list of Access objects
	 * @throws DAOException when cannot save the Access objects
 	 */
	private Role addRoleAccessList(Role pRole) 
		throws DAOException 
	{
		LOG.debug("addRoleAccessList for role:" + pRole.getName());
		
		List<Access>  accessList  = pRole.getAccessList();
		if (accessList == null || accessList.size() < 1) {
			return pRole;
		}
		
		PreparedStatement prepareStmt = null;
		try {
	        Connection conn = ConnectionFactory.getInstance().getConnection();
	        String sql = null;        
	        Iterator<Access> itrLoc = accessList.iterator();
	        while (itrLoc.hasNext()){
	        	Access access = itrLoc.next();
				sql = getProperty("insertAccessForRoleSQL");
				prepareStmt = conn.prepareCall(sql);
				prepareStmt.setLong(1, getNextSequence());	// ROLE_ACCESS.id
				prepareStmt.setLong(2, pRole.getId());
				prepareStmt.setLong(3, access.getId());
				prepareStmt.executeUpdate();  				// insert a ROLE_ACCESS
	        }
		}
        catch (SQLException ex) {
        	LOG.error("ERROR while inserting ROLE_ACCESS for role: " + pRole.getName() 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }
        finally {
    		ConnectionFactory.closeAll(null, prepareStmt, null);
        }
		return pRole;
	}
	private boolean rolesChanged(User pUser)
	{
		List<Role>  changedRoles   	= pUser.getRoles();
		List<Role>  storedRoles   	= null;
		
		try {
			storedRoles = getRolesForUser(pUser);
		} catch (Exception ex){}
		
		if (storedRoles == null && changedRoles == null) {
			return false;
		}
		if (storedRoles != null && changedRoles == null) {
			return true;
		}
		if (storedRoles == null && changedRoles != null) {
			return true;
		}
		if (storedRoles.size() != changedRoles.size()) {
			return true;
		}
        Iterator<Role> itrChangedList = changedRoles.iterator();
        while (itrChangedList.hasNext()) {
        	Role changedLoc = itrChangedList.next();
            Iterator<Role> itrStoredList = storedRoles.iterator();
            boolean matchFound = false;
        	while (itrStoredList.hasNext()){
            	Role storedLoc = itrStoredList.next();
            	if (storedLoc.getId() == changedLoc.getId()) {
            		matchFound = true;
            	}
        	}
        	if (matchFound == false) {
        		return true;
        	}
        }
		
		return false;
	}
	
	/**
	 * Deletes the Roles by User
	 * @param pUserId: Id of user for which UserRole objects are deleted
	 * @throws DAOException when cannot delete the UserRole
 	 */
	private void deleteUserRoles(Long pUserId) 
		throws DAOException 
	{
		LOG.debug("deleteUserRoles for User ID:" + pUserId);
		
		PreparedStatement prepareStmt = null;
        Connection conn = null;
        
        try {
			conn = ConnectionFactory.getInstance().getConnection();
			prepareStmt = conn.prepareCall(getProperty("deleteRolesFromUserSQL"));
			prepareStmt.setLong(1, pUserId);							// USER_ROLE.PE_USER_id
			prepareStmt.executeUpdate();  								// deletes USER_ROLE for PE_USER_id
			ConnectionFactory.closeAll(null, prepareStmt, null);
        }
        catch (SQLException ex) {
        	LOG.error("ERROR while deleting USER_ROLE rows for User ID:" + pUserId 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }

		return;
	}

	/**
	 * Adds a list of Role objects for the User
	 * @param pUser: user object containing a list of Role objects
	 * @throws DAOException when cannot save the Role objects
 	 */
	private User addUserRoles(User pUser) 
		throws DAOException 
	{
		LOG.debug("addUserRoles for user:" + pUser.getId());
		
		List<Role>  roles  = pUser.getRoles();
		if (roles == null || roles.size() < 1) {
			return pUser;
		}
		
		PreparedStatement prepareStmt = null;
		try {
	        Connection conn = ConnectionFactory.getInstance().getConnection();
	        String sql = null;        
	        Iterator<Role> itrLoc = roles.iterator();
	        while (itrLoc.hasNext()){
	        	Role role = itrLoc.next();
				sql = getProperty("insertRolesForUserSQL");
				prepareStmt = conn.prepareCall(sql);
				prepareStmt.setLong(1, getNextSequence());	// USER_ROLE.id
				prepareStmt.setLong(2, role.getId());
				prepareStmt.setLong(3, pUser.getId());
				prepareStmt.executeUpdate();  				// insert a USER_ROLE
	        }
		}
        catch (SQLException ex) {
        	LOG.error("ERROR while inserting USER_ROLE for user:" + pUser.getId() 
        			+ ": " +  ex.getMessage());
        	throw new DAOException(ex);
        }
        finally {
    		ConnectionFactory.closeAll(null, prepareStmt, null);
        }
		return pUser;
	}

	
}
