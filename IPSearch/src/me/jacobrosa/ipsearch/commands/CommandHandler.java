package me.jacobrosa.ipsearch.commands;

import java.util.HashMap;

import me.jacobrosa.ipsearch.Logger;

public class CommandHandler {
	
	//Command hashmap
	private HashMap<String, Command> commands;
	
	//Logger variable
	private Logger logger;
	
	public CommandHandler(Logger logger) {
		//Init
		this.logger = logger;
		this.commands = new HashMap<String, Command>();
		
		//Add commands
		addCommand("clear", new CommandClear());
		addCommand("help", new CommandHelp());
	}
	
	//Add command
	public void addCommand(String key, Command command) {
		this.commands.put(key.toLowerCase(), command); //Put command into command list
	}
	
	//Check if command is valid
	public boolean isValidCommand(String string) {
		string = string.toLowerCase().substring(1); //Put string to lower case and remove the slash
		return this.commands.containsKey(string.toLowerCase()); //Check if command list contains command
	}
	
	public void runCommand(String string) {
		string = string.toLowerCase().substring(1); //Put string to lower case and remove the slash
		//Check if command list contains command
		if(commands.containsKey(string)) {
			Command command = commands.get(string); //Get command class
			command.runCommand(logger); //Run command
			return;
		}else {
			invalidCommand(); //Print invalid command message
			return;
		}
	}
	
	//Print invalid command message
	public void invalidCommand() {
		logger.print("Invalid Command! Use /help for help!"); //Invalid command message
	}
	

}
