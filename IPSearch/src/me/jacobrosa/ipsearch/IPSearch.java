//
//  IPSearch.java
//  IPSearch
//
//  Created by Jacob Rosa on 2019.
//  Copyright © 2019 Jacob Rosa. All rights reserved.
//

package me.jacobrosa.ipsearch;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import org.json.JSONObject;

public class IPSearch extends JPanel{

	private static final long serialVersionUID = 6401044427898159938L;

	//Window variables
	private JFrame frame;
	private JPanel topPanel, middlePanel, bottomPanel;
	private JLabel inputLabel;
	private JTextField ipInput;
	private JScrollPane scrollBar;
	private JTextArea frameLog;
	private JButton lookupButton;

	//Program info 
	private final String title = "Simple IP Lookup Tool";
	private final String version = "1.0";

	//Search History
	private List<String> searchedLog;

	//Placeholder used for search history indexing
	private int historyIndex = 1;

	//Called on program start
	public static void main(String[] string) {
		new IPSearch();
	}

	//Main initialization function
	public IPSearch() {
		//Initialize frame objects
		frame = new JFrame(title + " " + version);
		topPanel = new JPanel();
		middlePanel = new JPanel();
		bottomPanel = new JPanel();
		inputLabel = new JLabel("IP:");
		ipInput = new JTextField();
		frameLog = new JTextArea(20, 50);
		scrollBar = new JScrollPane(frameLog);
		lookupButton = new JButton("Look Up");
		searchedLog = new ArrayList<String>();

		//Frame setup
		frame.setSize(700, 460);
		frame.setResizable(false);

		//Frame log setup
		frameLog.setEditable(false);
		frameLog.setAutoscrolls(true);

		//IP input setup
		ipInput.setColumns(30);
		ipInput.setSize(200, 30);

		//Borders setup
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 5));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 8, 5));

		//Scroll bar setup
		scrollBar.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		//Add panels to frame
		topPanel.add(inputLabel, BorderLayout.WEST);
		topPanel.add(ipInput, BorderLayout.NORTH);
		middlePanel.add(scrollBar);
		bottomPanel.add(lookupButton);
		frame.add(topPanel, BorderLayout.PAGE_START);
		frame.add(middlePanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.PAGE_END);

		//Handle IP input listeners
		ipInput.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				//Handle enter pressed
				if(keyCode == KeyEvent.VK_ENTER) {
					lookup();
					return;
				}

				//Handle up arrow pressed
				if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_KP_UP) {
					int nextIndex = searchedLog.size() - historyIndex;
					if(nextIndex >= 0) {
						ipInput.setText(searchedLog.get(nextIndex));
						historyIndex++;
						return;
					}
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				//If input text edited set back to first index
				if(historyIndex != 1)
					historyIndex = 1;
			}
		});

		//Handle lookup button pressed
		lookupButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				lookup();
				return;
			}
		});

		//Final frame setup
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Close window
		frame.setLocationRelativeTo(null); //Open in center of screen
		frame.setVisible(true); //Show window

		//Startup message
		log("Copyright © 2019 Jacob Rosa. All rights reserved.");
		log("By using this software you agree to follow the the license and disclaimer at https://github.com/JacobRosa/IPSearch/");
	}

	//Add message to frame log
	private void log(String msg) {
		frameLog.setText(frameLog.getText() + msg + "\n"); //Add message to log and add new line
	}

	//Add items to history log
	private void addToHistoryLog(String string) {
		if(string.isEmpty() || string.equals(null))
			return;
		int logCount = searchedLog.size(); //Get current count of logs
		int maxLogCount = 100; //Max amount of items we want saved
		if(logCount >= maxLogCount)
			searchedLog.remove(0); //Remove oldest item
		searchedLog.add(string); //Add new item
	}

	//Lookup IP method
	public void lookup() {
		historyIndex = 1; //Set history index back to 1
		lookupButton.setEnabled(false); //Prevent user from submitting while running

		String inputUrl = ipInput.getText().toString(); //Get IP input text
		String baseUrl = "http://ip-api.com/json/" + inputUrl; //Base URL for the IP API

		URL url;

		try {
			url = new URL(baseUrl); //Set URL to baseUrl
			log("Starting queue...");
			URLConnection conn = url.openConnection(); //Open connection
			BufferedReader br;
			try {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream())); //Initialize buffer reader
			}catch(UnknownHostException connectionException) {
				log("Connection Error.");
				addToHistoryLog(inputUrl); //Add input to history
				ipInput.setText(""); //Clear input field
				lookupButton.setEnabled(true); //Enable submit button again
				return;
			}

			String inputLine, jsonString = null;

			while ((inputLine = br.readLine()) != null) //Read through the lines of text
				jsonString = inputLine;

			br.close(); //Close the stream

			if(jsonString.isEmpty() || jsonString.equals(null)) {
				ipInput.setText(""); //Clear input field
				lookupButton.setEnabled(true); //Enable submit button again
				return;
			}

			JSONObject jsonObject = new JSONObject(jsonString); //Create JsonObject
			String status = jsonObject.getString("status"); //Get response status

			if(status.equals("success")) {
				log("--------------------------------------------------------------------");
				log("Query: " + (jsonObject.has("query") ? jsonObject.getString("query") : inputUrl));
				if(jsonObject.has("country") && jsonObject.has("countryCode")) 
					log("Country: " + "(" + jsonObject.getString("countryCode") + ") " + jsonObject.getString("country"));
				if(jsonObject.has("regionName")) 
					log("Region: " + jsonObject.getString("regionName"));
				if(jsonObject.has("city")) 
					log("City: " + jsonObject.getString("city"));
				if(jsonObject.has("zip")) 
					log("Zip: " + jsonObject.getString("zip"));
				if(jsonObject.has("isp")) 
					log("ISP: " + jsonObject.getString("isp"));
				if(jsonObject.has("org")) 
					log("Organization: " + jsonObject.getString("org"));
				log("--------------------------------------------------------------------");
			}else{
				log("--------------------------------------------------------------------");
				log("Query: " + (jsonObject.has("query") ? jsonObject.getString("query") : inputUrl));
				log("Error: Invalid IP");
				log("--------------------------------------------------------------------");
			}

			if(jsonObject.has("query"))
				addToHistoryLog(jsonObject.getString("query")); //Add searched IP to history

		}catch (IOException ex){
			log("An error has occured.");
		}

		ipInput.setText(""); //Clear input field
		lookupButton.setEnabled(true); //Enable search button again
	}

}
