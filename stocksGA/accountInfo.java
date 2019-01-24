import java.util.*;
// class of information for each account
public class accountInfo{
  // initial parameters for company
  public HashMap<String, Integer> dates = new HashMap<>();
  public ArrayList <Double> prices;
  double accountBal;
  double gain;
  int shares;
  // create new instance of company
  public accountInfo(HashMap<String, Integer> d, ArrayList<Double> p, double a, double g, int s){
    dates.putAll(d);
    prices = new ArrayList<Double>(p);
    accountBal = a;
    gain = g;
    shares = s;
  }
}
