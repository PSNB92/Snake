package org.psnbtech;

import java.awt.Point;
import java.util.LinkedList;

import org.psnbtech.GameBoard.TileType;

public class Snake {
	
	public static enum Direction {
		
		UP,
		
		DOWN,
		
		LEFT,
		
		RIGHT,
		
		NONE
		
	}
	
	private Direction currentDirection;
	
	private Direction temporaryDirection;
	
	private GameBoard board;
		
	private LinkedList<Point> points;
	
	public Snake(GameBoard board) {
		this.board = board;
		this.points = new LinkedList<Point>();
	}
	
	public void resetSnake() {		
		this.currentDirection = Direction.NONE;
		this.temporaryDirection = Direction.NONE;
		
		Point head = new Point(GameBoard.MAP_SIZE / 2, GameBoard.MAP_SIZE / 2);
		points.clear();
		points.add(head);
		board.setTile(head.x, head.y, TileType.SNAKE);		
	}
	
	public void setDirection(Direction direction) {
		if(direction.equals(Direction.UP) && currentDirection.equals(Direction.DOWN)) {
			return;
		} else if(direction.equals(Direction.DOWN) && currentDirection.equals(Direction.UP)) {
			return;
		} else if(direction.equals(Direction.LEFT) && currentDirection.equals(Direction.RIGHT)) {
			return;
		} else if(direction.equals(Direction.RIGHT) && currentDirection.equals(Direction.LEFT)) {
			return;
		}
		this.temporaryDirection = direction;
	}

	public TileType updateSnake() {
		this.currentDirection = temporaryDirection;
		
		Point head = points.getFirst();	
				
		switch(currentDirection) {
		
		case UP:
			if(head.y <= 0) {
				return null;
			}
			points.push(new Point(head.x, head.y - 1));
			break;
			
		case DOWN:
			if(head.y >= GameBoard.MAP_SIZE - 1) {
				return null;
			}
			points.push(new Point(head.x, head.y + 1));
			break;
			
		case LEFT:
			if(head.x <= 0) {
				return null;
			}
			points.push(new Point(head.x - 1, head.y));
			break;
			
		case RIGHT:
			if(head.x >= GameBoard.MAP_SIZE - 1) {
				return null;
			}
			points.push(new Point(head.x + 1, head.y));
			break;
			
		case NONE:
			return TileType.EMPTY;
		}
		
		head = points.getFirst();
		
		TileType oldType = board.getTile(head.x, head.y);
		if(!oldType.equals(TileType.FRUIT)) {
			Point last = points.removeLast();
			board.setTile(last.x, last.y, TileType.EMPTY);
			oldType = board.getTile(head.x, head.y);
		}
				
		board.setTile(head.x, head.y, TileType.SNAKE);
		
		return oldType;
	}
	
	public int getSnakeLength() {
		return points.size();
	}
		
	public Direction getCurrentDirection() {
		return currentDirection;
	}
	
}
