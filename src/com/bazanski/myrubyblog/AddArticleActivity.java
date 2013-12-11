package com.bazanski.myrubyblog;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddArticleActivity extends BaseActivity{
	
	InternetConnection ic;
	
	Button send;
	EditText et_title, et_text;
	
	private String title, text;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_addpost);

        setTitle(R.string.actiontitle_addarticle);
        ic = new InternetConnection(this);
        ic.getRSAkeysFromServer();
        
        et_title = (EditText)findViewById(R.id.et_title);
        et_text = (EditText)findViewById(R.id.et_text);
        
        
        send = (Button)findViewById(R.id.but_send);
        send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				title = et_title.getText().toString();
				text = et_text.getText().toString();
				if(!title.isEmpty() && !text.isEmpty()) {
					//Log.v("RESPONSE", "befor send");
					if(ic.php_addPost(title, text)) {
						Toast.makeText(AddArticleActivity.this, "post added", Toast.LENGTH_SHORT).show();
						finish();
					}
					else Toast.makeText(AddArticleActivity.this, "Error", Toast.LENGTH_SHORT).show();
				}
				else Toast.makeText(AddArticleActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
				
			}
		});
        
        onNewIntent(getIntent());
    }
	
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		final Bundle extras = intent.getExtras();
		if(extras != null)
			if(extras.getString("do").equals("update")) {
				et_title.setText(extras.getString("title"));
				et_text.setText(extras.getString("text"));
				send.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						title = et_title.getText().toString();
						text = et_text.getText().toString();
						if(!title.isEmpty() && !text.isEmpty()) {
							//Log.v("RESPONSE", "befor send");
							if(ic.php_updatePost(title, text, String.valueOf(extras.getLong("p_id")))) {
								Toast.makeText(AddArticleActivity.this, "post updated", Toast.LENGTH_SHORT).show();
								Intent intent=new Intent();
			                    intent.putExtra("title", title);
			                    intent.putExtra("text", text);
			                    setResult(Activity.RESULT_OK, intent);
								finish();
							}
							else Toast.makeText(AddArticleActivity.this, "Error", Toast.LENGTH_SHORT).show();
						}
						else Toast.makeText(AddArticleActivity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
					}
				});
			}
	}
}
