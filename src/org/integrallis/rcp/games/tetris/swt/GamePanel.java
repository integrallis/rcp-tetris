package org.integrallis.rcp.games.tetris.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.integrallis.rcp.games.tetris.model.Board;


/**
 * Implements a Panel to display a tetris gameboard
 * 
 * @author chris
 *
 */
public class GamePanel extends Canvas {

	public GamePanel(Composite parent) {
		super(parent, SWT.NONE);
		
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
                onPaint(event);
            }
        });
	}

	private void onPaint(PaintEvent event) {
		Rectangle clientArea = getClientArea();
		int canvasWidth = clientArea.width;
		int canvasHeight = clientArea.height;
		if (canvasWidth == 0 || canvasHeight == 0)
			return;
		GC gc = event.gc;
		gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY));		
		gc.fillRectangle(0, 0, canvasWidth, canvasHeight);
		if (board != null) {
			int width = board.getWidth();
			int height = board.getHeight();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					if (board.getField(x, y) != 0) {
						gc.setBackground(getDisplay().getSystemColor(
								SWT.COLOR_RED));
						gc.fillRectangle(x * 12, y * 12, 10, 10);
					}
				}
			}
		}
	}
	
	public static Point preferredSize = new Point(120, 240);
	
	@Override
	public Point computeSize(int hint, int hint2) {
		return preferredSize;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -9073308186742942554L;
	private Board board;

	/**
	 * @return
	 */
	public Board getBoard() {
		return board;
	}

	/**
	 * @param game
	 */
	public void setBoard(Board game) {
		this.board = game;
	}
	


}
