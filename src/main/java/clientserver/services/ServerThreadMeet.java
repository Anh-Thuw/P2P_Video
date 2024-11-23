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
			// Lấy thông tin từ lệnh
			String port = cont.nextToken(); // Room code
			String username = cont.nextToken();

			// Lấy user_id của người dùng dựa vào username
			int userId = getId(username);
			if (userId == -1) {
				System.out.println("User not found: " + username);
				return;
			}

			// Lấy meet_id dựa trên room_code (port)
			String sqlGetMeetId = "SELECT meet_id FROM meets WHERE room_code = ?";
			PreparedStatement psGetMeetId = connection.prepareStatement(sqlGetMeetId);
			psGetMeetId.setString(1, port);
			ResultSet rs = psGetMeetId.executeQuery();

			int meetId = -1;
			if (rs.next()) {
				meetId = rs.getInt("meet_id");
			}

			if (meetId == -1) {
				System.out.println("Meeting not found for room code: " + port);
				return;
			}

			// Kiểm tra nếu người dùng đã tham gia phòng họp này trước đó
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

			// Thêm người dùng vào bảng meet_participants
			String sqlInsertParticipant = "INSERT INTO meet_participants (meet_id, user_id, joined_at) VALUES (?, ?, ?, NOW())";
			PreparedStatement psInsertParticipant = connection.prepareStatement(sqlInsertParticipant);
			psInsertParticipant.setInt(1, meetId);
			psInsertParticipant.setInt(2, userId);
			psInsertParticipant.executeUpdate();

			// Gửi xác nhận thành công về cho client
			doSendData("Call_Meet<?>Call_Meet_Create_OK", username, port);
			System.out.println("User " + username + " joined meeting successfully.");

		} catch (Exception e) {
			e.printStackTrace();
			doSendData("Call_Meet<?>Call_Meet_Join_Fail", "Error occurred while joining the meeting.");
		}
	}

	public void doMeetCreate(StringTokenizer cont) {
		try {
			String port = cont.nextToken();
			String username = cont.nextToken();

			// Get user ID of the host
			int userId = getId(username);
			if (userId == -1) {
				System.out.println("User not found: " + username);
				return;
			}

			// Insert into meets table
			String sqlInsertMeet = "INSERT INTO meets (host_user_id, room_code) VALUES (?, ?)";
			PreparedStatement psMeet = connection.prepareStatement(sqlInsertMeet, PreparedStatement.RETURN_GENERATED_KEYS);
			psMeet.setInt(1, userId);
			psMeet.setString(2, port); // Room code as String
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