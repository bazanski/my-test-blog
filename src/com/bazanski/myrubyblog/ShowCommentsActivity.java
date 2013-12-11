package com.bazanski.myrubyblog;

import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowCommentsActivity extends BaseActivity{

	InternetConnection ic;
	Account acc;
	
	Bundle article_data;
	
	LinearLayout all_comments;
	Button send;
	EditText text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showcomments);
		
		ic = new InternetConnection(this);
		acc = new Account(this);
		ic.getRSAkeysFromServer();
		
		article_data = getIntent().getExtras();
		
		setTitle(article_data.getString("title", getResources().getString(R.string.actiontitle_showarticle)));//тут будет название статьи R.string.actiontitle_showarticle
		
		all_comments = (LinearLayout)findViewById(R.id.comments_layout);
		
		showComments(all_comments);
		
		text = (EditText)findViewById(R.id.input_comment);
		send = (Button)findViewById(R.id.but_savecomment);
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String comment_text = text.getText().toString();
				if(!comment_text.isEmpty())
					if(ic.php_addComment(comment_text, String.valueOf(article_data.getLong("p_id")))) {
						text.setText("");
						loadComments();
						Toast.makeText(ShowCommentsActivity.this, "comment added", Toast.LENGTH_SHORT).show();
					}
					else Toast.makeText(ShowCommentsActivity.this, "Adding Error, please try again later", Toast.LENGTH_SHORT).show();
				else Toast.makeText(ShowCommentsActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
					
				//addComment(text.getText().toString());				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadComments();
	}
	
	private void showComments(LinearLayout layout) {
		for(int i = 0; i < new Random().nextInt(7) + 2; i++) {
	//		addComment(getResources().getString(R.string.text_testcomment) + " " + i);
		}
	}
	
	private void addComments(final CommentItem[] comms, LinearLayout v) {
				
		
	//==========
		for(int i = 0; i < comms.length ; i++) {
			View vComment = getLayoutInflater().inflate(R.layout.item_comment, null);
			TextView text = (TextView)vComment.findViewById(R.id.comment_text);
			text.setText(comms[i].getText());
			
			TextView comAuthor = (TextView)vComment.findViewById(R.id.tv_author);
			comAuthor.setText(getResources().getString(R.string.text_articleauthor) + " " + comms[i].getAuthor());
			
			TextView comTime = (TextView)vComment.findViewById(R.id.tv_time);
			comTime.setText(comms[i].getTime());
			
			final int num = i;
			
			Button del = (Button)vComment.findViewById(R.id.but_delete);
			del.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.v("c_id", comms[num].getCommentID_toString());
					if(ic.php_deleteComment(comms[num].getCommentID_toString())) {
						Toast.makeText(ShowCommentsActivity.this, "comment deleted", Toast.LENGTH_SHORT).show();
						loadComments();
					}
					else Toast.makeText(ShowCommentsActivity.this, "Deleting error, please try again", Toast.LENGTH_SHORT).show();
				}
			});
			if(acc.getUID() != comms[i].getAuthorID()) del.setVisibility(LinearLayout.GONE);
			v.addView(vComment);
		}
	}
	
	private void loadComments() {
		all_comments.removeAllViews();
		String serverresponse = ic.php_showAllComments(String.valueOf(article_data.getLong("p_id")));
		//Log.v("serverresponse", serverresponse);
		if(serverresponse != null && serverresponse.length() > 2) {
			String tmp[] = serverresponse.split(";;");
			CommentItem[] comments = new CommentItem[tmp.length];
			for(int i = 0; i < tmp.length; i++) {
				//Log.v("tmp[i]", tmp[i]);
				String[] comm_info = tmp[i].split(";");
				//$data['id'].";".$data['text'].";".$data['u_id'].";".$data['name'].";".$data['datetime'];
				if(acc.getUID_toString().equals(comm_info[2])) {
					comm_info[3] = getResources().getString(R.string.text_you);
				}
				comments[i] = new CommentItem(Long.parseLong(comm_info[0]), comm_info[1], comm_info[3], comm_info[4], Long.parseLong(comm_info[2]));
			}
			addComments(comments, all_comments);	
		}
		//else Toast.makeText(ShowCommentsActivity.this, "Error while loading comments, please try again.",  Toast.LENGTH_SHORT).show();
		else Toast.makeText(ShowCommentsActivity.this, "No comments, be a first.",  Toast.LENGTH_SHORT).show();
	}
}
