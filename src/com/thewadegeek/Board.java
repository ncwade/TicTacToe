package com.thewadegeek;

/* Everything we need for file IO. */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Represent the board for Tic-tac-toe. Also contains the basic logic for the game.
 * @author Nick
 *
 */
public class Board {
	private final static int ROWS = 3;
	private final static int COLS = 3;
	private String[][] cells = new String[ROWS][COLS];
	private String lastTurn;
	
	/*
	 * Default constructor, basic instantiation of an empty board.
	 */
	public Board(){
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				cells[row][col] = Cell.EMPTY;
			}
		}
		lastTurn = Cell.O;
	}
	
	/*
	 * Construct an object from a save file.
	 * TODO: Add better error handling.
	 */
	public Board(Path saveFile){
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(saveFile, charset)) {
			for (int row = 0; row < ROWS; ++row) {
				for (int col = 0; col < COLS; ++col) {
					switch(reader.readLine().trim()) {
						case Cell.X:
							cells[row][col] = Cell.X;
							break;
						case Cell.O:
							cells[row][col] = Cell.O;
							break;
						default:
							cells[row][col] = Cell.EMPTY;
							break;
					}
				}
			}
		    switch(reader.readLine().trim()) {
			case Cell.X:
				lastTurn = Cell.O;
				break;
			case Cell.O:
				lastTurn = Cell.X;
				break;
			default:
				lastTurn = Cell.EMPTY;
				break;
		    }
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	/*
	 * Are there any freespaces left?
	 */
	public Boolean freeSpace(){
		Boolean isDraw = true;
		for (int row = 0; row < ROWS; ++row) {
			for (int col = 0; col < COLS; ++col) {
				if (cells[row][col] == Cell.EMPTY) {
					isDraw = false;
				}
			}
		}	
		return isDraw;
	}
	
	/*
	 * Check if someone won the game, and return the individual who won the game.
	 */
	public String gameWon(){
		String gameWon = Cell.EMPTY;
		
		// Check all the rows for win conditions
		for (int row = 0; row < ROWS && (gameWon == Cell.EMPTY); ++row) {
			if(cells[row][0] != Cell.EMPTY &&
			   cells[row][0] == cells[row][1] && 
			   cells[row][0] == cells[row][2]){
				gameWon = cells[row][0];
			}
		}
		
		// Check columns for win conditions
		for (int col = 0; col < COLS && (gameWon == Cell.EMPTY); ++col) {
			if(cells[0][col] != Cell.EMPTY &&
			   cells[0][col] == cells[1][col] && 
			   cells[0][col] == cells[2][col]){
				gameWon = cells[0][col];
			}
		}
		
		// Check diagonally left to right
		if(gameWon == Cell.EMPTY){
			if(cells[0][0] != Cell.EMPTY &&
			   cells[0][0] == cells[1][1] &&
			   cells[0][0] == cells[2][2]){
				gameWon = cells[0][0];
			}
		}
		
		// Check diagonally right to left
		if(gameWon == Cell.EMPTY){
			if(cells[0][2] != Cell.EMPTY &&
			   cells[0][2] == cells[1][1] &&
			   cells[0][2] == cells[2][0]){
				gameWon = cells[0][2];
			}
		}
		
		return gameWon;
	}
	
	/*
	 * Set the value of a cell.
	 */
	public Boolean setCell(Cell move){
		Boolean ret = false;
		if(cells[move.row][move.col] == Cell.EMPTY && 
		   (move.value == Cell.X || 
		    move.value == Cell.O)){
			cells[move.row][move.col] = move.value;
			lastTurn = move.value;
			ret = true;
		}
		return ret;
	}
	
	/*
	 * Who get's to move next?
	 */
	public String whichTurn(){
		if(lastTurn == Cell.X){
			return Cell.O;
		} else{
			return Cell.X;
		}
	}
	
	/*
	 * Who moved last?
	 */
	public String PrevTurn(){
		return lastTurn;
	}
	
	/*
	 * Can we move to this cell?
	 */
	public boolean checkMove(int row, int col) {
		boolean retVal = false;
		if(col < 3 && col > -1){
			if(row < 3 && row > -1){
				if(getCell(row,col) == Cell.EMPTY){
					retVal = true;
				}
			}
		}
		return retVal;
	}
	
	/*
	 * Save the state of the game to a given location.
	 */
	public void save(Path path) {
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedWriter writer = Files.newBufferedWriter(path, charset)) {

			for (int row = 0; row < ROWS; ++row) {
				for (int col = 0; col < COLS; ++col) {
					writer.write(cells[row][col].toString().trim()+"\n");
				}
			}
		    writer.write(whichTurn().toString().trim());
		} catch (IOException x) {
		    System.err.format("IOException: %s%n", x);
		}
	}
	
	/*
	 * Return the value of a cell.
	 */
	public String getCell(int row, int col) {
		return cells[row][col];
	}

}
