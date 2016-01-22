package com.thewadegeek;

import java.awt.EventQueue;

/**
 * Main method for our game.
 * @author Nick
 */
class Main {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable(){
			public void run() {
				@SuppressWarnings("unused")
				/* Instantiate our frame and let that class take over. */
				TicTacToeFrame frame = new TicTacToeFrame();
			}
		});
	}
}