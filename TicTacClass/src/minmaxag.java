import java.util.*;

class Node {
	String state;
	int index;
	int score;
	Boolean player; // human->true AI-< false

	public Node(String s, int i, int sc, Boolean p) {
		state = s;
		index = i;
		score = sc;
		player = p;
	}
}

public class minmaxag {

	String State;

	private static int[][] winCombinations = new int[][] { { 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, // horizontal
																									// wins
			{ 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 }, // vertical wins
			{ 0, 4, 8 }, { 2, 4, 6 } // diagonal wins
	};
	private static char playerLetter = 'X';
	private static char aiLetter = 'O';

	public minmaxag() {

	}

	public int move(String S) {
		Node parent = new Node(S, -1, 0, false);
		return move(parent).index;
	}

	public Node move(Node child) {
		ArrayList<Integer> emptySlots = new ArrayList<Integer>(getEmptySlots(child.state));

		if (isEndState(child.state)) {
			child.score = getScore(child.state);
			return child;
		} else {
			if(child.player) {
				child.score = 99999;
			} else {
				child.score = -99999;
			}
		}
		Node bestChild = null;
		for (int i = 0; i < emptySlots.size(); i++) {
			StringBuilder st = new StringBuilder(child.state);
			String S = "";
			if (child.player) {
				st.setCharAt(emptySlots.get(i), playerLetter);
				S = st.toString();
			} else {
				st.setCharAt(emptySlots.get(i), aiLetter);
				S = st.toString();
			}
			int index = emptySlots.get(i);
			Node nextChild = new Node(S, index, 0, !child.player);
			int childScore = move(nextChild).score;
			
			if (child.player) {
				 if (child.score > childScore) {
					 child.score = childScore;
					 bestChild = nextChild;
				 }
			 } else {
				 if (child.score < childScore) {
					 child.score = childScore;
					 bestChild = nextChild;
				 }
			 }
		}
		return bestChild;
	}

	public Boolean isEndState(String S) {		
		Boolean endState = false;
		for (int i = 0; i < winCombinations.length; i++) {
			char win = S.charAt(winCombinations[i][0]);
			char win1 = S.charAt(winCombinations[i][1]);
			char win2 = S.charAt(winCombinations[i][2]);

			if (win == win1 && win1 == win2 && win2 == win && win != '_') {
				endState = true;
				break;
			} else if (!S.contains("_")) {
				endState = true;
				break;
			}
		}
		return endState;
	}

	public int getScore(String S) {
		int score = 0;
		for (int i = 0; i < winCombinations.length; i++) {
			char win = S.charAt(winCombinations[i][0]);
			char win1 = S.charAt(winCombinations[i][1]);
			char win2 = S.charAt(winCombinations[i][2]);
			if (win == playerLetter && win1 == playerLetter && win2 == playerLetter){
				score = -10;
				break;
			}
			else if (win == aiLetter && win1 == aiLetter && win2 == aiLetter){
				score = 10;
				break;
			}
			else
				score = 1;
		}
		return score;
	}

	public ArrayList<Integer> getEmptySlots(String S) {
		ArrayList<Integer> emptySlots = new ArrayList<Integer>();
		for (int i = 0; i < S.length(); i++) {
			if (Character.toString(S.charAt(i)).equals("_"))
				emptySlots.add(i);
		}
		return emptySlots;
	}
}
