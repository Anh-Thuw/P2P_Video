package Client.model;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class Receiver {
	public		static		ArrayList<Receiver> 	list 		=	new ArrayList<>();
	private 				String 					receiver;
	private 				String 					position;
	private 				boolean 				online;
	private 				JTextArea 				chatArea;
	
	public Receiver(String username, String position, boolean online) {
		this.receiver 	= 	username;
		this.position 	= 	position;
		this.online 	= 	online;
		this.chatArea 	= 	new JTextArea();
		this.chatArea.setFont(new Font("Times New Roman", Font.PLAIN, 30));
	}

	public String getReceiver() {
		return receiver;
	}

	public void setUsername(String username) {
		this.receiver = username;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
	
	public JTextArea getChatArea() {
		return chatArea;
	}
	
}