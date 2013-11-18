package com.bazanski.myrubyblog;

import java.util.ArrayList;

import android.os.Bundle;

public class ArticleItem {
	private String title;
	private String text;
	private String dateTime;
	private String author;
	private long authorID;
	private ArrayList<CommentItem> comments;
	private Bundle article_data;
	
	public ArticleItem(String title, String text, String dateTime, String author, ArrayList<CommentItem> all_comments, long authorID) {
		this.title = title;
		this.text = text;
		this.dateTime = dateTime;
		this.author = author;
		this.comments = all_comments;
		this.authorID = authorID;
		
		this.article_data = new Bundle(); 
		this.article_data.putString("title", title);
		this.article_data.putString("text", text);
		this.article_data.putString("dateTime", dateTime);
		this.article_data.putString("author", author);
		this.article_data.putString("comments", null);
		this.article_data.putLong("authorID", authorID);
	}
	
	public ArticleItem(Bundle article_data) {
		this.article_data = article_data;
		//тут зполнить все остальные поля
	}
	//============Setters==========
	
	//============Getters==========
	public Bundle getExtras() {
		return this.article_data;
	}
	
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
