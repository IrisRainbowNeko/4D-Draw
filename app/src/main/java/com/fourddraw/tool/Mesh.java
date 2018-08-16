package com.fourddraw.tool;

import android.graphics.*;
import android.opengl.*;
import java.nio.*;
import javax.microedition.khronos.opengles.*;

public class Mesh
 {//几何图形的基类
	// Our vertex buffer.
	FloatBuffer verticesBuffer = null;

	// Our index buffer.
	ShortBuffer indicesBuffer = null;

	// The number of indices.
	int numOfIndices = -1;
	int verticecount=0;
	// Flat Color
	int[] rgba= new int[] {0xFFFF,0xFFFF,0xFFFF,0xFFFF};

	// Smooth Colors
	IntBuffer colorBuffer = null;
	FloatBuffer NormalBuffer = null;
	FloatBuffer texbuff=null;
	// Translate params.
	public float x = 0;

	public float y = 0;

	public float z = 0;

	// Rotate params.
	public float rx = 0;

	public float ry = 0;

	public float rz = 0;
	public int drawtype=GL10.GL_TRIANGLES;
	int texid;
	public boolean texopen;//开启纹理的标识
	public void setposition(float x,float y,float z)
	{
		this.x=x;this.y=y;this.z=z;
	}
	public void setrotate(float x,float y,float z)
	{
		rx=x;ry=y;rz=z;
	}
	public void draw(GL10 gl) {
		gl.glPushMatrix();
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticesBuffer);
		if(texopen){
			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glTexCoordPointer(2,GL10.GL_FLOAT,0,texbuff);
			gl.glBindTexture(GL10.GL_TEXTURE_2D, texid);
		}
		// Set flat color
		gl.glColor4x(rgba[0], rgba[1], rgba[2], rgba[3]);
		if(NormalBuffer!=null)
		gl.glNormalPointer(GL10.GL_FLOAT,0,NormalBuffer);
		// Smooth color
		if (colorBuffer != null) {
			// Enable the color array buffer to be
			//used during rendering.
			gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
			gl.glColorPointer(4, GL10.GL_FIXED, 0, colorBuffer);
		}
		//gl.glLoadIdentity();
		gl.glTranslatef(x, y, z);
		gl.glRotatef(rx, 1, 0, 0);
		gl.glRotatef(ry, 0, 1, 0);
		gl.glRotatef(rz, 0, 0, 1);

		// Point out the where the color buffer is.
		if(indicesBuffer==null)
			gl.glDrawArrays(drawtype, 0, verticecount);
		else
		gl.glDrawElements(GL10.GL_TRIANGLES, numOfIndices,
						  GL10.GL_UNSIGNED_SHORT, indicesBuffer);
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glPopMatrix();
	}

	protected void setVertices(float[] vertices,boolean type) {
		// a float is 4 bytes, therefore
		//we multiply the number if
		// vertices with 4.
		ByteBuffer vbb= ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		verticesBuffer = vbb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);
		if(type){
		verticecount=vertices.length/3;
		float[] xl=new float[vertices.length];
		for(int i=0;i<vertices.length;i+=9)
		{
			float X1=vertices[i]-vertices[i+3],Y1=vertices[i+1]-vertices[i+4],Z1=vertices[i+2]-vertices[i+5],
				X2=vertices[i+3]-vertices[i+6],Y2=vertices[i+4]-vertices[i+7],Z2=vertices[i+5]-vertices[i+8];
			xl[i+6]=xl[i+3]=xl[i]=Y1*Z2-Y2*Z1;
			xl[i+7]=xl[i+4]=xl[i+1]=Z1*X2-Z2*X1;
			xl[i+8]=xl[i+5]=xl[i+2]=X1*Y2-X2*Y1;
		}
		setNormalPointer(xl);
		}
	}

	protected void setIndices(short[] indices) {
		// short is 2 bytes, therefore we multiply
		//the number if
		// vertices with 2.
		ByteBuffer ibb
			= ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indicesBuffer = ibb.asShortBuffer();
		indicesBuffer.put(indices);
		indicesBuffer.position(0);
		numOfIndices = indices.length;
	}

	protected void setColor(int red, int green,int blue, int alpha) {
		// Setting the flat color.
		rgba[0] = red;
		rgba[1] = green;
		rgba[2] = blue;
		rgba[3] = alpha;
	}

	protected void setColors(int[] colors) {
		// float has 4 bytes.
		ByteBuffer cbb
			= ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asIntBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}
	protected void setNormalPointer(float[] vertices) {
		// float has 4 bytes.
		ByteBuffer vbb= ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		NormalBuffer = vbb.asFloatBuffer();
		NormalBuffer.put(vertices);
		NormalBuffer.position(0);
	}
	
}
