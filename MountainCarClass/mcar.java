import java.util.*;

public class mcar {

		private double p;
		private double v;
		public static final double GRAVITY = -0.0025;
		public static final double GOAL  = 0.5 ;
		public static final double POS_RANGE[] = {-1.2, GOAL}; 
		public static final double VEL_RANGE[] = {-0.07,0.07}; 
		

		public void  mcar() {
			double vrand, prand; // random values for velocity and position 
            Random rand = new Random();
            
			prand = rand.nextDouble();  

			p = (POS_RANGE[1] - POS_RANGE[0]) * prand + POS_RANGE[0]; // scale position into legal range 

			vrand = rand.nextDouble();  

			v =  (VEL_RANGE[1] - VEL_RANGE[0]) * vrand + VEL_RANGE[0]; // scale velocity into legal range

		}

		// second constructor function where pos and vel are given 

		public void mcar(double pos, double vel){
			p = pos; 
			v = vel; 

		}

		// reward function for mountain car problem 

		public double reward(){
			if (reached_goal())
			   return(1);  
			else 
			   return(-1); 
		}

		// generate random starting position 

		public double random_pos(){
		    Random rand = new Random();
			double  prand = rand.nextDouble();  

			return((POS_RANGE[1] - POS_RANGE[0]) * prand + POS_RANGE[0]); // scale position into legal range 
		}

		// generate random starting velocity 
		public double random_vel(){
		    Random rand = new Random();
			double  vrand = rand.nextDouble();  

			return((VEL_RANGE[1] - VEL_RANGE[0]) * vrand + VEL_RANGE[0]); // scale velocity into legal range
		}
		// update velocity and position  -- range is clipped if it is out of bounds
		public void update_position_velocity(int a){

			double oldv = v; // preserve old values
			double oldp = p; 

			double newv, newp;  // new values of velocity and position

			int aval = a;
            /*
			if (a == backward)
			 aval = -1; 
			else aval = (int) a;  // coast = 0, forward = +1, backward = -1; */

			newv = oldv + (0.001 * aval) + (GRAVITY * Math.cos(3 * oldp)); // update equation for velocity 

			if (newv < VEL_RANGE[0])  // clip velocity if necessary to keep it within range
			{
				newv = VEL_RANGE[0]; 
			}
			else if (newv > VEL_RANGE[1])
			{
				newv = VEL_RANGE[1];

			}
		
			newp = p + newv;  // update equation for position 

			if (newp < POS_RANGE[0])  // clip position and velocity if necessary to keep it within range
			{
					newp = POS_RANGE[0]; 
					newv = 0;  // reduce velocity to 0 if position was out of bounds
			}
			else if (newp > POS_RANGE[1])
			{
					newp = POS_RANGE[1]; 
					newv = 0;
			}
	
			p = newp; 
			v = newv;   // update state to new values 

		}

        public double curr_pos(){ return(p); };  // retrieve current position
		
		public double curr_vel(){ return(v); };   // retrieve current velocity
		
		public void set_curr_pos(double pos) { p = pos;};  // set position to pos
		
		public void set_curr_vel(double vel) {v = vel;};  // set velocity to vel
		
		// see if car is up the hill
		public boolean reached_goal(){
			if (p >= GOAL)  // over the hill!
				return true; 
			else return false; 

		}
		public int choose_random_act(){
		    Random rand = new Random();
	        int rvalue = rand.nextInt(3); // return a value between 0 and 2  
	        return rvalue - 1; 
        }
        public void print(){
            System.out.println("Position: " + Double.toString(p) + " Velocity: "+ Double.toString(v) + " ");
        }
        

}
