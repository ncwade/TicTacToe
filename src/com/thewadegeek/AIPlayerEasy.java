package com.thewadegeek;

/**
 * A very basic implementation of an AI. Simply loop through a set of moves and
 * select them as available.
 * @author Nick
 */
public class AIPlayerEasy extends AIPlayer {
	private int[][] preferredMoves = {{1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2},{0, 1}, {1, 0}, {1, 2}, {2, 1}};
	
	Cell move(Board board) {
		for (int index = 0; index < preferredMoves.length; ++index) {
			if(board.checkMove(preferredMoves[index][0],preferredMoves[index][1])) {
				return new Cell(preferredMoves[index][0],preferredMoves[index][1],board.whichTurn());
			}
		}
		// Actually impossible to get here...
		return null;
	}

}
