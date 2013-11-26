package com.bazanski.myrubyblog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LauncherActivity extends BaseActivity {

	Button login, register;
	
	Account acc;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
		setTitle(R.string.actiontitle_launcher);
		
		acc = new Account(this);
		acc.setVkId(0);
		
		login = (Button)findViewById(R.id.but_login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
				startActivity(i);
			}
		});
		
		register = (Button)findViewById(R.id.but_register);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LauncherActivity.this, RegisterActivity.class);
				startActivity(i);
			}
		});
	}
	
}
