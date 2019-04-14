package me.jacobrosa.ipsearch.commands;

import me.jacobrosa.ipsearch.Logger;

public class CommandHelp extends Command{

	@Override
	public void runCommand(Logger logger, String input) {
		logger.print("------ Command Help ------");
		logger.print("/clear - Clears log");
		logger.print("/clearhistory - Clears search history");
		logger.print("/help - Prints this message");
		logger.print("/setTheme <light|dark> - Set window theme");
	}

}
