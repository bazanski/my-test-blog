package com.bazanski.myrubyblog;

import java.util.ArrayList;

public class ArticleItem {
	private String title;
	private String text;
	private String dateTime;
	private String author;
	private long authorID;
	private ArrayList<CommentItem> comments;
	
	public ArticleItem(String title, String text, String dateTime, String author, ArrayList<CommentItem> all_comments, long authorID) {
		this.title = title;
		this.text = text;
		this.dateTime = dateTime;
		this.author = author;
		this.comments = all_comments;
		this.authorID = authorID;
	}
	//============Setters==========
	
	//============Getters==========
	public String getText() {
		return this.text;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDateTime() {
		return this.dateTime;
	}
	
	public String getAuthor() {
		return this.author;
	}
	
	public long getAuthorID() {
		return this.authorID;
	}
	
	public ArrayList<CommentItem> getComments() {
		return this.comments;
	}
}
