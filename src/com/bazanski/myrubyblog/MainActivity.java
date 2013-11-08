package com.bazanski.myrubyblog;

import android.os.Bundle;
import android.widget.LinearLayout;

public class MainActivity extends BaseActivity{

	LinearLayout main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle(R.string.actiontitle_main);
		
		main = (LinearLayout)findViewById(R.id.main_layout);
		
	}
}
