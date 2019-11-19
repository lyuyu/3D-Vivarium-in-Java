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
* The code in this file construct the main parts of the bees 
* No error is found
* INPUT: scale
* OUTPUT: main parts of the bee as a component
*******************************************************************/
public class BeeDisplayable implements Displayable{

	private int wing;
	private int body;

	Random r = new Random();
	
	public boolean eaten;
	public boolean foodEaten;
	
	private float scale;
	private int head;
	private int eyes;
	
	public BeeDisplayable(final float scale) {
		this.scale = scale;
	}
	
	/*
	 * Method to be called for data retrieving
	 * 
	 * */
	@Override
	public void draw(GL2 gl) {

		gl.glCallList(this.body);
		gl.glCallList(this.eyes);
		gl.glCallList(this.head);

	}

	/*
	 * Initialize our example model and store it in display list
	 * 
	 * */
	@Override
	public void initialize(GL2 gl) {
		
		GLUT glut = new GLUT();
		
		//head
		this.head = gl.glGenLists(1);
		gl.glNewList(this.head, GL2.GL_COMPILE);
		gl.glColor3f(0.3f, 0.15f, 0.15f);//brown
		gl.glPushMatrix();
		glut.glutSolidSphere(0.1*0.6, 36, 18);
		gl.glPopMatrix();
		
		// legs
				//first pair of legs left right
				gl.glPushMatrix();
				gl.glTranslated(0.05*0.6, -.1*0.6, -.1*0.6);
				gl.glRotated(50, 50, 10, 5);
				glut.glutSolidCylinder(.015*0.6, .06*0.6, 36, 18);
				gl.glPopMatrix();
				gl.glPushMatrix();
				gl.glTranslated(-0.05*0.6, -.1*0.6, -.1*0.6);
				gl.glRotated(50, 50, -10, 5);
				glut.glutSolidCylinder(.015*0.6, .06*0.6, 36, 18);
				gl.glPopMatrix();
				
				//second pair of legs left right
				gl.glPushMatrix();
				gl.glTranslated(0.05*0.6, -.13*0.6, -.18*0.6);
				gl.glRotated(50, 50, 10, 5);
				glut.glutSolidCylinder(.015*0.6, .06*0.6, 36, 18);
				gl.glPopMatrix();
				gl.glPushMatrix();
				gl.glTranslated(-0.05*0.6, -.13*0.6, -.18*0.6);
				gl.glRotated(50, 50, -10, 5);
				glut.glutSolidCylinder(.015*0.6, .06*0.6, 36, 18);
				gl.glPopMatrix();
				
				//third pair of legs left right
				gl.glPushMatrix();
				gl.glTranslated(0.05*0.6, -.15*0.6, -.33*0.6);
				gl.glRotated(150, 50, 10, 5);
				glut.glutSolidCylinder(.015*0.6, .06*0.6, 36, 18);
				gl.glPopMatrix();
				gl.glPushMatrix();
				gl.glTranslated(-0.05*0.6, -.15*0.6, -.33*0.6);
				gl.glRotated(150, 50, -10, 5);
				glut.glutSolidCylinder(.015*0.6, .06*0.6, 36, 18);
				gl.glPopMatrix();
				
		gl.glEndList();

		//eyes
		this.eyes = gl.glGenLists(1);
		gl.glNewList(this.eyes, GL2.GL_COMPILE);
		gl.glColor3f(0.1f, 0.1f, 0.1f);//brown
		gl.glPushMatrix();
		gl.glTranslated(0.07*0.6, 0, 0.02*0.6);
		gl.glScalef(0.4f, 0.4f, 0.4f);
		glut.glutSolidSphere(0.11*0.6, 36, 18);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-0.07*0.6, 0, 0.02*0.6);
		gl.glScalef(0.4f, 0.4f, 0.4f);
		glut.glutSolidSphere(0.1*0.6, 36, 18);
		gl.glPopMatrix();

		//second torus
		gl.glPushMatrix();
		gl.glTranslated(0, -.01*0.6, -.32*0.6);
//		gl.glRotated(-10, 0, 0, 0);
		glut.glutSolidTorus(0.07*0.6, .08*0.6, 36, 18);
		gl.glPopMatrix();
		//first torus
		gl.glPushMatrix();
		gl.glTranslated(0, -.01*0.6, -.18*0.6);
		//gl.glRotated(-10, 0, 0, 0);
		glut.glutSolidTorus(0.07*0.6, .08*0.6, 36, 18);
		gl.glPopMatrix();
		
		//antenaes
		gl.glPushMatrix();
		gl.glTranslated(0.05*0.6, 0.15*0.6, -.03*0.6);
		gl.glRotatef(80, 0, 0, 0);
		glut.glutSolidSphere(.025*0.6,36,18);
		glut.glutSolidCylinder(.015*0.6, 0.1*0.6, 36, 18);
		gl.glPopMatrix();
		gl.glPushMatrix();
		gl.glTranslated(-0.05*0.6, 0.15*0.6, -.03);
		gl.glRotatef(80, 0, 0, 0);
		glut.glutSolidSphere(.025*0.6,36,18);
		glut.glutSolidCylinder(.015*0.6, 0.1*0.6, 36, 18);
		gl.glPopMatrix();
		
		//sting
		gl.glPushMatrix();
		gl.glTranslated(0, 0, -.4*0.6);
		gl.glRotated(180, 0, 0, 0);
		glut.glutSolidCone(.08*0.6, .1*0.6, 36, 18);
		gl.glPopMatrix();
		
		
		gl.glEndList();
		
		// body
		this.body = gl.glGenLists(1);
		gl.glNewList(this.body, GL2.GL_COMPILE);
		gl.glPushMatrix();
		gl.glTranslated(0, -0.01*0.6, -0.25*0.6);
		gl.glScalef(1f, 1f, 1.4f);
		glut.glutSolidSphere(0.15*0.6, 36, 18);
		gl.glPopMatrix();
		
		gl.glEndList();
	}


}
