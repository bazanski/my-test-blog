package com.bazanski.myrubyblog;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends BaseActivity{

	LinearLayout main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle(R.string.actiontitle_main);
		
		main = (LinearLayout)findViewById(R.id.main_layout);
		
	}
	
	private void showArticles(LinearLayout layout) {
		//запрос на сервер для получения списка статей
		ArticleItem[] articles = new ArticleItem[7];
		for(int i = 0; i < articles.length; i++) {
			articles[i] = new ArticleItem("Article " + i, getResources().getString(R.string.text_testarticle), "12.12.2013 00:00:00", "Gleb", null, 1);
		}
		
		addArticles(articles, layout);
	}
	
	private void addArticles(ArticleItem[] arts, LinearLayout layout) {
		for(int i = 0; i < arts.length ; i++) {
			View vArt = getLayoutInflater().inflate(R.layout.item_article, null);
			TextView artTitle = (TextView)vArt.findViewById(R.id.article_name);
			artTitle.setText(arts[i].getTitle());
			
			TextView artAuthor = (TextView)vArt.findViewById(R.id.article_author);
			artAuthor.setText(arts[i].getAuthor());
			layout.addView(vArt);
		}
	}
}
