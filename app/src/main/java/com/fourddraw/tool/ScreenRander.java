package com.fourddraw.tool;
import android.opengl.*;
import javax.microedition.khronos.opengles.*;
import javax.microedition.khronos.egl.EGLConfig;
import android.hardware.*;
import java.nio.*;
import android.graphics.*;
import android.util.*;
import java.io.*;
import android.os.*;

public class ScreenRander
{//界面渲染类
	
	FourDMesh fourDcube;
	float[] color_light = {1,1,1,1};
	float theta,fai=90,dist=2;
	public static final String SD = Environment.getExternalStorageDirectory().getPath().toString();
	
	
	public ScreenRander()
	{
		fourDcube=new FourDMesh(SD+"/4dscript.txt");
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig p2)
	{
		gl.glDisable(GL10.GL_DITHER);//关闭抗抖动
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,GL10.GL_FASTEST);//设置特定Hint项目的模式，这里为设置使用快速模式
		gl.glClearColor(0, 0, 0, 0);//设置屏幕背景色为黑色
		gl.glEnable(GL10.GL_DEPTH_TEST);//启用深度检测	
		
	}

	public void onSurfaceChanged(GL10 gl, int width, int height)
	{
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		float ratio=(float)width/height;
		gl.glFrustumf(-ratio, ratio, -1, 1, 1, 30);
	}

	public boolean onDrawFrame(GL10 gl)
	{
		try{
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);//清除颜色缓存和深度缓存
			
		gl.glDisable(GL10.GL_CULL_FACE);//设置为打开背面剪裁	
		gl.glShadeModel(GL10.GL_SMOOTH);//设置着色模型为平滑着色
		gl.glFrontFace(GL10.GL_CCW);//设置逆时针为正面
		gl.glMatrixMode(GL10.GL_MODELVIEW);//设置当前矩阵为模式矩阵
		gl.glLoadIdentity();//设置当前矩阵为单位矩阵
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);
		/*gl.glMultMatrixf(rotez(xyz[0]));
		gl.glMultMatrixf(rotey(xyz[1]));
		gl.glMultMatrixf(rotex(xyz[2]));*/
		GLU.gluLookAt(gl,dist*tool.sin(fai)*tool.cos(theta), dist*tool.sin(fai)*tool.sin(theta), dist*tool.cos(fai)//根据 球坐标->直角坐标 公式计算视点坐标
		, 0, 0, 0, 
		tool.sin(fai-90)*tool.cos(theta), tool.sin(fai-90)*tool.sin(theta), tool.cos(fai-90));
		/*gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0);
		
		int[] colors=toColorArray(Color.HSVToColor(color_light));
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, new float[]{0.6f * (colors[0] / 255f),0.6f * (colors[1] / 255f),0.6f * (colors[2] / 255f),1}, 0);
		gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_DIFFUSE,new float[]{1.2f*(colors[0]/255f),1.2f*(colors[1]/255f),1.2f*(colors[2]/255f),1},0);
		gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_SPECULAR,new float[]{2f*(colors[0]/255f),2f*(colors[1]/255f),2f*(colors[2]/255f),1},0);
		gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_POSITION,new float[]{1,1,1,1},0);
		gl.glLightfv(GL10.GL_LIGHT0,GL10.GL_QUADRATIC_ATTENUATION,new float[]{0.02f},0);*/
			
			//开启颜色追踪
			//gl.glEnable(gl.GL_COLOR_MATERIAL);
		fourDcube.update();
		fourDcube.draw(gl);
		for(int i=0;i<6;i++)fourDcube.rots[i]+=fourDcube.rot_speed[i];
		}catch(Exception e){System.exit(0);}
		return false;
	}
	public int[] totexarray(Bitmap[] bmp,GL10 gl)
	{
		int[] a=new int[bmp.length];
		gl.glGenTextures(a.length,a,0);
		for(int i=0;i<a.length;i++){
		gl.glBindTexture(GL10.GL_TEXTURE_2D,a[i]);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER,GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,GL10.GL_CLAMP_TO_EDGE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,GL10.GL_CLAMP_TO_EDGE);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0,GL10.GL_RGBA,bmp[i], 0);
		}
		return a;
	}
	public int totex(Bitmap bmp,GL10 gl){
		return totexarray(new Bitmap[]{bmp},gl)[0];
	}
	
	public int[] toColorArray(int color){
		return new int[]{(color>>16)&0xFF,(color>>8)&0xFF,color&0xFF};
	}
}

