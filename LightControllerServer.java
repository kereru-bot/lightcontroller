import java.io.*;
import java.net.*;
import java.util.*;

class LightControllerServer {
	public static void main(String args[]) {
		Random rand;
		ServerSocket servSocket;	
		System.out.println("Server Starting");
		
		if(args[0] == null) {
			System.out.println("Choose a port number, thanks");
			return;
		}
		
		try {
			int port = Integer.parseInt(args[0]);
			servSocket = new ServerSocket(port);
			System.out.println("Listening on port " + port);


			//Set up and run process builder to run python script	
			//ProcessBuilder lights = new ProcessBuilder("sudo","python3","tkintertest.py");
			//Process process = lights.start();
			
		} catch(IOException ex) {
			System.out.println("IOException " + ex);
			return;
		}
		
		
		while(true) {
			try {
				Socket socket = servSocket.accept();
				System.out.println("connection recieved");
				InputStream input = socket.getInputStream();
				InputStreamReader r = new InputStreamReader(input);
				BufferedReader reader = new BufferedReader(r);
				ProcessBuilder builder;
				while(true) {
					String command = reader.readLine(); // reads in a command from the client
					System.out.println(command);
					String[] parts = command.split(" ");
					//client must've closed connection
					if(command == null) {
						break;
					}

					if(command.compareTo("on") == 0) {
						//builder = new ProcessBuilder("sudo","on","test.py");

					} else if(command.compareTo("off") == 0) {
						builder = new ProcessBuilder("sudo","python3","off.py");
						builder.start();
					} else if(command.compareTo("random") == 0) {
						builder = new ProcessBuilder("sudo","python3","randomlights.py");
					} else if(parts[0].compareTo("rgb") == 0) {
						builder = new ProcessBuilder("sudo","python3","on.py",parts[1],parts[2],parts[3]);
						builder.start();
					} else if(parts[0].compareTo("brightness") == 0) {
						//ignore brightness for now
					}
				}
			} catch(IOException ex) {
				System.out.println("IOException: " + ex);
				return;
			}
		}
		
		//try {
		//	servSocket.close();
		//} catch(IOException ex) {
		//	System.out.println("IOException: " + ex);
		//	return;
		//}
	}
}
