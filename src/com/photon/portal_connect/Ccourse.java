package com.photon.portal_connect;

public class Ccourse {

	private String name;
	private String teacher;
	private int credit;
	private String number;
	private char Class;
	private int year;
	private int section;
	private String url;
	
	public void setname(String name){
		this.name = name;
	}
	public void setteacher(String teacher){
		this.teacher = teacher;
	}
	public void setcredit(int credit){
		this.credit = credit;
	}
	public void setnumber(String number){
		this.number = number;
	}
	public void setclass (char x){
		this.Class = x ;	
	}
	public void setyear (int year)
	{
		this.year = year;
	}
	public void setsection (int s)
	{
		this.section = s ;
	}
	public void seturl(String url){
		this.url = url;
	}
	
	//--
	
	public String getname()
	{
		return this.name;
	}
	public String getteacher()
	{
		return this.teacher;
	}
	public int getcredit()
	{
		return this.credit;
	}
	public String getnumber()
	{
		return this.number;
	}
	public char getclass (){
		return this.Class;	
	}
	public int getyear ()
	{
		return this.year;
	}
	public int getsection ()
	{
		return this.section ;
	}
	public String geturl(){
		return this.url ;
	}
}
