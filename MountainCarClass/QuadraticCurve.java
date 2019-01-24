import java.awt.*;
import javax.swing.*;
 
public class QuadraticCurve extends JPanel{
    
  private Circle C1;
    
  public QuadraticCurve (){
      C1 = new Circle(10, Color.green, (int)xval(0),(int)yval(f(0))-10);
  }
  public void drawMt(Graphics g){
      //draw the mountain
      for(double i = -1.2; i <= 0.5; i+= 0.1){
          
          double val1 = xval(i);
          double val2 = xval(i+ 0.1);
          double f1  =  yval(f(i));
          double f2  =  yval(f(i + 0.1));
          
          g.drawLine((int)val1,(int) f1, (int)val2, (int)f2);
      }
        
  }
    
   public void drawCl(Graphics g){
       C1.draw(g);
   }
 
  public void paintComponent(Graphics g) {
      drawMt(g);
      drawCl(g);
 
  }
  public double f(double x){
    return 0.8 * (x * x);
  }
  
  public double xval(double x){
    return (((x + 1.2)/1.7) * 200) + 30;
  }
  public double yval(double x){
    return 250 - ((x / 1.15) * 200);
  }
  public void updateCircle(double x){
      double newx = xval(x);;
      double newy;
      if(x <= 0.2){newy = yval(f(x)) - 10;}
      else {newy = yval(f(x)) - 13;}
      C1.setX((int)newx);
      C1.setY((int)newy);
  }
    
}
