/**
 * Represents an individual snake and stores information such as location, 
 * id, last direction and size. 
 */
public class Snake implements Runnable{
	private Buffer theBufferToConnectTo;
	public int snakeID;
	public int snakeLocalsOnGrid[][];
	public Buffer.direction previousDirection = Buffer.direction.NONE;

	public int snakeSize = 1;
	private boolean destroyed = false;
	
	public Snake(){}
	
	/**
	 * Sets the buffer of the snake.
	 * @param theBuffer The buffer being used by the snake.
	 */
	public void setBuffer(Buffer theBuffer){
		theBufferToConnectTo = theBuffer;
	}
	
	/**
	 * Places the snake on the grid at the start of the game.
	 * @param x	Coordinates
	 * @param y	Coordinates
	 * @param gameSize	The size of the grid.
	 */
	public void initalizeSnakeLocation(int x, int y, int gameSize){
		snakeLocalsOnGrid = new int[gameSize*gameSize][2];
		// first initalize everything to -1
		for (int i = 0; i < gameSize * gameSize; i++) {
			snakeLocalsOnGrid[i][0] = -1;
			snakeLocalsOnGrid[i][1] = -1;
		}
		// now add the head location to the front
		snakeLocalsOnGrid[0][0] = x;
		snakeLocalsOnGrid[0][1] = y;
	}
	
	/**
	 * Adds the snake and direction to the buffer for the server thread
	 * to make a move.
	 * @param snakeID	The ID of the snake.
	 * @param theDirection The direction the snake is moving.
	 */
	public void addToBuffer(int snakeID, Buffer.direction theDirection){
		theBufferToConnectTo.addToBuffer(snakeID, theDirection);
	}
	
	/**
	 * The size of the snake, also used to determine the score of the snake.
	 * @return Length of the snake.
	 */
	public int getSize() {
		return snakeSize;
	}
	
	/**
	 * Increases the length of the snake.
	 */
	public void increaseSnakeSize() {
		snakeSize++;
	}
	
	/**
	 * A snake is destroyed once they are removed from the grid in 
	 * the ServerThread class.
	 */
	public void destroyed() {
		destroyed = true;		
	}
	
	/**
	 * Can be used to know if the snake is no longer in the game.
	 * @return True if the snake is destroyed, false if not.
	 */
	public boolean isDestroyed() {
		return destroyed;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
