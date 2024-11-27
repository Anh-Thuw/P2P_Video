package clientserver.services;


import clientserver.db.ConnectDB;
import clientserver.model.Account;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ServerThreadMeet extends Thread {

	private 	DataInputStream 				dataInputStream;
	private		DataOutputStream				dataOutputStream;
	private 	String 							username;
	private		StringTokenizer 				cont;
	private 	Socket 							socket ;

	private   	int 							port ;
	private 	Connection 						connection ;

	public ServerThreadMeet(StringTokenizer cmd, Socket socket) {
		try {
			this.dataInputStream 	= 	new DataInputStream(socket.getInputStream());
			this.dataOutputStream 	= 	new DataOutputStream(socket.getOutputStream());
			this.cont				= 	cmd;
			this.socket 			= 	socket;
			this.connection 		= 	ConnectDB.ConnectionDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static final String sepa = "<?>";
	
	public void doSendData(String cmd,String... cont) {
		try {
			synchronized (dataOutputStream) {
				String msg = cmd;
				for (String s : cont) msg = msg + sepa + s;
				dataOutputStream.writeUTF(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void run() {
		
		try {			
			String cmd = cont.nextToken();
			System.out.println("......"+cmd);

			switch (cmd) {
//			case "Call_Meet_Client":
//				doMeetClient(cont);
//				break;
			case "Call_Meet_Create":
				doMeetCreate(cont);
				break;
			case "Call_Meet_Join":
				doMeetJoin(cont);
				break;

			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void doMeetJoin(StringTokenizer cont) {
		try {
			String port = cont.nextToken();
			String username = cont.nextToken();

			int userId = getId(username);
			if (userId == -1) {
				System.out.println("User not found: " + username);
				return;
			}
			// Get meet_id from the meets table using room_code (port)
			String sqlGetMeetId = "SELECT meet_id, host_ip_address FROM meets WHERE room_code = ?";
			PreparedStatement psGetMeetId = connection.prepareStatement(sqlGetMeetId);
			psGetMeetId.setString(1, port);
			ResultSet rs = psGetMeetId.executeQuery();

			int meetId = -1;
			String hostIpAddress = null;

			if (rs.next()) {
				meetId = rs.getInt("meet_id");
				hostIpAddress = rs.getString("host_ip_address");
			}

			if (meetId == -1) {
				System.out.println("Meeting not found for room code: " + port);
				return;
			}
			String sqlCheckParticipant = "SELECT id FROM meet_participants WHERE meet_id = ? AND user_id = ?";
			PreparedStatement psCheckParticipant = connection.prepareStatement(sqlCheckParticipant);
			psCheckParticipant.setInt(1, meetId);
			psCheckParticipant.setInt(2, userId);
			ResultSet rsCheck = psCheckParticipant.executeQuery();

			if (rsCheck.next()) {
				System.out.println("User already in the meeting: " + username);
				doSendData("Call_Meet<?>Call_Meet_Join_KO", "User already joined the meeting.");
				return;
			}

			String sqlInsertParticipant = "INSERT INTO meet_participants (meet_id, user_id, joined_at) VALUES (?, ?, NOW())";
			PreparedStatement psInsertParticipant = connection.prepareStatement(sqlInsertParticipant);
			psInsertParticipant.setInt(1, meetId);
			psInsertParticipant.setInt(2, userId);
			psInsertParticipant.executeUpdate();

			doSendData("Call_Meet<?>Call_Meet_Join_OK", username, port, hostIpAddress);
			System.out.println("User " + username + " joined meeting successfully.");

		} catch (Exception e) {
			e.printStackTrace();
			doSendData("Call_Meet<?>Call_Meet_Join_Fail", "Error occurred while joining the meeting.");
		}
	}

	public void doMeetCreate(StringTokenizer cont) {
		try {
			String port = cont.nextToken();
			String ip = cont.nextToken();
			String username = cont.nextToken();

			int userId = getId(username);
			if (userId == -1) {
				System.out.println("User not found: " + username);
				return;
			}
			String sqlInsertMeet = "INSERT INTO meets (host_user_id , host_ip_address, room_code ) VALUES (?, ? , ?)";
			PreparedStatement psMeet = connection.prepareStatement(sqlInsertMeet, PreparedStatement.RETURN_GENERATED_KEYS);
			psMeet.setInt(1, userId);
			psMeet.setString(2, ip);
			psMeet.setString(3, port);
			psMeet.executeUpdate();

			// Retrieve generated meet_id
			ResultSet rs = psMeet.getGeneratedKeys();
			int meetId = -1;
			if (rs.next()) {
				meetId = rs.getInt(1);
			}

			if (meetId == -1) {
				System.out.println("Failed to retrieve meet_id for the new meeting.");
				return;
			}

			// Insert host into meet_participants table
			String sqlInsertParticipant = "INSERT INTO meet_participants (meet_id, user_id, joined_at) VALUES (?, ?, NOW())";
			PreparedStatement psParticipant = connection.prepareStatement(sqlInsertParticipant);
			psParticipant.setInt(1, meetId);
			psParticipant.setInt(2, userId);
			psParticipant.executeUpdate();

			// Send confirmation to the client
			doSendData("Call_Meet<?>Call_Meet_Create_OK", username, port);
			System.out.println("Meeting created successfully. Host added to participants.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int getId(String username) {
		int userId = -1;
		try {
			String sql = "SELECT user_id FROM users WHERE username = ?";
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				userId = rs.getInt("user_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userId;
	}

}