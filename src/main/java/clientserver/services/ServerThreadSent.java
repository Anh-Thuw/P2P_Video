package clientserver.services;

import clientserver.db.ConnectDB;

import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ServerThreadSent extends Thread {

	private ArrayList<ServerThreadReceive> clientList;
	private String username;
	private DataOutputStream dataOutputStream;
	private String cmd;

	public ServerThreadSent(String cmd, Socket socket, String username) throws Exception {
		this.username = username;
		this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
		this.cmd = cmd;
		this.clientList = ServerThreadReceive.clientList;
	}

	@Override
	public void run() {
		switch (cmd) {
			case "Call_Sent_Data": {
//				doSentClientList();
//				doSentNewFeedList();
				break;
			}

			case "Call_Update_Client_List": {
				doUpdateClientList();

				break;
			}

			case "Call_Update_NewFeed_List": {
			//	doUpdateNewFeedList();

				break;
			}

			default: {
				break;
			}

		}


	}

	public void doSendData(String cmd, String... cont) {
		try {
			synchronized (dataOutputStream) {
				String msg = cmd;
				for (String s : cont) msg = msg + "<?>" + s;
				dataOutputStream.writeUTF(msg);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doSentClientList() {
		String sql = "select * from ta_aut_account";
		Connection connection = ConnectDB.ConnectionDB();

		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				doSendData("Sent_Client_List", resultSet.getString("T_username"), resultSet.getString("T_position"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//
//	public void doSentNewFeedList() {
//		try {
//			String sql = "select T_username_User,T_status,T_content,T_title from ta_con_browser";
//
//			Connection 			con = ConnectDB.ConnectionDB();
//			PreparedStatement 	pre = con.prepareStatement(sql);
//			ResultSet 			rs 	= pre.executeQuery(sql);
//
//			while (rs.next()) {
//				NewFeed newFeed = new NewFeed();
//
//				newFeed.setUsername(rs.getString(1));
//				newFeed.setStatus(rs.getString(2));
//				newFeed.setPath(rs.getString(3));
//				newFeed.setTitle(rs.getString(4));
//
//				doSendData("Sent_NewFeed",newFeed.getUsername(), newFeed.getTitle(), newFeed.getPath(), newFeed.getStatus());
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//	}
//
	public void doUpdateClientList() {
		String sql = "select * from ta_aut_account";
		Connection connection = ConnectDB.ConnectionDB();

		try {
			for (ServerThreadReceive client : clientList) {
				if (!client.getUsername().equals(username)) {
					client.getDataOutputStream().writeUTF("Update_Client_List");

				}
			}

			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				for (ServerThreadReceive client : clientList) {
					if (!client.getUsername().equals(username)) {
						client.getDataOutputStream().writeUTF("Sent_Client_List<?>" + resultSet.getString("T_username") + "<?>" + resultSet.getString("T_position"));

					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//
//	public void doUpdateNewFeedList() {
//		String sql = "select T_username_User,T_status,T_content,T_title from ta_con_browser";
//		Connection connection = ConnectDB.ConnectionDB();
//
//		try {
//			for (ServerThreadReceive client : clientList) {
//				if (!client.getUsername().equals(username)) {
//					client.getDataOutputStream().writeUTF("Update_NewFeed_List");
//
//				}
//			}
//
//			PreparedStatement preparedStatement = connection.prepareStatement(sql);
//			ResultSet resultSet = preparedStatement.executeQuery();
//
//			while (resultSet.next()) {
//				for (ServerThreadReceive client : clientList) {
//					if (!client.getUsername().equals(username)) {
//						NewFeed newFeed = new NewFeed();
//
//						newFeed.setUsername(resultSet.getString(1));
//						newFeed.setStatus(resultSet.getString(2));
//						newFeed.setPath(resultSet.getString(3));
//						newFeed.setTitle(resultSet.getString(4));
//
//						client.getDataOutputStream().writeUTF("Sent_NewFeed<?>" + newFeed.getUsername() + "<?>" + newFeed.getTitle() + "<?>" + newFeed.getPath() + "<?>" + newFeed.getStatus());
//
//					}
//				}
//
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//
}
