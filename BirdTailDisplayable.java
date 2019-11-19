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
//The code in this file construct the tail of the bird
//No error is found
//INPUT: scale
//OUTPUT: a displaylist that has the tail
//****************************************************************************
public class BirdTailDisplayable implements Displayable{

	private int bird;
	private int body;
	private Component wing;

	private float scale;
	private GLUquadric qd;
	
	public BirdTailDisplayable(final float scale) {
		this.scale = scale;
	}
	
	/*
	 * Method to be called for data retrieving
	 * 
	 * */
	@Override
	public void draw(GL2 gl) {
		gl.glCallList(this.bird);
	}

	/*
	 * Initialize our example model and store it in display list
	 * 
	 * */
	@Override
	public void initialize(GL2 gl) {
		GLUT glut = new GLUT();

		this.bird = gl.glGenLists(1);
		gl.glNewList(this.bird, GL2.GL_COMPILE);
		gl.glColor3f(0, .5f, .9f); //blue

		// tail
		gl.glPushMatrix();
		gl.glTranslated(0, 0, -.5*1.2);
		gl.glScalef(1f, .2f, 1f);
		glut.glutSolidCone(.15*1.2, .2*1.2, 36, 18);
		gl.glPopMatrix();
		
		gl.glEndList();
	}
}
