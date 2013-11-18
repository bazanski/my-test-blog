package com.bazanski.myrubyblog;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowCommentsActivity extends BaseActivity{

	Bundle article_data;
	
	LinearLayout all_comments;
	Button send;
	EditText text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showcomments2);
		
		article_data = getIntent().getExtras();
		
		setTitle(article_data.getString("title", getResources().getString(R.string.actiontitle_showarticle)));//тут будет название статьи R.string.actiontitle_showarticle
		
		all_comments = (LinearLayout)findViewById(R.id.comments_layout);
		
		showComments(all_comments);
		
		text = (EditText)findViewById(R.id.input_comment);
		send = (Button)findViewById(R.id.but_savecomment);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addComment(text.getText().toString());				
			}
		});
	}
	
	private void showComments(LinearLayout layout) {
		for(int i = 0; i < new Random().nextInt(7) + 2; i++) {
			addComment(getResources().getString(R.string.text_testcomment) + " " + i);
		}
	}
	
	private void addComment(String text) {
		View vComment = getLayoutInflater().inflate(R.layout.item_comment, null);
		TextView artTitle = (TextView)vComment.findViewById(R.id.comment_text);
		artTitle.setText(text);
				
		all_comments.addView(vComment);
	}
}
