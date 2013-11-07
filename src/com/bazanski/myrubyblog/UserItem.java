package com.bazanski.myrubyblog;

public class UserItem {
	private String name;
	private String dateOfBirth;
	private int sex; //1-man, 0-woman
	private String email;
	
	public UserItem(String name, String dateOfBirth, String email, int sex) {
		this.name = name;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.sex = sex;
	}

}
