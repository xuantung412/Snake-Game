import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;

import javax.swing.JOptionPane;

public class HumanSnakeController implements KeyListener{

	LinkedList<Snake> listOfHumans; // List of the human snake players currently we will hardcode it to 4
	UserAccountManagement man;
	int numberOfPlayers;
	int numberOfAI;
	
	/**
	 * Adds available accounts into the mapdb.
	 * @param accManagement Manages logged in user accounts.
	 */
	public HumanSnakeController(UserAccountManagement accManagement){
		numberOfPlayers = 0;	
		numberOfAI = 0;

		//add new account
		UserAccount ac1 = new UserAccount("Ian","123");
		UserAccount ac2 = new UserAccount("Mati","123");
		UserAccount ac3 = new UserAccount("Tung","123");
		UserAccount ac4 = new UserAccount("Zack","123");
		accManagement.addAccount(ac1);
		accManagement.addAccount(ac2);
		accManagement.addAccount(ac3);
		accManagement.addAccount(ac4);
		
		LoginManagement logMan = new LoginManagement(accManagement, this);
		logMan.setVisible(true);
		
		while(logMan.startGame == false) {
			// This while loop allows for the lobby to stay open
			// Once the game has started, we will close it.
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {}
		}

		// Creates a new snake for each player and populates the list.
		listOfHumans = new LinkedList<Snake>();
		Snake player1 = new Snake();
		Snake player2 = new Snake();
		Snake player3 = new Snake();
		Snake player4 = new Snake();
		// Creates the number of snakes required for the number of players.
		if(numberOfPlayers >=1 ){
			listOfHumans.add(player1);
		}
		if(numberOfPlayers >=2 ){
			listOfHumans.add(player2);
		}
		if(numberOfPlayers >=3 ){
			listOfHumans.add(player3);
		}
		if(numberOfPlayers >=4 ){
			listOfHumans.add(player4);
		}
		
	}

	/**
	 * A key listener that waits for keyboard input. Each user is 
	 * assigned their own keys, if a user makes a key press the controller
	 * will interpret the direction and player that pressed the key and 
	 * put the snake and direction into the buffer.
	 */
	@Override
	public void keyReleased(KeyEvent event) {
		// Need to interpret the key pressed on the keyboard and figure out what key corresponds to 
		// what player. For example if W is pressed then use 
		// listOfHumans.get(0).addToBuffer(Buffer.direction.UP);

		switch(event.getKeyCode()){
		//Check avaiable for player1 and lock key by change it value
		// Player 1 keys
		case 37: // LEFT
			if(this.numberOfPlayers >=1){
				listOfHumans.get(0).addToBuffer(0, Buffer.direction.LEFT);
			}
			break;
		case 38: // UP
			if(this.numberOfPlayers >=1){
				listOfHumans.get(0).addToBuffer(0, Buffer.direction.UP);
			}
			break;				
		case 39: // RIGHT
			if(this.numberOfPlayers >=1){

				listOfHumans.get(0).addToBuffer(0, Buffer.direction.RIGHT);
			}
			break;				
		case 40: // DOWN
			if(this.numberOfPlayers >=1){

				listOfHumans.get(0).addToBuffer(0, Buffer.direction.DOWN);
			}
			break;			

			// Player 2 keys
		case 65: // A
			if(this.numberOfPlayers >=2){
				listOfHumans.get(1).addToBuffer(1, Buffer.direction.LEFT);
			}
			break;			
		case 68: // D
			if(this.numberOfPlayers >=2){
				listOfHumans.get(1).addToBuffer(1, Buffer.direction.RIGHT);
			}
			break;				
		case 83: // S
			if(this.numberOfPlayers >=2){
				listOfHumans.get(1).addToBuffer(1, Buffer.direction.DOWN);
			}
			break;				
		case 87: // W
			if(this.numberOfPlayers >=2){
				listOfHumans.get(1).addToBuffer(1, Buffer.direction.UP);
			}
			break;

			// Player 3 keys
		case 74: // J
			if(this.numberOfPlayers >=3){
				listOfHumans.get(2).addToBuffer(2, Buffer.direction.LEFT);
			}
			break;			
		case 76: // L
			if(this.numberOfPlayers >=3){
				listOfHumans.get(2).addToBuffer(2, Buffer.direction.RIGHT);
			}
			break;				
		case 75: // K
			if(this.numberOfPlayers >=3){
				listOfHumans.get(2).addToBuffer(2, Buffer.direction.DOWN);
			}
			break;				
		case 73 : // I
			if(this.numberOfPlayers >=3){
				listOfHumans.get(2).addToBuffer(2, Buffer.direction.UP);
			}
			break;

			// Player 4 keys
		case 100: // NUMPAD 4
			if(this.numberOfPlayers >=4){
				listOfHumans.get(3).addToBuffer(3, Buffer.direction.LEFT);
			}
			break;			
		case 102: // NUMPAD 6
			if(this.numberOfPlayers >=4){
				listOfHumans.get(3).addToBuffer(3, Buffer.direction.RIGHT);
			}
			break;				
		case 101: // NUMPAD 5
			if(this.numberOfPlayers >=4){
				listOfHumans.get(3).addToBuffer(3, Buffer.direction.DOWN);
			}
			break;				
		case 104: // NUMPAD 8
			if(this.numberOfPlayers >=4){
				listOfHumans.get(3).addToBuffer(3, Buffer.direction.UP);
			}
			break;	
		}		
	}

	// UNIMPLEMENTED METHODS - Don't need to touch these
	@Override
	public void keyPressed(KeyEvent arg0) {}
	@Override
	public void keyTyped(KeyEvent arg0) {}
}
