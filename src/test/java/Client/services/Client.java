package Client.services;

import java.net.Socket;

import Client.tmpl.Home;




public class Client {
    private 	Socket 		socket;
    private 	String 		username;

	private Home home;

    public Client(Socket socket, String username) throws Exception {
            this.socket		= 	socket;
            this.username 	= 	username;
    }


	public void start() throws Exception {
		home = new Home(socket , username);
		home.setVisible(true);

		ClientThreadReceive receive = new ClientThreadReceive(socket, username, home);
		receive.start();

	}

}