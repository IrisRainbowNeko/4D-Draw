package com.fourddraw.tool;
import java.io.*;
import javax.microedition.khronos.opengles.*;

public class FourDMesh extends Mesh
{//4维物体
	//顶点坐标
	float[][] vertex={{-0.5f,-0.5f,-0.5f,-0.5f},{-0.5f,-0.5f,-0.5f,0.5f},{-0.5f,-0.5f,0.5f,-0.5f},{-0.5f,-0.5f,0.5f,0.5f},{-0.5f,0.5f,-0.5f,-0.5f},{-0.5f,0.5f,-0.5f,0.5f},{-0.5f,0.5f,0.5f,-0.5f},{-0.5f,0.5f,0.5f,0.5f},{0.5f,-0.5f,-0.5f,-0.5f},{0.5f,-0.5f,-0.5f,0.5f},{0.5f,-0.5f,0.5f,-0.5f},{0.5f,-0.5f,0.5f,0.5f},{0.5f,0.5f,-0.5f,-0.5f},{0.5f,0.5f,-0.5f,0.5f},{0.5f,0.5f,0.5f,-0.5f},{0.5f,0.5f,0.5f,0.5f}};
	//连接方式
	int[][] links={{2,3,5,9},{1,4,6,10},{1,4,7,11},{2,3,8,12},{1,6,7,13},{2,5,8,14},{3,5,8,15},{4,6,7,16},{1,10,11,13},{2,9,12,14},{3,9,12,15},{4,10,11,16},{5,9,14,15},{6,10,13,16},{7,11,13,16},{8,12,14,15}};
	//宽，高
	float w,h;
	//6个方向旋转角度
	float[] rots=new float[6];
	//旋转速度
	float[] rot_speed=new float[6];
	//用于opengl绘图的顶点数据
	float[] dd;
	
	public FourDMesh(){}
	public FourDMesh(float w,float h)
	{
		set(w,h);
	}
	public FourDMesh(String path){
		set(1,1);
		if(new File(path).exists())
		creatFromSD(path);
	}
	
	public void creatFromSD(String path){//从文件创建4维物体
		//清空物体信息
		vertex=null;
		links=null;
		rots=new float[6];
		rot_speed=new float[6];
		
		//读取文件
		String res=tool.cutstr_all(tool.readFileSdcard(path),"/*","*/");
		//获取顶点数据
		String[] vex_str=tool.getitem(res,"vertex","").split(";");
		vertex=new float[vex_str.length][];
		for(int i=0;i<vex_str.length;i++){
			vertex[i]=tool.str2float(vex_str[i].split(","));
		}
		//获取连接方式
		String[] link_str=tool.getitem(res,"link","").split(";");
		links=new int[link_str.length][];
		for(int i=0;i<link_str.length;i++){
			links[i]=tool.str2int(link_str[i].split(","));
		}
		//获取旋转速度(xow,yow,zow)
		rot_speed[0]=Float.parseFloat(tool.getkey(res,"xow","0"));
		rot_speed[1]=Float.parseFloat(tool.getkey(res,"yow","0"));
		rot_speed[2]=Float.parseFloat(tool.getkey(res,"zow","0"));
		
	}
	
	public float[] rotate(float[] pos,float[] angles){
		//各平面的旋转矩阵
		//xow,yow,zow,xoy,yoz,xoz
		float[][][] rotmat={
			{
				{cos(angles[0]),0,0,-sin(angles[0])},
				{0,1,0,0},
				{0,0,1,0},
				{sin(angles[0]),0,0,cos(angles[0])}
			},
			{
				{1,0,0,0},
				{0,cos(angles[1]),0,-sin(angles[1])},
				{0,0,1,0},
				{0,sin(angles[1]),0,cos(angles[1])}
			},
			{
				{1,0,0,0},
				{0,1,0,0},
				{0,0,cos(angles[2]),-sin(angles[2])},
				{0,0,sin(angles[2]),cos(angles[2])}
			},
			{
				{cos(angles[3]),-sin(angles[3]),0,0},
				{sin(angles[3]),cos(angles[3]),0,0},
				{0,0,1,0},
				{0,0,0,1}
			},
			{
				{1,0,0,0},
				{0,cos(angles[4]),-sin(angles[4]),0},
				{0,sin(angles[4]),cos(angles[4]),0},
				{0,0,0,1}
			},
			{
				{cos(angles[5]),0,-sin(angles[5]),0},
				{0,1,0,0},
				{sin(angles[5]),0,cos(angles[5]),0},
				{0,0,0,1}
			}};
		//计算旋转后点的坐标
		float[] res=pos.clone();
		for(int i=0;i<angles.length;i++)
		res=tool.matconv(res,rotmat[i]);
		return res;
	}
	
	public void set(float w,float h)
	{
		this.w=w;this.h=h;
		update();
		drawtype=GL10.GL_LINES;//绘图方式设置为每两个顶点连一条线
	}
	
	public void update()//刷新图像
	{
		dd=new float[vertex.length*3*tool.getlength(links)*2];//创建opengl绘图的顶点数组
		float[][] vex_res = new float[vertex.length][];
		//计算旋转后点的坐标
		for(int i=0;i<vertex.length;i++){
			vex_res[i]=rotate(vertex[i],rots);
			vex_res[i][0]/=(2-vex_res[i][3]);
			vex_res[i][1]/=(2-vex_res[i][3]);
			vex_res[i][2]/=(2-vex_res[i][3]);
		}

		int count=0;//记录已存放数据的个数

		//根据连接方式将顶点信息存入数组，两个顶点一组
		for(int i=0;i<links.length;i++)
		{
			for(int u=0;u<links[i].length;u++)
			{
				dd[count]=vex_res[i][0];
				dd[count+1]=vex_res[i][1];
				dd[count+2]=vex_res[i][2];

				dd[count+3]=vex_res[links[i][u]-1][0];
				dd[count+4]=vex_res[links[i][u]-1][1];
				dd[count+5]=vex_res[links[i][u]-1][2];
				count+=6;
			}
		}
		setVertices(dd,false);
		verticecount=dd.length/3;
	}
	
	public void setVertex(float[][] vex){
		vertex=vex;
	}
	public void setLinks(int[][] links){
		this.links=links;
	}
	
	private float cos(float angle){
		return (float)Math.cos(Math.toRadians(angle));
	}
	private float sin(float angle){
		return (float)Math.sin(Math.toRadians(angle));
	}
}
