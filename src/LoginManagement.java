import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *  LoginManagement - The Player Lobby
 *  
 *  Provides the user with an interface that allows them
 *  to specify how many AI players they would like in the 
 *  game as well as allowing multiple user's to join the 
 *  lobby.
 * 
 * @author Mateusz Kowalski
 */
class LoginManagement extends JFrame implements ActionListener
{
	// Swing Elements
	JPanel mainPanel;
	// AI Panel
	JPanel topPanel;
	JPanel aiPanel;
	JTextField aiTextBox;
	// Lobby
	JPanel bottomPanel;
	JPanel playerPanel;
	JLabel playersLabel;
	JLabel player1;
	JLabel player2;
	JLabel player3;
	JLabel player4;
	// Buttons
	JButton addButton;
	JButton startButton;

	// Variable and Object Storage
	boolean startGame;
	int playerCount;
	UserAccountManagement accManagementStore;
	HumanSnakeController parentStore;

	public LoginManagement(UserAccountManagement accManagement, HumanSnakeController parent){	
		startGame = false;
		accManagementStore = accManagement;
		parentStore = parent;

		setTitle("Snake - Player Lobby");
		setLayout(new BorderLayout());
		setSize(300, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Centering the Lobby Window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((screenSize.getWidth() - getWidth()) / 2);
		int y = (int) ((screenSize.getHeight() - getHeight()) / 2);
		setLocation(x, y);

		setVisible(true);



		playerCount = 0;

		GridLayout layout = new GridLayout(4,0);
		layout.setVgap(10);

		mainPanel = new JPanel(layout);
		mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		mainPanel.add(createTopPanel());
		mainPanel.add(createBottomPanel());

		addButton = new JButton("Login");
		addButton.setSize(new Dimension(100, 100));
		addButton.setActionCommand("AddPlayer");
		addButton.addActionListener(this);
		mainPanel.add(addButton);

		startButton = new JButton("Start");
		startButton.setActionCommand("StartGame");
		startButton.addActionListener(this);
		mainPanel.add(startButton);

		add(mainPanel);
	}

	/**
	 * Creates a JPanel that has a JLabel and JTextField that prompt
	 * and allow the user to input the desired amount of AI players
	 * they would like to have in their game.
	 * 
	 * @return a JPanel with a JTextField for the desired number of AI
	 */
	public JPanel createTopPanel() {
		// Instantiating and Styling the Swing Elements
		topPanel = new JPanel(new GridLayout(2,0));	
		topPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		aiTextBox = new JTextField("0");
		aiTextBox.setHorizontalAlignment(JTextField.CENTER);

		// Adding those elements to the JPanel
		topPanel.add(new JLabel("Desired # of AI:", SwingConstants.CENTER));
		topPanel.add(aiTextBox);

		return topPanel;
	}

	/**
	 * Creates a JPanel that has 5 JLabels within it. One to display a
	 * heading with the number of currently logged in players and the 
	 * other four to display player information of the players who are 
	 * currently logged in.
	 * 
	 * @return a JPanel with JLabels that display lobby information
	 */
	public JPanel createBottomPanel() {
		// Instantiating and Styling the Swing Elements
		GridLayout layout = new GridLayout(5,0);
		layout.setVgap(5);

		bottomPanel = new JPanel(layout);
		bottomPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		playersLabel = new JLabel("Players (" + playerCount + "/4)", SwingConstants.CENTER);

		player1 = new JLabel("...", SwingConstants.CENTER);
		player2 = new JLabel("...", SwingConstants.CENTER);
		player3 = new JLabel("...", SwingConstants.CENTER);
		player4 = new JLabel("...", SwingConstants.CENTER);

		// Adding those elements to the JPanel
		bottomPanel.add(playersLabel);
		bottomPanel.add(player1);
		bottomPanel.add(player2);
		bottomPanel.add(player3);
		bottomPanel.add(player4);

		return bottomPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("AddPlayer")) {
			if(playerCount < 4) {
				Login frame = new Login(accManagementStore, 4, this);
				parentStore.numberOfPlayers++;
				Thread login = new Thread(frame);
				login.run();
			} else {
				JOptionPane.showMessageDialog(null, "The lobby is full.");
			}
		}

		if(e.getActionCommand().equals("StartGame")) {

			int checkCount = 0;
			boolean[] errorCheck = new boolean[3];

			if(isNumeric(aiTextBox.getText())) {
				errorCheck[0] = true;
			} else {
				errorCheck[0] = false;
				JOptionPane.showMessageDialog(null, "Please make sure the number of AI players is an integer.");
			}

			if(playerCount > 0) {
				errorCheck[1] = true;
			} else {
				errorCheck[1] = false;
				JOptionPane.showMessageDialog(null, "Not enough human players.");
			}
			
			if(playerCount < 101) {
				errorCheck[2] = true;
			} else {
				errorCheck[2] = false;
				JOptionPane.showMessageDialog(null, "Too many AI, the maximum allowed is 100.");
			}

			for(int i = 0; i < errorCheck.length; i++) {
				if(errorCheck[i] == true) {
					checkCount++;
				}
			}
			
			if(checkCount == 3) {
				parentStore.numberOfAI = Integer.parseInt(aiTextBox.getText());
				startGame = true;
				dispose();
			} else {
				checkCount = 0;
			}
			
		}
	}

	/**
	 * A helper method that rewrites the number of logged in players
	 * to the playersLabel in the lobby.
	 */
	public void rewriteLobbyCounter() {
		playersLabel.setText("Players (" + playerCount + "/4)");
	}

	/**
	 * A helper method that checks whether an input string is numeric,
	 * this is used to check whether an integer is input by the user
	 * in the aiTextBox.
	 */
	private boolean isNumeric(String inputString) {
		try {
			int tryConvert = Integer.parseInt(inputString);
		} catch(NumberFormatException e) {
			return false;
		}
		return true;
	}

}
