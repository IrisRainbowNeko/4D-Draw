package com.fourddraw.tool;

import android.content.res.*;
import android.graphics.*;
import android.os.*;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class tool
{//工具类
	
	public static String readFileSdcard(String dir) {
		String res = null;
		try {
			//String dir = Environment.getExternalStorageDirectory().getPath().toString()+"/7exfsz/jl.txt";
			FileInputStream fin = new FileInputStream(dir);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = new String(buffer);
			fin.close();
		}
		catch (Exception e){}
		return res;
	}
	
	public static float[] matconv(float[] vec,float[][] mat){//计算矩阵和向量的乘积
		float[] res=new float[vec.length];
		for(int i=0;i<mat[0].length;i++){
			for(int u=0;u<vec.length;u++){
				res[i]+=(vec[u]*mat[u][i]);
			}
		}
		return res;
	}
	public static FloatBuffer tofloatbuff(float[] vertices) {
		ByteBuffer vbb= ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer verticesBuffer = vbb.asFloatBuffer();
		verticesBuffer.put(vertices);
		verticesBuffer.position(0);
		return verticesBuffer;
	}
	public static FloatBuffer tofloatbuff(float[] vertices,int cishu) {
		ByteBuffer vbb= ByteBuffer.allocateDirect(vertices.length * 4*cishu);
		vbb.order(ByteOrder.nativeOrder());
		FloatBuffer verticesBuffer = vbb.asFloatBuffer();
		for(int i=0;i<cishu;i++)
			verticesBuffer.put(vertices);
		verticesBuffer.position(0);
		return verticesBuffer;
	}
	
	public static int getlength(int[][] arr){
		int len=0;
		for(int i=0;i<arr.length;i++){
			len+=arr[i].length;
		}
		return len;
	}
	
	public static String cutstr_all(String str,String a,String b){
		while(str.indexOf(a)!=-1){
			str=cutstr(str,a,b,0);
		}
		return str;
	}
	public static String cutstr(String str,String a,String b,int i){
		int p=str.indexOf(a,i);
		if(p==-1)return str;
		return str.substring(0,p)+str.substring(str.indexOf(b,p)+b.length());
	}
	public static String getsubstr(String str,String a,String b,int i){
		int p=str.indexOf(a,i);
		if(p==-1)return "";
		p+=a.length();
		return str.substring(p,str.indexOf(b,p));
	}
	public static String getkey(String str,String skey,String notfind){
		if(str.indexOf(skey)==-1)return notfind;
		return getsubstr(str,skey+"=",";",0);
	}
	public static String getitem(String str,String skey,String notfind){
		if(str.indexOf(skey)==-1)return notfind;
		return getsubstr(str,skey+"[","]",0);
	}
	public static int[] str2int(String[] a){
		int[] b=new int[a.length];
		for(int i=0;i<b.length;i++){
			try{
				b[i]=Integer.parseInt(a[i]);
			}catch(Exception e){}
		}
		return b;
	}
	public static float[] str2float(String[] a){
		float[] b=new float[a.length];
		for(int i=0;i<b.length;i++){
			try{
				b[i]=Float.parseFloat(a[i]);
			}catch(Exception e){}
		}
		return b;
	}
	
	public static float cos(float angle){
		return (float)Math.cos(Math.toRadians(angle));
	}
	public static float sin(float angle){
		return (float)Math.sin(Math.toRadians(angle));
	}
}
