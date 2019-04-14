//
//  IPSearch.java
//  IPSearch
//
//  Created by Jacob Rosa on 2019.
//  Copyright © 2019 Jacob Rosa. All rights reserved.
//

package me.jacobrosa.ipsearch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class IPSearch extends JPanel{

	private static final long serialVersionUID = 6401044427898159938L;
	
	//Window variables
	private JFrame frame;
	private JPanel topPanel, middlePanel, bottomPanel;
	private JLabel inputLabel;
	private JTextField ipInput;
	private JScrollPane scrollBar;
	public JTextArea frameLog;
	private JButton lookupButton;
	
	//Log variables
	private Logger logger;

	//Program info 
	private final String title = "Simple IP Lookup Tool";
	private final String version = "1.1";
	
	private final Color defaultColor = new Color(238, 238, 238);

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
		
		applyTheme(Theme.LIGHT); //Set default light them colors

		//Init logger
		this.logger = new Logger(getInstance());

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
					int nextIndex = logger.getHistoryCount() - historyIndex;
					if(nextIndex >= 0) {
						ipInput.setText(logger.getFromHistory(nextIndex));
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
		logger.print("Copyright © 2019 Jacob Rosa. All rights reserved.");
		logger.print("By using this software you accept the license and disclaimer at https://github.com/JacobRosa/IPSearch/");
		logger.print("Type '/help' for a list of commands!");
	}
	
	public IPSearch getInstance() {
		return this;
	}

	//Handle lookup
	private void lookup() {
		historyIndex = 1; //Set history index back to 1
		lookupButton.setEnabled(false); //Prevent user from submitting while running

		String string = ipInput.getText().toString(); //Get IP input text
		logger.handleInput(string); //Handle input
		
		lookupButton.setEnabled(true); //Enable button
		ipInput.setText(""); //Clear input field
		
	}
	
	public void applyTheme(Theme theme) {
		switch(theme) {
		case LIGHT:
			topPanel.setBackground(defaultColor);
			middlePanel.setBackground(defaultColor);
			bottomPanel.setBackground(defaultColor);
			inputLabel.setForeground(Color.black);
			ipInput.setBackground(Color.white);
			frameLog.setBackground(Color.white);
			frameLog.setForeground(Color.black);
			scrollBar.setBackground(defaultColor);
			scrollBar.setBorder(null);
			break;
		case DARK:
			topPanel.setBackground(Color.darkGray);
			middlePanel.setBackground(Color.darkGray);
			bottomPanel.setBackground(Color.darkGray);
			inputLabel.setForeground(Color.white);
			ipInput.setBackground(Color.gray);
			frameLog.setBackground(Color.gray);
			frameLog.setForeground(Color.white);
			scrollBar.setBackground(Color.darkGray);
			scrollBar.setBorder(null);
			break;
		}
	}

}
