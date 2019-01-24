import java.awt.*;
import javax.swing.*;
import java.util.*;

public class Simulation extends JPanel {
   
    public static void main(String[] args) {
    //DO NOT CHANGE
    JFrame.setDefaultLookAndFeelDecorated(true);
    JFrame frame = new JFrame("Montain Car Simulation");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setBackground(Color.white);
    frame.setSize(300, 300);
 
    QuadraticCurve panel = new QuadraticCurve();
    
 
    frame.add(panel);
    frame.setVisible(true);
    //END OF DO NOT CHANGE
    
        
    mcar car = new mcar();
    
    
        
    car.set_curr_pos(0);
    car.set_curr_vel(0);
    double pos;
    double vel;
    int count = 0;
    while(!car.reached_goal()){
            
            int act = car.choose_random_act();
            car.update_position_velocity(act);
            car.print();
        
            pos = car.curr_pos();
            vel = car.curr_vel();
    
            panel.updateCircle(pos);
            
            try {
                Thread.sleep(50);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            frame.repaint();
            System.out.println("Step count: "+Integer.toString(count));
            count++;
    }
   
    
        
   }

}
