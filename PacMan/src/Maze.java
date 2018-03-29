
//*** Maze

import java.awt.*;
import java.io.EOFException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import javax.swing.*;

/* ******************************************************************
************
Author¡¯s name(s): Youbin Huang
Course Title:Artificial Intelligence
Semester:2017 fall
Assignment Number homework 2
Submission Date: Oct 16th
Purpose: This program finishing A star algorithm
Input:
Output:
Help:worked alone.
*********************************************************************
********* */

//******************************************************
//*** Purpose: State of Pacman
//*** Input: None
//*** Output: None
//******************************************************

@SuppressWarnings("unchecked")
class Pacman implements Comparable {

	private Pacman supernode = null;
	private int x, y;
	private int f, g, h;

	public Pacman() {
		// TODO Auto-generated constructor stub
	}

	public Pacman(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Pacman(int x, int y, int g, int h) {
		this.x = x;
		this.y = y;
		this.g = g;
		this.h = h;
		f = h + g;
	}

	public Pacman getSupernode() {
		return supernode;
	}

	public void setSupernode(Pacman supernode) {
		this.supernode = supernode;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getF() {
		return f;
	}

	public void computeF() {
		this.f = getG() + getH();
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	// ******************************************************
	// *** Purpose: compute current Pacman f h g
	// *** Input: value of h g
	// *** Output: f
	// ******************************************************

	public int computeFandGandH(int h, int g) {
		setG(g);
		setH(h);
		computeF();
		int tF = getF();
		return tF;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\n x: " + x + " y: " + y + " f: " + f + " g: " + g + " h: " + h + "\n";
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Pacman tPacman = null;
		if (obj instanceof Pacman) {
			tPacman = (Pacman) obj;
		} else {
			new Exception("it is not type of pacman");
		}

		if (this.getX() == tPacman.getX() && this.getY() == tPacman.getY() && this.getG() == tPacman.getG()
				&& this.getH() == tPacman.getH()) {
			return true;
		}
		return false;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Pacman tPacman = null;
		if (o instanceof Pacman) {
			tPacman = (Pacman) o;
		} else {
			try {
				throw new Exception("it is not type of pacman");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.f < tPacman.f) {
			return -1;
		}
		if (this.f > tPacman.f) {
			return 1;
		}
		return 0;
	}
}

// ***********************************************************************
public class Maze extends JFrame {
	// *** can keep track of visited cell positions here
	static boolean[][] visited;

	// *** the maze itself
	// *** 0 means Power Pellet
	// *** 1 means wall
	// *** 2 means Stripes
	// *** 3 means Pirate
	static int[][] mazePlan = { { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 1, 0, 1, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
			{ 1, 0, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1 },
			{ 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 0, 0, 0, 0, 3, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 1, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 1, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 2, 1, 1, 1, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 3, 1, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 1, 0, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 3, 1 },
			{ 1, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1 },
			{ 1, 0, 0, 3, 1, 0, 0, 1, 1, 1, 1, 1, 2, 0, 1, 0, 0, 0, 1 },
			{ 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1 },
			{ 1, 0, 1, 1, 1, 3, 0, 1, 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1 },
			{ 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1 },
			{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, };

	// *** set up the maze wall positions and set all visited states to false
	static MazePanel mp = new MazePanel(mazePlan, visited);

	// *** set up and display main characters' initial maze positions
	static int ghostX = 1, ghostY = 17; // ** Ghost
	static int pacmanX = 1, pacmanY = 1; // *** Pacman

	// *** each maze cell is 37 pixels long and wide
	static int panelWidth = 37;

	// *** a simple random number generator for random search
	static int randomInt(int n) {
		return (int) (n * Math.random());
	}

	// ******************************************************
	// *** main constructor
	// ******************************************************
	public Maze() {
		// *** display the ghost
		mp.setupChar(ghostX, ghostY, "ghost.gif");

		// *** display Pacman
		mp.setupChar(pacmanX, pacmanY, "pacman.gif");

		// *** set up the display panel
		getContentPane().setLayout(new GridLayout());
		setSize(mazePlan[0].length * panelWidth, mazePlan[0].length * panelWidth);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		getContentPane().add(mp);
	}

	// ******************************************************
	// *** a delay routine
	// ******************************************************
	public void wait(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (Exception e) {
		}
	}

	// ******************************************************
	// *** move Pacman to position (i, j)
	// ******************************************************

	public void movePacman(int i, int j) {
		mp.setupChar(i, j, "pacman.gif");
	}

	// ******************************************************
	// *** remove Pacman from position (i, j)
	// ******************************************************
	public void removePacman(int i, int j) {
		mp.removeChar(i, j);
	}

	// ******************************************************
	// *** is position (i,j) a power-pellet cell?
	// ******************************************************
	public boolean openSpace(int i, int j) {
		return (mazePlan[i][j] == 0);
	}

	// ******************************************************
	// *** MODIFY HERE -- MODIFY HERE -- MODIFY HERE
	// ******************************************************
	public static void main(String[] args) throws Exception {
		// *** create a new frame and make it visible

		// when pacman or ghost at unreachable place throws exception
		if (mazePlan[pacmanX][pacmanY] != 0 || pacmanX >= mazePlan.length - 1 || pacmanX <= 0 || pacmanY <= 0
				|| pacmanY >= mazePlan.length - 1) {
			throw new Exception("Pacman are at unreachable place");
		}
		if (mazePlan[ghostX][ghostY] != 0 || ghostX >= mazePlan.length - 1 || ghostX <= 0 || ghostY <= 0
				|| ghostY >= mazePlan.length - 1) {
			throw new Exception("Ghost are at unreachable place");
		}
		Maze mz = new Maze();
		mz.setVisible(true);
		// path for pacman from start point to goal
		LinkedList<Pacman> path = new LinkedList<>();
		// *** Pacman's current board position
		int gbx = pacmanX, gby = pacmanY;
		Pacman pacman = new Pacman(gbx, gby);
		Pacman find = mz.Astar();

		if (find == null) {
			throw new Exception("Ghost are at unreachable place");
		}
		// according to supernode of pacman to find path
		while (find != null) {
			path.add(find);
			find = find.getSupernode();
		}
		// go through the path from start to goal
		for (int i = path.size() - 1; i >= 0; i--) {
			mz.movePacman(path.get(i).getX(), path.get(i).getY());
			mz.wait(200);
			mz.removePacman(path.get(i).getX(), path.get(i).getY());
		}

		// *** exhaustively search all open spaces one row at a time
	} // main

	PriorityQueue<Pacman> openList = new PriorityQueue<>();
	LinkedList<Pacman> closedList = new LinkedList<>();
	public int newX, newY;
	private int[] dirX = { -1, 1, 0, 0 };// direction for 0
	private int[] dirY = { 0, 0, -1, 1 };

	// ******************************************************
	// *** Purpose: compute current Pacman g
	// *** Input: g
	// *** Output: g
	// ******************************************************
	public int computeG(Pacman pacman) {
		Pacman supoerPacman = pacman.getSupernode();
		int ansG = supoerPacman.getG() + 2;
		return ansG;
	}

	// ******************************************************
	// *** Purpose: compute current Pacman h
	// *** Input: h
	// *** Output: h
	// ******************************************************

	public int computeH(Pacman pacman) {
		int x = pacman.getX(), y = pacman.getX();
		int hX = Math.abs(ghostX - x);
		int hY = Math.abs(ghostY - y);
		int ansH = hX + hY;
		ansH = ansH + ansH + ansH + ansH + ansH + ansH + ansH + ansH + ansH + ansH ;
		return ansH;
	}

	public boolean isPirate(int i, int j) {
		return (mazePlan[i][j] == 3);
	}

	public boolean isStripes(int i, int j) {
		return (mazePlan[i][j] == 2);
	}

	// ******************************************************
	// *** Purpose: using A star algorithm to compute path
	// *** Input: g
	// *** Output: goal of pacman'state
	// ******************************************************
	public Pacman Astar() {
		// initial pacman state
		Pacman firstPec = new Pacman(pacmanX, pacmanY);
		firstPec.setG(0);
		firstPec.setH(computeH(firstPec));
		firstPec.computeF();
		openList.offer(firstPec);
		Pacman tempPacman = null;
		Pacman tempPacman2 = null;
		Pacman tempPacman3 = null;
		int tentative_g_score;
		boolean tentative_is_better = false;
		int tempH, tempG;
		while (!openList.isEmpty()) {
			tempPacman = openList.peek();
			// check pecman reach the goal or not.
			if (tempPacman.getX() == ghostX && tempPacman.getY() == ghostY) {
				return tempPacman;
			}
			tempPacman2 = openList.poll();
			int x = tempPacman2.getX();
			int y = tempPacman2.getY();
			closedList.add(tempPacman2);
			// go up down left right
			for (int i = 0; i < 4; i++) {
				newX = x + dirX[i];
				newY = y + dirY[i];
				System.out.println("newX: "+newX+" newY: "+newY);
				// test the is legal or not?
				if ((openSpace(newX, newY) && (newX > 0 && newX < mazePlan.length - 1)
						&& (newY > 0 && newY < mazePlan.length - 1))
						|| (isPirate(newX, newY) && (newX > 0 && newX < mazePlan.length - 1)
								&& (newY > 0 && newY < mazePlan.length - 1))
						|| (isStripes(newX, newY) && (newX > 0 && newX < mazePlan.length - 1)
								&& (newY > 0 && newY < mazePlan.length - 1))) {
					tempPacman3 = new Pacman(newX, newY);
					tempPacman3.setSupernode(tempPacman2);
					tempG = computeG(tempPacman3);
					tempH = computeH(tempPacman3);
					if (isPirate(tempPacman3.getX(), tempPacman3.getY())) {
						tempG = computeG(tempPacman3) + 85;
					}
					if (isStripes(tempPacman3.getX(), tempPacman3.getY())) {
						tempG = computeG(tempPacman3) + 40;
					}
					tempPacman3.computeFandGandH(tempH, tempG);
					// if step is in closedList ,then skip
					if (closedList.contains(tempPacman3)) {
						continue;
					}
					tentative_g_score = tempPacman3.getSupernode().getG() + tempPacman3.getG();
					// if step is not in openlist ,add it into openlist.
					if (!openList.contains(tempPacman3)) {
						openList.add(tempPacman3);
						// test it is better step or not
					} else if (tentative_g_score < tempPacman3.getG()) {
						tentative_is_better = true;
					} else {
						tentative_is_better = false;
					}
					// if it is better step ,do it.
					if (tentative_is_better == true) {
						tempPacman3.setG(tentative_g_score);
						tempPacman3.setH(computeH(tempPacman3));
						tempPacman3.computeF();
					}
				}
			}
		}
		return null;
	}

} // Maze
