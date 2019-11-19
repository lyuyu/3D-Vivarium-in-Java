
import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;
/******************************************************************
* Yu Liu  liuyu1@bu.edu
* CS480
* Assignment 3
* due Tuesday, Nov 12
* Problem number --
* The code in this file construct the vivarium. the tank the bird 
* the bees, and food can be added by call the funciton add food
* it is responsible for initializing the components and draw them out
* and update them. when a component is eaten, it will remove such object 
* from its list.
* No error is found
* INPUT: gl
* OUTPUT: draws out the vivarium
*******************************************************************/
public class Vivarium implements Displayable, Animate {
	private Tank tank;
	
	public Bird bird;
	public ArrayList<Bee> bees = new ArrayList<Bee>();
	public ArrayList<Food> food = new ArrayList<Food>();
	public ArrayList<Component> vivarium = new ArrayList<Component>();
	
	public boolean fooddropped = false;

	private boolean foodInit = false;

	public Vivarium() {
		tank = new Tank(4.0f, 4.0f, 4.0f);
		
		// create predator bird
		bird = new Bird(new Point3D(1, 1.5, 1), 1.2f, this);
		vivarium.add(bird);

//		// create bees
		for (int i = 0; i < 5; i++) {
			Bee b = new Bee(new Point3D(-1+i/5, -1+i/5, 0-i/5), 0.6f, this);
			bees.add(b);
		}
		vivarium.addAll(bees);

	}
	// inits objects
	public void initialize(GL2 gl) {
		tank.initialize(gl);
		for (Bee b : bees) {
			b.initialize(gl);
		}
		bird.initialize(gl);
	}
	
	// updates objects
	public void update(GL2 gl) {
		tank.update(gl);
		for (Bee b : bees) {
//			if (!b.eaten) {
				b.update(gl);
//			}
		}
		// remove bee that was eaten
	    for (ListIterator<Bee> iter = this.bees.listIterator(); iter.hasNext();) {
	        Bee b = iter.next();
	        if (b.eaten) {
	        	System.out.print("a bee is removed\n");
	        	iter.remove();
	        }
	    }
	    
		bird.update(gl);
		
		if (fooddropped) {
			// initialize food when new food added
			if (foodInit ) {
				for (Food f : food) {
					f.initialize(gl);
				}
				foodInit = false;
			}
			// update food status
			for (Food f : food) {
//				if (!f.eaten) {
				f.translate(gl);
//				} 
			}
			// remove food was ate
		    for (ListIterator<Food> iter = this.food.listIterator(); iter.hasNext();) {
		        Food f = iter.next();
		        if (f.eaten) {
		          iter.remove();
		        }
		    }
			// when there is no food left
			if (food.isEmpty()) {
				fooddropped = false;
			}
		}
	}

	public void draw(GL2 gl) {
		tank.draw(gl);
		for (Bee b : bees) {
//			if (!b.eaten) {
			b.draw(gl);
//			}
		}	
		bird.draw(gl);
		for (Food f : food) {
//			if (!f.eaten) {
			f.draw(gl);
//			}
		}
	}

	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		// assign configurations in config_list to all Components in here
	}

	@Override
	public void animationUpdate(GL2 gl) {
		for (Component example : vivarium) {
			if (example instanceof Animate) {
				((Animate) example).animationUpdate(gl);
			}
		}
	}
	// add new food to the vivarium and initialize them then flag food dropped
	public void addFood() {
		// TODO Auto-generated method stub
		// create food
		for (int i = 0; i < 5; i++) {
			Food f = new Food(new Point3D(0, 2, 0),1f, this);
			food.add(f);
		}
		fooddropped = true;
		vivarium.addAll(food);
		foodInit = true;
	}
}
