
package org.integrallis.rcp.games.tetris.model;

import java.io.Serializable;

/**
 * Simply contains a highscor entry
 * 
 * @author chris
 *
 */
public class Score implements Serializable {

	public String name;
	public int score;
	
	public Score() {
		name="";
		score=0;
	}
	
	public Score(String name,int score) {
		this.name=name;
		this.score=score;
	}

	/**
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param string
	 */
	public void setName(String string) {
		name = string;
	}

	/**
	 * @param i
	 */
	public void setScore(int i) {
		score = i;
	}
	
	private static final long serialVersionUID = 1071198925649496574L;

}

