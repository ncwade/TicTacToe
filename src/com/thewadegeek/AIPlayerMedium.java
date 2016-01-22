package com.thewadegeek;

/**
 * A more advanced AI, still rather dumb though. Given a little time you will
 * figure out how to beat it.
 * @author Nick
 */

public class AIPlayerMedium extends AIPlayer {
	
	/*
	 * The possible win combinations.
	 */
	private int[][][] possibleWins = {
									  {{0,0},{0,1},{0,2}}, // First row
									  {{1,0},{1,1},{1,2}}, // Second row
									  {{2,0},{2,1},{2,2}}, // Third row
									  {{0,0},{1,0},{2,0}}, // First Column
									  {{0,1},{1,1},{2,1}}, // Second Column
									  {{0,2},{1,2},{2,2}}, // Third Column
									  {{0,0},{1,1},{2,2}}, // Diagonal, left->right.
									  {{0,2},{1,1},{2,0}}  // Diagonal, right->left.
									 };
	
	// List the moves in the order we want to take them.
	private int[][] otherMoves = {
								  {1,1}, // Center location
  							      {0,1}, // Top center
								  {1,0}, // Left center
								  {1,2}, // Right center
								  {2,1}, // Bottom center
  							      {0,0}, // Top left corner
  							      {0,2}, // Top right corner
  							      {2,0}, // Bottom left corner
  							      {2,2}, // Bottom right corner
								 };
	/*
	 * Pick the winning move for the player specified by person.
	 */
	private Cell winningMove(Board board, String person) {
		for(int i = 0; i < possibleWins.length; i++){
			int value = 0;
			int place = 0;
			
			// Loop through the cells in this set.
			for(int j = 0; j < possibleWins[i].length;j++) {
				// If a cell is set then this row get's a point.
				if(board.getCell(possibleWins[i][j][0], possibleWins[i][j][1]) == person){
					value++;
				// Otherwise mark it as empty.
				} else if (board.getCell(possibleWins[i][j][0], possibleWins[i][j][1]) == Cell.EMPTY) {
					place = j;
				}
			}
			
			/* If this row get +2 points, someone can end the game. If the remaining move is free
			 * we take it.
			 */
			if(value > 1 && board.checkMove(possibleWins[i][place][0],possibleWins[i][place][1])){
				return new Cell(possibleWins[i][place][0],possibleWins[i][place][1],board.whichTurn());
			}
		}
		return null;
	}
	
	private Cell bestMove(Board board) {
		// Always take center first.
		if(!board.checkMove(1,1)) {
			board.checkMove(1,1);
		}

		// Otherwise pick from the ordered array.
		for(int i = 0; i < otherMoves.length; i++){
			if(board.checkMove(otherMoves[i][0], otherMoves[i][1])) {
				return new Cell(otherMoves[i][0],otherMoves[i][1],board.whichTurn());
			}
		}
		return null;
	}
	
	Cell move(Board board) {
		// Check to see if there is a move we can win with.
		if(winningMove(board,board.whichTurn()) != null){
			return winningMove(board,board.whichTurn());
		// Otherwise check to see if we can block the opponent from winning.
		} else if(winningMove(board, board.PrevTurn()) != null) {
			return winningMove(board, board.PrevTurn());
		// Pick a move, any move...
		} else {
			return bestMove(board);
		}
	}
}
