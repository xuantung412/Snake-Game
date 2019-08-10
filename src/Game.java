import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.LinkedList;

public class Game implements WindowListener {
	// GRID CONTENT
	public GridNode[][] grid = null;
	public int[][] gridLocker = null; // lock by id/numbers -1 means unlocked
	public int height = 600;
	public int width = 600;
	public int gameSize = 60;
	public Frame frame = null;
	public Canvas canvas = null;
	public Graphics graph = null;
	public BufferStrategy strategy = null;
	public boolean game_over = false;
	public boolean paused = false;
	public int bonusTime = 0;
	public boolean running = true;
	public LinkedList<Snake> snakesList = new LinkedList<Snake>();
	UserAccountManagement manSys;


	public Game(UserAccountManagement man) {
		super();
		this.manSys = man;
		frame = new Frame();
		canvas = new Canvas();
		grid = new GridNode[gameSize][gameSize];
		gridLocker = new int[gameSize][gameSize];
	}

	// need to set the key listener as Human snake controller from the main with
	// this
	public void setKeyListener(HumanSnakeController listener) {
		canvas.addKeyListener(listener);
	}

	public void init() {
		frame.setSize(width + 7, height + 27);
		frame.setResizable(false);
		frame.setLocationByPlatform(true);
		canvas.setSize(width + 7, height + 27);
		frame.add(canvas);
		frame.addWindowListener(this);
		frame.dispose();
		frame.validate();
		frame.setTitle("Snake");
		frame.setVisible(true);
		canvas.setIgnoreRepaint(true);
		canvas.setBackground(Color.WHITE);
		canvas.createBufferStrategy(2);
		strategy = canvas.getBufferStrategy();
		graph = strategy.getDrawGraphics();
		
		// Centering the Lobby Window
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((screenSize.getWidth() - frame.getWidth()) / 2);
	    int y = (int) ((screenSize.getHeight() - frame.getHeight()) / 2);
	    frame.setLocation(x, y);
		
		initGame();
		renderGame();
	}

	public void initGame() {
		// Initializing grid to empty
		for (int i = 0; i < gameSize; i++) {
			for (int j = 0; j < gameSize; j++) {
				grid[i][j] = new GridNode(GridNode.NodeType.EMPTY);
				gridLocker[i][j] = -1;
			}
		}

		// Snakes are placed into start positions here
		for (int i = 0; i < snakesList.size(); i++) {
			int x = (int) (Math.random() * 1000) % gameSize;
			int y = (int) (Math.random() * 1000) % gameSize;
			if (grid[x][y].nodeType == GridNode.NodeType.EMPTY) {
				Snake currentSnake = snakesList.get(i);
				grid[x][y].SnakeID = currentSnake.snakeID;
				currentSnake.initalizeSnakeLocation(x, y, gameSize);
				grid[x][y].nodeType = GridNode.NodeType.SNAKEHEAD;
			} else {
				// Every snake has to be allocated a spot so go back to the
				// index
				// of the snake to add and try again
				i--;
			}
		}

		// Places some snake food randomly somewhere
		for(int i = 0; i <4; i++) {
			placeBonus();
		}
	}

	public void renderGame() {
		int numberOfPlayers = this.manSys.lockedNavmap.size();
		System.out.println("#######################\n" + numberOfPlayers);
		int gridUnit = height / gameSize;
		canvas.paint(graph);
		do {
			do {
				graph = strategy.getDrawGraphics();
				((Graphics2D) graph).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				// Draw Background
				graph.setColor(Color.WHITE);
				graph.fillRect(0, 0, width, height);
				// Draw snake, bonus ...
				GridNode.NodeType gridCase = GridNode.NodeType.EMPTY;
				for (int i = 0; i < gameSize; i++) {
					for (int j = 0; j < gameSize; j++) {
						gridCase = grid[i][j].nodeType;
						switch (gridCase) {
						case SNAKE:
							if (grid[i][j].SnakeID == 0 && this.manSys.lockedNavmap.size() >=1) {
								graph.setColor(Color.blue);
							} else if (grid[i][j].SnakeID == 1 && this.manSys.lockedNavmap.size() >=2) {
								graph.setColor(Color.red);
							} else if(grid[i][j].SnakeID == 2 && this.manSys.lockedNavmap.size() >=3){
								graph.setColor(Color.orange);
							} else if(grid[i][j].SnakeID == 3 && this.manSys.lockedNavmap.size() >=4){
								graph.setColor(Color.green);
							} else {
								graph.setColor(Color.black);
							}
							graph.fillOval(i * gridUnit, j * gridUnit, gridUnit, gridUnit);
							break;
						case SNAKEHEAD:

							// set humans to colours
							if (grid[i][j].SnakeID == 0 && this.manSys.lockedNavmap.size() >=1) {
								graph.setColor(Color.blue);
							} else if (grid[i][j].SnakeID == 1 && this.manSys.lockedNavmap.size() >=2) {
								graph.setColor(Color.red);
							} else if (grid[i][j].SnakeID == 2 && this.manSys.lockedNavmap.size() >=3) {
								graph.setColor(Color.orange);
							} else if (grid[i][j].SnakeID == 3 && this.manSys.lockedNavmap.size() >=4) {
								graph.setColor(Color.green);
							} else {
								graph.setColor(new Color(0, 0, 0));
							}

							// graph.setColor(new Color(0,0,0));
							graph.fillOval(i * gridUnit, j * gridUnit, gridUnit, gridUnit);
							break;
						case FOODBONUS:
							graph.setColor(new Color(3, 171, 14));
							graph.fillOval(i * gridUnit + gridUnit / 4, j * gridUnit + gridUnit / 4, gridUnit / 2,
									gridUnit / 2);
							break;
						default:
							break;
						}
					}
				}

				// Checks to see if the game is over.
				game_over = checkGameState();
				
				graph.setFont(new Font(Font.SANS_SERIF, Font.BOLD, height / 40));
				if (game_over) {
					graph.setColor(Color.RED);
					graph.drawString("GAME OVER", height / 2 - 30, height / 2);
				} else if (paused) {
					graph.setColor(Color.RED);
					graph.drawString("PAUSED", height / 2 - 30, height / 2);
				}
				

				// Draws the scores of the players to the top of the game. Only displays the scores of the snakes
				// in the current game. If the game is set to have only 2 players, only 2 scores will be shown.
				graph.setColor(new Color(0, 0, 0, 140));
				if( numberOfPlayers >= 1){
					graph.drawString(manSys.getUser(0).getUserName() + ": " + (snakesList.get(0).getSize() - 1), 10, 20);
				}
				if (numberOfPlayers >= 2){
					graph.drawString(manSys.getUser(1).getUserName() + ": " + (snakesList.get(1).getSize() - 1), width/4 + 10, 20);
				}
				if( numberOfPlayers >= 3){
					graph.drawString(manSys.getUser(2).getUserName() + ": " + (snakesList.get(2).getSize() - 1), width/2 + 10, 20);
				}
				if( numberOfPlayers == 4){
					graph.drawString(manSys.getUser(3).getUserName() + ": " + (snakesList.get(3).getSize() - 1), width/4*3 + 10, 20);
				}
				graph.dispose();
				

			} while (strategy.contentsRestored());
			// Draw image from buffer
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
		} while (strategy.contentsLost());
	}

	// Checks if the human snakes are alive. If they are not. The game is then over.
	private boolean checkGameState() {
		int numberOfPlayers = this.manSys.lockedNavmap.size();
		for (int i = 0; i < numberOfPlayers; i++) {
			if (!snakesList.get(i).isDestroyed()) {
				return false;
			}
		}
		return true;
	}

	public void moveSnake() {

	}

	public void placeBonus() {
		int x = (int) (Math.random() * 1000) % gameSize;
		int y = (int) (Math.random() * 1000) % gameSize;
		if (grid[x][y].nodeType == GridNode.NodeType.EMPTY) {
			grid[x][y].nodeType = GridNode.NodeType.FOODBONUS;
		} else {
			placeBonus();
		}
	}

	public void gameOver() {
		game_over = true;
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		running = false;
		System.exit(0);

	}

	// UNUSED IMPLEMENTED FUNCTIONS
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

}
