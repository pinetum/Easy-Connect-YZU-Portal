package com.photon.portal_connect;

public class Cmateria {

	private String title;
	private String subtitle;
	private String date;
	private String url;
	
	public void setTitle(String title)
	{
		this.title=title;
	}
	public void setSubTitle(String subtitle)
	{
		this.subtitle=subtitle;
	}
	public void setDate(String date)
	{
		this.date=date;
	}
	public void setUrl(String url)
	{
		this.url=url;
	}
	
	
	public String getTitle()
	{
		return this.title;
	}
	public String getDate()
	{
		return this.date;
	}
	public String getUrl()
	{
		return this.url;
	}
	public String getSubTitle()
	{
		return this.subtitle;
	}
		
}
