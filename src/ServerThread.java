import java.awt.Color;

public class ServerThread implements Runnable {

	SnakeAndDirection command;
	Game theGame;

	public ServerThread(SnakeAndDirection command, Game theGame){
		this.command = command;
		this.theGame = theGame;
	}

	// removes instance of self from grid
	public void destroySnakeSelf(){
		GridNode.NodeType gridCase = GridNode.NodeType.EMPTY;
		// getting reference to the array containing locations of this snake on the grid so can 'destroy it'
		int[][] theSnake = theGame.snakesList.get(command.snakeID).snakeLocalsOnGrid;
		theGame.snakesList.get(command.snakeID).destroyed();
		for (int i = 0; i < theGame.gameSize * theGame.gameSize; i++) {
			if(theSnake[i][0] == -1 || theSnake[i][1] == -1){
				// reached the end of snake references so breaks
				break;
			}
			// removing reference to it in the game grid
			theGame.grid[theSnake[i][0]][theSnake[i][1]].nodeType = GridNode.NodeType.EMPTY;
			theGame.grid[theSnake[i][0]][theSnake[i][1]].SnakeID = -1;
			// removing from own snake list so when checked will know is dead
			// can change around later if we put in scoring an actual acknowledgement of death
			theSnake[i][0] = -1;
			theSnake[i][1] = -1;
		}
	}

	@Override
	public void run() {
		// THIS IS ALL ABOUT MOVING SNAKES VERY MESSY AT THE MOMENT
		// JUST SO THAT WE CAN TEST ACTUAL THREADS AND MOVEMENTS ON UI
		// WILL BE ALOT DIFFERENT WHEN CHANGE TO MAKE WORK PROPERLY
		System.out.println("Start thread: " + Thread.currentThread().getId());
		
		// getting reference to the array containing locations of this snake on the grid
		int[][] theSnake = theGame.snakesList.get(command.snakeID).snakeLocalsOnGrid;

		// can then use this to get locations of head etc and edit the snake
		int iLocation = theSnake[0][0];
		int jLocation = theSnake[0][1];

		if(iLocation == -1 || jLocation == -1){
			// Snake is dead
			return;
		}

		int newSpotI = iLocation;
		int newSpotJ = jLocation;

		// now to find the spot snake wants to move to
		Snake theSnakeReference = theGame.snakesList.get(command.snakeID);
		switch(command.direction){
		case UP:			
			if(theSnakeReference.snakeSize > 1 && theSnakeReference.previousDirection == Buffer.direction.DOWN){
				// make sure snake cant go back on itself if its bigger than 1
				return;
			} 
			newSpotJ--;
			theSnakeReference.previousDirection = Buffer.direction.UP;
			break;
		case DOWN:			
			if(theSnakeReference.snakeSize > 1 && theSnakeReference.previousDirection == Buffer.direction.UP){
				// make sure snake cant go back on itself if its bigger than 1
				return;
			}
			newSpotJ++;
			theSnakeReference.previousDirection = Buffer.direction.DOWN;
			break;
		case LEFT:			
			if(theSnakeReference.snakeSize > 1 && theSnakeReference.previousDirection == Buffer.direction.RIGHT){
				// make sure snake cant go back on itself if its bigger than 1
				return;
			}			
			newSpotI--;
			theSnakeReference.previousDirection = Buffer.direction.LEFT;
			break;
		case RIGHT:			
			if(theSnakeReference.snakeSize > 1 && theSnakeReference.previousDirection == Buffer.direction.LEFT){
				// make sure snake cant go back on itself if its bigger than 1
				return;
			}
			newSpotI++;
			theSnakeReference.previousDirection = Buffer.direction.RIGHT;
			break;
		}

		// This bit moves the snake to 'opposite sides of the screen' if hits edge
		if(newSpotI < 0)
			newSpotI = theGame.gameSize - 1;
		if(newSpotJ < 0)
			newSpotJ = theGame.gameSize - 1;
		if(newSpotI >= theGame.gameSize)
			newSpotI = 0;
		if(newSpotJ >= theGame.gameSize)
			newSpotJ = 0;

		// now to try and lock the grid for access
		while(theGame.gridLocker[newSpotI][newSpotJ] != command.snakeID ){

			if(theGame.gridLocker[newSpotI][newSpotJ] == -1){
				// Even if two threads at the same time check and see it is negative one only one will end up having changed the ID
				// to their ID and be able to edit
				theGame.gridLocker[newSpotI][newSpotJ] = command.snakeID;
			} else {
				// For debugging to know if spots locked
				System.out.println("LOCKED");
			}
		}
		boolean grow = false;
		// This snake dies if spot moving too is another snake
		if(theGame.grid[newSpotI][newSpotJ].nodeType == GridNode.NodeType.SNAKE || theGame.grid[newSpotI][newSpotJ].nodeType == GridNode.NodeType.SNAKEHEAD){
			destroySnakeSelf();
			// Unlock the grid because ending here
			theGame.gridLocker[newSpotI][newSpotJ] = -1;
			return;
		} else if (theGame.grid[newSpotI][newSpotJ].nodeType == GridNode.NodeType.FOODBONUS){
			// if is moving to food increase snakes size set the new head of snake in snakes list
			grow = true;
			theGame.placeBonus();
		}
		theSnake[0][0] = newSpotI;
		theSnake[0][1] = newSpotJ;
		theGame.grid[iLocation][jLocation].nodeType = GridNode.NodeType.EMPTY;
		theGame.grid[iLocation][jLocation].SnakeID = -1;
		
		int snakex, snakey, i;
		for (i = 1; i < theGame.gameSize * theGame.gameSize; i++) {
			if ((theSnake[i][0] < 0) || (theSnake[i][1] < 0)) {
				break;
			}
			theGame.grid[theSnake[i][0]][theSnake[i][1]].nodeType = GridNode.NodeType.EMPTY;
			theGame.grid[theSnake[i][0]][theSnake[i][1]].SnakeID = -1;
			snakex = theSnake[i][0];
			snakey = theSnake[i][1];
			theSnake[i][0] = iLocation;
			theSnake[i][1] = jLocation;
			iLocation = snakex;
			jLocation = snakey;
		}
		theGame.grid[theSnake[0][0]][theSnake[0][1]].nodeType = GridNode.NodeType.SNAKEHEAD;
		theGame.grid[theSnake[0][0]][theSnake[0][1]].SnakeID = command.snakeID;
		for (i = 1; i < theGame.gameSize * theGame.gameSize; i++) {
			if ((theSnake[i][0] < 0) || (theSnake[i][1] < 0)) {
				break;
			}
			theGame.grid[theSnake[i][0]][theSnake[i][1]].nodeType = GridNode.NodeType.SNAKE;
			theGame.grid[theSnake[i][0]][theSnake[i][1]].SnakeID = command.snakeID;
		}
		if (grow) {
			theSnake[i][0] = iLocation;
			theSnake[i][1] = jLocation;
			theGame.grid[theSnake[i][0]][theSnake[i][1]].nodeType = GridNode.NodeType.SNAKE;
			theGame.grid[theSnake[i][0]][theSnake[i][1]].SnakeID = command.snakeID;
			theGame.snakesList.get(command.snakeID).increaseSnakeSize();
		}
		
		// now to move the snake for nows sake will only move head but will need proper movement for when snake grows
		// THIS IS JUST A TEST REMEMBER!
//		theGame.grid[iLocation][jLocation].nodeType = GridNode.NodeType.EMPTY;
//		theGame.grid[newSpotI][newSpotJ].nodeType = GridNode.NodeType.SNAKEHEAD;
//		theGame.grid[newSpotI][newSpotJ].SnakeID = command.snakeID;
		
		

		// now to unlock the grid!
		theGame.gridLocker[newSpotI][newSpotJ] = -1;

		System.out.println("Finish thread: " + Thread.currentThread().getId());
	}

}
