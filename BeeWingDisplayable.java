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
* The code in this file construct the wings of the bees 
* No error is found
* INPUT: scale
* OUTPUT: wings of the bee as a component
*******************************************************************/
public class BeeWingDisplayable implements Displayable{

	private int wing;
	public boolean eaten;
	private float scale;
	
	public BeeWingDisplayable(final float scale) {
		this.scale = scale;
	}
	
	/*
	 * Method to be called for data retrieving
	 * 
	 * */
	@Override
	public void draw(GL2 gl) {
		gl.glCallList(this.wing);
	}

	/*
	 * Initialize our example model and store it in display list
	 * 
	 * */
	@Override
	public void initialize(GL2 gl) {
		
		GLUT glut = new GLUT();
		
		//wing
		this.wing = gl.glGenLists(1);
		gl.glNewList(this.wing, GL2.GL_COMPILE);
		gl.glColor3f(0.9f, 0.8f, 0.7f);

		gl.glPushMatrix();
		gl.glTranslated(0.05*this.scale, .2*this.scale, -0.1*this.scale);
		gl.glRotated(-55, 100, 80, 10);
		gl.glScalef(0.3f, 0.7f, 0.01f);
		glut.glutSolidSphere(.2*this.scale, 36, 18);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslated(-0.05*this.scale, .2*this.scale, -.1*this.scale);
		gl.glRotated(-55, 100, -80, 10);
		gl.glScalef(0.3f, 0.7f, 0.01f);
		glut.glutSolidSphere(0.2*this.scale, 36, 18);
		gl.glPopMatrix();
		
		gl.glEndList();
	}


}
