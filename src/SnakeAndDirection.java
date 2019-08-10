/**
 * Used to send snake moves to the buffer. Contains snake ID and
 * the direction in which they are being moved.
 */
public class SnakeAndDirection {
	
	Buffer.direction direction;
	int snakeID;
	
	/**
	 * Constructor
	 * @param snakeID	ID of the snake being moved.
	 * @param direction	Direction the snake is being moved.
	 */
	public SnakeAndDirection(int snakeID, Buffer.direction direction){
		this.direction = direction;
		this.snakeID = snakeID;
	}
}
