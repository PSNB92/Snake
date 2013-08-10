package org.psnbtech;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import org.psnbtech.GameBoard.TileType;
import org.psnbtech.Snake.Direction;

public class Engine extends KeyAdapter {

	private static final int UPDATES_PER_SECOND = 10;
	
	private static final Font FONT_SMALL = new Font("Times New Roman", Font.BOLD, 20);
	
	private static final Font FONT_LARGE = new Font("Times New Roman", Font.BOLD, 40);
		
	private Canvas canvas;
	
	private GameBoard board;
	
	private Snake snake;
	
	private int score;
	
	private boolean gameOver;
				
	public Engine(Canvas canvas) {
		this.canvas = canvas;
		this.board = new GameBoard();
		this.snake = new Snake(board);
		
		resetGame();
		
		canvas.addKeyListener(this);
	}
	
	public void startGame() {
		canvas.createBufferStrategy(2);
		
		Graphics2D g = (Graphics2D)canvas.getBufferStrategy().getDrawGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		long start = 0L;
		long sleepDuration = 0L;
		while(true) {
			start = System.currentTimeMillis();

			update();
			render(g);

			canvas.getBufferStrategy().show();

			g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			
			sleepDuration = (1000L / UPDATES_PER_SECOND) - (System.currentTimeMillis() - start);

			if(sleepDuration > 0) {
				try {
					Thread.sleep(sleepDuration);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void update() {
		if(gameOver || !canvas.hasFocus()) {
			return;
		}
		TileType snakeTile = snake.updateSnake();
		if(snakeTile == null || snakeTile.equals(TileType.SNAKE)) {
			gameOver = true;
		} else if(snakeTile.equals(TileType.FRUIT)) {
			score += 10;
			spawnFruit();
		}
	}
	
	private void render(Graphics2D g) {
		board.draw(g);
		
		g.setColor(Color.WHITE);
		
		if(gameOver) {
			g.setFont(FONT_LARGE);
			String message = new String("Final Score: " + score);
			g.drawString(message, canvas.getWidth() / 2 - (g.getFontMetrics().stringWidth(message) / 2), 250);
			
			g.setFont(FONT_SMALL);
			message = new String("Press Enter to Restart");
			g.drawString(message, canvas.getWidth() / 2 - (g.getFontMetrics().stringWidth(message) / 2), 350);
		} else {
			g.setFont(FONT_SMALL);
			g.drawString("Score:" + score, 10, 20);
		}
	}
	
	private void resetGame() {
		board.resetBoard();
		snake.resetSnake();
		score = 0;
		gameOver = false;
		spawnFruit();
	}
	
	private void spawnFruit() {
		int random = (int)(Math.random() * ((GameBoard.MAP_SIZE * GameBoard.MAP_SIZE) - snake.getSnakeLength()));
		
		int emptyFound = 0;
		int index = 0;
		while(emptyFound < random) {
			index++;
			if(board.getTile(index % GameBoard.MAP_SIZE, index / GameBoard.MAP_SIZE).equals(TileType.EMPTY)) {
				emptyFound++;
			}
		}
		board.setTile(index % GameBoard.MAP_SIZE, index / GameBoard.MAP_SIZE, TileType.FRUIT);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W) {
			snake.setDirection(Direction.UP);
		}
		if(e.getKeyCode() == KeyEvent.VK_S) {
			snake.setDirection(Direction.DOWN);
		}
		if(e.getKeyCode() == KeyEvent.VK_A) {
			snake.setDirection(Direction.LEFT);
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {
			snake.setDirection(Direction.RIGHT);
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER && gameOver) {
			resetGame();
		}
	}
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Snake Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		
		Canvas canvas = new Canvas();
		canvas.setBackground(Color.BLACK);
		canvas.setPreferredSize(new Dimension(GameBoard.MAP_SIZE * GameBoard.TILE_SIZE, GameBoard.MAP_SIZE * GameBoard.TILE_SIZE));
		
		frame.add(canvas);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		new Engine(canvas).startGame();
	}
	
}
