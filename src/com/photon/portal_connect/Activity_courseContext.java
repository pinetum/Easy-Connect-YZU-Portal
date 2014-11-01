package com.photon.portal_connect;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


public class Activity_courseContext extends FragmentActivity {
	
	
	ViewPager mViewPager;
	SectionsPagerAdapter mSectionsPagerAdapter;

	int nNews = 1;
	int nHomework = 2;
	int nTeachFile = 3;
	
	private static Activity_courseContext _ccinstance;
	static SimpleAdapter aNews;
	static SimpleAdapter aHw;
	static SimpleAdapter aFile;
	static String coursename=""; 
	public static Activity_courseContext get() {
        return _ccinstance;
    }
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.course_context);
		
		_ccinstance = this;
    
		if(getIntent().getExtras().getBoolean("logout"))
		{
			Intent intent = new Intent(this, Activity_courseTable.class);
			startActivityForResult(intent, 0);
			this.finish();
		}
		setTitle(getIntent().getExtras().getString("Cname"));
		coursename =getIntent().getExtras().getString("Cname");
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
	}
	
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}
		//傳回數量
		@Override
		public int getCount() {
				return 3;
		}
		//	標題
		@Override
		public CharSequence getPageTitle(int position) {
			//Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "最新消息";
			case 1:
				return "作業清單";
			case 2:
				return "教材下載";
			}
			return null;
		}
		
		
		
	}
	
	//頁面
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,container, false);

			
			ListView context_list =(ListView) rootView.findViewById(R.id.context_list);
			
			
			
			switch (getArguments().getInt(ARG_SECTION_NUMBER)){
			case 1:
				
				final Cnews allnews[]= constructaNews();
				context_list.setAdapter(aNews);
				
				
				context_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
					@Override
		        	public void onItemClick(@SuppressWarnings("rawtypes") AdapterView arg0, View arg1, int arg2,long arg3) {
						if (allnews==null)
						{
							
						}
						else
						{
							ccopenDialog(allnews[(int) arg3+1].gettitle(),
									allnews[(int) arg3+1].gettext(),
									allnews[(int) arg3+1].getattachmentUrl(),
									allnews[(int) arg3+1].getdate());
						}
						
		        	}
		        });
				
				
					
					
				
				
				

				break;
			case 2:
				
				 
				final Chw allhw[]=constructaHw();
				context_list.setAdapter(aHw);
				context_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
		        	 @Override
		        	 public void onItemClick(AdapterView arg0, View arg1, int arg2,long arg3) {
		        			//ccopenDialog("",Activity_courseTable.alldata[2],"null");
		        		
		        		 if(allhw!=null)
		        		 {
		        			 ccopenDialog(allhw[(int) arg3+1].gettitle(),
		        					allhw[(int) arg3+1].gettext(),
		        					"null",
		        					allhw[(int) arg3+1].getlimit());
		        		 }
		        		 
		        	 }
		        
		        });
				
				break;
			case 3:
				
				
				final Cmateria allfile[] = constructaFile();
				context_list.setAdapter(aFile);
				context_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
		        	 @Override
		        	 public void onItemClick(AdapterView arg0, View arg1, int arg2,long arg3) {
		        		 if (allfile==null)
						{
								
						}
						else
						{
								ccopenDialog(allfile[(int) arg3].getTitle(),
										allfile[(int) arg3].getSubTitle(),
										allfile[(int) arg3].getUrl(),
										allfile[(int) arg3].getDate());
						}

		        	 }
		        
		        });
				
				break;
			}
			
			return rootView;
		}
	}
	

	//---News
	public static Cnews[] constructaNews() {
		
		ArrayList<HashMap<String,String>> myNews = new ArrayList<HashMap<String,String>>();
		aNews =  new SimpleAdapter(get(),myNews ,
	            R.layout.row_listview,
	            new String[] { "TITLE", "SUBTITLE","DATE" },
	            new int[] { R.id.txtTitle, R.id.txtDesc ,R.id.txtDate}
	    );
		if (Activity_courseTable.alldata[1].indexOf("尚無最新消息！")!=-1)
		{
			HashMap<String,String> item = new HashMap<String,String>();
			item.put("TITLE","尚無最新消息！");
			item.put("SUBTITLE","尚無最新消息！");
			item.put("DATE","");
			myNews.add(item);
			return null;
		}
		else 
		{
			
			String all[] = Activity_courseTable.alldata[1].split("<span class='news_up'");
			Cnews[] mnews =new Cnews[all.length+1];
			
			for(int i=1 ; i<all.length ; i++)
			{
				HashMap<String,String> item = new HashMap<String,String>();
				mnews[i]=new Cnews();
				String mtitt[]=all[i].split("plus.gif'>");
				String title[]=mtitt[1].split("<");
				
				String msub[]=all[i].split("<FONT size='2'>");
				String subtitle[]=msub[1].split("</Font>");
				subtitle[0]=subtitle[0].replaceAll("<br>", "\n");
				
				String key="";
				if(i%2==1)
				{
					key="<td class='record2' valign=\"top\">";
				}
				else
				{
					key="<td class='hi_line' valign=\"top\">";
				}
				
				String mdate[]=all[i-1].split(key);
				
				String kdate[]=mdate[1].split(" ");
				kdate[0]=kdate[0].replace("\n", "z");
				String date[]=kdate[0].split("z");
				
				String mpub[] = all[i].split("'80' valign='top'>");
				String pub[] = mpub[2].split("<");
				
				if(all[i].indexOf("href")!=-1)
				{
					String opop[] = all[i].split("href");
					String ujuj[] = opop[1].split("'>");
					String furl[] = ujuj[0].split("/");
					String urlal="https://portal.yzu.edu.tw/VC2/"+furl[2];
					urlal="https://portal.yzu.edu.tw/VC2/File_DownLoad.aspx?file_Name=Test1.rar&id=36286";
					mnews[i].setattachmentUrl(urlal);
					
				}
				else
				{
					mnews[i].setattachmentUrl("null");
				}
				
				mnews[i].settitle(title[0]);
				mnews[i].settext(subtitle[0]);
				mnews[i].setdate(date[1]);
				mnews[i].setpublisher(pub[0]);
				
				String attachment="";
				if (mnews[i].getattachmentUrl()!="null")
				{
					attachment="       有附檔";
				}
				
				item.put("TITLE",mnews[i].gettitle());
				item.put("SUBTITLE",mnews[i].gettext().replace("\n", ""));
				item.put("DATE",mnews[i].getpublisher()+"  "+mnews[i].getdate()+attachment);
				myNews.add(item);
				
				
				
				
			}
			return mnews;
		}
		
		 
		
		
	}
	//---HW
	public static Chw[] constructaHw() {
		ArrayList<HashMap<String,String>> myHW = new ArrayList<HashMap<String,String>>();
		aHw =  new SimpleAdapter(get(),myHW ,
	            R.layout.row_listview,
	            new String[] { "TITLE", "SUBTITLE","DATE" },
	            new int[] { R.id.txtTitle, R.id.txtDesc ,R.id.txtDate}
	    );
		
		if(Activity_courseTable.alldata[2].indexOf("<tr class=\"hi_line\">")==-1)
		{
			HashMap<String,String> item = new HashMap<String,String>();
			item.put("TITLE","尚無作業！");
			item.put("SUBTITLE","尚無作業！");
			item.put("DATE","");
			myHW.add(item);
			return null;
		}
		else
		{
			

			String mallhw[] = Activity_courseTable.alldata[2].split("color=\"Blue\">");
			
			Chw[] hwall =new Chw[mallhw.length];
			
			
			for(int r=0;r<mallhw.length-1;r++)
			{
				hwall[r+1]=new Chw();
				String title[]=mallhw[r+1].split("<");
				String xsubtitle[]=mallhw[r+1].split("width=\"280\">");
				String subtitle[]=xsubtitle[1].split("<");
				
				//String score[]=mallhw[r+1].split("width=\"30\">");
				String key="";
				if(mallhw[r+1].indexOf("color=\"Red\">")!=-1)
				{
					key="color=\"Red\">";
				}
				else
				{
					key="width=\"60\">";
				}
				String xdate[]=mallhw[r+1].split(key);
				String date[]=xdate[1].split("<");
				
				hwall[r+1].settext(subtitle[0]);
				hwall[r+1].settitle(title[0]);
				hwall[r+1].setlimit(date[0]);
				HashMap<String,String> item = new HashMap<String,String>();
				item.put("TITLE",title[0]);
				item.put("SUBTITLE",subtitle[0]);
				item.put("DATE","截止日期:"+date[0]);
				myHW.add(item);
				
			}
				
			return hwall;
		}
			
		
		
	}
	//---Materia
	public static Cmateria[] constructaFile() {
		ArrayList<HashMap<String,String>> myFile = new ArrayList<HashMap<String,String>>();
		aFile =  new SimpleAdapter(get(),myFile ,
	            R.layout.row_listview,
	            new String[] { "TITLE", "SUBTITLE","DATE" },
	            new int[] { R.id.txtTitle, R.id.txtDesc ,R.id.txtDate}
	    );

		
		
		if(Activity_courseTable.alldata[3].indexOf("未上傳教材")!=-1)
		{
			HashMap<String,String> item = new HashMap<String,String>();
			item.put("TITLE","尚未上傳教材");
			
			item.put("SUBTITLE","尚未上傳教材");
			item.put("DATE","");
			myFile.add(item);
			return null;
		}
		else
		{
			
			String key,skey;
			String tii[] = Activity_courseTable.alldata[3].split("<tr>");
			
			Cmateria[] mmatria =new Cmateria[tii.length-7];
			
			
			for(int r=0;r<=tii.length-8;r++)
			{
				mmatria[r]=new Cmateria();
				if(r%2==0)
				{
					key = "<td class='record2'>";
					skey = "class='record2'>";
				}
				else
				{
					key = "<td class='hi_line'>";
					skey = "class='hi_line'>";
				}
				String nam[] = tii[r+7].split(key);
				String title[] = nam[1].split("<");
				String sub[] = nam[1].split(skey);
				String subs[] = sub[0].replace("\n", "").split("<");
				String date[] = sub[2].split("<");
				String urll[] = nam[1].split("../../");
				String url[]  = urll[1].split("'");
				mmatria[r].setUrl("https://portal.yzu.edu.tw/VC2/"+url[0]);
				mmatria[r].setSubTitle(subs[0].replace(" ", ""));
				mmatria[r].setTitle(title[0]);
				mmatria[r].setDate(date[0]);
				HashMap<String,String> item = new HashMap<String,String>();
				item.put("TITLE",mmatria[r].getTitle());
				item.put("SUBTITLE",mmatria[r].getSubTitle());
				item.put("DATE",mmatria[r].getDate());
				myFile.add(item);
			}
			return mmatria;
		}
		
	}
	
	
	
	
	//---對話框
	private static void ccopenDialog(final String title , final String state, final String url, final String date) {
		if(url=="null")
		{
			new AlertDialog.Builder(Activity_courseContext.get())
			.setTitle(title)
			.setMessage(state)
			.setPositiveButton("確定",null)
			.setNegativeButton("加入行事曆", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) {
		        	 
		        	 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		        	 Date d = null;
		        	 long milliseconds = System.currentTimeMillis();
					try {
						//char[] array = date.toCharArray();
						//array[10]='z';
						//String datess[] = array.toString().split("z");
						
						String thedate=date.replaceAll("/", "-");
						d = (Date)f.parse(thedate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					milliseconds= d.getTime();
		        	 
		        	 
		        	 Intent intent = new Intent(Intent.ACTION_EDIT);
		             intent.setType("vnd.android.cursor.item/event");
		             intent.putExtra("title",coursename+"-"+title);
		             intent.putExtra("description", state);
		             intent.putExtra("beginTime", milliseconds);
		             //intent.putExtra(CalendarIntent.EVENT_END_TIME, getEndTimeInMillis());
		             intent.putExtra("allDay", true);
		             //intent.putExtra(CalendarIntent.EVENT_LOCATION, getLocation());
		             Activity_courseContext.get().startActivity(intent);
		          }})
			.setCancelable(false)
			.show();
		}
		else
		{
			new AlertDialog.Builder(Activity_courseContext.get())
			.setTitle(title)
			.setMessage(state)
			.setPositiveButton("確定",null)
			.setNeutralButton("加入行事曆", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) {
		        	 
		        	 SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		        	 Date d = null;
		        	 long milliseconds = System.currentTimeMillis();
					try {
						//char[] array = date.toCharArray();
						//array[10]='z';
						//String datess[] = array.toString().split("z");
						
						String thedate=date.replaceAll("/", "-");
						d = (Date)f.parse(thedate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					milliseconds= d.getTime();
		        	 
		        	 
		        	 Intent intent = new Intent(Intent.ACTION_EDIT);
		             intent.setType("vnd.android.cursor.item/event");
		             intent.putExtra("title",coursename+"-"+title);
		             intent.putExtra("description", state);
		             intent.putExtra("beginTime", milliseconds);
		             //intent.putExtra(CalendarIntent.EVENT_END_TIME, getEndTimeInMillis());
		             intent.putExtra("allDay", true);
		             //intent.putExtra(CalendarIntent.EVENT_LOCATION, getLocation());
		             Activity_courseContext.get().startActivity(intent);
		          }})
			.setNegativeButton("下載附檔", new DialogInterface.OnClickListener() {
		         public void onClick(DialogInterface dialog, int which) {

		        	download_attachment(url,coursename);
		          }

				private void download_attachment(final String url, final String coursename) {
					// TODO Auto-generated method stub
					
					
					File sdroot;
					File approot;
					final File courseroot;
					String fnam[]=url.split("=");
					String filename[]=fnam[1].split("&");
					final String sFileName = filename[0];
					
					
					sdroot=Environment.getExternalStorageDirectory();
					approot = new File(sdroot,"portal");
					courseroot = new File(approot,coursename);
					if(!approot.exists())
						approot.mkdirs();
					if(!courseroot.exists())
						courseroot.mkdirs();
					//----SD卡建立資料夾完成
					
					
					
					
					final ProgressDialog pd = ProgressDialog.show(Activity_courseContext.get(),"讀取中","請稍後");
					
					StringRequest req = new StringRequest(Method.GET, url, null, new Response.Listener<String>() {
					    @Override
					    public void onResponse(String response) {
					    	 pd.dismiss();
					     
					        FileOutputStream fs;
					        try {
								fs=new FileOutputStream(new File(courseroot,sFileName));
								try {
									fs.write(response.getBytes());
									fs.flush();
									fs.close();
								} catch (IOException e) {
									
									e.printStackTrace();
								}
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
					        pd.dismiss();

			                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("file:/"+courseroot+"/"+sFileName));
			                Activity_courseContext.get().startActivity(intent);
							
					    }
					    
					}, new Response.ErrorListener() {
					    @Override
					    public void onErrorResponse(VolleyError error) {
					        VolleyLog.e("Error: ", error.getMessage());
					        pd.dismiss();
					        new AlertDialog.Builder(Activity_courseContext.get())
					        .setTitle("下載錯誤")
							.setMessage(error.getMessage())
							.setPositiveButton("確定",null)
							.show();
					        
					    }
					});

					 
					// add it to the RequestQueue  
					
					Activity_courseTable._requestQueue.add(req);
					
					
				}
		         
		         
				
		    }
			
					
					)
			.setCancelable(false)
			.show();
			
		}
		
		
	}


	
	//返回按鈕

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK) {
	    	Activity_courseTable.alldata[1]="";
	    	Activity_courseTable.alldata[2]="";
	    	Activity_courseTable.alldata[3]="";
	    	this.finish();
	        return true;
	    } 
	    return super.onKeyDown(keyCode, event);
	}

	
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

