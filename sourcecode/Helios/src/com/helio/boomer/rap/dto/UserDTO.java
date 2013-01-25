package com.helio.boomer.rap.dto;

import java.util.List;

import org.eclipse.swt.widgets.DateTime;

/*
 @Date   : 7-Sep-2012
 @Author : RSystems International Ltd
 @purpose: This method is used to traveling data from front end to back end
 @Task   : RMAP-115
 */
public class UserDTO {

	private long userId;
	private String fuserName;
	private String luserName;
	private List<String> roleName;
	private String client;
	private String email;
	private DateTime expire;
	private String expireText;
	private List<String> locationName;
	private String password;
	private String message;
	private String errorMessage;
	private String exMessage;

	public String getExpireText() {
		return expireText;
	}

	public void setExpireText(String expireText) {
		this.expireText = expireText;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getExMessage() {
		return exMessage;
	}

	public void setExMessage(String exMessage) {
		this.exMessage = exMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFuserName() {
		return fuserName;
	}

	public void setFuserName(String fuserName) {
		this.fuserName = fuserName;
	}

	public String getLuserName() {
		return luserName;
	}

	public void setLuserName(String luserName) {
		this.luserName = luserName;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DateTime getExpire() {
		return expire;
	}

	public void setExpire(DateTime expire) {
		this.expire = expire;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getRoleName() {
		return roleName;
	}

	public void setRoleName(List<String> roleName) {
		this.roleName = roleName;
	}

	public List<String> getLocationName() {
		return locationName;
	}

	public void setLocationName(List<String> locationName) {
		this.locationName = locationName;
	}

}
