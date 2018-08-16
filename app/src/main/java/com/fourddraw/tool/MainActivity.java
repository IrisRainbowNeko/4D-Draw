package com.fourddraw.tool;

import android.app.*;
import android.os.*;
import android.view.*;

public class MainActivity extends Activity 
{
	public static int fblx,fbly;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		WindowManager windowManager = getWindowManager();  
        Display display = windowManager.getDefaultDisplay();  
        fblx = display.getWidth();  
      	fbly = display.getHeight();
		
		setContentView(new mScreen(this));
		
		
    }
}
