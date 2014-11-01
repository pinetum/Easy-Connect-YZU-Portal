package com.photon.portal_connect;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.Volley;

import com.photon.portal_connect.StringRequest;

import android.R.menu;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
//import android.view.Menu;
//import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Activity_courseTable extends Activity {
	
	
	//  1010329  
	//	CN2A
	//  s1010329@mail.yzu.edu.tw
	//  UTF-8
	//  這個軟體的性能有待加強
	// 	假若學校伺服器提供JSON資料讀取並搭配volley則此軟體性能將能大幅提升
	//  
	//	
	//
	
	private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "ASP.NET_SessionId";
    private SharedPreferences _preferences; 
    private static Activity_courseTable _instance;
    public static  RequestQueue _requestQueue;
    private ProgressDialog pd;
	private CheckBox autologin;
    private EditText username;
    private EditText password;
	private ListView courseList;
	private boolean loginsuccess;
	
	
	SimpleAdapter courseAdapter;
	public static String[] alldata=new String[4];
	
	static int count=0;
	static String selectC;
	public static Activity_courseTable get() {
        return _instance;
    }
    public RequestQueue getRequestQueue() {
        return _requestQueue;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_instance = this;
		_preferences = PreferenceManager.getDefaultSharedPreferences(this);
		_requestQueue = Volley.newRequestQueue(this);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);//不要標題列
		setContentView(R.layout.loginpage);
        password = (EditText) findViewById(R.id.input_password);
        username = (EditText) findViewById(R.id.input_username);
        autologin = (CheckBox) findViewById(R.id.autologin);
        loginsuccess=false;
        if(_preferences.getBoolean("auto", false))
        {
        	mPost(_preferences.getString("username", ""),_preferences.getString("password", ""));
        	username.setText(_preferences.getString("username", ""));
        	password.setText(_preferences.getString("password", ""));
        }

        
        
        ((Button) findViewById(R.id.submit)).setOnClickListener(new OnClickListener() {
			public void onClick(View V) {;				
				mPost(username.getText().toString(),password.getText().toString());
				//記住此帳號密碼(若有勾選)
				measureLoginauto(username.getText().toString(),password.getText().toString());	
			}
        });
       
	}

	protected void setcourseTable(String response) {
		setContentView(R.layout.course_table);
		//getMenuInflater().inflate(R.menu.course_table, inmenu);
		setTitle(R.string.all_course_list);
		loginsuccess=true;
		courseList =  (ListView) findViewById (R.id.course_list);
		
		
		String[] mmm =response.split("<a class=\"left_menu\" title=\"");
		int k = mmm.length;
		final Ccourse[] allcourse=new Ccourse[k];
		
		
		
		ArrayList<HashMap<String,String>> myCourse = new ArrayList<HashMap<String,String>>();
		courseAdapter =  new SimpleAdapter(this,myCourse ,
                android.R.layout.simple_list_item_2,
                new String[] { "TITLE", "SUBTITLE" },
                new int[] { android.R.id.text1, android.R.id.text2 }
        );
		
		
		
		 
		
		
		for(int i=0;i<k-1;i++)//一次跑一個課程
		{
			allcourse[i] = new Ccourse() ;
			String[] ll = mmm[i+1].split("】");
			String[] name = ll[0].split("【");
			String[] tea = ll[2].split("【");
			String[] kk = ll[4].split("\"");
			String[] nn = kk[1].split("\""); 
			String[] url = nn[0].split("'"); 
			String[] nu = url[1].split("id=");
			String[] num = nu[1].split("&");
			
			allcourse[i].setname(name[1]);
			allcourse[i].setteacher(tea[1]);
			allcourse[i].setnumber(num[0]);
			allcourse[i].seturl("https://portal.yzu.edu.tw/VC2/Student/"+url[1]);
			
			HashMap<String,String> item = new HashMap<String,String>();
			item.put("TITLE",allcourse[i].getname());
            item.put("SUBTITLE",allcourse[i].getnumber()+"  "+allcourse[i].getteacher());
            myCourse.add(item);
		}
		
		
		courseList.setAdapter(courseAdapter);
		
		courseList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        	 @Override
        	 public void onItemClick(AdapterView arg0, View arg1, int arg2,long arg3) {
        		//ListView colist = (ListView) arg0;
        		//Toast.makeText(_instance, "ID：" + arg3 +"選取課程："+ colist.getItemAtPosition(arg2).toString(),Toast.LENGTH_LONG).show();
        		//mgetAllData(allcourse[(int) arg3].geturl());
        		 
        		 pd = ProgressDialog.show(_instance,"讀取中","請稍後");
        		
        		 mAlldataget(allcourse[(int) arg3].geturl(),allcourse[(int) arg3].getname());
        		 
        		
        	 	}
        
        });
        
		
	}
	
	private void mAlldataget(String url,String courseName) {
		
		selectC=courseName;
		final String news="https://portal.yzu.edu.tw/VC2/Student/board/showboard_S.aspx";
		final String file="https://portal.yzu.edu.tw/VC2/Student/Materials/Materials_S.aspx";
		final String hw="https://portal.yzu.edu.tw/VC2/Student/Homework/HomeWork_S.aspx";
		StringRequest reqC = new StringRequest(Method.GET, url , null, new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	count++;
		    	if(count==4)
		    	{
		    		count=0;
		    		alldata[3]=response;
		    		Intent intent = new Intent(_instance, Activity_courseContext.class);
		    		intent.putExtra("logout",false);
		    		intent.putExtra("Cname",selectC);
		    		startActivityForResult(intent, 0);	
		    		pd.dismiss();
		    	}
		    	else if (count==1)
		    	{
		    		mAlldataget(news, selectC);
		    	}
		    	else if (count==2)
		    	{
		    		alldata[1]=response;
		    		mAlldataget(hw, selectC);
		    	}
		    	else if (count==3)
		    	{
		    		alldata[2]=response;
		    		mAlldataget(file, selectC);
		    	}
		    	
		    }
		    
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	openDialog("讀取錯誤"+count);
		    }
		});

		_requestQueue.add(reqC);
		
	}
	
	protected void logout() {
		Editor prefEditor = _preferences.edit();
        prefEditor.putBoolean("auto", false);
        prefEditor.commit();
		Intent intent = new Intent(_instance, Activity_courseContext.class);
		intent.putExtra("logout",true);
		intent.putExtra("url","");
		intent.putExtra("Cname","");
		startActivityForResult(intent, 0);	
		_instance.finish();
	}
	protected void mPost(String u , String p) {
		
		final String urlpost = "https://portal.yzu.edu.tw/logincheck_new.aspx?lang=tw";
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", u);
		params.put("pwd", p);
		params.put("ck_fast", "on");
		params.put("ok", "登入");
		
		pd = ProgressDialog.show(_instance,"登入中","請稍後");
		StringRequest myReq = new StringRequest(Method.POST,urlpost,params, new Response.Listener<String>(){
			        @Override
			        public void onResponse(String response) {
			        	if(response.indexOf("index.aspx")!=-1)
			        	{
			        		pd.dismiss();
			        		
			        		mGet("https://portal.yzu.edu.tw/VC2/Login_student.aspx?account="+username.getText().toString()+"&sid="+_preferences.getString(SESSION_COOKIE, "")+"&Language=TW");
			        		
			        		
			        	}
			        	else
			        	{
			        		pd.dismiss();
			        		openDialog("帳號或密碼錯誤");
			        		
			        	}
			        	
			        }
			    },new Response.ErrorListener()
			    {
			         @Override
			         public void onErrorResponse(VolleyError error) {
			             // error
			        	pd.dismiss();
			        	openDialog("請檢查網路連線後重試");
			            VolleyLog.e("Error: ", error.getMessage());
			       }
			    }
			)  {

			
			
		};
		_requestQueue.add(myReq);
		
		
	}
	protected void measureLoginauto(String string, String string2) {
		if(autologin.isChecked())
		{
			Editor prefEditor = _preferences.edit();
            prefEditor.putString("username", username.getText().toString());
            prefEditor.putString("password", password.getText().toString());
            prefEditor.putBoolean("auto", true);
            prefEditor.commit();
			
		}
	}
	protected void mGet(String url) {
		pd = ProgressDialog.show(_instance,"讀取中","請稍後");
		StringRequest req0 = new StringRequest(Method.GET, url, null, new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) 
		    {	
		    	
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	openDialog("請檢查網路連線後重試");
		    }
		});
		
	
		StringRequest req = new StringRequest(Method.GET, "https://portal.yzu.edu.tw/VC2/Student/classLeft_S.aspx", null, new Response.Listener<String>() {
		    @Override
		    public void onResponse(String response) {
		    	if(response.indexOf("元智大學虛擬教室")!=-1 && response.indexOf("】")!=-1)
	        	{
		    		setcourseTable(response);
	        	}
		    	else
		    		openDialog("登入失敗請重新嘗試");
		    	pd.dismiss(); 
		    }
		}, new Response.ErrorListener() {
		    @Override
		    public void onErrorResponse(VolleyError error) {
		    	openDialog("請檢查網路連線後重試");
		    	pd.dismiss();
		    }
		});

		 
		// add it to the RequestQueue  
		_requestQueue.add(req0);
		_requestQueue.add(req);
		//pd.dismiss();
		
		
	}
	private void openDialog(String state) {
		new AlertDialog.Builder(this)
			.setTitle("錯誤")
			.setMessage(state)
			.setPositiveButton("確定",null)
			.setCancelable(false)
			.show();
	}
	
	
	
	
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        MenuInflater inflater = getMenuInflater();
		 	getMenuInflater().inflate(R.menu.course_table, menu);
	        return true;
	 }
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.action_logout:
			logout();
			break;
		case R.id.action_about:	
			new AlertDialog.Builder(this)
			.setTitle("關於")
			.setMessage("開發者:林冠廷\nsatimsa@gmail.com\n元智大學通訊2A\n2013/10")
			.setPositiveButton("確定",null)
			.setCancelable(false)
			.show();
			break;
		}
		
		return true;
    }
    @Override   
    public boolean onPrepareOptionsMenu(Menu menu) {   
       
    	if(loginsuccess)
    	{
    		MenuItem logout = menu.findItem(R.id.action_logout);   
    		logout.setVisible(true);
    	}
    	else
    	{
    		MenuItem logout = menu.findItem(R.id.action_logout);   
    		logout.setVisible(false);
    	}
        
       
        return super.onPrepareOptionsMenu(menu);   
    }  
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public final void checkSessionCookie(Map<String, String> headers) {
        if (headers.containsKey(SET_COOKIE_KEY)
                && headers.get(SET_COOKIE_KEY).startsWith(SESSION_COOKIE)) {
                String cookie = headers.get(SET_COOKIE_KEY);
                if (cookie.length() > 0) {
                    String[] splitCookie = cookie.split(";");
                    String[] splitSessionId = splitCookie[0].split("=");
                    cookie = splitSessionId[1];
                    Editor prefEditor = _preferences.edit();
                    prefEditor.putString(SESSION_COOKIE, cookie);
                    prefEditor.commit();
                    
                }
            }
      
    }
	public final void addSessionCookie(Map<String, String> headers) {    
		String sessionId = _preferences.getString(SESSION_COOKIE, "");
        if (sessionId.length() > 0) {
            StringBuilder builder = new StringBuilder();
            builder.append(SESSION_COOKIE);
            builder.append("=");
            builder.append(sessionId);
            if (headers.containsKey(COOKIE_KEY)) {
                builder.append("; ");
                builder.append(headers.get(COOKIE_KEY));
            }
            
            headers.put(COOKIE_KEY, builder.toString());
        }
    }
	
	
	
	

	
}























