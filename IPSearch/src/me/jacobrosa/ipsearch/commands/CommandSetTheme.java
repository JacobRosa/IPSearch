package me.jacobrosa.ipsearch.commands;

import me.jacobrosa.ipsearch.Logger;
import me.jacobrosa.ipsearch.Theme;

public class CommandSetTheme extends Command{

	@Override
	public void runCommand(Logger logger, String input) {
		String[] splitString = input.split(" "); //Split string 
		//Check if input has arguments
		if(splitString.length == 2) {
			String arg = splitString[1]; //Get argument
			Theme theme = Theme.valueOf(arg.toUpperCase()); //Get theme from string
			//Check if theme is null
			if(theme == null) {
				logger.print("Invalid theme!");
			}else {
				logger.getMainInstance().applyTheme(theme); //Apply the theme
				logger.print("Theme set to " + theme.name().toString().toLowerCase() + ".");
				if(theme == Theme.DARK)
					logger.print("Note: Program will reset to light mode once closed!");
			}
		}else {
			logger.print("Invalid argument for /settheme <light|dark>");
		}
		
	}

}
