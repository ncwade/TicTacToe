package com.thewadegeek;

/* AWT Imports */
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/* Swing Imports */
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * We don't utilize the serialization functionality, suppress the warning.
 * @author Nick
 * 
 **/
@SuppressWarnings("serial")
class TicTacToeFrame extends JFrame {
	// Array of graphical buttons, these map to indexes in our button class.
	private JButton [][] buttons= new JButton[3][3];
	// Display the status along the bottom of the window.
	private JTextField statusBar;
	// Every frame needs a panel for objects to reside within.
	private TicTacToePanel panel;
	// Need a listener that the buttons can utilize.
	private ButtonListener btnListener = new ButtonListener();
	// Our Board, the backbone of the program.
	private Board board = new Board();
	// An AIPlayer to play against, if the user asks.
	private AIPlayer fakePlayer = null;

	/**
	 * @author Nick
	 * @return nothing
	 * 
	 * Our default constructor.
	 **/
	public TicTacToeFrame() {
		// Configure the layout of the frame such that there will be no gaps.
		setLayout(new BorderLayout());
		panel = new TicTacToePanel();
		add(panel,BorderLayout.CENTER);
		
		// Title, make visible, bounds, and close behavior.
		setTitle("TicTacToe");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400,400,300,300);

		// Configure the status bar.
		statusBar = new JTextField(board.whichTurn()+"'s Turn");
		statusBar.setEditable(false);
		add(statusBar,BorderLayout.SOUTH);
		
		// Add a menu bar to the frame.
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// Create our menu entries. 
		JMenu fileMenu = new JMenu("Main");
		menuBar.add(fileMenu);
		JMenu newSingleAction = new JMenu("Choose an AI");
		JMenuItem newSingleActionEasy = new JMenuItem("Easy Mode");
		JMenuItem newSingleActionHard = new JMenuItem("Medium Mode");
		JMenuItem newTwoAction = new JMenuItem("New Game");
		JMenuItem loadAction = new JMenuItem("Load Game");
		JMenuItem saveAction = new JMenuItem("Save Game");
		
		// Map functions to each menu item.
		newSingleActionEasy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fakePlayer = new AIPlayerEasy();
			}
		});
		
		newSingleActionHard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fakePlayer = new AIPlayerMedium();
			}
		});

		newTwoAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fakePlayer = null;
				initComponents();
			}
		});
		
		loadAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {loadComponents();}
		});
		
		saveAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {saveComponents();}
		});
		
		// Map action/listeners for all the menu items.
		newSingleAction.add(newSingleActionEasy);
		newSingleAction.add(newSingleActionHard);
		fileMenu.add(newSingleAction);
		fileMenu.add(newTwoAction);
		fileMenu.add(loadAction);
		fileMenu.add(saveAction);
	}

	/**
	 * @author Nick
	 * 
	 * Create a panel that contains the buttons/establishes the grid. This 
	 * class resides within our Frame class because it simplifies our logic 
	 * greatly.
	 **/
	class TicTacToePanel extends JPanel {
		public TicTacToePanel() {
			setLayout(new GridLayout(3,3));
			for(int i=0;i<3;i++) {
				for(int j=0;j<3;j++) {
					buttons[i][j]=new JButton();
					buttons[i][j].putClientProperty("INDEX", new Integer[]{i,j});
					buttons[i][j].putClientProperty("OWNER", null);
					buttons[i][j].addActionListener(btnListener);
					add(buttons[i][j]);
				}
			}
		}
	}

	/**
	 * @author Nick
	 **/
	class ButtonListener implements ActionListener {
		/**
		 * @author Nick
		 * @return nothing
		 * 
		 * The action performed whenever a button is clicked.
		 **/
		public void actionPerformed(ActionEvent e) {
			// Need to establish which button got clicked.
			JButton b= (JButton)e.getSource();
			Integer[]index=(Integer[]) b.getClientProperty("INDEX");
			// Determine the turn.
			b.setText(board.whichTurn());
			// Set to false to the button can't be clicked again.
			b.setEnabled(false);
			
			// Create a move to pass to the board object.
			Cell move = new Cell();
			move.row = index[0];
			move.col = index[1];
			move.value = board.whichTurn();
			board.setCell(move);
			
			// Determine the state of the game.
			if(isOver()) {
				if(board.gameWon() != Cell.EMPTY){
					JOptionPane.showMessageDialog(null, board.gameWon()+" has won the game!");
					disableButtons();
				} else {
					JOptionPane.showMessageDialog(null, "Match is a draw!");
					disableButtons();
				}
				statusBar.setText("Game over.");
			} else {
				if(fakePlayer != null) {
					Cell aiMove = fakePlayer.move(board);
					for(int i=0;i<3;i++) {
						for(int j=0;j<3;j++) {
							Integer[] aiIndex=(Integer[]) buttons[i][j].getClientProperty("INDEX");
							if(aiMove.row == aiIndex[0] && aiMove.col == aiIndex[1]){
								buttons[i][j].setText(board.whichTurn());
								buttons[i][j].setEnabled(false);
								board.setCell(aiMove);
								if(isOver()) {
									if(board.freeSpace()){
										JOptionPane.showMessageDialog(null, "Match is a draw!");
										disableButtons();
									} else {
										JOptionPane.showMessageDialog(null, board.gameWon()+" has won the game!");
										disableButtons();
									}
									statusBar.setText("Game over.");
								}
								
							}
						}
					}
				} else {
					statusBar.setText(board.whichTurn()+"'s Turn");
				}
			}
		}

		public Integer getOwner(JButton b) {
			return (Integer)b.getClientProperty("OWNER");
		}
	}
	
	/**
	 * @author Nick
	 * @return nothing
	 * 
	 * Someone wants to start a new game. We need a new copy of the board class
	 * and to reset the GUI elements.
	 **/
	private void initComponents() {
		board = new Board();
		
		// Loop through all the buttons and enable/blank them.
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				buttons[i][j].setEnabled(true);
				buttons[i][j].setText("");
			}
		}
		
		// Reset status to show who's turn it is.
		statusBar.setText(board.whichTurn()+"'s Turn");
	}

	/**
	 * @author Nick
	 * @return nothing
	 * 
	 * Someone wants to save the game. Present them with the file chooser,
	 * ensure the correct extension will be used, update GUI as needed.
	 **/	
	private void saveComponents() {
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Save files", "save");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showSaveDialog(getParent());
		
		/* If someone chose a valid file, disable the GUI elements,
		 * save the board, and let the user know the game has been
		 * saved.
		 */
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			board.save(chooser.getSelectedFile().toPath());
			disableButtons();
			statusBar.setText("Game saved!");
		} else {
			// Else let them know we couldn't save.
			statusBar.setText("Game wasn't saved!");
		}
	}

	/**
	 * @author Nick
	 * @return nothing
	 * 
	 * Someone wants to load a game. Give them a file open dialog, load the
	 * class, update the panel/frame, call it good.
	 **/
	private void loadComponents() {
		// Present the user with a file open dialog.
		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Save files", "save");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(getParent());

		/*
		 * If the user selects a valid file, we need to call the board save
		 * function, disable used cells, update the cell text, enable un-set
		 * buttons.
		 */
		if(returnVal == JFileChooser.APPROVE_OPTION) {
			board = new Board(chooser.getSelectedFile().toPath());
			for(int i=0;i<3;i++) {
				for(int j=0;j<3;j++) {
					if(board.getCell(i, j) != Cell.EMPTY){
						buttons[i][j].setEnabled(false);
						buttons[i][j].setText(board.getCell(i, j));	
					} else {
						buttons[i][j].setEnabled(true);
					}
				}
			}
			statusBar.setText(board.whichTurn()+"'s Turn");
		} else {
			statusBar.setText("Couldn't load file.");
		}
	}
	
	/**
	 * @author Nick
	 * @return nothing
	 * 
	 * Disable the buttons of the frame.
	 **/
	private void disableButtons() {
		// Disable buttons
		for(int i=0;i<3;i++) {
			for(int j=0;j<3;j++) {
				buttons[i][j].setEnabled(false);
			}
		}
	}
	
	/**
	 * @author Nick
	 * @return nothing
	 * 
	 * Is the game over?
	 **/
	private boolean isOver() {
		boolean gameOver = false;
		// Is it a draw?
		if(board.freeSpace() || board.gameWon() != Cell.EMPTY) {
			gameOver = true;
		}
		return gameOver;
	}
}
