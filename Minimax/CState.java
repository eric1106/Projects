
/* ********************************************************************
   CState
   Forouraghi
***********************************************************************/

import java.util.*;

import com.sun.swing.internal.plaf.basic.resources.basic_zh_CN;

//********************************************************************
//*** this aux class represents an ADT for checkers states
@SuppressWarnings("unchecked")
public class CState implements Comparable {

	// true for blue ,false for red
	boolean player;
	// *** the evaluation function e(n)
	private double e;

	// *** node type: MAX or MIN
	private String type;

	// *** some board configuration
	public int[][] state;

	
	int x, y, newX, newY,midX,midY,beCaptureX,beCaptureY;
	// **************************************************************
	CState(int[][] state, String type) {
		this.state = state;
		this.type = type;
	}

	CState(int[][] state) {
		this.state = copyBoard(state);
	}
	
	CState(int[][] state, int x, int y, int newX, int newY, boolean player) {
		this(state);
		this.newX=newX;
		this.newY=newY;
		this.x=x;
		this.y=y;
		this.player=player;
		this.type = type;
		evalState();
	}
	
	CState(int[][] state, int x, int y, int newX, int newY, boolean player, int beCaptureX, int beCaptureY,int midX,int midY) {
		this(state, x, y, newX, newY, player);
		this.beCaptureX=beCaptureX;
		this.beCaptureY=beCaptureY;
		this.midX=midX;
		this.midY=midY;
	}

	public int[][] copyBoard() {
		int[][] temp = new int[state.length][state.length];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				temp[i][j]=state[i][j];
			}
		}
		return temp;
	}
	
	public int[][] copyBoard(int[][] board) {
		int[][] temp = new int[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board.length; j++) {
				temp[i][j]=board[i][j];
			}
		}
		return temp;
	}

	// **************************************************************
	// *** evaluate a state based on the evaluation function we
	// *** discussed in class
	void evalState() {
		// *** add your own necessary logic here to properly evaluate a state
		// *** I am just assigning some random numbers for demonstration purposes
		int countBS = 0, countBK = 0, countRK = 0, countRS = 0;
		int[][] tempState = getStateArray();
		for (int i = 0; i < tempState.length; i++) {
			for (int j = 0; j < tempState.length; j++) {
				if (tempState[i][j] == 1) {
					countBS++;
				}
				if (tempState[i][j] == 2) {
					countBK++;
				}
				if (tempState[i][j] == 3) {
					countRS++;
				}
				if (tempState[i][j] == 4) {
					countRK++;
				}
			}
		}
			e = (5 * countBK + countBS) - (5 * countRK + countRS);
	
	}

	// **************************************************************
	// *** get a node's E() value
	double getE() {
		return e;
	}

	// **************************************************************
	// *** get a node's type
	String getType() {
		return type;
	}

	// **************************************************************
	// *** get a state
	String getState() {
		String result = "";

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++)
				result = result + state[i][j] + " ";

			result += "\n";
		}

		return result;
	}

	public int[][] getStateArray() {
		return state;
	}

	public String toString() {
		String result = "";

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++)
				result = result + state[i][j] + " ";

			result += "\n";
		}

		return result;
	}

	public void setE(double e) {
		this.e = e;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		CState state = null;
		if (arg0 instanceof CState) {
			state = (CState) arg0;
		} else {
			try {
				throw new Exception("it is not type of CState");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (this.e < state.e) {
			return -1;
		}
		if (this.e > state.e) {
			return 1;
		}
		return 0;
	}
}
