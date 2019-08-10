import java.util.LinkedList;

public class Buffer {
	
	public enum direction {UP, DOWN, LEFT, RIGHT, NONE}
	
	// Using simple linked list as collection inside of buffer because
	// not allowed to use a thread safe collection
	LinkedList<SnakeAndDirection> bufferQueue = new LinkedList<SnakeAndDirection>();
	public Buffer() {
		
	}
	
	public synchronized void addToBuffer(int snakeID, Buffer.direction theDirection){
		// add the given direction to the snakes buffer
		bufferQueue.add(new SnakeAndDirection(snakeID, theDirection));
		notify();
	}
	
	public synchronized SnakeAndDirection pollBuffer() throws InterruptedException{
		while(bufferQueue.isEmpty()){
			wait();
		}
		return bufferQueue.remove();
	}
}
