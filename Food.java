import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;
/******************************************************************
* Yu Liu  liuyu1@bu.edu
* CS480
* Assignment 3
* due Tuesday, Nov 12
* Problem number --
* The code in this file construct the food and implements the collision, potential 
* funciotn and update its position. Also it is bouneded in the tank。
* The food will attract bees and avoid collide with each other. when it gets
* collided with bees, it gets eaten by removing from the food list in vivarium
* and it slowly moves downward and stop at the bottom of the tank
* No error is found
* INPUT: initial position and scale
* OUTPUT: updated position and other parameters
*******************************************************************/
public class Food extends Component implements Animate{
	private Component food;
	private float X, Y, Z, dX, dY, dZ;

	private float speedY;
	private float  directionX, directionY, directionZ;

	Random r = new Random();
	Vivarium v;

	public boolean eaten;

	private float collisionScale = -0.04f, beeScale =-0.01f;

	public Food(Point3D p, float scale, Vivarium vivarium) {
		super(new Point3D(p));
		
		// the distance at which the creature is from the origin
		float rand = r.nextFloat();
		// avoid to be initialized to be on the same position
		X = (float) (p.x() + rand*r.nextFloat());
		Y = (float) p.y();
		Z = (float) (p.z() + rand*r.nextFloat());
		//keeps the food in tank
		while (X>2 || X<-2 || Z>2 || Z<-2) {
			rand = r.nextFloat();
			X = (float) (p.x() + rand*5-2);
			Z = (float) (p.z() + rand*3-2);
		}
		
		food = new Component(new Point3D(X,Y,Z), new FoodDisplayable(scale));
		food.setColor(new FloatColor(0.5f,0.6f,1f));
		this.addChild(food);

		eaten = false;

		// food is dropping
		directionX = 0;
		directionY = -1;
		directionZ = 0;
		// speed of dropping
		speedY = 0.005f;
		
		this.v = vivarium;
	}

	// when food is eaten
	public void eaten() {
		this.eaten = true;
	}
	
	// avoid penetration
	public void collision(GL2 gl) {
		Point3D dist;
		for (Food f : v.food) {
			dist = new Point3D(Math.abs(X - f.position.x()), 
					Math.abs(Y - f.position.y()),
					Math.abs(Z - f.position.z()));

			if (dist.norm() < (0.2)) {
				collisionScale = .05f;
			}
		}
	}
	
	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
	}

	@Override
	public void animationUpdate(GL2 gl) {
		collision(gl);
		potential(gl);
		if (Y - 0.1 < -2) {
			directionY = 0;
		}
		// food slowly moves downwards
		dY = directionY * speedY;
		Y += dY;
		this.position = new Point3D(X,Y,Z);
	}
	
	// attract bees
	public void potential (GL2 gl) {
		Point3D myPosition = new Point3D(X,Y,Z);

		ArrayList<Point3D> list = new ArrayList<Point3D>();

		// add the potential function about the bird for each of the bees 
		for (Bee b : v.bees) {
			list.add(potentialFunction(myPosition, b.position, beeScale));
		}
	}
	// Gaussian potential function
	private Point3D potentialFunction(Point3D p1, Point3D q1, float scale) {
		// TODO Auto-generated method stub
		float x = (float) (scale * (p1.x() - q1.x()) * Math.pow(Math.E,
				-1 * (Math.pow((p1.x() - q1.x()), 2) + Math.pow((p1.y() - q1.y()), 2) + Math.pow((p1.z() - q1.z()), 2))));
		float y = (float) (scale * (p1.y() - q1.y()) * Math.pow(Math.E,
				-1 * (Math.pow((p1.x() - q1.x()), 2) + Math.pow((p1.y() - q1.y()), 2) + Math.pow((p1.z() - q1.z()), 2))));
		float z = (float) (scale * (p1.z() - q1.z()) * Math.pow(Math.E,
				-1 * (Math.pow((p1.x() - q1.x()), 2) + Math.pow((p1.y() - q1.y()), 2) + Math.pow((p1.z() - q1.z()), 2))));
		Point3D potential = new Point3D(x, y, z);
		return potential;
	}

}