import java.io.*;
import java.net.*;
import java.util.*;

class LightControllerClient {
	static int red;
	static int green;
	static int blue;
	static int brightness;
	public static void main(String args[]) {
		OutputStream output;
		InputStream input;
		PrintWriter writer;
		Socket socket;
		int port;
		String address;
		red = 0;
		green = 0;
		blue = 0;
		brightness = 0;
		if(args[0] == null) {
			System.out.println("Choose an ip address, thanks");
			return;
		}

		if(args[1] == null) {
			System.out.println("Choose a port number, thanks");
		}
		
		try {
			address = args[0];
			port = Integer.parseInt(args[1]);
		} catch(Exception ex) {
			System.out.println("Exception " + ex);
			return;
		}
		System.out.println("Starting server...");
		try {
			socket = new Socket(address,port);
			input = socket.getInputStream();
			output = socket.getOutputStream();
			writer = new PrintWriter(output, true);
			if(readConfig() == true) {
				System.out.println("Config successfully loaded");
			} else {
				System.out.println("Error loading config");
			}
			//initialise lights from saved values
			sendCommand("rgb" + " " + red + " " + green + " " + blue, writer);
			sendCommand("brightness" + " " + brightness, writer);
			while(true) {
				System.out.println("Awaiting command, type help for list of commands");
				String command = System.console().readLine();
				if(command.toLowerCase().compareTo("help") == 0) {
					//print commands file
					FileReader reader = new FileReader(new File("commands"));
					int charVal = 0;
					while((int)(charVal = reader.read()) != -1) {
						char c = (char)charVal;
						if(c == '\n') {
							System.out.println();
						}
						System.out.print(c);
					}
					reader.close();
				} else if(command.toLowerCase().compareTo("exit") == 0) {
					socket.close();
					return;
				}
				sendCommand(command, writer); //sends command to the server
				//System.out.println(command);
			}
		
		} catch(UnknownHostException ex) {
			System.out.println("Unknown Host Exception " + ex);
			return;
		} catch(IOException ex) {
			System.out.println("IOException " + ex);
			return;
		}
		
	}

	public static boolean sendCommand(String command, PrintWriter writer) {

		
			try {
				//check command types, incase some info needs to be updated
				String[] parts = command.split(" ");
				if(parts[0].compareTo("rgb") == 0) {
					red = Integer.parseInt(parts[1]);
					green = Integer.parseInt(parts[2]);
					blue = Integer.parseInt(parts[3]);
				} else if(parts[0].compareTo("brightness") == 0) {
					brightness = Integer.parseInt(parts[1]);
					//not really a good command atm
					//need to think of better solution
				}
				 else if(parts[0].compareTo("save") == 0) {
					saveConfig(red, green, blue, brightness);
					return true;
				} else if(parts[0].compareTo("on") == 0) {
					 command = "rgb" + " " + red + " " + green + " " + blue;
				 }

			} catch(Exception ex) {
				System.out.println("Exception: " + ex);
				return false;
			}
			writer.println(command);

		return true;
	}

	public static boolean readConfig() {
		File config = new File("config.txt");
		if (!config.exists()) {
			saveConfig(20, 20, 20, 20);
			red = 20;
			green = 20;
			blue = 20;
			brightness = 20;
			return true;
		}

		try {
			FileReader reader = new FileReader(config);
			char[] buffer = new char[255];
			int cRead = reader.read(buffer, 0, 255);
			int currentChar = 0;
			String line = new String(buffer, 0, cRead);
			String[] vals = line.split(",");
			red = Integer.parseInt(vals[0]);
			green = Integer.parseInt(vals[1]);
			blue = Integer.parseInt(vals[2]);
			brightness = Integer.parseInt(vals[3]);
			reader.close();
			return true;
		} catch (FileNotFoundException ex) {
			System.out.println("FileNotFoundException: " + ex);
		} catch (Exception ex) {
			System.out.println("Exception: " + ex);
		}
		return false;
	}

	public static boolean saveConfig(int red, int green, int blue, int brightness) {
		File config = new File("config.txt");
		try {
			FileWriter writer = new FileWriter(config, false);
			String vals = red + "," + green + "," + blue + "," + brightness;
			writer.write(vals);
			writer.close();
			return true;
		} catch (IOException ex) {
			System.out.println("IOException: " + ex);
		}
		return false;
	}
}
