package com.bazanski.myrubyblog;

import android.os.Bundle;
import android.widget.LinearLayout;

public class ShowArticleActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showarticle);
		
		setTitle(R.string.actiontitle_showarticle);//тут будет название статьи
		
	}
}
