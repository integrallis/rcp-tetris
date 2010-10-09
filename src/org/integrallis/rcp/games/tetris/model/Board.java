package org.integrallis.rcp.games.tetris.model;

/**
 * Implements the Tetris gameboard.
 * 
 * Supports clearing the field, setting and reading single fields. Removelines
 * supports deleting full lines and moving the rest down.
 * 
 * @author chris
 * 
 */
public class Board {

	private int width;
	private int height;
	protected int field[][];

	public Board(int width, int height) {
		setWidth(width);
		setHeight(height);
		field = new int[height][width];
		clearField();
	}

	public boolean lineFull(int y) {
		for (int x = 0; x < width; x++) {
			if (field[y][x] == 0)
				return false;
		}
		return true;
	}

	public void copyLine(int srcY, int destY) {
		for (int x = 0; x < width; x++) {
			field[destY][x] = field[srcY][x];
		}
	}

	public void removeLine(int y) {
		for (int cY = y; cY > 0; cY--) {
			copyLine(cY - 1, cY);
		}
		for (int x = 0; x < width; x++)
			field[0][x] = 0;
	}

	/**
	 * Deletes full lines and moves the rest down Returns the number of rows
	 * removed.
	 * 
	 * @return
	 */
	public int removeFullLines() {
		int lines = 0;
		for (int y = height - 1; y > 0; y--) {
			if (lineFull(y)) {
				removeLine(y);
				lines++;
				y++;
			}
		}
		return lines;
	}

	/**
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param i
	 */
	public void setHeight(int i) {
		height = i;
	}

	/**
	 * @param i
	 */
	public void setWidth(int i) {
		width = i;
	}

	public void clearField() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				field[y][x] = 0;
			}
		}
	}

	public void setField(int x, int y, int color) {
		if (x >= 0 && x < width && y >= 0 && y < height)
			field[y][x] = color;
	}

	public int getField(int x, int y) {
		if (x >= 0 && x < width && y >= 0 && y < height) {
			return field[y][x];
		} else {
			return 1;
		}
	}

}
