package com.photon.portal_connect;

public class Cnews {
	private
		String date;
		String title;
		String text;
		String attachmentUrl;
		String publisher;
	public
		void setdate(String date){
			this.date = date;
		}
		void settext(String text){
			this.text = text;
		}
		void settitle(String title){
			this.title = title;
		}
		void setattachmentUrl(String url){
			this.attachmentUrl = url;
		}
		void setpublisher(String publisher){
			this.publisher = publisher;
		}
		String getdate()
		{
			return this.date;
		}
		String gettext()
		{
			return this.text;
		}
		String gettitle()
		{
			return this.title;
		}
		String getattachmentUrl()
		{
			return this.attachmentUrl;
		}
		String getpublisher()
		{
			return this.publisher;
		}
	
	
	
	
}
