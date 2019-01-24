import java.util.*;

class Node {
	String state;
	int index;
	int score;
	Boolean player; // human->true AI-< false
	int depth;
	
	public Node(String s, int i, int sc, Boolean p, int d) {
		state = s;
		index = i;
		score = sc;
		player = p;
		depth = d;
	}
}

public class minmaxag {

	private static int MAX_DEPTH = 6;
	private static char playerLetter = 'X';
	private static char aiLetter = 'O';

	public minmaxag() {

	}

	public int move(String S) {
		Node parent = new Node(S, 0, 0, false, 0);
		return move(parent).index;
	}

	public Node move(Node child) {
		ArrayList<Integer> emptySlots = new ArrayList<Integer>(getEmptySlots(child.state));

		if (isEndState(child.state) || child.depth == MAX_DEPTH) {
			child.score = getScore(child.state, child.player);
			return child;
		}else {
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
			Node nextChild = new Node(S, index, 0, !child.player, child.depth + 1);
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
		 for(int i = 0; i < 42; i++){
			 if((straightup(S, i, playerLetter) || straightdown(S, i, playerLetter) || left(S, i, playerLetter) || right(S, i, playerLetter) ||
					 diagleftup(S, i, playerLetter) || diagrightup(S, i, playerLetter) || diagrightdown(S, i, playerLetter) || diagleftdown(S, i, playerLetter))){
				 return true;
		     }else if((straightup(S, i, aiLetter) || straightdown(S, i, aiLetter) || left(S, i, aiLetter) || right(S, i, aiLetter) ||
		    		 diagleftup(S, i, aiLetter) || diagrightup(S, i, aiLetter) || diagrightdown(S, i, aiLetter) || diagleftdown(S, i, aiLetter))){
		    	 return true;
		     }else if(!S.contains("_")){
            	 return true;
             }
         }
		 return false;
	}
	public int getScore(String S, Boolean p) {
		int score = 0;
		for(int i = 0; i< 42; i++){
            if((straightup(S, i, playerLetter) || straightdown(S, i, playerLetter) || left(S, i, playerLetter) || right(S, i, playerLetter) ||
               diagleftup(S, i, playerLetter) || diagrightup(S, i, playerLetter) || diagrightdown(S, i, playerLetter) || diagleftdown(S, i, playerLetter))){
            	score = -100;
            }
            else if((straightup(S, i, aiLetter) || straightdown(S, i, aiLetter) || left(S, i, aiLetter) || right(S, i, aiLetter) ||
               diagleftup(S, i, aiLetter) || diagrightup(S, i, aiLetter) || diagrightdown(S, i, aiLetter) || diagleftdown(S, i, aiLetter))){
            	score = 100;
            }else if(strupwithgap(S, i, 3, playerLetter)  || leftwithgap(S, i, 3, playerLetter) ){ //if possible solution with 3's
            		score += -5;
            }
            else if(strupwithgap(S, i, 3, aiLetter)  || leftwithgap(S, i, 3, aiLetter) ){
            		score += 5;
            
            }else if(strupwithgap(S, i, 2, playerLetter)  || leftwithgap(S, i, 2, playerLetter) ){//if possible solution with 2's
            		score += -2;
            }
            else if(strupwithgap(S, i, 2, aiLetter)  || leftwithgap(S, i, 2, aiLetter) ){
            		score += 2;
            }
            else{
            	score += 0;
            }
		}
		return score;
	}

	public ArrayList<Integer> getEmptySlots(String S) {
		ArrayList<Integer> emptySlots = new ArrayList<Integer>();
		
		for (int i = 0; i < S.length(); i++) {
			char slot = S.charAt(i);
			if (slot != '_' && (i-7) > 0 && S.charAt(i-7) =='_')
				emptySlots.add(i-7);
			else if(slot == '_' && i<42 && i>34){
				emptySlots.add(i);
			}
		}
		return emptySlots;
	}
	public static boolean inRowWithGap(int[] list, String S, int inrow, char Letter){
		char[] row = new char[4];
    	for(int i = 0; i< 4; i++)
    		row[i] = S.charAt(list[i]);
    	
    	if(inrow == 2  && list[3]+7 < 42 && ((row[0] == '_' && row[1] == '_' && row[2] == row[3] && row[2] == Letter && S.charAt(list[0]+7) != '_' && S.charAt(list[1]+7) != '_') 
    			|| (row[2] == '_' && row[3] == '_' && row[0] == row[1] && row[0] == Letter && S.charAt(list[2]+7) != '_' && S.charAt(list[3]+7) != '_')
    			|| (row[0] == '_' && row[2] == '_' && row[1] == row[3] && row[1] == Letter && S.charAt(list[0]+7) != '_' && S.charAt(list[2]+7) != '_') 
    			|| (row[1] == '_' && row[3] == '_' && row[0] == row[2] && row[2] == Letter && S.charAt(list[1]+7) != '_' && S.charAt(list[3]+7) != '_')
    			|| (row[1] == '_' && row[2] == '_' && row[0] == row[3] && row[0] == Letter && S.charAt(list[1]+7) != '_' && S.charAt(list[2]+7) != '_') 
    			|| (row[0] == '_' && row[3] == '_' && row[1] == row[2] && row[1] == Letter && S.charAt(list[0]+7) != '_' && S.charAt(list[3]+7) != '_'))){ //if there are at least 2 blanks and 2 filled with same letter in a row of 4
    		return true;
    	}else if(inrow == 2 && list[3]+7 > 41 && ((row[0] == '_' && row[1] == '_' && row[2] == row[3] && row[2] == Letter) || (row[2] == '_' && row[3] == '_' && row[0] == row[1] && row[0] == Letter)
        		|| (row[0] == '_' && row[2] == '_' && row[1] == row[3] && row[1] == Letter) || (row[1] == '_' && row[3] == '_' && row[0] == row[2] && row[2] == Letter)
        		|| (row[1] == '_' && row[2] == '_' && row[0] == row[3] && row[0] == Letter) || (row[0] == '_' && row[3] == '_' && row[1] == row[2] && row[1] == Letter))){ //if there are at least 2 blanks and 2 filled with same letter in a row of 4
        	return true;
    	}else if(inrow == 3 && list[3]+7 < 42 && ((row[0] == '_' && row[1] == row[2] && row[2]==row[3] && row[1] == Letter && S.charAt(list[0]+7) != '_') 
    			||(row[1] == '_' && row[0] == row[2] && row[2]==row[3] && row[0] == Letter && S.charAt(list[1]+7) != '_')
    			|| (row[2] == '_' && row[0] == row[1] && row[1]==row[3] && row[1] == Letter && S.charAt(list[2]+7) != '_') 
    			|| (row[3] == '_' && row[0] == row[1] && row[1]==row[2] && row[1] == Letter && S.charAt(list[3]+7) != '_'))){
    		return true;
    	}else if(inrow == 3 && list[3]+7 > 41 && ((row[0] == '_' && row[1] == row[2] && row[2]==row[3] && row[1] == Letter) ||(row[1] == '_' && row[0] == row[2] && row[2]==row[3] && row[0] == Letter)
    			|| (row[2] == '_' && row[0] == row[1] && row[1]==row[3] && row[1] == Letter) || (row[3] == '_' && row[0] == row[1] && row[1]==row[2] && row[1] == Letter))){
    		return true;
    	}else
    		return false;
	}
	
	public static boolean strupwithgap(String S, int b, int inrow, char Letter){
		int [] list = new int[4];
        int val = b;
        boolean filled = true;
        for(int i = 0; i< 4; i++){
            if (val  >= 0){
                list[i] = val;
                val -= 7;
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
        	return inRowWithGap(list, S, inrow, Letter);
        }
	}
	
	public static boolean strdownwithgap(String S, int b, int inrow, char Letter){
		int [] list = new int[4];
        int val = b;
        boolean filled = true;
        for(int i = 0; i< 4; i++){
            if (val  < 42){
                list[i] = val;
                val += 7;
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
        	return inRowWithGap(list, S, inrow, Letter);
        }
	}
	
	public static boolean leftwithgap(String S,int b, int inrow, char Letter){
        int [] list = new int[4];
        int val = b;
        boolean filled = true;
        int level = findlevel(b);
        
        for(int i = 0; i< 4; i++){
            if (findlevel(val) == level && val >= 0){
                list[i] = val;
                val--;
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
        	return inRowWithGap(list, S, inrow, Letter);
        }
    }
	
	public static boolean rightwithgap(String S,int b, int inrow, char Letter){
        int [] list = new int[4];
        int val = b;
        boolean filled = true;
        int level = findlevel(b);
        
        for(int i = 0; i< 4; i++){
            if (findlevel(val) == level && val >= 0){
                list[i] = val;
                val++;
            }
            else{filled = false;}
        }
        if (filled == false)
            return false;
        else{
        	return inRowWithGap(list, S, inrow, Letter);
        }
    }

	//for finding endState
	public static boolean inRow(int[] list, String S, char Letter){
		char slot1 = S.charAt(list[0]);
    	char slot2 = S.charAt(list[1]);
    	char slot3 = S.charAt(list[2]);
    	char slot4 = S.charAt(list[3]);
        if(slot1 == slot2 && slot2 == slot3 && slot3 == slot4 && slot3 == Letter)
            return true;
        else
            return false;
	}
	 public static boolean straightup(String S, int b, char Letter){
	        int [] list = new int[4];
	        int val = b;
	        boolean filled = true;
	        for(int i = 0; i< 4; i++){
	            if (val  >= 0){
	                list[i] = val;
	                val -= 7;
	            }
	            else{filled = false;}
	        }
	        if (filled == false)
	            return false;
	        else{
	        	return inRow(list, S, Letter);
	        }
	   }
	    public static boolean straightdown(String S, int b, char Letter){
	        int [] list = new int[4];
	        int val = b;
	        boolean filled = true;
	        for(int i = 0; i< 4; i++){
	            if (val  < 42){
	                list[i] = val;
	                val += 7;
	            }
	            else{filled = false;}
	        }
	        if (filled == false)
	            return false;
	        else{
	        	return inRow(list, S, Letter);
	        }
	    }
	    public static int findlevel(int b)
	    {
	        
	        int level = 0;
	        
	        for(int i = 6; i < 42; i+=7)
	            if(b > i)
	                level += 1;
	        
	        return level;
	                
	    }
	    public static boolean left(String S,int b, char Letter){
	        int [] list = new int[4];
	        int val = b;
	        boolean filled = true;
	        int level = findlevel(b);
	        
	        for(int i = 0; i< 4; i++){
	            if (findlevel(val) == level && val >= 0){
	                list[i] = val;
	                val--;
	            }
	            else{filled = false;}
	        }
	        if (filled == false)
	            return false;
	        else{
	        	return inRow(list, S, Letter);
	        }
	    }
	    public static boolean right(String S,int b, char Letter){
	        int [] list = new int[4];
	        int val = b;
	        boolean filled = true;
	        int level = findlevel(b);
	        
	        for(int i = 0; i< 4; i++){
	            if (findlevel(val) == level && val < 42){
	                list[i] = val;
	                val++;
	            }
	            else{filled = false;}
	        }
	        if (filled == false)
	            return false;
	        else{
	        	return inRow(list, S, Letter);
	        }
	    }
	    public static boolean diagleftup(String S, int b, char Letter){
	        int [] list = new int[4];
	        int levela = 0;
	        int levelb = 1;
	        int val = b;
	        boolean filled = true;
	        for(int i = 0; i< 4; i++){
	            if (val  >= 0 && Math.abs(levela - levelb) == 1){
	                levela = findlevel(val);
	                list[i] = val;
	                val -= 8;
	                levelb = findlevel(val);
	            }
	            else{filled = false;}
	        }
	        if (filled == false)
	            return false;
	        else{
	        	return inRow(list, S, Letter);
	        }
	    }
	    public static boolean diagrightdown(String S, int b, char Letter){
	        int [] list = new int[4];
	        int val = b;
	        int levela = 0;
	        int levelb = 1;
	        boolean filled = true;
	        for(int i = 0; i< 4; i++){
	            if (val  < 42 && Math.abs(levela - levelb) == 1){
	                levela = findlevel(val);
	                list[i] = val;
	                val += 8;
	                levelb = findlevel(val);
	            }
	            else{filled = false;}
	        }
	        if (filled == false)
	            return false;
	        else{
	        	return inRow(list, S, Letter);
	        }
	    }
	    public static boolean diagleftdown(String S, int b, char Letter){
	        int [] list = new int[4];
	        int levela = 0;
	        int levelb = 1;
	        int val = b;
	        boolean filled = true;
	        for(int i = 0; i< 4; i++){
	            if (val  < 42 && Math.abs(levela - levelb) == 1){
	                levela = findlevel(val);
	                list[i] = val;
	                val += 6;
	                levelb = findlevel(val);
	            }
	            else{filled = false;}
	        }
	        if (filled == false)
	            return false;
	        else{
	        	return inRow(list, S, Letter);
	            
	        }
	    }
	    public static boolean diagrightup(String S, int b, char Letter){
	        int [] list = new int[4];
	        int levela = 0;
	        int levelb = 1;
	        int val = b;
	        boolean filled = true;
	        for(int i = 0; i< 4; i++){
	            if (val  >= 0 && Math.abs(levela - levelb) == 1){
	                levela = findlevel(val);
	                list[i] = val;
	                val -= 6;
	                levelb = findlevel(val);
	            }
	            else{filled = false;}
	        }
	        if (filled == false)
	            return false;
	        else{
	        	return inRow(list, S, Letter);
	        }
	    }
}
