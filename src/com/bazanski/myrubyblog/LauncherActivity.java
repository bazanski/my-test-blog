package com.bazanski.myrubyblog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LauncherActivity extends BaseActivity {

	Button login, register;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
		setTitle(R.string.actiontitle_launcher);
		
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
