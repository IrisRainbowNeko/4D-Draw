package com.fourddraw.tool;
import android.opengl.*;
import android.content.*;
import android.view.*;
import javax.microedition.khronos.opengles.*;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.egl.EGLConfig;
import android.media.*;
import android.util.*;

public class mScreen extends GLSurfaceView
{//游戏界面
	ScreenRander sr;
	float X,Y;
	float lfai,ltheta;
	
	public mScreen(Context context)
	{
		super(context);
		//sp.setVolume(u1,0,0);
		sr=new ScreenRander();
		setRenderer(new GLSurfaceView.Renderer(){

				@Override
				public void onSurfaceCreated(GL10 p1, EGLConfig p2)
				{
					sr.onSurfaceCreated(p1,p2);
				}

				@Override
				public void onSurfaceChanged(GL10 p1, int p2, int p3)
				{
					sr.onSurfaceChanged(p1,p2,p3);
				}

				@Override
				public void onDrawFrame(GL10 p1)
				{
					sr.onDrawFrame(p1);
				}
			});
		
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:{
				X=getX(event);
				Y=getY(event);
				ltheta=sr.theta;
				lfai=sr.fai;
			}break;
			case MotionEvent.ACTION_MOVE:{
				float dx=getX(event)-X,
					  dy=getY(event)-Y;
				sr.fai=lfai-(dy/6);
				sr.theta=ltheta-(dx/3);
				sr.fai=Math.min(Math.max(sr.fai,0),180);
				sr.theta%=360;
			}break;
			case MotionEvent.ACTION_UP:{
				float dx=getX(event)-X,
					  dy=getY(event)-Y;
				sr.fai=lfai-(dy/6);
				sr.theta=ltheta-(dx/3);
				sr.fai=Math.min(Math.max(sr.fai,0),180);
				sr.theta%=360;
			}break;
		}
		return true;
	}
	
	public float getX(MotionEvent event){
		return event.getX()*(1080/MainActivity.fblx);
	}
	public float getY(MotionEvent event){
		return event.getY()*(1920/MainActivity.fbly);
	}
	
}
