package org.integrallis.rcp.games.tetris;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.integrallis.rcp.games.tetris.swt.Tetris;

public class TretrisView extends ViewPart {
	public static final String ID = "org.integrallis.rcp.games.tetris.view";

	private Tetris tetris;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
		tetris = new Tetris(parent);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		tetris.setFocus();
	}

	public Tetris getTetris() {
		return tetris;
	}
}