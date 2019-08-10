import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

public class AIWorkerThread implements Runnable {
	LinkedList<Snake> AISnakes;
	Game theGame;

	// ai has access to the game so it can attempt to make a move based on the
	// grid
	public AIWorkerThread(LinkedList<Snake> AISnakes, Game theGame) {
		this.AISnakes = AISnakes;
		this.theGame = theGame;
	}

	class LocationNode {
		public int x;
		public int y;

		public LocationNode(int x, int y) {
			this.x = x;
			this.y = y;
			// sets the coordinates to the other side of screen if created
			// negatively
			if (x < 0) {
				this.x = theGame.gameSize - 1;
			}
			if (y < 0) {
				this.y = theGame.gameSize - 1;
			}
			if (x >= theGame.gameSize) {
				this.x = 0;
			}
			if (y >= theGame.gameSize) {
				this.y = 0;
			}
		}
	}

	@Override
	public void run() {
		while (AISnakes.size() > 0) {

			for (int index = 0; index < AISnakes.size(); ++index) {
				// int index = (int) (Math.random() * 1000) % AISnakes.size();
				// // chooses a random index of a snake in AISnakes
				// makeMove(index);
				// calculates a random number between 1 and 4
				int dir = (int) (Math.random() * 1000) % 4 + 1; 

				// AI have a 20% chance of changing direction to prevent them
				// from staying in the same spot.
				Random r = new Random();
				float chance = r.nextFloat();
				if (chance <= 0.80f && AISnakes.get(index).previousDirection != Buffer.direction.NONE) {
					AISnakes.get(index).addToBuffer(AISnakes.get(index).snakeID, AISnakes.get(index).previousDirection);
				} else {
					switch (dir) {
					case 1:
						AISnakes.get(index).addToBuffer(AISnakes.get(index).snakeID, Buffer.direction.UP);
						break;
					case 2:
						AISnakes.get(index).addToBuffer(AISnakes.get(index).snakeID, Buffer.direction.DOWN);
						break;
					case 3:
						AISnakes.get(index).addToBuffer(AISnakes.get(index).snakeID, Buffer.direction.LEFT);
						break;
					case 4:
						AISnakes.get(index).addToBuffer(AISnakes.get(index).snakeID, Buffer.direction.RIGHT);
						break;
					}
				}
			}
			// sleeps the thread
			try {
				int randomDelay = (int) (Math.random() * 1000) % 500 + 200;
				Thread.sleep(randomDelay);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
