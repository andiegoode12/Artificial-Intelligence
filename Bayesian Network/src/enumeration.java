
import java.util.*;

class bayesianNetwork {
	// THIS IS SO UGLY IM SORRY
	public static Node[] createNetwork() {
		String[][] temp = { { "low", "0.9" }, { "high", "0.1" } };
		// create pollution node
		Node pollution = new Node("pollution", "", temp, 2);
		temp = new String[][] { { "true", "0.3" }, { "false", "0.7" } };
		// create smoker node
		Node smoker = new Node("smoker", "", temp, 2);
		temp = new String[][] { { "true", "high,true", "0.95" }, { "true", "high,false", "0.2" },
				{ "true", "low,true", "0.3" }, { "true", "low,false", "0.001" }, { "false", "high,true", "0.05" },
				{ "false", "high,false", "0.8" }, { "false", "low,true", "0.7" }, { "false", "low,false", "0.999" } };
		// create cancer node
		Node cancer = new Node("cancer", "pollution,smoker", temp, 3);
		temp = new String[][] { { "positive", "true", "0.9" }, { "positive", "false", "0.2" },
				{ "negative", "true", "0.1" }, { "negative", "false", "0.8" } };
		// create xray node
		Node xray = new Node("xray", "cancer", temp, 3);
		temp = new String[][] { { "true", "true", "0.65" }, { "true", "false", "0.3" }, { "false", "true", "0.35" },
				{ "false", "false", "0.7" } };
		// create dyspnea node
		Node dyspnea = new Node("dyspnea", "cancer", temp, 3);
		Node[] n = { pollution, smoker, cancer, xray, dyspnea };
		return n;
	}

	public static int getNode(String n, Node[] net) {
		for (int i = 0; i < net.length; i++) {
			if (n.equals(net[i].name)) {
				return i;
			}
		}
		return -1;
	}

}

class Node {
	String name;
	String parents;
	String[][] table;
	int vars;

	public Node(String n, String s, String[][] a, int v) {
		name = n;
		parents = s;
		table = a;
		vars = v;
	}

}

public class enumeration {
	public static void main(String[] args) {
		// create network
		Node[] bn = bayesianNetwork.createNetwork();

		Scanner sc = new Scanner(System.in);
		System.out.println("What is your query and its outcome?");
		String query = sc.nextLine().toLowerCase();
		ArrayList<String> evidence = new ArrayList<String>();
		String input = "";
		while (!input.equals("exit")) {
			System.out.println("What is the evidence and its outcome(s)?(\'exit\' when finished)");
			input = sc.nextLine().toLowerCase();
			if (!input.equals("exit") && !input.equals(""))
				evidence.add(input);
		}

		sc.close();
		String origE = "";
		for (int i = 0; i < evidence.size(); i++) {
			if (i == evidence.size() - 1)
				origE += evidence.get(i);
			else
				origE += evidence.get(i) + ",";
		}
		double solution = enumerationAsk(query, evidence, bn);
		System.out.print("P(" + query + "|");
		System.out.print(origE + ")");
		System.out.print("=" + solution);
	}

	private static double enumerationAsk(String q, ArrayList<String> e, Node[] bn) {
		// get all vars
		ArrayList<String> vars = new ArrayList<String>();
		for (int i = 0; i < bn.length; i++) {
			vars.add(bn[i].name);
		}
		checkOrder(e);
		// index of query in bn
		int indexNode = bayesianNetwork.getNode(q.substring(0, q.indexOf('=')), bn);
		// array of querys parents
		String[] p = bn[indexNode].parents.split(",");
		// calculate alpha from the evidence
		double alpha = enumerateAll(e, vars, bn);

		// if the query has at least one parent find the last location of the
		// query's parent in e and add the query to e at that index(must come
		// after its parents)
		checkOrder(e);
		addEvidence(indexNode, bn, q, e, p);
		// calculate probability
		double probability = enumerateAll(e, vars, bn);
		System.out.println("before alpha = " + probability);
		System.out.println("alpha = " + alpha);
		// return the normalized probability
		return probability/alpha;

	}

	private static double enumerateAll(ArrayList<String> e, ArrayList<String> vars, Node[] bn) {
		// if vars is empty (base case) then return 1
		if (vars.isEmpty())
			return 1.0;
		//get first variable and remove

		ArrayList<String> vars2 = new ArrayList<String>(vars);
		String Y = vars2.get(0);
		vars2.remove(0);
		int nodeIndex = bayesianNetwork.getNode(Y, bn);
		String[][] table = bn[nodeIndex].table;
		String[] parents = bn[nodeIndex].parents.split(",");
		ArrayList<String> parentOutcomes = new ArrayList<String>();
		double value = 0;
		int index = -1;

		// find if Y is in e
		index = inEvidence(e,Y);
		// if Y has parents then get their outcomes and store them in ArrayList
		getParentOutcomes(bn, e, nodeIndex,parents,parentOutcomes);

		// if Y has an outcome in e
		if (index != -1) {
			String outcomeY = "";
			for(int i = 0; i < e.size(); i++){
				if(e.get(i).contains(Y)){
					outcomeY = e.get(i).substring(e.get(i).indexOf('=')+1);
					break;
				}
			}
			// if Y has two parents combine evidences of parents into pairs for
			// table referencing
			if (parents.length > 1)
				parentOutcomes = new ArrayList<String>(getPairs(parentOutcomes));
			// if Y has parents
			if (!bn[nodeIndex].parents.equals("")) {
				// loop through the parents outcomes and the Nodes table
				for (int j = 0; j < parentOutcomes.size(); j++) {
					for (int k = 0; k < table.length; k++) {
						// for each table entry if the first element=the outcome
						// of Y and the second element = the parents outcome
						if (table[k][0].equals(outcomeY) && table[k][1].equals(parentOutcomes.get(j))) {
							// get its probability
							value = Double.parseDouble(table[k][2]);
							return value * enumerateAll(e, vars2, bn);
						}
					}
				}
			} else  { // if Y has no parents
				// loop through the Nodes table
				// if the outcome of Y = the element
				for (int i = 0; i < table.length; i++) {
					if (table[i][0].equals(outcomeY)){
						// get its probability
						value = Double.parseDouble(table[i][table.length - 1]);
						// return P(Y=y | values assigned to Y’s parents in e) ×
						// ENUMERATE-ALL(REST(vars), e)
						return value * enumerateAll(e, vars2, bn);
					}
				}
			}
			return 1.0;


		} else { //if Y is not in e
			String TF = "";
			// if Y has two parents combine evidences of parents into pairs for table referencing
			//ArrayList<String> e2 = new ArrayList<String>(e);
			ArrayList <String> e2 = new ArrayList<String>(e);
			if (parents.length > 1)
				parentOutcomes = new ArrayList<String>(getPairs(parentOutcomes));
			//if Y has at least one parent
			if (!bn[nodeIndex].parents.equals("")) {
				//loop through the table
				for (int i = 0; i < table.length; i++) {
					//get the outcome yi of the query (preventing duplicates)
					if (!table[i][0].equals(TF)) {
						TF = table[i][0];
						//add Y=yi to e
						addEvidence(nodeIndex, bn, Y + "=" + TF, e2, bn[nodeIndex].parents.split(","));
					}
						// loop through the list of parent outcomes
						for (int k = 0; k < parentOutcomes.size(); k++) {
							double temp1 = 0.0;

							//if the first element in the table= query outcome and second element in table= parent outcome

								if(table[i][0].equals(TF) && table[i][1].equals(parentOutcomes.get(k))){
									//grab probability and recurse
									temp1 = Double.parseDouble(table[i][2]);
									value += (temp1 * enumerateAll(e2, vars2, bn));

							}
						}

				}
			} else { // if Y has no parents
				//for each possible value of yi
				for (int i = 0; i < table.length; i++) {
					//add Y=yi to e
					addEvidence(nodeIndex, bn, Y + "=" + table[i][0], e2, bn[nodeIndex].parents.split(","));
					//grab probability and recurse
					double temp = Double.parseDouble(table[i][1]);
					value += (temp * enumerateAll(e2, vars2, bn));
				}
			}
			return value;
		}
	}


	public static ArrayList<String> getPairs(ArrayList<String> nopairs) {
		ArrayList<String> pairs = new ArrayList<String>();
		for (int i = 0; i < nopairs.size(); i++) {
			for (int j = i + 1; j < nopairs.size(); j++) {
				pairs.add(nopairs.get(i) + "," + nopairs.get(j));
			}
		}
		return pairs;
	}
	public static void checkOrder(ArrayList<String> e){
		if(e.get(0).contains("smoker") && e.get(1).contains("pollution")){
			String temp = e.get(1);
			e.set(1, e.get(0));
			e.set(0, temp);
		}
	}
	public static void addEvidence(int indexNode, Node[] bn, String q, ArrayList<String> e, String[] p){
		int index = -1;
		String t = bn[indexNode].name;
		if(inEvidence(e, t) != -1){
			index = inEvidence(e, t);
			e.set(index,q);
		}
		else if (!bn[indexNode].parents.equals("")) {
			for (int j = 0; j < p.length; j++) {
				for (int i = e.size() - 1; i > 0; i--) {
					if (e.get(i).contains(p[j])) {
						index = i;
						e.add(index+1, q);
						break;
					}
				}
			}
		}
		// if it has no parents then add the query to the beginning of e
		else {
			if(e.get(0).contains("pollution"))
				index = 1;
			else
				index=0;
			e.add(index, q);
		}
		if(index==-1)
			e.add(e.size(), q);

	}

	public static int inEvidence(ArrayList<String> e, String Y){
		for (int i = 0; i < e.size(); i++) {
			if (e.get(i).contains(Y) && e.get(i).contains("=")) {
				return i;
			}
		}
		return -1;
	}

	public static void getParentOutcomes(Node[] bn, ArrayList<String> e, int nodeIndex,String[] parents, ArrayList<String> parentOutcomes){
		if (!bn[nodeIndex].parents.equals("")) {
			// get parents of Y in e
			for (int i = 0; i < e.size(); i++) {
				for (int j = 0; j < parents.length; j++) {
					if (e.get(i).contains(parents[j])) {
						int indexEqual = e.get(i).indexOf('=');
						parentOutcomes.add(e.get(i).substring(indexEqual + 1));
					}
				}
			}
		}
	}
}
