import java.util.*;

class Variable{
	int var; //index of each box?
	ArrayList<Integer> domain = new ArrayList<Integer>(); //possible values for each box {1-9}
	//constraints no repeated numbers in each column, row, or 3x3 box
	public Variable(int v, ArrayList<Integer> d){
		var = v;
		domain = new ArrayList<Integer>(d);
	}
	
}
class Arc{
	int Xi;
	int Xj;
	public Arc(int i, int j){
		Xi = i;
		Xj = j;
	}
}
class Node{
	int index;
	String state;
	public Node(int i, String s){
		index = i;
		state = s;
	}
}

public class SudokuSolver {

    public String solve(String State){
    	ArrayList<Variable> csp = new ArrayList<Variable>(buildCSP(State)); //Initial CSP contains each variable with its domain
    	ArrayList<Arc> arcs = new ArrayList<Arc>(getInitialArcs());// queue of all arcs in CSP
    	while(!arcs.isEmpty()){
    		Arc pop = arcs.get(0);
    		arcs.remove(0);
    		if(removeInconsistentValues(pop, csp)){
    			ArrayList<Integer> neighbours = new ArrayList<Integer> (getNeighbours(pop.Xi));
    			for(int k = 0; k < neighbours.size(); k++){
    				arcs.add(new Arc(neighbours.get(k), pop.Xi));
    			}
    		}
    	}
    	
        return DFS(csp);
    }
    
    public boolean removeInconsistentValues(Arc arc, ArrayList<Variable>csp){
    	boolean removed = false;
    	
    	ArrayList<Integer> domainXi = new ArrayList<Integer>(getDomain(arc.Xi, csp));
    	ArrayList<Integer> domainXj = new ArrayList<Integer>(getDomain(arc.Xj, csp));
    	for(int i = 0; i < domainXi.size(); i++){
    		if(domainXj.size() == 1 && domainXj.get(0) == domainXi.get(i)){
    			domainXi.remove(i);
    			csp.get(arc.Xi).domain.remove(i);
    			removed = true;
    		}
    	}
    	return removed;
    }
    
    public String DFS(ArrayList<Variable> csp){
    	String State = "_________________________________________________________________________________";
    	ArrayList<Node> dfsStack = new ArrayList<Node>();
    	ArrayList<String>  visited = new ArrayList<String>();
    	
    	dfsStack.add(0,new Node(-1, State));
    	
    	while(!dfsStack.isEmpty()){
    		Node node = dfsStack.get(0);
    		dfsStack.remove(0);
    		if(!node.state.contains("_") && checkConstraints(node.state))
    			return node.state;
    		else if(!checkConstraints(node.state))
    			continue;
    		else if(!visited.contains(node.state)){
    			visited.add(node.state);
    			if(node.index<80){
		    		int count = node.index + 1;
		    		for(int i = 0; i < csp.get(count).domain.size(); i++){
		    			StringBuilder sol = new StringBuilder(node.state);
		    			int dvalue = csp.get(count).domain.get(i);
		    			String x = Integer.toString(dvalue);
		    			char d = x.charAt(0);
		    			sol.setCharAt(count, d);
		    			String newState = sol.toString();
		    			dfsStack.add(0,new Node(count,newState));
	    			}
    			}
    		}
    	}
    	return "";
    }
    
    public boolean checkConstraints(String State){
    	for(int i = 0; i < State.length(); i++){
    		int count = i;
    		for(int j = 1; j < 9; j++){
    			if((count+1)%9 == 0)//check if end of row
    				count = count-8;
    			else//next space
    				count++;
    			if(State.substring(i, i+1).equals(State.substring(count, count+1)) && !State.substring(i,i+1).equals("_"))
    				return false;
    			
    		}
    		//arcs for each space in column
    		count = i;
    		for(int j = 1; j < 9; j++){
    			if((count+9) > 80)
    				count = i%9;
    			else
    				count+=9;
    			if(State.substring(i, i+1).equals(State.substring(count, count+1)) && !State.substring(i,i+1).equals("_"))
    				return false;
    		}
    		//arcs for each space in 3x3 square
    		count = i;
    		for(int j = 1; j < 9; j++){
    			if(count == 20 || count == 23 || count == 26 || count == 47 || count == 50 || count == 53 || count == 74 || count == 77 || count == 80)
    				count -= 20;
    			else if(count%3 == 2)
    				count += 7;
    			else 
    				count ++;
    			if(State.substring(i, i+1).equals(State.substring(count, count+1)) && !State.substring(i,i+1).equals("_"))
    				return false;
    		}
    	}
    	return true;
    }
    //find specific index and return its domain
    public ArrayList<Integer> getDomain(int var, ArrayList<Variable>csp){
    	ArrayList<Integer> domainX = new ArrayList<Integer>();
    	for(int i = 0; i < csp.size(); i++){
    		if(var == csp.get(i).var){
    			domainX = new ArrayList<Integer>(csp.get(i).domain);
    			break;
    		}
    	}
    	return domainX;
    	
    }
    public ArrayList<Variable> buildCSP(String State){
    	ArrayList<Variable> csp = new ArrayList<Variable>();
        for(int i = 0; i < State.length(); i++){
     	   ArrayList<Integer> dom = new ArrayList<Integer>();
     	  char value = State.charAt(i);
     	 if(value != '_')
			   dom.add(Character.getNumericValue(value));
     	 else { 
     	   for(int j = 1; j < 10; j++){
     		   dom.add(j);
     	   }
        }
     	 csp.add(new Variable(i,dom));
        }
        return csp;
    }
    
    public ArrayList<Integer> getNeighbours(int x){
    	ArrayList<Integer> neighbours = new ArrayList<Integer>();
    	int count = x;
    	for(int j = 1; j < 9; j++){
			if((count+1)%9 == 0)//check if end of row
				count = count-8;
			else//next space
				count++;
			neighbours.add(count);
		}
		//arcs for each space in column
		count = x;
		for(int j = 1; j < 9; j++){
			if((count+9) > 80)
				count = x%9;
			else
				count+=9;
			neighbours.add(count);
		}
		//arcs for each space in 3x3 square
		count = x;
		for(int j = 1; j < 9; j++){
			if(count == 20 || count == 23 || count == 26 || count == 47 || count == 50 || count == 53 || count == 74 || count == 77 || count == 80)
				count -= 20;
			else if(count%3 == 2)
				count += 7;
			else 
				count ++;
			if(!neighbours.contains(count))
				neighbours.add(count);
		}
		return neighbours;
    	
    }
    
    public ArrayList<Arc> getInitialArcs(){
    	//for each variable there is an arc from it to
    	//each space in its row
    	//each space in its column
    	//each space in its 3x3 square
    	ArrayList<Arc> arcs = new ArrayList<Arc>();// queue of all arcs in CSP
    	for(int i = 0; i< 81; i++){
    		//arcs for each space in row
    		int count = i;
    		for(int j = 1; j < 9; j++){
    			if((count+1)%9 == 0)//check if end of row
    				count = count-8;
    			else//next space
    				count++;
    			arcs.add(new Arc(i, count));
    		}
    		//arcs for each space in column
    		count = i;
    		for(int j = 1; j < 9; j++){
    			if((count+9) > 80)
    				count = i%9;
    			else
    				count+=9;
    			arcs.add(new Arc(i, count));
    		}
    		//arcs for each space in 3x3 square
    		count = i;
    		for(int j = 1; j < 9; j++){
    			if(count == 20 || count == 23 || count == 26 || count == 47 || count == 50 || count == 53 || count == 74 || count == 77 || count == 80)
    				count -= 20;
    			else if(count%3 == 2)
    				count += 7;
    			else 
    				count ++;
    			Arc x = new Arc(i, count);
    			if(!arcs.contains(x))
    				arcs.add(x);
    		}
    	}
    	return arcs;
    }
    
}
