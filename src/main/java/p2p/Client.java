package p2p;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String argv[]) throws Exception {
        Socket socketServer = new Socket("localhost", 1106);

        while (true) {
            Thread clientThread = new Thread(() -> CLientRun(socketServer));
            clientThread.start();
        }
    }

    public static void CLientRun(Socket socketServer) {

        Scanner sc = new Scanner(System.in);

        try {
            // Connect to the server

            DataInputStream din = new DataInputStream(socketServer.getInputStream());
            DataOutputStream dos = new DataOutputStream(socketServer.getOutputStream());

            while (true) {
                boolean checkStateListPort = din.readBoolean();

                if (checkStateListPort) {
                    System.out.println(din.readUTF());

                    System.out.print("enter name: ");
                    String name1 = sc.nextLine();
                    dos.writeUTF(name1);

                    System.out.print("enter port join meet: ");
                    String port = sc.nextLine();

                    int portInt = Integer.parseInt(port);

                    
                } else {
                    System.out.println(din.readUTF());

                    System.out.print("enter name: ");
                    String name = sc.nextLine();
                    dos.writeUTF(name);

                    System.out.print("enter port create server meet: ");
                    String port = sc.nextLine();
                    int portInt = Integer.parseInt(port);

                    dos.writeInt(portInt);

                    System.out.println("waitting client connect...");
                    // Corrected code for ServerSocket instantiation
                    ServerSocket sSClient = new ServerSocket(portInt);

                    try {
                        Socket sClinet = sSClient.accept();

                        DataInputStream din2 = new DataInputStream(sClinet.getInputStream());
                        DataOutputStream dos2 = new DataOutputStream(sClinet.getOutputStream());

                        String clientName = din2.readUTF();
                        System.out.println("Client connected: " + clientName);
                        dos2.writeUTF("hello client " + clientName);

                        while (true) {
                            System.out.println("client aswer: " + din2.readUTF());

                            System.out.print("enter message to client: ");
                            String message = sc.nextLine();

                            dos2.writeUTF(message);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(" sClinet - 93");
                    }
                }
            }

        } catch (Exception e) {

            try {

                System.out.print("enter port join meet: ");
                String port = sc.nextLine();

                int portInt = Integer.parseInt(port);

                Socket socketClient = new Socket("localhost", portInt);

                DataInputStream din3 = new DataInputStream(socketClient.getInputStream());
                DataOutputStream dos3 = new DataOutputStream(socketClient.getOutputStream());

                System.out.print("enter name: ");
                String name1 = sc.nextLine();
                dos3.writeUTF(name1);

                while (true) {
                    System.out.println("client aswer: " + din3.readUTF());

                    System.out.print("enter message to client: ");
                    String message = sc.nextLine();

                    dos3.writeUTF(message);
                }

            } catch (Exception e1) {
                System.out.println("don't access socket");
                System.out.println(" socketClient - 131 ");
            }

        }

    }

}
