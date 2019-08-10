import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	ExecutorService executor;
	boolean running = true;
	int threads;
	int numberOfAI = 6;
	
	int snakeID = 0; // can be used to assign id's definitely not the best for a real world scenario but for this its fine (maybe change later)
	
	Buffer gameBuffer;
	Game theSnakeGame;
	UserAccountManagement man;
	
	public Server(int numOfThreads, UserAccountManagement man){	
		this.man = man;
		gameBuffer = new Buffer();		
		threads = numOfThreads;
		executor = Executors.newFixedThreadPool(threads);	
	}
	
	/**
	 * I replace this method to automatic add player to the game when a user log in
	 *-- By Tran
	 * @param playerToLogin
	 */
	// This will have to be replaced by a proper login function to his specs but for now to run the game will use this
	public void loginPlayer(Snake playerToLogin){
		playerToLogin.setBuffer(gameBuffer);		
		// setting id as its logged in
		playerToLogin.snakeID = snakeID;
		snakeID++;
		// Adding snake to the game itself
		theSnakeGame.snakesList.add(playerToLogin);
	}
	
	// Game starts running on call with while loop here - maybe change later?
	public void startGame(){
		while(running){
			theSnakeGame.renderGame();
			
			Runnable worker;
			try {
				worker = new ServerThread(gameBuffer.pollBuffer(), theSnakeGame);
				executor.execute(worker);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
//			if(gameBuffer.bufferQueue.size() > 0){				
//				System.out.println("here");
//				Runnable worker = new ServerThread(gameBuffer.pollBuffer(), theSnakeGame);
//				executor.execute(worker);
//			}
		}
		
	}	
	
	public void setGame(Game theGame){
		theSnakeGame = theGame;
	}
	
	public void setNumberOfAI(int n) {
		numberOfAI = n;
	}
	
	public int getNumberOfAI() {
		return numberOfAI;
	}
}
