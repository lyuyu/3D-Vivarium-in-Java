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
//The code in this file construct the main parts of the bird
//No error is found
//INPUT: scale
//OUTPUT: a displaylist that has the main parts
//****************************************************************************
public class BirdDisplayable implements Displayable{

	private int body;
	private float scale;
	private int beak;
	private int eyes;
	
	public BirdDisplayable(final float scale) {
		this.scale = scale;
	}
	
	/*
	 * Method to be called for data retrieving
	 * 
	 * */
	@Override
	public void draw(GL2 gl) {
		gl.glCallList(this.body);
		gl.glCallList(this.beak);
		gl.glCallList(this.eyes);

	}

	/*
	 * Initialize our example model and store it in display list
	 * 
	 * */
	@Override
	public void initialize(GL2 gl) {
		GLUT glut = new GLUT();

		this.body = gl.glGenLists(1);
		gl.glNewList(this.body, GL2.GL_COMPILE);
		gl.glColor3f(0, .5f, 1f); //blue
		//head
		gl.glPushMatrix();
		glut.glutSolidSphere(0.1*scale, 36, 18);
		gl.glPopMatrix();

		// body
		gl.glPushMatrix();
		gl.glTranslated(0, -0.01*scale, -0.2*scale);
		gl.glScalef(1f, 1f, 1.2f);
		glut.glutSolidSphere(0.15*scale, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();

		//eyes
		this.eyes = gl.glGenLists(1);
		gl.glNewList(this.eyes, GL2.GL_COMPILE);
		gl.glColor3f(.1f, .1f, .1f); //dark
		gl.glPushMatrix();
		gl.glTranslated(0.07*scale, 0, 0.02*scale);
		gl.glScalef(0.4f, 0.4f, 0.4f);
		glut.glutSolidSphere(0.11*scale, 36, 18);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
		gl.glTranslated(-0.07*scale, 0, 0.02*scale);
		gl.glScalef(0.4f, 0.4f, 0.4f);
		glut.glutSolidSphere(0.1*scale, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();

		// beak
		this.beak = gl.glGenLists(1);
		gl.glNewList(this.beak, GL2.GL_COMPILE);
		gl.glColor3f(1f, .5f, 0f); //dark
		gl.glPushMatrix();
		gl.glTranslated(0, 0, .08*scale);
		gl.glRotated(0, 0, 0, 0);
		glut.glutSolidCone(.05*scale, .1*scale, 36, 18);
		gl.glPopMatrix();
		gl.glEndList();

		
	}
}
