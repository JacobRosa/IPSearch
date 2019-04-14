package me.jacobrosa.ipsearch.commands;

import me.jacobrosa.ipsearch.Logger;

public class CommandClearHistory extends Command{

	@Override
	public void runCommand(Logger logger, String input) {
		logger.clearHistory();
	}

}
