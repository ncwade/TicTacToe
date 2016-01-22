package com.thewadegeek;
/**
 * 
 * @author Nick
 * A base type to allow us to implement multiple AIs.
 *
 */
public abstract class AIPlayer {

	protected final static int ROWS = 3;
	protected final static int COLS = 3;
	abstract Cell move(Board board);
}
