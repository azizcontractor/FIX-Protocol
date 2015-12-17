package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Random;

public class Server {

	public static void main(String[] args) throws Exception {

		ServerSocket serverSocket = null;

        boolean listeningSocket = true;
        try {
            serverSocket = new ServerSocket(6667);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 6667");
        }
        System.out.println("IP Address: " + getIp() + "\tPort: 6667");
        System.out.println("Waiting for connection ...");

        while(listeningSocket){
            Socket clientSocket = serverSocket.accept();
            MiniServer mini = new MiniServer(clientSocket);
            mini.start();
        }
        serverSocket.close();       
		
	}
	
	public static String getIp() throws Exception {
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
