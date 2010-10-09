/**
 * 
 */
package org.integrallis.rcp.games.tetris.actions;


import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchWindow;
import org.integrallis.rcp.games.tetris.Activator;
import org.integrallis.rcp.games.tetris.TretrisView;
import org.integrallis.rcp.games.tetris.swt.Tetris;

/**
 * @author bsbodden
 * 
 */
public class NewGameAction extends Action {
	
	private final IWorkbenchWindow window;
	private final String viewId;
	
	public NewGameAction(IWorkbenchWindow window, String viewId) {
		this.window = window;
		this.viewId = viewId;
		
		setId("NEW_GAME_ACTION");
		
		this.setText("New Game");
		setImageDescriptor(Activator.getImageDescriptor("/icons/bullet_triangle_green.png"));
	}
	
	
	@Override
	public void run() {
		System.out.println("NewGameAction invoked");
		if (window != null) {
			System.out.println("Window is not null");
			TretrisView view = (TretrisView) window.getActivePage().findView(viewId);
			Tetris tetris = view.getTetris();
			if (tetris != null) {
				System.out.println("View succesfully retrieved");
				tetris.newGame();
			}
		}
	}
	
}
