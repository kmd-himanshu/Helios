package com.helio.boomer.rap.service;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.helio.boomer.rap.dto.UserDTO;

/*
 @Date   : 7-Sep-2012
 @Author : RSystems International Ltd
 @purpose: This class is used to send email in the application
 @Task   : RMAP-115
 */
public class SendEmailServices {

	/*
	 * This method is used to send email from the Global Admin user screen
	 */
	public UserDTO sendEmail(UserDTO user) {

		final String username = "confluencehes@gmail.com";
		final String password = "holyloch655";

		UserDTO userdto = new UserDTO();

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {

			Message message = new MimeMessage(session);
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(user.getEmail()));
			message.setSubject("User Profile with PredictEnergy");
			message.setText("Dear,"
					+ user.getFuserName().concat(" ")
							.concat(user.getLuserName())
					+ "\n\n your profile has been created with username : "
					+ user.getEmail()
					+ " and password "
					+ user.getPassword()
					+ "\n\n Should you have any questions regarding your Logon or profile, please contact your Administrator."
					+ "\n\n Best Regards," + "\n PredictEnergy Admin");

			Transport.send(message);
			System.out.println("email send");
			userdto.setMessage("email successfully send to user ["
					+ user.getFuserName().concat(" ")
							.concat(user.getLuserName()) + " ] at "
					+ user.getEmail());

		} catch (MessagingException e) {
			userdto.setErrorMessage(e.getMessage());
		}
		return userdto;
	}

}
