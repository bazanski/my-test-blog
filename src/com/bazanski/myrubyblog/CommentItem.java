package com.bazanski.myrubyblog;

public class CommentItem {
	private String commentText;
	private String commentAuthor;
	private String creationDateTime;
	//������ � ������� ��������
	
	public CommentItem(String commentText, String commentAuthor, String creationDateTime) {
		this.commentText = commentText;
		this.commentAuthor = commentAuthor;
		this.creationDateTime = creationDateTime;
	}
	
}
