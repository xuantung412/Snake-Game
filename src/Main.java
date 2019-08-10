import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 *  Main - The Program Runner
 *  
 *  Handles the instantiation of all the objects that are
 *  required to have the program up and running.
 * 
 * @author Ian Hanan, Zack Lomax, Mateusz Kowalski & Tung Nguyen
 */
public class Main {

	public static void main(String[] args) {
		// Instantiates the Account Management System
		UserAccountManagement accManagement = new UserAccountManagement();

		// Adding Accounts to the Management System
		UserAccount ac1 = new UserAccount("Tung1","123");
		UserAccount ac2 = new UserAccount("Tung2","123");
		UserAccount ac3 = new UserAccount("Tung3","123");
		UserAccount ac4 = new UserAccount("Tung4","123");
		
		accManagement.addAccount(ac1);
		accManagement.addAccount(ac2);
		accManagement.addAccount(ac3);
		accManagement.addAccount(ac4);
		
		// Instantiates the Game Server
		// 1 Thread  - Player Actions
		// 4 Threads - 1/4 AI Actions each
		Server theGameServer = new Server(5, accManagement);

		// Instantiates a listener for Player(Human) actions
		// Game Lobby is instantiated within the Controller
		HumanSnakeController controller = new HumanSnakeController(accManagement);

		// Instantiating the Game + Handles Game UI
		Game theGame = new Game(accManagement);

		// Adding the Player Actions listener to the Game Screen
		theGame.setKeyListener(controller);  

		// Injecting the Game into the Server 
		theGameServer.setGame(theGame);      

		// Setting the Number of AI
		theGameServer.setNumberOfAI(controller.numberOfAI);	

		// Logging in Human Players
		// NOTE: game must be set on the server before logging in players
		if(controller.numberOfPlayers >= 1){
			theGameServer.loginPlayer(controller.listOfHumans.get(0));
		}
		if(controller.numberOfPlayers >= 2){
			theGameServer.loginPlayer(controller.listOfHumans.get(1));
		}
		if(controller.numberOfPlayers >= 3){
			theGameServer.loginPlayer(controller.listOfHumans.get(2));
		}
		if(controller.numberOfPlayers >= 4){
			theGameServer.loginPlayer(controller.listOfHumans.get(3));
		}
		
		// Instantiating LinkedLists that will contain AI players
		LinkedList<Snake> aiThread1 = new LinkedList<Snake>();
		LinkedList<Snake> aiThread2 = new LinkedList<Snake>();
		LinkedList<Snake> aiThread3 = new LinkedList<Snake>();
		LinkedList<Snake> aiThread4 = new LinkedList<Snake>();

		// Calculating the number of AI players that each AI thread will maintain
		int aiPerThread = (int) theGameServer.getNumberOfAI() / 4;
		
		// The total number of AI may not be divisible by 4, therefore the last
		// thread might have more or less than the first 3.
		int aiLastThread = theGameServer.getNumberOfAI() - aiPerThread * 3;
		
		// Logging in AI players to the Game Server and adding them to their
		// respective LinkedLists
		for (int i = 0; i < aiPerThread; ++i) {
			Snake newSnake = new Snake();
			theGameServer.loginPlayer(newSnake);
			aiThread1.add(newSnake);
		}
		for (int i = 0; i < aiPerThread; ++i) {
			Snake newSnake = new Snake();
			theGameServer.loginPlayer(newSnake);
			aiThread2.add(newSnake);
		}
		for (int i = 0; i < aiPerThread; ++i) {
			Snake newSnake = new Snake();
			theGameServer.loginPlayer(newSnake);
			aiThread3.add(newSnake);
		}
		for (int i = 0; i < aiLastThread; ++i) {
			Snake newSnake = new Snake();
			theGameServer.loginPlayer(newSnake);
			aiThread4.add(newSnake);
		}

		// Instantiating AI threads
		Thread worker1 = new Thread(new AIWorkerThread(aiThread1, theGame));
		Thread worker2 = new Thread(new AIWorkerThread(aiThread2, theGame));
		Thread worker3 = new Thread(new AIWorkerThread(aiThread3, theGame));
		Thread worker4 = new Thread(new AIWorkerThread(aiThread4, theGame));

		// Instantiating UI for the game do after players have logged in
		theGame.init();
		theGame.renderGame();

		// Starting the AI threads
		worker1.start();
		worker2.start();
		worker3.start();
		worker4.start();

		// Starting the game
		theGameServer.startGame();
		accManagement.closeDB();
	}

}
