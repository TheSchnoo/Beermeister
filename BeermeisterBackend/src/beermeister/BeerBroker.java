package beermeister;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BeerBroker {
	public static void start() {
		try {
			AccessDatabase accessDB = new AccessDatabase();
			
			int port = 8020;
//					Integer.parseInt(args[0]);
			ServerSocket socket = new ServerSocket(port);
			
			for (;;) {
				System.out.println("connection");
				Socket client = socket.accept();
				BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				PrintWriter out = new PrintWriter(client.getOutputStream());
				
				 out.print("HTTP/1.1 200 \r\n"); // Version & status code
			     out.print("Content-Type: text/plain\r\n"); // The type of data
			     out.print("Connection: close\r\n"); // Will close stream
			     out.print("Access-Control-Allow-Headers: Content-Type\r\n");
			     out.print("Access-Control-Allow-Methods: GET, POST, OPTIONS\r\n");
			     out.print("Access-Control-Allow-Origin: *\r\n");
			     out.print("\r\n"); // End of headers
				
				String line;
				while ((line = in.readLine()) != null) {
					if (line.length() == 0){
						break;
					}
					// Get requests
					if(line.contains("GET")){
						// Get recommended beers by userid
						if(line.contains("/?/recommendedbeers?userid=")){
							String user = line.split("userid=")[1];
							out.print(accessDB.searchBeers(new String[0]));
						}
						// Search beers by various parameters in form GET /searchbeers?&ibu=IBU&abv=
						// ABV&breweryname=BREWERYNAME&type=TYPE&name=NAME&rating=RATING&vendor=VENDORNAME
						if(line.contains("/searchbeers")){
							String[] searchParams = line.split("&");
							out.print(accessDB.searchBeers(searchParams));
//							if(splitLine.length < 2){
//								out.print(accessDB.searchBeers());
//							}
//							else{
//								for (int i=0; i < splitLine.length; i++){
//									if(splitLine[i].contains("ibu=")){
//										splitLine[i] = splitAndReplace(splitLine[i], "ibu=");
//										continue;
//									}
//									if(splitLine[i].contains("brewery=")){
//										splitLine[i] = splitAndReplace(splitLine[i], "brewery=");
//										out.print(accessDB.getBeersFromBrewery(splitLine[i]));
//									}
//								}
//							}
						}
						
						if(line.contains("/reviews")){
							// Search reviews by userid in form GET /reviews?&userid=USERID
							if(line.contains("userid=")){
								
							}
							// Search reviews by beerid in form GET /reviews/BEERID
							else{
								
							}
						}
						// Search vendors by beerid in form GET /vendors/BEERID
						if(line.contains("/vendors")){
							if(line.split("vendors/").length > 1){
								String beer = line.split("vendors/")[1];
								out.print(accessDB.getVendors(beer));
							}
							else{
								out.print(accessDB.getVendors(""));
							}
						}
					}
				}
				
				out.close();
				in.close();
				client.close();
			}
		}
		catch (Exception e) {
			System.err.println(e);
			}
		}
}