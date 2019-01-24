
/**
* Andie Goode
* 02/16/18
* The goal of this project is to use a genetic algorithm to find a simple and good
* rule that could be used in Stock Market trading. The genetic algorithm is to
* implemented in Java
*/

import java.io.File;
import java.util.*;
import java.io.FileNotFoundException;

public class stockGA {
	// set start and end dates
	public static String startDate = "2017-02-02";
	public static String endDate = "2017-12-29";

	// function check if value over 365
	static String over365(int days) {
		Random rand = new Random();
		if (days > 365) {
			days = rand.nextInt(365) + 1;
			if (days < 10)
				return "0" + "0" + days;
			else if (days < 100)
				return "0" + days;
			else
				return "" + days;
		} else
			return "" + days;
	}

	// check string for duplicate rules
	static void checkDuplicateRule(String c) {
		int countM = 0;
		int countS = 0;
		int countE = 0;
		int index = 0;
		Random rand = new Random();
		int random = 0;
		while (countM != 1 && countE != 1 && countS != 1) {
			for (int i = 0; i < 11; i++) {
				if (Character.toString(c.charAt(i)).equals("m"))
					countM++;
				else if (Character.toString(c.charAt(i)).equals("s"))
					countS++;
				else if (Character.toString(c.charAt(i)).equals("e"))
					countE++;
			}
			random = rand.nextInt(2);
			if (countM > 1) {
				index = c.indexOf("m");
				if (random == 1)
					c = (c.substring(0, index)) + "s" + (c.substring(index + 1));
				else
					c = (c.substring(0, index)) + "e" + (c.substring(index + 1));
			} else if (countE > 1) {
				index = c.indexOf("e");
				if (random == 1)
					c = (c.substring(0, index)) + "m" + (c.substring(index + 1));
				else
					c = (c.substring(0, index)) + "s" + (c.substring(index + 1));
			} else if (countS > 1) {
				index = c.indexOf("s");
				if (random == 1)
					c = (c.substring(0, index)) + "m" + (c.substring(index + 1));
				else
					c = (c.substring(0, index)) + "e" + (c.substring(index + 1));
			}
		}
	}

	static Boolean EMA(accountInfo accounts, int numDays, int startIndex) {
		int n = startIndex - numDays;
		double alpha = 2.0 / (numDays + 1);
		double ema = 0;
		double emaNumerator = accounts.prices.get(startIndex);
		double emaDenominator = 1;
		double power = 1;
		for (int i = startIndex - 1; i >= n; i--) {
			emaNumerator += Math.pow((1 - alpha), power) * accounts.prices.get(i);
		}
		for (int i = 1; i < numDays; i++) {
			emaDenominator += Math.pow((1 - alpha), i);
		}
		ema = (emaNumerator / emaDenominator);
		if (ema < accounts.prices.get(startIndex))
			return true;
		else
			return false;
	}

	static Boolean SMA(accountInfo accounts, int numDays, int startIndex) {
		int n = startIndex - numDays;
		double sma = 0;
		for (int i = n - 1; i < startIndex; i++) {
			sma += accounts.prices.get(i);
		}
		sma = (sma / n);
		if (sma < accounts.prices.get(startIndex))
			return true;
		else
			return false;
	}

	static Boolean MAX(accountInfo accounts, int numDays, int startIndex) {
		int n = startIndex - numDays;
		double indexprice = 0;
		double max = 0;
		for (int i = n - 1; i < startIndex; i++) {
			indexprice = accounts.prices.get(i);
			if (indexprice > max)
				max = indexprice;
		}
		if (max < accounts.prices.get(startIndex))
			return true;
		else
			return false;
	}

	static void evalFitness(ArrayList<accountInfo> accounts, ArrayList<individual> population) {
		int startIndex = 0;
		int endIndex = 0;
		Boolean r1 = true;
		Boolean r2 = true;
		Boolean r3 = true;
		for (int i = 0; i < population.size(); i++) {

			if (population.get(i).fitness != 0) {
				continue;
			}
			individual ind = population.get(i);
			String rule1 = ind.rule.substring(0, 1);
			int days1 = Integer.valueOf(ind.rule.substring(1, 4));
			String rule2 = ind.rule.substring(5, 6);
			int days2 = Integer.valueOf(ind.rule.substring(6, 9));
			String rule3 = ind.rule.substring(10, 11);
			int days3 = Integer.valueOf(ind.rule.substring(11));
			double fitness = 0;
			Boolean action = false;
			for (int j = 0; j < accounts.size(); j++) {
				accountInfo acc = accounts.get(j);
				acc.accountBal = 20000.00;
				acc.gain = 0;
				acc.shares = 0;
				startIndex = accounts.get(j).dates.get(startDate);
				endIndex = accounts.get(j).dates.get(endDate);
				for (int k = startIndex; k <= endIndex; k++) {
					if (rule1.equals("s"))
						r1 = SMA(acc, days1, k);
					else if (rule1.equals("e"))
						r1 = EMA(acc, days1, k);
					else if (rule1.equals("m"))
						r1 = MAX(acc, days1, k);
					if (rule2.equals("s"))
						r2 = SMA(acc, days2, k);
					else if (rule2.equals("e"))
						r2 = EMA(acc, days2, k);
					else if (rule2.equals("m"))
						r2 = MAX(acc, days2, k);
					if (rule3.equals("s"))
						r3 = SMA(acc, days3, k);
					else if (rule3.equals("e"))
						r3 = EMA(acc, days3, k);
					else if (rule3.equals("m"))
						r3 = MAX(acc, days3, k);
					// Boolean operations determine buy or sell
					double closePr = acc.prices.get(k);
					boolean buy = false;

					if ((Character.toString(ind.rule.charAt(4)).equals("&"))
							&& (Character.toString(ind.rule.charAt(9)).equals("&"))) {
						if (((r1 && r2) && r3) == true) {
							buy = true;
						} else {
							buy = false;
						}

					} else if ((Character.toString(ind.rule.charAt(4)).equals("&"))
							&& (Character.toString(ind.rule.charAt(9)).equals("|"))) {
						if (((r1 && r2) || r3) == true) {
							buy = true;
						} else {
							buy = false;
						}
					} else if ((Character.toString(ind.rule.charAt(4)).equals("|"))
							&& (Character.toString(ind.rule.charAt(9)).equals("&"))) {
						if (((r1 || r2) && r3) == true) {
							buy = true;
						} else {
							buy = false;
						}
					} else if ((Character.toString(ind.rule.charAt(4)).equals("|"))
							&& (Character.toString(ind.rule.charAt(9)).equals("|"))) {
						if (((r1 || r2) || r3) == true) {
							buy = true;
						} else {
							buy = false;
						}
					}

					if (buy) {
						// buy at closing price if money in account to buy
						// $7 penalty
						if ((acc.accountBal - 7) >= closePr) {
							acc.accountBal = (acc.accountBal - 7.0);
						}
						// while the accountBalance is greater than or equal to
						// the closing price
						// buy as many stocks as possible
						while (acc.accountBal >= closePr) {
							acc.shares++;
							acc.accountBal = (acc.accountBal - closePr);
							action = true;
						}
					} else {
						// sell at closing price if shares in account to
						// if acc has shares
						if (acc.shares > 0) {
							// $7 penalty
							acc.accountBal = (acc.accountBal - 7.0);
							// add the closePrice*numShares to account balance
							acc.accountBal += (acc.shares * closePr);
							// no longer have any shares
							acc.shares = 0;
						}
					}
					// if you have money in gains account and account balance is
					// below 20000, then try to balance it out
					if ((acc.gain > 0) && (acc.accountBal < 20000.00)) {
						acc.accountBal = (acc.accountBal + acc.gain);
						acc.gain = 0;
					}
					// if account balance is over 20000, then add profit to
					// gains account
					if (acc.accountBal > 20000.00) {
						acc.gain = acc.gain + (acc.accountBal - 20000.00);
						acc.accountBal = 20000.00;
					}
				}

				if ((acc.shares > 0)) {
					acc.accountBal += acc.shares * acc.prices.get(endIndex);
					acc.shares = 0;
				}
				// penalize for rules that don't buy or sell
				if (action == false) {
					acc.accountBal = (acc.accountBal + acc.gain) / 2;
					acc.gain = 0;
				}
				fitness += acc.accountBal + acc.gain;
				action = false;
			}
			population.get(i).fitness = fitness;
		}
	}

	static void roulette(ArrayList<individual> population) {
		double total_fitness = 0;
		for (int i = 0; i < population.size(); i++) {
			total_fitness += population.get(i).fitness;
		}
		for (int i = 0; i < population.size(); i++) {
			population.get(i).selection = (double) (population.get(i).fitness / total_fitness);
		}
	}

	static int select(ArrayList<individual> population) {
		Random rand = new Random();
		double val = rand.nextDouble();
		int x = rand.nextInt(population.size());
		for (int i = x; i < population.size(); i++) {
			val += population.get(i).selection;
			if (val > 1)
				return i;
		}
		int s = rand.nextInt(population.size());
		return s;
	}

	static individual crossover(individual parent1, individual parent2) {
		Random rand = new Random();
		int divide = rand.nextInt(14);
		String p1 = parent1.rule.substring(0, divide);
		String p2 = parent2.rule.substring(divide);
		String c = p1 + p2;
		int days = 0;
		int index = 1;
		for (int i = 0; i < 3; i++) {
			if (i == 2)
				days = Integer.valueOf(c.substring(11));
			else if (i == 1)
				days = Integer.valueOf(c.substring(6, 9));
			else
				days = Integer.valueOf(c.substring(1, 4));
			if (days > 365)
				c = c.substring(0, index) + over365(days) + c.substring(index + 3);
		}
		checkDuplicateRule(c);
		individual child = new individual(c, 0);
		return child;
	}

	static void mutate(individual child) {
		String c = child.rule;
		Random rand = new Random();
		int mutationRate = rand.nextInt(1000);
		int index = rand.nextInt(14);
		int index2 = 0;
		int days = 0;
		if (mutationRate == 0) { // mutationRate = 0.001
			if ((index == 0) || (index == 5) || (index == 10)) { // if rule char
				int rule = rand.nextInt(2); // choose which rule to swap to
				String replace = "";
				if (Character.toString(c.charAt(index)).equals("m")) { // if
																		// rule
																		// is m
																		// swap
																		// rule
																		// to s
																		// or e
																		// and
																		// replace
																		// its
																		// duplicate
																		// with
																		// m
					if (rule == 0)
						replace = "s";
					else
						replace = "e";
					index2 = c.indexOf(replace);
					if (index < index2)
						c = c.substring(0, index) + replace + c.substring(index + 1, index2) + "m"
								+ c.substring(index2 + 1);
					else
						c = c.substring(0, index2) + "m" + c.substring(index2 + 1, index) + replace
								+ c.substring(index + 1);
				} else if (Character.toString(c.charAt(index)).equals("s")) { // if
																				// rule
																				// is
																				// s
																				// swap
																				// rule
																				// to
																				// m
																				// or
																				// e
																				// and
																				// replace
																				// its
																				// duplicate
																				// with
																				// s
					if (rule == 0)
						replace = "m";
					else
						replace = "e";
					index2 = c.indexOf(replace);
					if (index < index2)
						c = c.substring(0, index) + replace + c.substring(index + 1, index2) + "s"
								+ c.substring(index2 + 1);
					else
						c = c.substring(0, index2) + "s" + c.substring(index2 + 1, index) + replace
								+ c.substring(index + 1);
				} else if (Character.toString(c.charAt(index)).equals("e")) { // if
																				// rule
																				// is
																				// e
																				// swap
																				// rule
																				// to
																				// s
																				// or
																				// m
																				// and
																				// replace
																				// its
																				// duplicate
																				// with
																				// e
					if (rule == 0)
						replace = "s";
					else
						replace = "m";
					index2 = c.indexOf(replace);
					if (index < index2)
						c = c.substring(0, index) + replace + c.substring(index + 1, index2) + "e"
								+ c.substring(index2 + 1);
					else
						c = c.substring(0, index2) + "e" + c.substring(index2 + 1, index) + replace
								+ c.substring(index + 1);
				}
			} else if ((index == 4) || (index == 9)) { // change bool operator
				if (Character.toString(c.charAt(index)).equals("&"))
					c = c.substring(0, index) + "|" + c.substring(index + 1);
				else
					c = c.substring(0, index) + "&" + c.substring(index + 1);
			} else { // change an int value
				int num = rand.nextInt(10);
				int x = 1;
				c = c.substring(0, index) + num + c.substring(index + 1);
				for (int i = 0; i < 3; i++) {
					days = Integer.valueOf(c.substring(x, x + 3));
					if (days > 365)
						days = Integer.valueOf(over365(days));
					c = c.substring(0, x) + days + c.substring(x + 3);
					x += 5;
				}
			}
			child.rule = c;
		}
	}

	static int answersofar(ArrayList<individual> population) {
		double fitness = -1;
		int index = 0;
		for (int i = 0; i < population.size(); i++) {
			if (population.get(i).fitness >= fitness) {
				fitness = population.get(i).fitness;
				index = i;
			}
		}
		return index;
	}

	static String produce(ArrayList<accountInfo> accounts, ArrayList<individual> population) {
		int count = 0;
		int current_generation = 0;
		int generation = 0;
		int current = -1;
		String solution = "";
		double solutionGain = 0.0;

		while (count < 200) { // maximum iteration is set to 1000

			// evaluate the fitness of the population in S
			evalFitness(accounts, population);
			// create the roullete wheel for the population in S
			roulette(population);
			// select the father and the mother (parents)
			int f = select(population);
			int m = select(population);

			Random prob = new Random();
			int luck = prob.nextInt(100);
			if (luck < 80) { // probability of crossover is set to 0.8
				individual child1 = crossover(population.get(f), population.get(m)); // offspring
																						// 1
				individual child2 = crossover(population.get(m), population.get(f)); // offspring
																						// 2
				mutate(child1); // mutate the offspring 1
				mutate(child2); // mutate the offspring 2
				// Add the offspring to the population
				population.add(child1);
				population.add(child2);

				current_generation++;

				// re-evaluate the fitness of the population again
				evalFitness(accounts, population);
				// re-set the roulette wheel again
				roulette(population);
			}
			// get the best answer so far
			int sol = answersofar(population); // index of current answer so
												// biggest fitness?
			solution = population.get(sol).rule;
			solutionGain = population.get(sol).fitness;

			if (sol != -1 && sol != current) {
				current = sol;
				generation = current_generation;
			}
			System.out.println("answer's generation: " + generation + ", total generation: " + current_generation);
			count++;
		}
		// return the index of the best solution at the end
		System.out.println("Solution: " + solution + " average profit: " + String.format("%.2f", solutionGain / 5.0 - 20000.0));
		return solution;

	}

	public static void main(String[] args) {
		int count = 0;
		int capacity = 365 * 4;
		// array to hold all file names for historical data of each company
		String[] files = { "F.csv", "AAPL.csv", "GOOG.csv", "NATI.csv", "NKE.csv" };
		// arrayList for Population
		ArrayList<individual> population = new ArrayList<individual>();
		// arrayList to temporarily hold historical data for each account
		ArrayList<Double> closePrices = new ArrayList<Double>();
		// hashmap for dates so can get indexes where close prices are located
		HashMap<String, Integer> dateMap = new HashMap<String, Integer>(capacity);
		// arrayList to hold info for all n accounts
		ArrayList<accountInfo> accounts = new ArrayList<accountInfo>();
		// loop through array of n file names
		for (int i = 0; i < files.length; i++) {
			// try scanner for each file
			try (Scanner sc = new Scanner(new File(files[i]))) {
				// ignore first line
				String x = sc.nextLine();
				// until empty
				while (sc.hasNext()) {
					// set x to next line
					x = sc.nextLine();
					// split by commas and store in array
					String[] tempArr = x.split(",");
					// add to array the date, opening price, and closing price
					dateMap.put(tempArr[0], count);
					closePrices.add(Double.valueOf(tempArr[4]));
					count++;
				}
				// for each account, add that accounts dateMap, closePrices,
				// startbalance, gains, and shares
				accounts.add(new accountInfo(dateMap, closePrices, 20000.0, 0.0, 0));
				// clear closePrices array for next file to use
				closePrices.clear();
				count = 0;
				dateMap.clear();
				// catch if no file found and exit program
			} catch (FileNotFoundException e) {
				System.out.println("File not found " + e);
				System.exit(1);
			}
		}
		// read in starting input from txt file
		Scanner sc = new Scanner(System.in);
		while (sc.hasNext()) {
			population.add(new individual(sc.nextLine(), 0));
		}
		sc.close();
		produce(accounts, population);
	}

}
