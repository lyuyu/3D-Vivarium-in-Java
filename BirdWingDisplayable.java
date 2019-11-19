import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.gl2.GLUT;
//****************************************************************************
//Yu Liu  liuyu1@bu.edu
//CS480
//Assignment 3
//due Tuesday, Nov 12
//Problem number --
//The code in this file construct the wing of the bird
//No error is found
//INPUT: scale
//OUTPUT: a displaylist that has the wing
//****************************************************************************
public class BirdWingDisplayable implements Displayable{

	private int wing;
	private float scale;
	
	public BirdWingDisplayable(final float scale) {
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

		this.wing = gl.glGenLists(1);
		gl.glNewList(this.wing, GL2.GL_COMPILE);
		gl.glColor3f(0, .5f, .9f); //blue

		//wing
		gl.glPushMatrix();
		gl.glTranslated(0.2*1.2, .1*1.2, -0.2*1.2);
		gl.glRotated(50, -100, -50, -10);
		gl.glScalef(0.5f, 0.7f, 0.1f);
		glut.glutSolidSphere(.2*1.2, 36, 18);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-0.2*1.2, .1*1.2, -.2*1.2);
		gl.glRotated(50, -100, 50, 10);
		gl.glScalef(0.5f, 0.7f, 0.1f);
		glut.glutSolidSphere(0.2*1.2, 36, 18);
		gl.glPopMatrix();
		
		gl.glEndList();
	}
}
