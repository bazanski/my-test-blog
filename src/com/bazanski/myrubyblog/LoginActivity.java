package com.bazanski.myrubyblog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginActivity extends BaseActivity{
	
	Button login, loginvk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	
		setTitle(R.string.actiontitle_login);
		
		login = (Button)findViewById(R.id.but_login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, null);
				startActivity(i);
			}
		});
		
		loginvk = (Button)findViewById(R.id.but_loginvk);
		loginvk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, null);
				startActivity(i);
			}
		});
	}
}
