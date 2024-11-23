package clientserver.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
	
	String email;
	String password;	
	String username ; 
	public Account (ResultSet rs) {
	
		try {
			username 	= rs.getString("username");
			email 		= rs.getString("email");
			password	= rs.getString("password");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Account(String email, String username , String password) {
		this.email    = email;
		this.username  =username ; 
		this.password = password;
	}
	public Account() {
		//userName =  password = position = null;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsename() {
		return username;
	}
	public void setUsename(String username) {
		this.username = username;
	}
	

}
