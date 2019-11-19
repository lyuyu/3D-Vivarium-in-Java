import java.util.Random;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;
/******************************************************************
* Yu Liu  liuyu1@bu.edu
* CS480
* Assignment 3
* due Tuesday, Nov 12
* Problem number --
* The code in this file construct the food
* No error is found
* INPUT: scale
* OUTPUT: food as a component
*******************************************************************/
public class FoodDisplayable implements Displayable{

	static int food;

	Random r = new Random();
	
	public boolean eaten;
	private float scale;
	
	public FoodDisplayable(final float scale) {
		this.scale = scale;
	}
	
	/*
	 * Method to be called for data retrieving
	 * 
	 * */
	@Override
	public void draw(GL2 gl) {
		gl.glCallList(this.food);
	}

	/*
	 * Initialize our example model and store it in display list
	 * 
	 * */
	@Override
	public void initialize(GL2 gl) {
		
		GLUT glut = new GLUT();
		
		this.food = gl.glGenLists(1);
		gl.glNewList(this.food, GL2.GL_COMPILE);
		gl.glPushMatrix();
		glut.glutSolidSphere(0.05, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();
	}


}
