package com.thewadegeek;
/**
 * A class to represent a cell. The possible states, index, and value.
 * @author Nick
 *
 */
public final class Cell {
	public static final String X = "X";
    public static final String O = "O";
    public static final String EMPTY = " ";
	public int row;
	public int col;
	public String value;

	public Cell () {
		row = -1;
		col = -1;
		value = Cell.EMPTY;
	}
	
	public Cell (int iRow, int iCol, String iVal) {
		row = iRow;
		col = iCol;
		value = iVal;
	}
}