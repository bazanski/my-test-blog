package com.bazanski.myrubyblog;

public class CommentItem {
	private String commentText;
	private String commentAuthor;
	private String creationDateTime;
	private long commentAuthorID;
	//������ � ������� ��������
	
	public CommentItem(String commentText, String commentAuthor, String creationDateTime, long commentAuthorID) {
		this.commentText = commentText;
		this.commentAuthor = commentAuthor;
		this.creationDateTime = creationDateTime;
		this.commentAuthorID = commentAuthorID;
	}
	
}
