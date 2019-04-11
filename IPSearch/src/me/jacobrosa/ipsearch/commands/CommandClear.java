package me.jacobrosa.ipsearch.commands;

import me.jacobrosa.ipsearch.Logger;

public class CommandClear extends Command{

	@Override
	public void runCommand(Logger logger) {
		logger.clearLog();
	}

}
