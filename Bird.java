
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
* The code in this file construct the bird and implements the collision, potential 
* funciotn and update its position. Also it is bouneded in the tank。
* The bird will chase other bees and avoid collide with the walls. when it gets
* collided with other bees, the bee will be removed from the bee list thus disappears.
* No error is found
* INPUT: initial position and scale
* OUTPUT: updated position and other parameters
*******************************************************************/
public class Bird extends Component implements Animate{
	Random r = new Random();

	private Component body;
	private Component wing;
	private Component tail;

	public Vivarium v;
	public boolean beeEaten;
	
	private double rotateSpeed = 1;
	private double X, Y, Z, dX, dY, dZ;
	private float directionX, directionY, directionZ;
	private float speedX, speedY, speedZ;
	private float[] rotationMatrix;
	
	private float beeScale=-0.1f, wallScale=0.05f;
	
	public Bird(Point3D p, float scale, Vivarium vivarium) {
		super(new Point3D(p));
		X = p.x();
		Y= p.y();
		Z = p.z();
		body = new Component(new Point3D(X,Y,Z), new BirdDisplayable(scale));
		wing = new Component(new Point3D(X,Y,Z), new BirdWingDisplayable(scale));
		tail = new Component(new Point3D(X,Y,Z), new BirdTailDisplayable(scale));

		
		this.addChild(body);
		body.addChild(tail);
		body.addChild(wing);

		// direction that which the creature is facing/moving
		directionX = -1;
		directionY = 1;
		directionZ = -1;
		
		wing.setXNegativeExtent(-20);
		wing.setXPositiveExtent(20);
		tail.setXNegativeExtent(-30);
		tail.setXPositiveExtent(30);
		
		// speed of movement on the three axis
		speedX = 0.009f;
		speedY = 0.009f;
		speedZ = 0.009f;
		
		rotationMatrix = new float[16];

		beeEaten = false;
		this.v = vivarium;
//		System.out.print(v);
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
		
		// avoid collision to walls
		float rand = r.nextFloat();
		// avoid trapped in corner
		if(rand < 0.2f){
			rand = r.nextFloat();
		}

		if (X + .5 > 2) {
			directionX = -1 - rand;
		}
		if (X - 0.5 < -2) {
			directionX = 1 + rand;
		}
		if (Y + .5 > 2) {
			directionY = -1 - rand;
		}
		if (Y - 0.5 < -2) {
			directionY = 1 + rand;
		}
		if (Z + .5 > 2) {
			directionZ = -1 - rand ;
		}
		if (Z - .5 < -2) {
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
		
		// rotate wings and tails
		if (this.wing != null) {
			if (wing.checkRotationReachedExtent(Axis.X)) {
				rotateSpeed = -rotateSpeed;
		}
		wing.rotate(Axis.X, rotateSpeed);
		tail.rotate(Axis.X, rotateSpeed);
		
		// update the rotationMatrix
		body.rotationMatrix = rotationMatrix;
		noBee();
		}
	}
	// find the right rotation matrix to make the creature 
	// to face the direction it's going
	private void rotate() {
		// normalize vector dX, dY, dZ to map to the Z axis
		Point3D myPosition = new Point3D(dX, dY, dZ);
		// up vector
		Point3D oldY = new Point3D(0.0f, 1.0f, 0.0f);
		Point3D newX = myPosition.crossProduct(oldY);
		// perpendicular to the up vector
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
		Point3D dist;
		for (Bee b: v.bees) {
//			System.out.println("bee Position: "+b.position+"\n");
			dist = new Point3D(Math.abs(X-b.position.x()), Math.abs(Y-b.position.y()), Math.abs(Z-b.position.z()));
			if (dist.norm()< .5f) {
				b.eaten = true;
//				System.out.print(b);
			} 
		}
	}
	
	public void potential (GL2 gl) {
		Point3D myPosition = new Point3D(X,Y,Z);
		Point3D px = new Point3D(2.0f, Y, Z);
		Point3D nx = new Point3D(-2.0f, Y, Z);
		Point3D py = new Point3D(X, 2.0f, Z);
		Point3D ny = new Point3D(X, -2.0f, Z);
		Point3D pz = new Point3D(X, Y, 2.0f);
		Point3D nz = new Point3D(X, Y, -2.0f);

		ArrayList<Point3D> list = new ArrayList<Point3D>();

		// make the potential functions work for all 6 walls
		list.add(potentialFunction(myPosition, px, wallScale));
		list.add(potentialFunction(myPosition, nx, wallScale));
		list.add(potentialFunction(myPosition, py, wallScale));
		list.add(potentialFunction(myPosition, ny, wallScale));
		list.add(potentialFunction(myPosition, pz, wallScale));
		list.add(potentialFunction(myPosition, nz, wallScale));

		// add potential function for each of the bees
		for (Bee b : v.bees) {
			list.add(potentialFunction(myPosition, b.position, beeScale));
		}
		// new transition
		Point3D newTrans = new Point3D(0f, 0f, 0f);
		for (Point3D c : list) {
			newTrans = new Point3D(newTrans.x()+c.x(), newTrans.y()+c.y(),newTrans.z()+c.z());
		}
		directionX += newTrans.x();
		directionY += newTrans.y();
		directionZ += newTrans.z();	
	}
	
	// used to get a new position
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
	
	// stop chasing if no bee left
	private void noBee() {
		if (v.bees.isEmpty()) {
			beeScale = 0;
//			System.out.print("no bee left");
		}
	}
}
