/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package p2p;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author admin
 */

class ClientInfo {

    private int port;
    private Socket clientSocket;
    private String clientName;

    public ClientInfo(int port, Socket clientSocket, String clientName) {
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

public class RemoveClientOutServer {

    static final List<ClientInfo> clientInfos = new ArrayList<>();
   
}
