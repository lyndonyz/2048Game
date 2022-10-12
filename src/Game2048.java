import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Game2048 {
	public static void main(String[] args) {
		new Game2048();
	}
	//setting global variables
	final static int SIZE = 4;
	
	int score = 0;
	int hiScore = 0;
	boolean win = false;
	boolean gameEnd = false;
	boolean continueGame = false;
	int movedblock = 0;
	int[][] board = new int[SIZE][SIZE];
	
	JLabel title = new JLabel("2048");
	JLabel scoreDisplay = new JLabel();
	JLabel newGameB = new JLabel();
	
	//starting the game
	Game2048() {
		refresh();
		init();
		createGUI();
	}
	
	//initializes the game, when the game ends (when you lose) checks if the current score is higher then the high score
	//and replaces it if it is.
	//also sets down two new boxes and refreshes the board.
	void init(){
		continueGame = false;
		if (score > hiScore) {
			hiScore = score;
		}
		score = 0;
		for(int i = 0; i<board.length; i++) {
			for(int j = 0; j<board.length; j++) {
				board[i][j] = 0;
			}
		}
		newBox();
		newBox();
		refresh();
		}
	//creates the JPanel, JFrame, and adds all the elements in the panel.
	void createGUI() {
		JFrame frame = new JFrame("2048 Game");
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1,3));
		topPanel.add(title);
		topPanel.add(scoreDisplay);
		topPanel.add(newGameB);
		
		topPanel.setBackground(new Color(250,248,239));
		title.setFont(new Font("Dialog", Font.BOLD, 45));
		scoreDisplay.setFont(new Font("Dialog", Font.BOLD, 15));
		newGameB.setFont(new Font("Dialog", Font.BOLD, 15));
		scoreDisplay.setText("Score: "+score+" Best: "+hiScore);
		scoreDisplay.setHorizontalAlignment(JLabel.CENTER);
		newGameB.setText("New Game: A");
		newGameB.setHorizontalAlignment(JLabel.RIGHT);
		frame.add(topPanel, BorderLayout.NORTH);

		DrawingPanel dPanel = new DrawingPanel();
		frame.add(dPanel, BorderLayout.CENTER);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,600);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	//This "refresh" code allows for the score to update constantly, check if the player has won and checks if you can't move and
	//therefore lose the game.
	void refresh() {
		//updates score and checks for 2048 box.
		scoreDisplay.setText("Score: "+score+" Best: "+hiScore);
		for(int i = 0; i<board.length; i++) {
			for(int j = 0; j<board.length; j++) {
				if (board[i][j] == 2048 && !continueGame) {
					gameEnd(0);
				}
			}
		}
		//checks for empty spaces
		int zeroCount = 0;
		for (int i = 0; i< board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board [i][j] == 0) {
					zeroCount++;
				}
			}
		}
		//if there are no empty spaces, check if the boxes can be merged
		if (zeroCount == 0) {
			for (int i = 0; i< board.length; i++) {
				for (int j = 0; j < board.length; j++) {
					if (i > 0) {
						if (board[i][j] == board[i-1][j]) {
							zeroCount++;
						}
					}
					if (i < board.length-1) {
						if (board[i][j] == board[i+1][j]) {
							zeroCount++;
						}
					}
					if (j > 0) {
						if (board[i][j] == board[i][j-1]) {
							zeroCount++;
						}
					}
					if (j < board.length-1) {
						if (board[i][j] == board[i][j+1]) {
							zeroCount++;
						}
					}
				}
			}
			//if its still not possible, end the game since there are no possible moves
			if (zeroCount == 0) {
				gameEnd(1);
			}
			//if it is possible to merge, allow the player to continue.
			else {
				zeroCount = 0; 
			}
		}
	}
	//game ending statements.
	void gameEnd(int wL) {
		if (wL == 0) {
			gameEnd = true;
			win = true;
		}
		if (wL == 1) {
			gameEnd = true;
			win = false;
		}
	}
	//this method allows us to spawn a new box on our grid. This method is a while loop to constantly loop through random numbers
	//of x and y (which is going to be grid coords) and int selection (which allows us to have a 90% chance of spawning a 2 and 
	//a 10% chance of spawning a 4). It then returns after it spawns a box on an empty location.
	void newBox() {
		while(true) {
			int x = (int)(Math.random()*4);
			int y = (int)(Math.random()*4);
			int selection = (int)(Math.random()*10);
			if (board[x][y] == 0) {
				if (selection == 0) {
					board[x][y] = 4;
				}
				else {
					board[x][y] = 2;
				}
				return;
			}
		}
	}
	int direction;
	int n;
	int m;
	//moveBox has multiple parts to it. First we move all the boxes closest to that side first while also making sure the box 
	//cannot go out of bounds. If the boxes can also not overlap each other and if there's a box in that direction, it will not move.
	//However, if its the game number, the boxes will merge and create a bigger box of its two sums.
	void moveBox(int n, int m, int direction) {
		this.direction = direction;
		if (this.direction == 0) {
			while(n>0 && board[n][m] > 0 && board[n-1][m]==0) {
				board[n-1][m] = board[n][m];
				board[n][m] = 0;
				n = n-1;
				movedblock++;
			}
			if (n > 0 && board[n][m] == board[n-1][m] && board[n][m] > 0&& board[n-1][m] > 0) {
				board[n][m] = 0;
				board[n-1][m] = (board[n-1][m]*2);
				score += board[n-1][m];
				movedblock++;
			}
		}
		else if (this.direction == 1) {
			while(m<board.length-1 && board[n][m] > 0 && board[n][m+1]==0) {
				board[n][m+1] = board[n][m];
				board[n][m] = 0;
				m = m+1;
				movedblock++;
			}
			if (m < board.length-1 && board[n][m] == board[n][m+1] && board[n][m] > 0&& board[n][m+1] > 0) {
				board[n][m] = 0;
				board[n][m+1] = (board[n][m+1]*2);
				score += board[n][m+1];
				movedblock++;
			}
		}
		else if (this.direction == 2) {
			while(n<board.length-1 && board[n][m] > 0 && board[n+1][m]==0) {
				board[n+1][m] = board[n][m];
				board[n][m] = 0;
				n = n+1;
				movedblock++;
			}
			if (n <board.length-1 && board[n][m] == board[n+1][m] && board[n][m] > 0&& board[n+1][m] > 0) {
				board[n][m] = 0;
				board[n+1][m] = (board[n+1][m]*2);
				score += board[n+1][m];
				movedblock++;
			}
		}
		else {
			while(m>0 && board[n][m] > 0 && board[n][m-1]==0) {
				board[n][m-1] = board[n][m];
				board[n][m] = 0;
				m = m-1;
				movedblock++;
			}
			if (m> 0 && board[n][m] == board[n][m-1] && board[n][m] > 0&& board[n][m-1] > 0) {
				board[n][m] = 0;
				board[n][m-1] = (board[n][m-1]*2);
				score += board[n][m-1];
				movedblock++;
			}
		}
	}
	private class DrawingPanel extends JPanel implements KeyListener{
		int panW, panH;
		int boxW, boxH; 
		DrawingPanel() {
			this.setBackground(new Color(205,193,180));
			this.addKeyListener(this);
			this.setFocusable(true);
		}
		//this draws all of our boxes and numbers.
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			initGrid();			
			//This code goes through the board and creates a box to every position that has a number thats not zero in it.
			//The color is based off the the sum of each of the individual boxes in the spaces.
			for(int i = 0; i<board.length; i++) {
				for(int j = 0; j<board.length; j++) {
					if (board[i][j] != 0) {
						if (board[i][j] == 2) {
						g.setColor(new Color(238,228,218));//2
						}
						else if (board[i][j] == 4) {
						g.setColor(new Color(238,225,199));//4
						}
						else if (board[i][j] == 8) {
						g.setColor(new Color(243,178,122));//8
						}
						else if (board[i][j] == 16) {
						g.setColor(new Color(246,150,100));//16
						}
						else if (board[i][j] == 32) {
						g.setColor(new Color(247,124,95));//32
						}
						else if (board[i][j] == 64) {
						g.setColor(new Color(247,95,95));//64
						}
						else{
						g.setColor(new Color(237,208,115));//128
						}
						g.fillRect(j*boxW, i*boxH, (boxW), (boxH));
					}
				}
			}
			//This code allows us to print out the number on the box. This is either black or white depending on the sum of the
			//box in that location.
			for(int i = 0; i<board.length; i++) {
				for(int j = 0; j<board.length; j++) {
					int length = (int) Math.log10(board[i][j]);
					if (board[i][j] != 0) {
						if (board[i][j] < 7) {
							g.setColor(new Color(0,0,0));
						}
						else {
							g.setColor(new Color(255,255,255));
						}
						g.setFont(new Font("SansSerif", Font.BOLD, 60));
						g.drawString(Integer.toString(board[i][j]), j*boxW+58-length*18, i*boxH+80);
					}
				}
			}
			//draws our grid lines.
			g.setColor(new Color(187,173,160));
			g2.setStroke(new BasicStroke(10));
			for (int i = 0; i<SIZE; i++) {
				g2.drawLine(boxW*i, 0, boxW*i, panH);
				g2.drawLine(0, boxH*i, panW, boxH*i);
			}
			//shows the win/lose screen for the player if they win/lose.
			if (gameEnd) {
				if (win) {
					//if the player wins, prints a new screen to signify they won, and allow them to continue for high scores.
					g.setColor(new Color(187,173,160));
					g.fillRect(0, 0, panW, panH);
					g.setColor(new Color(0,0,0));
					g.setFont(new Font("SansSerif", Font.BOLD, 60));
					g.drawString("You Win!", panW/2-125,panH/2);
					gameEnd = false;
					continueGame = true;
				}
				else {
					//if the player loses, prints a new screen to signify they lost and resets the game.
					g.setColor(new Color(187,173,160));
					g.fillRect(0, 0, panW, panH);
					g.setColor(new Color(0,0,0));
					g.setFont(new Font("SansSerif", Font.BOLD, 60));
					g.drawString("You Lose!", panW/2-125,panH/2);
					init();
					gameEnd = false;
				}
			}
		}
		//sets the grid.
		void initGrid() {
			panW = this.getSize().width;
			panH = this.getSize().height;
			boxW = (int) (panW/SIZE + 0.5);
			boxH = (int) (panH/SIZE + 0.5);
		}
		@Override
		public void keyTyped(KeyEvent e) {
		}
		@Override
		public void keyPressed(KeyEvent e) {
			//reads if the user clicks the key "A" and restarts the game. 
			if (e.getKeyCode() == 'A' ) {
				 init();
			}
			//if the user types a direction key, moves all boxes in that direction if possible.
			if (e.getKeyCode() == KeyEvent.VK_RIGHT ) {
				for(int i = board.length-1; i>=0; i--) {
					for (int j = 0; j<board.length; j++) {
						moveBox(j,i,1);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_UP ) {
				for(int i = 0; i<board.length; i++) {
					for (int j = 0; j<board.length; j++) {
						moveBox(i,j,0);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_LEFT ) {
				for(int i = 0; i<board.length; i++) {
					for (int j = 0; j<board.length; j++) {
						moveBox(j,i,3);
					}
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN ) {
				for(int i = board.length-1; i>=0; i--) {
					for (int j = 0; j<board.length; j++) {
						moveBox(i,j,2);
					}
				}
			}
			//if a box moved, spawn a new box. If not, don't spawn a box.
			if (movedblock >0) {
				newBox();
				movedblock = 0;
			}
			refresh(); 
			this.repaint();
		}
		@Override
		public void keyReleased(KeyEvent e) {
		}
	}
}