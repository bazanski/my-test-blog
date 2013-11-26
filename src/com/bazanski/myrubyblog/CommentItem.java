package com.bazanski.myrubyblog;

public class CommentItem {
	private String commentText;
	private String commentAuthor;
	private String creationDateTime;
	private long commentAuthorID;
	private long commentID;
	//статья к которой оставлен
	
	public CommentItem(long commentID, String commentText, String commentAuthor, String creationDateTime, long commentAuthorID) {
		this.commentID = commentID;
		this.commentText = commentText;
		this.commentAuthor = commentAuthor;
		this.creationDateTime = creationDateTime;
		this.commentAuthorID = commentAuthorID;
	}
	
	//====SETTERS======
	
	//====GETTERS======
	public String getText() {
		return this.commentText;
	}
	
	public String getTime() {
		return this.creationDateTime;
	}
	
	public String getAuthor() {
		return this.commentAuthor;
	}
	
	public long getAuthorID() {
		return this.commentAuthorID;
	}
	
	public String getAuthorID_toString() {
		return String.valueOf(this.commentAuthorID);
	}
	
	public long getCommentID() {
		return this.commentID;
	}
	
	public String getCommentID_toString() {
		return String.valueOf(this.commentID);
	}
}
