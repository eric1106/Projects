
/* **********************************************************************
   Checkers
   Forouraghi 

   This is a very simple graphical interface for your MINIMAX algorithm:

     -- boardPlan[][] keeps track of what pieces are to be displayed on the
        board

     -- there are four pieces: blue/red single pieces bs/rs
                               blue/red kings         bk/rk

     -- interface movePiece(from_i, from_j, to_i, to_j, piece) moves the
        requested "piece" from boardPlan[from_i][from_j] to
        boardPlan[to_i][to_j]

   You would only need to worry about what piece is being moved from what
   position to what other position; capturing of intermediate pieces is
   handled by the existing code.

   ***Warning***
   I didn't have time to implement a check to see whether an attempt is
   made to jump over one's own piece, i.e., blue over blue or red over red.
*
************************************************************************ */

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import com.sun.media.jfxmedia.events.PlayerEvent;

import jdk.nashorn.internal.ir.WhileNode;

import java.applet.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/* ******************************************************************
************
Author¡¯s name(s): Youbin Huang
Course Title:Artificial Intelligence
Semester:2017 fall
Assignment Number homework 
Submission Date: Nov 14th
Purpose: Minimax algorithm
Input:
Output:
Help:worked alone.
*********************************************************************
********* */

//***********************************************************************
public class Checkers extends JFrame {

	/*
	 * set up an 8 by 8 checkers board with only five pieces legends: 0 - empty 1/2
	 * = blue single/king 3/4 = red single/king
	 */
	/*
	 * private static int[][] boardPlan = { { 0, 3, 0, 3, 0, 3, 0, 3 }, // *** blue
	 * pieces become king here { 3, 0, 3, 0, 3, 0, 3, 0 }, { 0, 3, 0, 3, 0, 3, 0, 3
	 * }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 0, 1, 0, 1,
	 * 0, 1, 0 }, { 0, 1, 0, 1, 0, 1, 0, 1 }, { 1, 0, 1, 0, 1, 0, 1, 0 } // *** red
	 * pieces become king here };
	 */

	private static int[][] boardPlan = { { 0, 0, 0, 0, 0, 0, 0, 0 }, // *** blue pieces become king here
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 1 }, { 1, 0, 4, 0, 3, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 2, 0, 0, 0 } // *** red pieces become king here
	};

	// *** the legend strings

	// *** the legend strings
	private String[] legend = { "blank", "bs", "bk", "rs", "rk" };

	// *** create the checkers board
	private GameBoard board;

	// *** the xy dimensions of each checkers cell on board
	private int cellDimension;

	// *** pause in seconds inbetween moves
	private static int pauseDuration = 500;

	// ***********************************************************************
	Checkers() {
		// *** set up the initial configuration of the board
		board = new GameBoard(boardPlan);

		// *** each board cell is 70 pixels long and wide
		cellDimension = 70;

		// *** set up the frame containign the board
		getContentPane().setLayout(new GridLayout());
		setSize(boardPlan.length * cellDimension, boardPlan.length * cellDimension);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(board);

		// *** enable viewer
		setVisible(true);

		// *** place all initial pieces on board and pause a bit
		putInitialPieces();
		pause(2 * pauseDuration);
	}

	// ***********************************************************************
	public void pause(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
		}
	}

	// ***********************************************************************
	void putPiece(int i, int j, String piece) {
		// *** can do error checking here to make sure pieces are bs, bk, rs, rk
		board.drawPiece(i, j, "images/" + piece + ".jpg");
	}

	// ***********************************************************************
	void putInitialPieces() {
		// *** use legend variables to draw one piece at a time
		for (int i = 0; i < boardPlan.length; i++)
			for (int j = 0; j < boardPlan.length; j++)
				if (boardPlan[i][j] != 0)
					board.drawPiece(i, j, "images/" + legend[boardPlan[i][j]] + ".jpg");
	}

	// ******************************************************
	// *** Purpose: redraw the board to fix the double jump problem
	// *** Input:
	// *** Output:
	// ******************************************************

	void reDraw() {
		// *** use legend variables to draw one piece at a time
		int count = 0;
		for (int i = 0; i < boardPlan.length; i++) {
			count = 0;
			for (int j = 0; j < boardPlan.length; j++) {
				if ((boardPlan[i][j] == 0 && count % 2 == 1 && i % 2 == 0)
						|| (boardPlan[i][j] == 0 && count % 2 == 0 && i % 2 == 1))
					board.drawPiece(i, j, "images/blank.jpg");
				count++;
			}
		}
	}

	// ***********************************************************************
	boolean legalPosition(int i) {
		// *** can't go outside board boundaries
		return ((i >= 0) && (i < boardPlan.length));
	}

	// ***********************************************************************
	void movePiece(int i1, int j1, int i2, int j2, String piece) {
		// *** raise exception if outside the board or moving into a non-empty
		// *** cell
		if ((boardPlan[i2][j2] != 0) || !legalPosition(i1) || !legalPosition(i2) || !legalPosition(j1)
				|| !legalPosition(j1))
			throw new IllegalMoveException("An illegal move was attempted.");

		// *** informative console messages
		System.out.println("Moved " + piece + " from position [" + i1 + ", " + j1 + "] to [" + i2 + ", " + j2 + "]");

		// *** erase the old cell
		board.drawPiece(i1, j1, "images/blank.jpg");

		// *** draw the new cell
		board.drawPiece(i2, j2, "images/" + piece + ".jpg");

		// *** erase any captured piece from the board
		if ((Math.abs(i1 - i2) == 2) && (Math.abs(j1 - j2) == 2)) {
			// *** this handles hops of length 2
			// *** the captured piece is halfway in between the two moves
			int captured_i = i1 + (i2 - i1) / 2;
			int captured_j = j1 + (j2 - j1) / 2;

			// *** now wait a bit
			pause(pauseDuration);

			// *** erase the captured cell
			board.drawPiece(captured_i, captured_j, "images/blank.jpg");

			// *** print which piece was captured
			System.out.println("Captured " + legend[boardPlan[captured_i][captured_j]] + " from position [" + captured_i
					+ ", " + captured_j + "]");

			// *** the captured piece is removed from the board with a bang
			boardPlan[captured_i][captured_j] = 0;
			Applet.newAudioClip(getClass().getResource("images/hit.wav")).play();
			drawCounter = 0;
		}
		drawCounter++;
		// *** update the internal representation of the board by moving the old
		// *** piece into its new position and leaving a blank in its old position
		boardPlan[i2][j2] = boardPlan[i1][j1];
		boardPlan[i1][j1] = 0;

		// *** red single is kinged
		if ((i2 == boardPlan.length - 1) && (boardPlan[i2][j2] == 3)) {
			boardPlan[i2][j2] = 4;
			putPiece(i2, j2, "rk");
		}

		// *** blue single is kinged
		if ((i2 == 0) && (boardPlan[i2][j2] == 1)) {
			boardPlan[i2][j2] = 2;
			putPiece(i2, j2, "bk");
		}
		// *** now wait a bit
		pause(pauseDuration);
	}

	// ******************************************************
	// *** Purpose: print out the board
	// *** Input:
	// *** Output: the game board
	// ******************************************************

	void printBoard() {
		for (int i = 0; i < boardPlan.length; i++) {
			for (int j = 0; j < boardPlan.length; j++) {
				System.out.print(boardPlan[i][j] + "\t");
			}
			System.out.println();
		}
	}

	// ***********************************************************************
	// *** incorporate your MINIMAX algorithm in here
	// ***********************************************************************
	public static void main(String[] args) {
		// *** create a new game and make it visible

		// *** arbitrarily move a few pieces around
		// while (!game.done())
		{
			try {
				Checkers game = new Checkers();
				CState blue = new CState(boardPlan);
				blue.player = true;
				CState red = null;
				Random rand = new Random();
				List<CState> listR = null;
				List<CState> listB = null;
				PriorityQueue<CState> pQueue = new PriorityQueue<CState>

				(6, new Comparator<CState>() {
					// *** define your own comparator object for ordering
					public int compare(CState stateA, CState stateB) {
						// *** sort into ascending order based on evaluation function e()
						if (stateA.getE() <= stateB.getE())
							return -1;

						else
							return 1;
					}
				});
				double lastBestB = -55;
				double lastBestR = -35;
				while (!game.isGameOver() && !game.isDraw()) {
					blue = new CState(boardPlan);
					blue.player = true;
					blue = game.miniMax(blue, 3, true);
					listB = game.generateMoves(blue, true);
					if (listB.isEmpty()) {
						System.out.println("red win!");
						break;
					}
					game.movePiece(blue.x, blue.y, blue.newX, blue.newY, game.getPiece(boardPlan[blue.x][blue.y]));
					boardPlan = blue.state;
					game.printBoard();
					game.reDraw();
					if (game.isGameOver() || game.isDraw()) {
						break;
					}
					red = new CState(boardPlan);
					red.player = false;
					red = game.miniMaxRed(red, 1, false);
					listR = game.generateMoves(red, false);
					if (listR.isEmpty()) {
						System.out.println("blue win");
						break;
					}
					game.movePiece(red.x, red.y, red.newX, red.newY, game.getPiece(boardPlan[red.x][red.y]));
					boardPlan = red.state;
					game.printBoard();
					game.reDraw();
					if (game.isGameOver() || game.isDraw()) {
						break;
					}
				}
			}

			catch (IllegalMoveException e) {
				e.printStackTrace();
			}
		}

	}

	// ******************************************************
	// *** Purpose: change int to string which could move piece
	// *** Input: int
	// *** Output: String
	// ******************************************************

	public String getPiece(int a) {
		if (a == 1) {
			return "bs";
		} else if (a == 2) {
			return "bk";
		} else if (a == 3) {
			return "rs";
		} else {
			return "rk";
		}
	}

	int drawCounter = 0;

	// ******************************************************
	// *** Purpose: test is it draw
	// *** Input:
	// *** Output: boolean
	// ******************************************************
	public boolean isDraw() {
		if (drawCounter >= 40) {
			System.out.println("this is draw");
			return true;
		}
		return false;
	}

	// ******************************************************
	// *** Purpose: test is the game has a winner
	// *** Input:
	// *** Output: boolean
	// ******************************************************

	boolean isGameOver() {

		int red = 0, blue = 0;
		for (int i = 0; i < boardPlan.length; i++) {
			for (int j = 0; j < boardPlan.length; j++) {
				if (boardPlan[i][j] == 1 || boardPlan[i][j] == 2) {
					blue++;
				}
				if (boardPlan[i][j] == 3 || boardPlan[i][j] == 4) {
					red++;
				}
			}
		}
		if (red == 0 && blue != 0) {
			System.out.println("blue win!!!");
			return true;
		} else if (red != 0 && blue == 0) {
			System.out.println("red win!!");
			return true;
		} else {
			return false;
		}
	}

	int infinity = 1000;

	// ******************************************************
	// *** Purpose: Minimax algorithm
	// *** Input: current board situation and player
	// *** Output: best move
	// ******************************************************

	public CState miniMax(CState state, int depth, boolean minOrMax) {
		ArrayList<CState> pQueue = new ArrayList<>();
		pQueue = generateMoves(state, minOrMax);
		CState best = new CState(state.state, state.x, state.y, state.newX, state.newY, state.player);
		CState temp = null;
		if (depth == 0 || pQueue.isEmpty()) {
			return state;
		}
		if (minOrMax) {
			best.setE(-infinity);
			for (CState a : pQueue) {
				temp = miniMax(a, depth - 1, false);
				best = getMax(temp, best);
			}
			return best;
		} else {
			best.setE(infinity);
			for (CState a : pQueue) {
				temp = miniMax(a, depth - 1, true);
				best = getMin(temp, best);
			}
			return best;
		}
	}

	public CState miniMaxRed(CState state, int depth, boolean minOrMax) {
		ArrayList<CState> pQueue = new ArrayList<>();
		pQueue = generateMoves(state, minOrMax);
		CState best = new CState(state.state, state.x, state.y, state.newX, state.newY, state.player);
		CState temp = null;
		if (depth == 0 || pQueue.isEmpty()) {
			return state;
		}
		if (!minOrMax) {
			best.setE(-infinity);
			for (CState a : pQueue) {
				temp = miniMax(a, depth - 1, false);
				best = getMax(temp, best);
			}
			return best;
		} else {
			best.setE(infinity);
			for (CState a : pQueue) {
				temp = miniMax(a, depth - 1, true);
				best = getMin(temp, best);
			}
			return best;
		}
	}

	// ******************************************************
	// *** Purpose: get the cstate which has larger e
	// *** Input: two cstate object
	// *** Output: cstate object
	// ******************************************************

	public CState getMax(CState a, CState b) {
		if (a.compareTo(b) > 0) {
			return a;
		} else {
			return b;
		}
	}

	// ******************************************************
	// *** Purpose: get the cstate which has lower e
	// *** Input: two cstate object
	// *** Output: cstate object
	// ******************************************************

	public CState getMin(CState a, CState b) {
		if (a.compareTo(b) < 0) {
			return a;
		} else {
			return b;
		}
	}

	// ******************************************************
	// *** Purpose: copy board
	// *** Input: the board
	// *** Output: same the board as inputed but nothing associated with original
	// board
	// ******************************************************

	public int[][] copyBoardPlan(int[][] board) {
		int[][] temp = new int[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				temp[i][j] = board[i][j];
			}
		}
		return temp;
	}

	public int newX, newY, captureX, captureY, beCaptureX, beCaptureY, midX, midY;
	private int[] dirX = { -1, -1, 1, 1 };// direction for 0
	private int[] dirY = { -1, 1, -1, 1 };

	// ******************************************************
	// *** Purpose: generate every possible from the board and player
	// *** Input: object of cstate class
	// *** Output: a list of possible moves
	// ******************************************************

	public ArrayList<CState> generateMoves(CState state, boolean miniMaxPlayer) {
		CState tempCState = state;

		ArrayList<CState> pStatesQueue = new ArrayList<CState>();

		if (isGameOver()) {
			return pStatesQueue;
		}

		int[][] tempBoard = copyBoardPlan(boardPlan);
		int x, y;
		// true for blue ,false for red
		if (miniMaxPlayer == true) {
			for (int i = 0; i < boardPlan.length; i++) {
				for (int j = 0; j < boardPlan.length; j++) {
					if (boardPlan[i][j] == 1) {
						x = i;
						y = j;
						for (int blueSDir = 0; blueSDir < 2; blueSDir++) {
							tempBoard = copyBoardPlan(boardPlan);
							newX = x + dirX[blueSDir];
							newY = y + dirY[blueSDir];
							if (legalPosition(x) && legalPosition(y) && legalPosition(newX) && legalPosition(newY)) {
								if (tempBoard[newX][newY] == 3 || tempBoard[newX][newY] == 4) {
									tempBoard[newX][newY] = 0;
									newX = newX + dirX[blueSDir];
									newY = newY + dirY[blueSDir];
									if (!legalPosition(newX) || !legalPosition(newY)) {
										continue;
									}
									for (int k = 0; k < 2; k++) {
										captureX = newX + dirX[k];
										captureY = newY + dirY[k];
										if (legalPosition(captureX) && legalPosition(captureY)
												&& (tempBoard[captureX][captureY] == 3
														|| tempBoard[captureX][captureY] == 4)) {
											tempBoard[captureX][captureY] = 0;
											newX = captureX + dirX[k];
											newY = captureY + dirY[k];
										}
									}
								}
								if (!legalPosition(newX) || !legalPosition(newY) || tempBoard[newX][newY] != 0) {
									continue;
								}
								tempBoard[x][y] = 0;
								tempBoard[newX][newY] = 1;
								if (newX == 0 && tempBoard[newX][newY] == 1) {
									tempBoard[newX][newY] = 2;
								}
								pStatesQueue.add(new CState(tempBoard, x, y, newX, newY, true));
							}
						}
					}
					if (boardPlan[i][j] == 2) {
						x = i;
						y = j;
						tempBoard = copyBoardPlan(boardPlan);
						for (int blueKDir = 0; blueKDir < 4; blueKDir++) {
							tempBoard = copyBoardPlan(boardPlan);
							newX = x + dirX[blueKDir];
							newY = y + dirY[blueKDir];
							if (legalPosition(x) && legalPosition(y) && legalPosition(newX) && legalPosition(newY)) {
								if (tempBoard[newX][newY] == 3 || tempBoard[newX][newY] == 4) {
									tempBoard[newX][newY] = 0;
									newX = newX + dirX[blueKDir];
									newY = newY + dirY[blueKDir];
									if (!legalPosition(newX) || !legalPosition(newY)) {
										continue;
									}
									for (int k = 0; k < 4; k++) {
										captureX = newX + dirX[k];
										captureY = newY + dirY[k];
										if (legalPosition(captureX) && legalPosition(captureY)
												&& (tempBoard[captureX][captureY] == 3
														|| tempBoard[captureX][captureY] == 4)) {
											tempBoard[captureX][captureY] = 0;
											newX = captureX + dirX[k];
											newY = captureY + dirY[k];
										}
									}
								}
								if (!legalPosition(newX) || !legalPosition(newY) || tempBoard[newX][newY] != 0) {
									continue;
								}
								tempBoard[x][y] = 0;
								tempBoard[newX][newY] = 2;
								pStatesQueue.add(new CState(tempBoard, x, y, newX, newY, true));
							}
						}
					}
				}
			}
		} else {
			for (int i = 0; i < boardPlan.length; i++) {
				for (int j = 0; j < boardPlan.length; j++) {
					if (boardPlan[i][j] == 3) {
						x = i;
						y = j;
						for (int redSDir = 2; redSDir < 4; redSDir++) {
							tempBoard = copyBoardPlan(boardPlan);
							newX = x + dirX[redSDir];
							newY = y + dirY[redSDir];
							if (legalPosition(x) && legalPosition(y) && legalPosition(newX) && legalPosition(newY)) {
								if (tempBoard[newX][newY] == 1 || tempBoard[newX][newY] == 2) {
									tempBoard[newX][newY] = 0;
									newX = newX + dirX[redSDir];
									newY = newY + dirY[redSDir];
									if (!legalPosition(newX) || !legalPosition(newY)) {
										continue;
									}
									for (int k = 2; k < 4; k++) {
										captureX = newX + dirX[k];
										captureY = newY + dirY[k];
										if (legalPosition(captureX) && legalPosition(captureY)
												&& (tempBoard[captureX][captureY] == 1
														|| tempBoard[captureX][captureY] == 2)) {
											tempBoard[captureX][captureY] = 0;
											newX = captureX + dirX[k];
											newY = captureY + dirY[k];
										}
									}
								}
								if (!legalPosition(newX) || !legalPosition(newY) || tempBoard[newX][newY] != 0) {
									continue;
								}
								tempBoard[x][y] = 0;
								tempBoard[newX][newY] = 3;
								if (newX == tempBoard.length - 1 && tempBoard[newX][newY] == 3) {
									tempBoard[newX][newY] = 4;
								}
								pStatesQueue.add(new CState(tempBoard, x, y, newX, newY, false));
							}
						}
					}
					if (boardPlan[i][j] == 4) {
						x = i;
						y = j;
						tempBoard = copyBoardPlan(boardPlan);
						for (int redKDir = 0; redKDir < 4; redKDir++) {
							tempBoard = copyBoardPlan(boardPlan);
							newX = x + dirX[redKDir];
							newY = y + dirY[redKDir];
							if (legalPosition(x) && legalPosition(y) && legalPosition(newX) && legalPosition(newY)) {
								if (tempBoard[newX][newY] == 1 || tempBoard[newX][newY] == 2) {
									tempBoard[newX][newY] = 0;
									newX = newX + dirX[redKDir];
									newY = newY + dirY[redKDir];
									if (!legalPosition(newX) || !legalPosition(newY)) {
										continue;
									}
									for (int k = 0; k < 4; k++) {
										captureX = newX + dirX[k];
										captureY = newY + dirY[k];
										if (legalPosition(captureX) && legalPosition(captureY)
												&& (tempBoard[captureX][captureY] == 1
														|| tempBoard[captureX][captureY] == 2)) {
											tempBoard[captureX][captureY] = 0;
											newX = captureX + dirX[k];
											newY = captureY + dirY[k];
										}
									}
								}
								if (!legalPosition(newX) || !legalPosition(newY) || tempBoard[newX][newY] != 0) {
									continue;
								}
								tempBoard[x][y] = 0;
								tempBoard[newX][newY] = 4;
								pStatesQueue.add(new CState(tempBoard, x, y, newX, newY, false));
							}
						}
					}
				}
			}
		}
		return pStatesQueue;
	}

}
