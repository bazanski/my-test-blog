package com.bazanski.myrubyblog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowArticleActivity extends BaseActivity{

	Account acc;
	InternetConnection ic;
	
	Button edit, delete, add_comment;
	TextView creation_date, text;
	LinearLayout show_comments;
	
	Bundle article_data;
	ArticleItem article;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showarticle);
		
		acc = new Account(this);
		ic = new InternetConnection(this);
		
		article_data = getIntent().getExtras();
		article = new ArticleItem(article_data);
		setTitle(article_data.getString("title", getResources().getString(R.string.actiontitle_showarticle)));//тут будет название статьи R.string.actiontitle_showarticle
		
		text = (TextView)findViewById(R.id.article_text);
		text.setText(article_data.getString("text"));
		
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
		
		delete = (Button)findViewById(R.id.but_deletearticle);
		edit = (Button)findViewById(R.id.but_editarticle);
		if(article_data.getLong("authorID") != acc.getUID()) {
			delete.setVisibility(LinearLayout.GONE);
			edit.setVisibility(LinearLayout.GONE);
		}
		else {
			delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//Toast.makeText(ShowArticleActivity.this, String.valueOf(article_data.getLong("p_id")), Toast.LENGTH_SHORT).show();
					if(ic.php_deletePost(String.valueOf(article_data.getLong("p_id")))) {
						Toast.makeText(ShowArticleActivity.this, "post deleted", Toast.LENGTH_SHORT).show();
						finish();
					}
					
				}
			});
			
			edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(ShowArticleActivity.this, AddArticleActivity.class);
					i.putExtra("do", "update");
					i.putExtras(article_data);
					startActivityForResult(i, 1000);
				}
			});
		}
	}

	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
            	setTitle(data.getExtras().getString("title", getResources().getString(R.string.actiontitle_showarticle)));
            	text.setText(data.getExtras().getString("text"));
            	article_data.putString("title", data.getExtras().getString("title"));
            	article_data.putString("text", data.getExtras().getString("text"));
            }
        }
    }
}
