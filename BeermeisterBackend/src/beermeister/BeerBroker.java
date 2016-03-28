package beermeister;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BeerBroker {
	public static void start() {
		try {
			int port = 8020;
//					Integer.parseInt(args[0]);
			ServerSocket socket = new ServerSocket(port);
			
			for (;;) {
				System.out.println("wes");
				Socket client = socket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream());
				
				 out.print("HTTP/1.1 200 \r\n"); // Version & status code
			     out.print("Content-Type: text/plain\r\n"); // The type of data
			     out.print("Connection: close\r\n"); // Will close stream
			     out.print("\r\n"); // End of headers
				
				String line;
				while ((line = in.readLine()) != null) {
					if (line.length() == 0){
						break;
					}
					if(line.contains("GET")){
						out.print("Moki" + "\r\n");
						
					}
				}
				
				out.close();
				in.close();
				client.close();
			}
		}
		catch (Exception e) {
			System.err.println(e);
			System.err.println("Usage: java HttpMirror <port>");
			}
		}
}