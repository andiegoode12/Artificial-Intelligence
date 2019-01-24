// class of information for the historical data of each account
public class individual{
  // initial parameters for histData
  String rule;
  double fitness;
  double selection;
  
  // create new instance of histData
  public individual(String r, double f){
    rule = r;
    fitness = f;
    selection = 0;
  }
}
