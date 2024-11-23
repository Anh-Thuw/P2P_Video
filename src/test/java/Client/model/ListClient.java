package Client.model;

import java.net.Socket;
import java.util.ArrayList;

public class ListClient {
    public		static ArrayList<ListClient> list 		=	new ArrayList<>();

    private int             port;
    private Socket          clientSocket;
    private String          clientName;

    public ListClient(int port, Socket clientSocket, String clientName) {
        this.port = port;
        this.clientSocket = clientSocket;
        this.clientName = clientName;
    }
    public int getPort() {
        return port;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public String getClientName() {
        return clientName;
    }

}
