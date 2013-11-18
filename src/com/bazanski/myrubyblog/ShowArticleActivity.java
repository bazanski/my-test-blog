package com.bazanski.myrubyblog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowArticleActivity extends BaseActivity{

	Button edit, delete, add_comment;
	TextView creation_date;
	LinearLayout show_comments;
	
	Bundle article_data;
	ArticleItem article;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showarticle);
		
		article_data = getIntent().getExtras();
		article = new ArticleItem(article_data);
		setTitle(article_data.getString("title", getResources().getString(R.string.actiontitle_showarticle)));//тут будет название статьи R.string.actiontitle_showarticle
		
		creation_date = (TextView)findViewById(R.id.creation_date);
		creation_date.setText(article_data.getString("dateTime", "12.12.2013 00:00:00"));
		
		show_comments = (LinearLayout)findViewById(R.id.show_comments);
		show_comments.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ShowArticleActivity.this, ShowCommentsActivity.class);
				i.putExtras(article.getExtras());
				startActivity(i);
			}
		});
	}
}
