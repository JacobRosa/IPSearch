package me.jacobrosa.ipsearch;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import me.jacobrosa.ipsearch.commands.CommandHandler;

public class Logger {

	//Instances
	private IPSearch ipSearch;
	
	//Variables
	private JTextArea textarea;
	
	//Command handler variable
	private CommandHandler commandHandler;
	
	//Search history list
	private List<String> logHistory;
	
	public Logger(IPSearch ipSearch) {
		//Init
		this.ipSearch = ipSearch;
		this.textarea = ipSearch.frameLog;
		this.commandHandler = new CommandHandler(this);
		this.logHistory = new ArrayList<String>();
	}
	
	//Get main instance
	public IPSearch getMainInstance() {
		return ipSearch;
	}
	
	//Get text area
	public JTextArea getTextArea() {
		return this.textarea;
	}
	
	//Print string to log
	public void print(String string) {
		this.textarea.setText(textarea.getText() + string + "\n");
	}
	
	//Handle input
	public void handleInput(String string) {
		//Check if its a command
		if(string.startsWith("/")) {
			addToHistory(string); //Add command to history
			//Check if command exists
			if(commandHandler.isValidCommand(string)) {
				commandHandler.runCommand(string); //Run command
				return;
			}else {
				commandHandler.invalidCommand(); //Send invalid command message
				return;
			}
		}else {
			IPUtils.lookupIP(string, this); //If not command treat as IP
			return;
		}
	}
	
	//Clear log
	public void clearLog() {
		this.textarea.setText(""); //Set text to nothing
	}
	
	//Add string to history
	public void addToHistory(String string) {
		//Check if string is null
		if(string.isEmpty() || string.equals(null))
			return;
		int logCount = getHistoryCount(); //Get current count of logs
		int maxLogCount = 100; //Max amount of items we want saved
		if(logCount >= maxLogCount)
			removeFirstHistoryItem(); //Remove oldest item
		logHistory.add(string); //Add new item
	}
	
	//Remove first item from history log
	public void removeFirstHistoryItem() {
		this.logHistory.remove(0); //Remove first item
	}
	
	//Get value from history from index
	public String getFromHistory(int index) {
		return this.logHistory.get(index); //Get string based on index
	}
	
	//Count history items
	public int getHistoryCount() {
		return this.logHistory.size(); //Return history count
	}
		
	//Clear search history
	public void clearHistory() {
		this.logHistory.clear(); //Clear log history
	}

}
