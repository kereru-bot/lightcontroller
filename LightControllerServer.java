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
			ProcessBuilder lights = new ProcessBuilder("sudo","python3","tkintertest.py");
			Process process = lights.start();
			
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
				
				while(true) {
					String command = reader.readLine(); // reads in a command from the client
					System.out.println(command);

					//client must've closed connection
					if(command == null) {
						break;
					}
					if(command.compareTo("activate") == 0) {
						ProcessBuilder lights = new ProcessBuilder("sudo","python3","test.py");
						Process process = lights.start();
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
