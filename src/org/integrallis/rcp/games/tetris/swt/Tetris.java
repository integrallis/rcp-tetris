package org.integrallis.rcp.games.tetris.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.integrallis.rcp.games.tetris.model.Board;
import org.integrallis.rcp.games.tetris.model.ScoreList;
import org.integrallis.rcp.games.tetris.model.Stone;


/* 
    Created on 02.01.2004

    Copyright (C) 2004 Christian Schneider

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 * Java Tetris main class
 * 
 * Implements the Controller part of the Tetris game.
 * Reacts on keyboard input and starts a thread for
 * moving the stone down after a certain amount of time.
 *
 * @author chris
 * @author bsbodden (ported to SWT)
 */
public class Tetris extends Composite implements Runnable {
	
	private Label scoreLabel = null;
	private GamePanel gamePanel = null;
	private GamePanel previewPanel = null;

	private Label levelLabel = null;
	private Label statusLine = null;	

	private Thread moverThread;
	Stone currentStone;
	Stone nextStone;
	private int stones;
	private int score;
	private int level;
	Board board;
	boolean gameOver;
	ScoreList highScores;
	
	/**
	 * 
	 */
	public Tetris(Composite parent) {
		super(parent, SWT.None);
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				onKeyPressed(e);
			}
		});
		
		initialize();
	}
	
	/**
	 * moverThread to move the stone down after a certain amount of time
	 */
	public void run() {
		while (moverThread==Thread.currentThread()) {
			try {
				Thread.sleep(70+700/level);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (!gameOver) {
				moveDown();
			}
		}
	}
	
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		setLayout(null);
		getScoreLabel();
		getGamePanel();
		getPreviewPanel();			
		getLevelLabel();
		getStatusLine();
		this.setSize(302, 353);
		this.setVisible(true);

		board=new Board(12,22);
		gamePanel.setBoard(board);
		stones=0;
		gameOver=true;
		setScore(0);
		setLevel(1);
		
		nextStone=new Stone();

        highScores=new ScoreList();
	}
	
	/**
	 * Moves the stone left, right and down.
	 * Rotates the stone.
	 * 
	 * @param e
	 */
	public void onKeyPressed(KeyEvent e) {
		if (gameOver) return;
		
		if (e.keyCode == SWT.ARROW_LEFT) {
			System.out.println("ARROW_LEFT");
			moveLeft();
			redrawGame();
		}
		
		if (e.keyCode == SWT.ARROW_RIGHT) {
			System.out.println("ARROW_RIGHT");
			moveRight();
			redrawGame();    
		}
		
		if (e.keyCode == SWT.ARROW_UP) {
			System.out.println("ARROW_UP");
			rotate();
			redrawGame();
		}	
		
		if (e.keyCode == SWT.ARROW_DOWN) {
			System.out.println("ARROW_DOWN");
			setScore(getScore()+5+getLevel());
			moveDown();
		}
		
		if (e.keyCode == SWT.ESC) {
			System.out.println("GAME START");
			newGame();
		}
	} 

	public void redrawGame() {
		if (!isDisposed()) {
			getDisplay().syncExec(new Runnable() {
				public void run() {
					gamePanel.redraw();
				}
			});
		}
	}

	/**
	 * Starts a new game
	 * 
	 * Resets the score, clears the board and creates a new stone. Starts the
	 * moverThread.
	 */
	public void newGame() {	
		System.out.println("In newGame Action");
		stones=0;	
		setScore(0);
		setLevel(1);
		statusLine.setText("");
		gamePanel.getBoard().clearField();
		createStone();
		redrawGame();
		gameOver=false;
		moverThread = new Thread ( this ) ;
		moverThread.start();
	}
	
	/**
	 * Creates a new random stone and places it at the top of the board.
	 * Returns true if the stone could be placed and false if there was no free space.
	 * 
	 * @return
	 */
	public boolean createStone() {
		stones++;
		if (stones>=10+level*2) {
			setLevel(getLevel()+1);
			stones=0;
		}
		currentStone=nextStone;
		currentStone.setBoard(board);
		currentStone.setPosX(3);
		currentStone.setPosY(-1);

		nextStone=new Stone();
		previewPanel.setBoard(nextStone);
		
		
		getDisplay().syncExec(new Runnable() {
			public void run() {
				previewPanel.redraw();
			}
		});

		if (currentStone.mayPlace(currentStone.getPosX(),currentStone.getPosY())) {
			currentStone.place();
			return true;
		} else {
			return false;
		}
	}

	public void moveLeft() {
		currentStone.take();
		if (currentStone.mayPlace(currentStone.getPosX()-1,currentStone.getPosY())) {			
			currentStone.setPosX(currentStone.getPosX() - 1);
		}
		currentStone.place();
	}
	
	public void moveRight() {
		currentStone.take();
		if (currentStone.mayPlace(currentStone.getPosX()+1,currentStone.getPosY())) {			
			currentStone.setPosX(currentStone.getPosX() + 1);
		}
		currentStone.place();
	}
	
	/**
	 * Try to move down the stone
	 * 
	 * Places the stone one line below it´s current position if possible.
	 * If the stone hits a filled block the board is cleared of full lines and the score
	 * is increased.
	 * 
	 * Returns if the stone was placed successfully.
	 * 
	 * @return
	 */
	public synchronized boolean moveDown() {
		currentStone.take();
		boolean mayPlace=currentStone.mayPlace(currentStone.getPosX(),currentStone.getPosY()+1);
		if (mayPlace) {
			currentStone.setPosY(currentStone.getPosY() + 1);
		}
		currentStone.place();
		if (!mayPlace) {
			int linesRemoved=board.removeFullLines();
			setScore(getScore() + 1000*level*linesRemoved);
			if (!createStone()) gameOver();			
		}
		redrawGame();
		return mayPlace;
	}
	
	/**
	 * Rotates the stone to the left if possible and places it there.
	 * 
	 * @return
	 */
	public boolean rotate() {
		currentStone.take();
		currentStone.rotate();
		if (!currentStone.mayPlace(currentStone.getPosX(),currentStone.getPosY())) {
			currentStone.rotate();
			currentStone.rotate();
			currentStone.rotate();
		}
		currentStone.place();
		return true;
		
	}
	
	/**
	 * Called when the game is over
	 * 
	 * Checks if the player gets into the highscore list.
	 * If yes the name is queried and the score added.
	 * Stops the moverThread.
	 */
	public void gameOver() {
		moverThread=null;
		gameOver=true;
		getDisplay().syncExec(new Runnable() {
			public void run() {
				statusLine.setText("Game Over");
			}
		});		 
	}

	
	/**
	 * This method initializes scoreLabel
	 * 
	 * @return 
	 */
	Label getScoreLabel() {
		if(scoreLabel == null) {
			scoreLabel = new Label(this, SWT.NONE);
			scoreLabel.setSize(106, 19);
			scoreLabel.setText("");
			scoreLabel.setLocation(179, 178);
		}
		return scoreLabel;
	}
	
	/**
	 * This method initializes gamePanel
	 * 
	 * @return GamePanel
	 */
	private GamePanel getGamePanel() {
		if(gamePanel == null) {
			gamePanel = new GamePanel(this);
			gamePanel.setLayout(null);
			gamePanel.setSize(144, 264);
			gamePanel.setLocation(13, 26);
		}
		return gamePanel;
	}
	
	/**
	 * This method initializes previewPanel
	 * 
	 * @return GamePanel
	 */
	private GamePanel getPreviewPanel() {
		if(previewPanel == null) {
			previewPanel = new GamePanel(this);
			previewPanel.setBounds(178, 26, 60, 60);
		}
		return previewPanel;
	}

	/**
	 * This method initializes levelLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private Label getLevelLabel() {
		if(levelLabel == null) {
			levelLabel = new Label(this, SWT.NONE);
			levelLabel.setBounds(179, 141, 106, 19);
			levelLabel.setText("");
		}
		return levelLabel;
	}
	/**
	 * @return
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param i
	 */
	public void setLevel(int i) {
		level = i;
		getDisplay().syncExec(new Runnable() {
			public void run() {
				levelLabel.setText("Level: "+level);
			}
		});	
	}

	/**
	 * This method initializes statusLine
	 * 
	 * @return 
	 */
	private Label getStatusLine() {
		if(statusLine == null) {
			statusLine = new Label(this, SWT.NONE);
			statusLine.setBounds(179, 211, 106, 19);
			statusLine.setText("");
		}
		return statusLine;
	}
	
	/**
	 * @return
	 */
	public Thread getMoverThread() {
		return moverThread;
	}

	/**
	 * @param thread
	 */
	public void setMoverThread(Thread thread) {
		moverThread = thread;
	}
	
	public void setScore(final int score) {
		this.score = score;
		if (!isDisposed()) {
			getDisplay().syncExec(new Runnable() {
				public void run() {
					scoreLabel.setText("Score: "+score);
				}
			});
		}
		
	}

	public int getScore() {
		return score;
	}
	
	public static void show(){
		try {
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			new Tetris(shell);
			shell.setLayout(new org.eclipse.swt.layout.FillLayout());
			Rectangle shellBounds = shell.computeTrim(0,0,329,223);
			shell.setSize(shellBounds.width, shellBounds.height);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		show();
	}
	
	private static final long serialVersionUID = -3267887732569843668L;

} 
