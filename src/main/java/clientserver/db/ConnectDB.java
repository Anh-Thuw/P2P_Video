package clientserver.db;

import java.sql.*;

public class ConnectDB {
	
	public static Connection ConnectionDB() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/videocall_p2p", "root", "");
			System.out.println("connect sucessfully");
			return connect;
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	public static void main(String[] args) {
		ConnectDB.ConnectionDB();
	}
	
	
}
