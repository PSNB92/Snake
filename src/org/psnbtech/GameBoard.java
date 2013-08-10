package org.psnbtech;

import java.awt.Color;
import java.awt.Graphics2D;

public class GameBoard {
	
	public static final int TILE_SIZE = 25;

	public static final int MAP_SIZE = 20;
	
	public static enum TileType {

		SNAKE(Color.GREEN),

		FRUIT(Color.RED),
		
		EMPTY(null);
		
		private Color tileColor;
		
		private TileType(Color color) {
			this.tileColor = color;
		}
		
		public Color getColor() {
			return tileColor;
		}
		
	}
	
	private TileType[] tiles;
	
	public GameBoard() {
		tiles = new TileType[MAP_SIZE * MAP_SIZE];
	}
	
	public void resetBoard() {
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = TileType.EMPTY;
		}
	}
	
	public void setTile(int x, int y, TileType type) {
		tiles[y * MAP_SIZE + x] = type;
	}

	public TileType getTile(int x, int y) {
		return tiles[y * MAP_SIZE + x];
	}

	public void draw(Graphics2D g) {
		g.setColor(TileType.SNAKE.getColor());

		for(int i = 0; i < tiles.length; i++) {
			int x = i % MAP_SIZE;
			int y = i / MAP_SIZE;

			if(tiles[i].equals(TileType.EMPTY)) {
				continue;
			}

			if(tiles[i].equals(TileType.FRUIT)) {
				g.setColor(TileType.FRUIT.getColor());
				g.fillOval(x * TILE_SIZE + 4, y * TILE_SIZE + 4, TILE_SIZE - 8, TILE_SIZE - 8);
				g.setColor(TileType.SNAKE.getColor());
			} else {
				g.fillRect(x * TILE_SIZE + 1, y * TILE_SIZE + 1, TILE_SIZE - 2, TILE_SIZE - 2);
			}
		}
	}

}