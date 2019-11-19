
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.*;

import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;
/******************************************************************
* Yu Liu  liuyu1@bu.edu
* CS480
* Assignment 3
* due Tuesday, Nov 12
* Problem number --
* The code in this file construct the bees and implements the collision, potential 
* funciotn and update its position. Also it is bouneded in the tank。
* The bee will chase other bees and avoid the predator bird. when it gets
* too close with other bees, the collision funciton will update its collision scalar 
* so it doesnt collide with others
* No error is found
* INPUT: initial position and scale
* OUTPUT: updated position and other parameters
*******************************************************************/
public class Bee extends Component implements Animate{
	
	Random r = new Random();

	private Component body;
	private Component wing;
	
	public Vivarium v;
	private Bird bird;
	public boolean eaten;
	public boolean noFood;
	
	private double rotateSpeed = 1;
	private double X, Y, Z, dX, dY, dZ;
	private float directionX, directionY, directionZ;
	private float speedX, speedY, speedZ;
	private float angleWing;
	private float directionWing;
	private float[] rotationMatrix;
	
	private float collisionScale=.5f, foodScale, predatorScale=.5f, wallScale=.1f;
	
	public Bee(Point3D p, float scale, Vivarium vivarium) {
		super(new Point3D(p));

		// sparse the creatures
		float rand = r.nextFloat()*2;
		if (rand > 1.5 || rand < -1.5) {
			rand = r.nextFloat()*1.5f;
		}
		
		X = p.x()+rand+r.nextFloat();
		Y = p.y()+rand+r.nextFloat();
		Z = p.z()+rand+.5;
		body = new Component(new Point3D(X, Y, Z), new BeeDisplayable(scale));
		wing = new Component(new Point3D(0, 0.03, -0.1), new BeeWingDisplayable(scale));

//		fishBodY.setColor(new FloatColor(0.3f, 0.6f, 1f));
		this.addChild(body);
		body.addChild(wing);
		
		eaten = false;
		
		// direction that which the creature is facing/moving
		directionX = -1+rand*2;
		directionY = -1+rand*3;
		directionZ = 1+rand*4;
		// speed of movement on the three axis
		speedX = 0.005f;
		speedY = 0.005f;
		speedZ = 0.005f;
		
		wing.setXNegativeExtent(-20);
		wing.setXPositiveExtent(10);

		rotationMatrix = new float[16];

		noFood = false;
		this.v = vivarium;
		bird = v.bird;
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
		rotate();
		// bounded in the tank
		float rand = r.nextFloat();
		if(rand < 0.2f){
			rand = r.nextFloat();
		}

		if (X + .3 > 2) {
			directionX = -1 - rand;
		}
		if (X - 0.3 < -2) {
			directionX = 1 + rand;
		}
		if (Y + .3 > 2) {
			directionY = -1 - rand;
		}
		if (Y - .3 < -2) {
			directionY = 1 + rand;
		}
		if (Z + .3 > 2) {
			directionZ = -1 - rand ;
		}
		if (Z - .3 < -2) {
			directionZ = 1  + rand;
		}
		
		//translate the create to simulate movements
		//update position
		this.position = new Point3D(X,Y,Z);

		dX = directionX * speedX;
		dY = directionY * speedY;
		dZ = directionZ * speedZ;
		
		X += dX;
		Y += dY;
		Z += dZ;
		
		// flap the wing
		if (wing.checkRotationReachedExtent(Axis.X)) {
			rotateSpeed = -rotateSpeed;
		}
		wing.rotate(Axis.X, rotateSpeed);
		body.rotationMatrix = rotationMatrix;
		
		// update food scalar by determine whether there are food left
//		if (v.fooddropped) {
		noFood();
//		}
	}

	private void rotate() {
		// normalize vector dX, dY, dZ to map to the Z axis
		Point3D myPosition = new Point3D(dX, dY, dZ);
		Point3D oldY = new Point3D(0.0f, 1.0f, 0.0f);
		Point3D newX = myPosition.crossProduct(oldY);
		Point3D newY = newX.crossProduct(myPosition);

		rotationMatrix[0] = (float) ((float) newX.x()/newX.norm());
		rotationMatrix[1] = (float) ((float) newX.y()/newX.norm());
		rotationMatrix[2] = (float) ((float) newX.z()/newX.norm());
		rotationMatrix[3] = 0.0f;
		rotationMatrix[4] = (float) ((float) newY.x()/newY.norm());
		rotationMatrix[5] = (float) ((float) newY.y()/newY.norm());
		rotationMatrix[6] = (float) ((float) newY.z()/newY.norm());
		rotationMatrix[7] = 0.0f;
		rotationMatrix[8] = (float) ((float) myPosition.x()/myPosition.norm());
		rotationMatrix[9] = (float) ((float) myPosition.y()/myPosition.norm());
		rotationMatrix[10] = (float) ((float) myPosition.z()/myPosition.norm());
		rotationMatrix[11] = 0.0f;
		rotationMatrix[12] = (float) X;
		rotationMatrix[13] = (float) Y;
		rotationMatrix[14] = (float) Z;
		rotationMatrix[15] = 1f;
	}
	// collision function 
	// scale is set to positive to avoid collision, and escape from predator
	// 
	private void collision(GL2 gl) {
		Point3D dist = new Point3D(0,0,0);
		// increase the collision scale to avoid collison 
		// decrease the collision scale to mimic flocking behavior
		for (Bee b: v.bees) {
			if (!b.equals(this)) {
				dist = new Point3D(Math.abs(X-b.position.x()), Math.abs(Y-b.position.y()), Math.abs(Z-b.position.z()));
				if ( dist.norm()< .5f) {
					// avoid
					collisionScale = 1.5f;
				} else {
					//pursue
					collisionScale = -0.05f;
				}
			}
		}
		// add predator bird
		Bird bird = v.bird;
		dist = new Point3D(Math.abs(X-bird.position.x()), Math.abs(Y-bird.position.y()), Math.abs(Z-bird.position.z()));
//		System.out.print(bird.position);
		// bee is eaten
		if (dist.norm()<.5) {
			this.eaten = true;
//			v.bees.remove(this);
//			System.out.print("bee is eaten\n");
		}
		
		//add food
		if (v.fooddropped) {
			foodScale = -0.1f;
			for (Food f:v.food) {
				dist = new Point3D(Math.abs(X-f.position.x()), Math.abs(Y-f.position.y()), Math.abs(Z-f.position.z()));
				if (!f.eaten && !eaten) {
					if (dist.norm()<.3) {
						f.eaten=true;
//						v.food.remove(f);
						System.out.println("food is eaten\n");
					}
				}
			}
		}
	}
	// used to calculate the potential by calling the potential function for each pair of objects
	public void potential (GL2 gl) {
		Point3D myPosition = new Point3D(X,Y,Z);
		Point3D px = new Point3D(2.0f, Y, Z);
		Point3D nx = new Point3D(-2.0f, Y, Z);
		Point3D py = new Point3D(X, 2.0f, Z);
		Point3D ny = new Point3D(X, -2.0f, Z);
		Point3D pz = new Point3D(X, Y, 2.0f);
		Point3D nz = new Point3D(X, Y, -2.0f);

		ArrayList<Point3D> list = new ArrayList<Point3D>();

		// potential functions for the tank
		list.add(potentialFunction(myPosition, px, wallScale));
		list.add(potentialFunction(myPosition, nx, wallScale));
		list.add(potentialFunction(myPosition, py, wallScale));
		list.add(potentialFunction(myPosition, ny, wallScale));
		list.add(potentialFunction(myPosition, pz, wallScale));
		list.add(potentialFunction(myPosition, nz, wallScale));

		// add the potential function about the bird for each of the bees 
		for (Bee b : v.bees) {
			if (!(b.equals(this))) {
				list.add(potentialFunction(myPosition, b.position, collisionScale));
			}
		}
		//predator
		Bird bird = v.bird;
		list.add(potentialFunction(myPosition, bird.position, predatorScale));
		// food
		for (Food f : v.food) {
			list.add(potentialFunction(myPosition, f.position, foodScale));
		}
		
		// new direction based on potential function
		Point3D newTrans = new Point3D(0f, 0f, 0f);
		for (Point3D c : list) {
			newTrans = new Point3D(newTrans.x()+c.x(), newTrans.y()+c.y(),newTrans.z()+c.z());
		}
		directionX += newTrans.x();
		directionY += newTrans.y();
		directionZ += newTrans.z();	
	}
	
	// Gaussian potential funciton
	private Point3D potentialFunction(Point3D p1, Point3D q1, float scale) {
		float x = (float) (scale * (p1.x() - q1.x()) * Math.pow(Math.E,
				-1 * (Math.pow((p1.x() - q1.x()), 2) + Math.pow((p1.y() - q1.y()), 2) + Math.pow((p1.z() - q1.z()), 2))));
		float y = (float) (scale * (p1.y() - q1.y()) * Math.pow(Math.E,
				-1 * (Math.pow((p1.x() - q1.x()), 2) + Math.pow((p1.y() - q1.y()), 2) + Math.pow((p1.z() - q1.z()), 2))));
		float z = (float) (scale * (p1.z() - q1.z()) * Math.pow(Math.E,
				-1 * (Math.pow((p1.x() - q1.x()), 2) + Math.pow((p1.y() - q1.y()), 2) + Math.pow((p1.z() - q1.z()), 2))));
		Point3D potential = new Point3D(x, y, z);
		return potential;
	}
	// no food, no chasing
	private void noFood() {
		if (v.food.isEmpty()) {
			// set food scalar to 0 to stop bee chasing the food
			foodScale = 0;
//			System.out.print("no food left");
		}
	}
}
