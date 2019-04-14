package me.jacobrosa.ipsearch.commands;

import me.jacobrosa.ipsearch.Logger;

public abstract class Command {
	
	public abstract void runCommand(Logger logger, String input);

}
