/*
 * Created on 05.01.2004
 *
 */
package org.integrallis.rcp.games.tetris.swt;


import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.integrallis.rcp.games.tetris.model.Score;
import org.integrallis.rcp.games.tetris.model.ScoreList;

/**
 * Displays the Highscores
 * 
 * The scores themself are modelled in the ScoreList class
 *
 *  @author chris
 */
public class ScorePanel extends Canvas {
	
	private ScoreList scores;
	public static Point preferredSize = new Point(300, 3000);

	public ScorePanel(Composite parent, int style) {
		super(parent, style);
		
        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent event) {
                onPaint(event);
            }
        });
	}

	@Override
	public Point computeSize(int hint, int hint2) {
		return preferredSize;
	}
	
	private void onPaint(PaintEvent event) {
		Rectangle clientArea = getClientArea();
		int canvasWidth = clientArea.width;
		int canvasHeight = clientArea.height;
		if (canvasWidth == 0 || canvasHeight == 0)
			return;
		GC gc = event.gc;
		if (getScores()==null) return;
		int c = 0;
		for (Score score : scores.asList()) {
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
			gc.drawString(score.getName(),10,20+c*20);
			gc.drawString(""+score.getScore(),100,20+c*20);
			c = c + 1;
		}
	}

	void setScores(ScoreList scores) {
		this.scores = scores;
	}

	ScoreList getScores() {
		return scores;
	}
	
	private static final long serialVersionUID = 6758718786766456726L;

}
