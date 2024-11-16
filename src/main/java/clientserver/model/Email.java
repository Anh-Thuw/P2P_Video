package clientserver.model;

import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {
	// Email: meetteam2024@gmail.com
	// Password: sdiy xomc vthw mpju
	static final String from = "thuna.22it@vku.udn.vn";
	static final String password = "sdiyxomcvthwmpju";
	static final String tieuDe = "Mã xác nhận tài khoản ";

	public static void sendEmail(String recepient , int otp) throws Exception {
		
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); 
		props.put("mail.smtp.port", "587"); 
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		Authenticator auth = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		};

		Session session = Session.getInstance(props, auth);
		Message msg = prepareMessage(session , from , recepient , otp);
		Transport.send(msg);
		System.out.println("mail sent");
		
	}
	private static Message prepareMessage(Session session , String from , String recepient , int otp) {
		try {
			Message message = new MimeMessage(session) ;
			message.setFrom(new InternetAddress(from));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
			message.setSubject("My First Email from Java App");
			message.setContent("Mã xác nhận tài khoản của bạn là : "+otp, "text/HTML; charset=UTF-8");	
			return message ; 
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return null ; 
	}
}