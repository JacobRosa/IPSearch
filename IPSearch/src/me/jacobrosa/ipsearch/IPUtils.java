package me.jacobrosa.ipsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.json.JSONObject;

public class IPUtils {

	public static void lookupIP(String inputUrl, Logger logger) {
		String baseUrl = "http://ip-api.com/json/" + inputUrl; //Base URL for the IP API

		URL url;

		try {
			url = new URL(baseUrl); //Set URL to baseUrl
			logger.print("Starting queue...");
			URLConnection conn = url.openConnection(); //Open connection
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream())); //Initialize buffer reader
			}catch(UnknownHostException connectionException) {
				logger.print("Connection Error.");
				logger.addToHistory(inputUrl); //Add input to history
				return;
			}

			String inputLine, jsonString = null;

			while ((inputLine = br.readLine()) != null) //Read through the lines of text
				jsonString = inputLine;

			br.close(); //Close the stream

			if(jsonString.isEmpty() || jsonString.equals(null))
				return;

			JSONObject jsonObject = new JSONObject(jsonString); //Create JsonObject
			String status = jsonObject.getString("status"); //Get response status

			if(status.equals("success")) {
				logger.print("--------------------------------------------------------------------");
				logger.print("Query: " + (jsonObject.has("query") ? jsonObject.getString("query") : inputUrl));
				if(jsonObject.has("country") && jsonObject.has("countryCode")) 
					logger.print("Country: " + "(" + jsonObject.getString("countryCode") + ") " + jsonObject.getString("country"));
				if(jsonObject.has("regionName")) 
					logger.print("Region: " + jsonObject.getString("regionName"));
				if(jsonObject.has("city")) 
					logger.print("City: " + jsonObject.getString("city"));
				if(jsonObject.has("zip")) 
					logger.print("Zip: " + jsonObject.getString("zip"));
				if(jsonObject.has("isp")) 
					logger.print("ISP: " + jsonObject.getString("isp"));
				if(jsonObject.has("org")) 
					logger.print("Organization: " + jsonObject.getString("org"));
				logger.print("--------------------------------------------------------------------");
			}else{
				logger.print("--------------------------------------------------------------------");
				logger.print("Query: " + (jsonObject.has("query") ? jsonObject.getString("query") : inputUrl));
				logger.print("Error: Invalid IP");
				logger.print("--------------------------------------------------------------------");
			}

			if(jsonObject.has("query"))
				logger.addToHistory(jsonObject.getString("query")); //Add searched IP to history

		}catch (IOException ex){
			logger.print("An error has occured.");
		}
	}
	
}
