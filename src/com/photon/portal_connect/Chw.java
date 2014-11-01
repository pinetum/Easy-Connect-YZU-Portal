package com.photon.portal_connect;

public class Chw {
	private
		String title;
		String name;
		String text;
		String limit;
		String attachmenturl;
		int score;
	public
		void settitle(String title){
			this.title = title;
		}
		void setname(String name){
			this.name = name;
		}
		void settext(String text){
			this.text = text;
		}
		void setlimit(String limit){
			this.limit = limit;
		}
		void setscore(int score){
			this.score = score;
		}
		void seturl(String attachmenturl){
			this.attachmenturl = attachmenturl;
		}
		
		String gettitle(){
			return this.title;
		}
		String getname(){
			return this.name ;
		}
		String gettext(){
			return this.text;
		}
		String getlimit(){
			return this.limit;
		}
		int getscore(){
			return this.score;
		}
		String geturl(){
			return this.attachmenturl;
		}
}
