package com.bazanski.myrubyblog;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity{

	InternetConnection ic;
	Account acc;
	
	LinearLayout main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		setTitle(R.string.actiontitle_main);
		
		acc = new Account(this);
		ic = new InternetConnection(this);
		ic.getRSAkeysFromServer();
		
		setMainActionItems();
		main = (LinearLayout)findViewById(R.id.main_layout);
		
		//showArticles(main);
	}
	
	private void showArticlesTEST(LinearLayout layout) {
		//запрос на сервер для получения списка статей
		ArticleItem[] articles = new ArticleItem[20];
		for(int i = 0; i < articles.length; i++) {
			articles[i] = new ArticleItem(0,"Article " + i, getResources().getString(R.string.text_testarticle), "12.12.2013 00:00:00", "Gleb", null, 1);
		}
		
		addArticles(articles, layout);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadPosts();
	}

	private void addArticles(final ArticleItem[] arts, LinearLayout layout) {
		for(int i = 0; i < arts.length ; i++) {
			View vArt = getLayoutInflater().inflate(R.layout.item_article, null);
			TextView artTitle = (TextView)vArt.findViewById(R.id.article_name);
			artTitle.setText(arts[i].getTitle());
			
			TextView artAuthor = (TextView)vArt.findViewById(R.id.article_author);
			artAuthor.setText(arts[i].getAuthor());
			final int num = i;
			
			vArt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//переход к показу статьи
					Intent i = new Intent(MainActivity.this, ShowArticleActivity.class);
					String serverResponse = ic.php_showOnePost(arts[num].getPostID_toString());
					Log.v("serverResponse", serverResponse);
					if(serverResponse != null) {
						String[] post_info = serverResponse.split(";");
						//$data['id'].";".$data['title'].";".$data['text'].";".$data['u_id'].";".$data['name'].";".$data['datetime'];
						ArticleItem articles = new ArticleItem(Long.parseLong(post_info[0]), post_info[1], post_info[2], post_info[5], post_info[4], null, Long.parseLong(post_info[3]));
						i.putExtras(articles.getExtras());
					}
					startActivity(i);
				}
			});
			
			layout.addView(vArt);
		}
	}
	
	public void setMainActionItems() {
    	Button item1st_tv = new Button(this);
    	item1st_tv = (Button)bar.getCustomView().findViewById(R.id.action_1stitem);
    	item1st_tv.setText("Refresh");
    	item1st_tv.setBackgroundResource(R.drawable.selector_default);
    	item1st_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadPosts();
			}
		});
    	
    	Button item2nd_tv = new Button(this);
    	item2nd_tv = (Button)bar.getCustomView().findViewById(R.id.action_2nditem);
    	item2nd_tv.setText("Add Post");
    	item2nd_tv.setBackgroundResource(R.drawable.selector_orange);
    	item2nd_tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, AddArticleActivity.class);
				startActivity(i);
			}
		});
    	
    	View sep = new View(this);
    	sep = (View)bar.getCustomView().findViewById(R.id.separator);
    	
    	item1st_tv.setVisibility(LinearLayout.VISIBLE);
    	item2nd_tv.setVisibility(LinearLayout.VISIBLE);
    	sep.setVisibility(LinearLayout.VISIBLE);
    }
	
	private void loadPosts() {
		main.removeAllViews();
		
		if(!ic.php_hasVkID()) {
			Button vUnion = new Button(this);
			vUnion.setText(getResources().getString(R.string.but_makevkunion));
			vUnion.setBackgroundResource(R.drawable.selector_orange);
			vUnion.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(MainActivity.this, CollapsingAccountsActivity.class);
					startActivity(i);
				}
			});
			main.addView(vUnion);
		}
		
		String serverresponse = ic.php_showAllPosts();
		if(serverresponse != null) {
			Log.v("serverresponse", serverresponse);
			String tmp[] = serverresponse.split(";;");
			ArticleItem[] articles = new ArticleItem[tmp.length];
			for(int i = 0; i < tmp.length; i++) {
				Log.v("tmp[i]", tmp[i]);
				String[] post_info = tmp[i].split(";");
				//$data['id'].";".$data['title'].";".$data['u_id'].";".$data['name'].";".$data['datetime'];
				if(acc.getUID_toString().equals(post_info[2])) {
					post_info[3] = getResources().getString(R.string.text_you);
				}
				articles[i] = new ArticleItem(Long.parseLong(post_info[0]), post_info[1], "null", post_info[4], post_info[3], null, Long.parseLong(post_info[2]));
			}
			addArticles(articles, main);	
		}
		else Toast.makeText(MainActivity.this, "Error while loading posts, please try again.",  Toast.LENGTH_SHORT).show();
	}
}
