/*
 * Created on 06.01.2004
 *
 */
package org.integrallis.rcp.games.tetris.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements a highscore table.
 * 
 * Supports adding a new score a the correct location in the table and checking
 * if a score makes it into the list.
 * 
 * @author chris
 * 
 */
public class ScoreList {

	private Map<String,Score> scores = new HashMap<String,Score>();
	/**
	 * Adds the score s into the highscore list. If the score is too low it
	 * won´t be added.
	 * 
	 * @param s
	 * @return
	 */
	public void add(Score newScore) {
		String name = newScore.getName();
		Score oldScore = scores.get(name);
        if (oldScore != null) {
            if (newScore.getScore() > oldScore.getScore()) {
            	scores.put(name, newScore);
            }
        }
        else {
        	scores.put(name, newScore);
        }
	}
	
	public Collection<Score> asList() {
		return scores.values();
	}

	
	private static final long serialVersionUID = 1481294261049397840L;
}
