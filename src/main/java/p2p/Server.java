package p2p;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends RemoveClientOutServer {

    private static final RemoveClientOutServer rm = new RemoveClientOutServer();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1106);
        System.out.println("Server is running and waiting for connections...");

        while (true) {
            Socket clientSocket = serverSocket.accept();

            // Create a new thread to handle the client connection
            Thread clientThread = new Thread(() -> handleClient(clientSocket));
            clientThread.start();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            DataInputStream din = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());

            if (rm.clientInfos.isEmpty()) {
                System.out.println("There are no clients connected");
               
                dos.writeUTF("Hello, no clients available.");
            } else {
                System.out.println("There are clients connected");
                for (ClientInfo info : rm.clientInfos) {
                    dos.writeUTF("Hello, clients available: " + info.getClientName() + " Port: " + info.getPort());
                }
            }
            dos.writeUTF("Hello, no clients available.");
            
            String clientName = din.readUTF();
            System.out.println("Client connected: " + clientName);

            // Read port from client
            int clientPort = din.readInt();
            System.out.println("Received port from client: " + clientPort);

           //  Save client information to the list of clients
            ClientInfo clientInfo = new ClientInfo(clientPort, clientSocket, clientName);
            rm.clientInfos.add(clientInfo);
        } catch (Exception e) {
            // Handle client disconnection without stopping the server
            System.out.println("Client disconnected.");
            e.printStackTrace();
        }
    }
}

// Rest of your code remains the same
