package clientserver.services;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket serverSocket;

	public Server(int port) throws Exception {
		serverSocket = new ServerSocket(port);
		System.out.println("Server runs on port " + port);
	}

	public void startServer() {
		try {
			while (!serverSocket.isClosed()) {
				Socket socket = serverSocket.accept();
				System.out.println(socket.getRemoteSocketAddress() + " has conneted");
            	
            	ServerCheckInfo checkInfo = new ServerCheckInfo(socket);
            	checkInfo.start();
            	
            }
    
		} catch (Exception e) {
			closeServer();
		}
	}



	public void closeServer() {
		try {
			if (serverSocket != null) {
				serverSocket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		try {
			Server server = new Server(1234);
			server.startServer();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}