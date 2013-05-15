package com.example.listviewtest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MyActivity extends Activity {
  private String currentpath = "/";//当前目录
	private ListView listview;//列表
	private TextView textview;//标题,显示目录
	private ListListen llisten;//列表监听
	private ArrayList<Map<String,Object>> al;//数据列表
	//private  ArrayList<Map<String,Object>> mData= new ArrayList<Map<String,Object>>();
	private String rootpath = "/";
	private SimpleAdapter simplelist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//初始化
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		llisten= new ListListen();
		al = new ArrayList<Map<String,Object>>();
		listview = (ListView)findViewById(R.id.listView1);
		
		getFileDir(rootpath);
		
		
		simplelist = new SimpleAdapter(this, al, R.layout.listlayout, new String[] {"title","icon"}, new int[] {R.id.title,R.id.img});
		listview.setOnItemClickListener(llisten);
		listview.setAdapter(simplelist);
		
		Log.v("sysout", "end");
		
	}

	public ArrayList getFileDir(String r){
		currentpath = r;
		al.clear();
		File file = new File(r);
		File[] files = file.listFiles();
		for(File f:files)
		{
			HashMap<String,Object> hm = new HashMap<String, Object>();
			String name = f.getName();
			 Log.v("File", (f.getName()+"     "+f.getPath()));
			hm.put("path", f.getPath());
			hm.put("title", name);
			hm.put("icon", f.isDirectory()?R.drawable.folder:
				name.endsWith(".pdf")?R.drawable.pdf:
				name.endsWith(".doc")?R.drawable.word:
				name.endsWith(".xls")?R.drawable.excel:
				R.drawable.file);
			hm.put("isdir",f.isDirectory()?"dir":"nodir");
			al.add(hm);
			
		}
		
		return al;
	}
	
	class ListListen implements AdapterView.OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			File f = new File(al.get(arg2).get("path").toString());
			if(f.canRead())
			{
				if(al.get(arg2).get("isdir").toString().equals("dir"))
				{
					getFileDir((String)al.get(arg2).get("path"));
					//textview.setText("当前目录为：" + currentpath);
					simplelist.notifyDataSetChanged();
				}
			}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(currentpath!="/")
			{
				File f = new File(currentpath); 
				getFileDir(f.getParent().toString());
				//textview.setText("当前目录为：" + currentpath);
				simplelist.notifyDataSetChanged();
			}
			else {
				finish();
			}
		return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
