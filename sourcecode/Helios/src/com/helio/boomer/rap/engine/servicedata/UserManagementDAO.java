package com.helio.boomer.rap.engine.servicedata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.widgets.DateTime;

import com.helio.app.boomer.common.dal.DAODuplicateNotAllowedException;
import com.helio.app.boomer.common.dal.DAOException;
import com.helio.app.boomer.common.dal.DAONotFoundException;
import com.helio.app.boomer.common.dal.UserServicesDAO;
import com.helio.app.boomer.common.dal.model.Client;
import com.helio.app.boomer.common.dal.model.Location;
import com.helio.app.boomer.common.dal.model.User;
import com.helio.boomer.rap.dto.ClientDTO;
import com.helio.boomer.rap.dto.LocationDTO;
import com.helio.boomer.rap.dto.UserDTO;

public class UserManagementDAO {

	public static UserServicesDAO userServicesDAO = UserServicesDAO
			.getInstance();

	public User getUserById(long userId) {

		User user = null;
		try {
			user = userServicesDAO.getUserById(userId);
		} catch (DAOException e) {
			System.out.println("DAOException : " + e.getMessage());

		}

		return user;

	}

	/**
	 * This method is used to save user in database
	 * 
	 * @param userDto
	 * @return saved user object with values
	 * @throws Exception
	 */
	public UserDTO addUser(UserDTO userDto) {

		User user = new User();
		User user1 = null;
		UserDTO userDTO = new UserDTO();
		Long clientId = 0L;
		if (null != userDto) {
			if (null != userDto.getFuserName()
					&& !"".equals(userDto.getFuserName())) {
				user.setFirstName(userDto.getFuserName());
			}
			if (null != userDto.getLuserName()
					&& !"".equals(userDto.getLuserName())) {
				user.setLastName(userDto.getLuserName());
			}

			if (null != userDto.getClient() && !"".equals(userDto.getClient())) {
				Client client = new Client();
				client.setName(userDto.getClient());
				clientId = getClientIdByName(userDto.getClient());
				System.out.println("Client ID : " + clientId);
				if (null != clientId) {
					client.setId(clientId);
				} else {
					userDTO.setErrorMessage(userDto.getClient().concat(
							" not found in database"));
				}
				user.setClient(client);
			}
			if (null != userDto.getEmail() && !"".equals(userDto.getEmail())) {
				user.setEmailAddr(userDto.getEmail());
			}
			if (null != userDto.getLocationName()
					&& !"".equals(userDto.getLocationName())) {
				Location location = new Location();
				location.setName("Test Location");

			}

			if (null != userDto.getPassword()
					&& !"".equals(userDto.getPassword())) {
				user.setPassword(userDto.getPassword());
			}
			if (null != userDto.getFuserName()
					&& !"".equals(userDto.getFuserName())
					&& null != userDto.getLuserName()
					&& !"".equals(userDto.getLuserName())) {
				user.setUserName(userDto.getFuserName().concat(" ")
						.concat(userDto.getLuserName()));
			}

			if (null != userDto.getExpire()) {

				java.sql.Date date = convertDateTimeToDateObject(userDto
						.getExpire());

				user.setExpire(date);

			}

			if (null != userDto.getExpireText()
					&& "never Expire".equals(userDto.getExpireText())) {

				java.sql.Date date = java.sql.Date.valueOf("2999-12-31");
				user.setExpire(date);

			}
		}

		try {
			user1 = userServicesDAO.addUser(user);
			if (null != user1 && 0L != user1.getId()) {
				userDTO.setPassword(user1.getPassword());
				userDTO.setFuserName(user1.getFirstName());
				userDTO.setLuserName(user1.getLastName());
				userDTO.setEmail(user1.getEmailAddr());
				userDTO.setUserId(user1.getId());

				userDTO.setMessage("User [".concat(user1.getUserName()).concat(
						" ] has been successfully created."));
			}
		} catch (DAODuplicateNotAllowedException e) {
			userDTO.setExMessage(e.getMessage());
		} catch (DAOException e) {
			userDTO.setExMessage(e.getMessage());
		}
		return userDTO;
	}

	/**
	 * This method is used to update user in database
	 * 
	 * @param userDto
	 * @return saved user object with values
	 * 
	 */
	public UserDTO updateUser(UserDTO userDto, User user) {

		UserDTO userDTO = new UserDTO();

		Long clientId = 0L;

		if (null != userDto) {

			user.setId(userDto.getUserId());

			if (null != userDto.getFuserName()
					&& !"".equals(userDto.getFuserName())) {
				user.setFirstName(userDto.getFuserName());
			}
			if (null != userDto.getLuserName()
					&& !"".equals(userDto.getLuserName())) {
				user.setLastName(userDto.getLuserName());
			}

			if (null != userDto.getClient() && !"".equals(userDto.getClient())) {

				Client client = new Client();
				client.setName(userDto.getClient());
				clientId = getClientIdByName(userDto.getClient());
				if (null != clientId) {
					client.setId(clientId);
				} else {
					userDTO.setErrorMessage(userDto.getClient().concat(
							" not found in database"));
				}
				user.setClient(client);
			}

			if (null != userDto.getEmail() && !"".equals(userDto.getEmail())) {
				user.setEmailAddr(userDto.getEmail());
			}

			if (null != userDto.getPassword()
					&& !"".equals(userDto.getPassword())) {
				user.setPassword(userDto.getPassword());
			}

			if (null != userDto.getFuserName()
					&& !"".equals(userDto.getFuserName())
					&& null != userDto.getLuserName()
					&& !"".equals(userDto.getLuserName())) {
				user.setUserName(userDto.getFuserName().concat(" ")
						.concat(userDto.getLuserName()));
			}
		}

		try {
			userServicesDAO.updateUser(user);
			userDTO.setUserId(user.getId());
			userDTO.setMessage("User [".concat(user.getUserName()).concat(
					" ] has been successfully updated."));
		} catch (DAODuplicateNotAllowedException e) {
			userDTO.setExMessage(e.getMessage());
		} catch (DAOException e) {
			userDTO.setExMessage(e.getMessage());
		}
		return userDTO;
	}

	/**
	 * This method is used to fetch all client from database
	 * 
	 * @return list of ClientDTO
	 */
	public List<ClientDTO> getAllClients() {

		List<ClientDTO> clientList = new ArrayList<ClientDTO>();
		List<Client> clients = null;
		try {
			clients = userServicesDAO.getAllClients();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (!clients.isEmpty()) {
			for (int i = 0; clients.size() > i; i++) {
				ClientDTO clientDTO = new ClientDTO();
				if (null != clients.get(i)) {
					clientDTO.setClientId(clients.get(i).getId());
					clientDTO.setClientName(clients.get(i).getName());
				}
				clientList.add(clientDTO);
			}
		}
		return clientList;
	}

	/**
	 * This method is used to fetch Client ID based on Client Name
	 * 
	 * @param clinetName
	 * @return Client ID
	 */
	public Long getClientIdByName(String clientName) {
		Long clientId = 0L;
		List<ClientDTO> clients = getAllClients();
		HashMap<String, Long> clientSet = new HashMap<String, Long>();

		for (int i = 0; clients.size() > i; i++) {
			clientSet.put(clients.get(i).getClientName(), clients.get(i)
					.getClientId());
		}
		if (null != clientSet) {
			clientId = clientSet.get(clientName);
		}
		return clientId;
	}

	/**
	 * This method is used to fetch Location names based on the Client Name
	 * 
	 * @param clientDto
	 * @return List of Locations, which are associated with the given Client
	 */
	public List<LocationDTO> getLocationFromClient(ClientDTO clientDto) {

		Long pClientId = getClientIdByName(clientDto.getClientName());
		Client pClient = null;
		List<Location> locationList = null;

		try {
			pClient = userServicesDAO.getClientById(pClientId);
			if (null != pClient) {
				locationList = userServicesDAO.getLocationsForClient(pClient);
			}
		} catch (DAONotFoundException e) {
			System.out.println("DAONotFoundException : " + e.getMessage());
		} catch (DAOException e) {
			System.out.println("DAOException : " + e.getMessage());
		}

		List<LocationDTO> locationDtoList = new ArrayList<LocationDTO>();
		if (null != locationList || "".equals(locationList)) {
			Iterator<Location> locationItr = locationList.iterator();
			while (locationItr.hasNext()) {
				Location location = locationItr.next();
				LocationDTO locationDTO = new LocationDTO();
				locationDTO.setLocationId(location.getId());
				locationDTO.setLocationName(location.getName());
				locationDtoList.add(locationDTO);
			}
		} else {
			System.out.println("list of location is empty for the client "
					+ clientDto.getClientName());
		}
		return locationDtoList;
	}


	/**
	 * This method is used to convert date from util to sql
	 * 
	 * @param dateTime
	 * @return sql type Date object
	 */
	private java.sql.Date convertDateTimeToDateObject(DateTime dateTime) {

		String sdate = String.valueOf(dateTime.getMonth() + 1).concat("/")
				.concat(String.valueOf(dateTime.getDay())).concat("/")
				.concat(String.valueOf(dateTime.getYear()));

		java.util.Date date = getDatePattern(sdate);
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;

	}

	/**
	 * This method is used to check the pattern of the Date
	 * 
	 * @param dates
	 * @return
	 */
	private java.util.Date getDatePattern(String dates) {
		SimpleDateFormat formatter;
		java.util.Date date = null;
		formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			date = formatter.parse(dates);
			System.out.println("Today is " + date);
		} catch (ParseException e) {

		}
		return date;
	}

}
