package com.bazanski.myrubyblog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class RegisterActivity extends BaseActivity{

	Button reg, regvk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		setTitle(R.string.actiontitle_register);
		
		reg = (Button)findViewById(R.id.but_reg);
		reg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent i = new Intent(RegisterActivity.this, null);
				//startActivity(i);
			}
		});
		
		regvk = (Button)findViewById(R.id.but_regvk);
		regvk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Intent i = new Intent(RegisterActivity.this, null);
				//startActivity(i);
			}
		});
	}
}
