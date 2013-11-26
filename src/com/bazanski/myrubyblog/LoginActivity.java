package com.bazanski.myrubyblog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.perm.kate.api.Api;

public class LoginActivity extends BaseActivity{
	
	Button login, loginvk;
	TextView info;
	EditText et_email, et_pass;
	
	private Api api;
	private Account acc;
	private InternetConnection ic;
	
	private String email, pass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	
		setTitle(R.string.actiontitle_login);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		acc = new Account(this);
		ic = new InternetConnection(this);
		
		info = (TextView)findViewById(R.id.tv_info);
		et_email = (EditText)findViewById(R.id.et_email);
		et_email.setText(acc.getLogin());
		et_pass = (EditText)findViewById(R.id.et_pass);
		et_pass.setText(acc.getPassword());
		
		login = (Button)findViewById(R.id.but_login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				email = et_email.getText().toString();
				pass = et_pass.getText().toString();
				if(!email.isEmpty() && !pass.isEmpty()) {
					if(ic.php_login(email, pass)) {
						acc.saveLogPass(email, pass);
						acc.setLoginType(0);
						Intent i = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(i);
					}
					else info.setText("Wrong email or password"); 
				}
				else info.setText("Fill all fields");				
			}
		});
		
		loginvk = (Button)findViewById(R.id.but_loginvk);
		loginvk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LoginActivity.this, VkLoginActivity.class);
				i.putExtra("what", "login");
				startActivityForResult(i, 1001);
			}
		});
		
		if(acc.getAccessToken()!=null) {
            api=new Api(acc.getAccessToken(), Constants.API_ID);
           // tv.setText(String.valueOf(account.user_id));
        }
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                //авторизовались успешно 
                acc.saveVkInfo(data.getLongExtra("user_id", 0), data.getStringExtra("token"));
                Log.v("VK",acc.getVkID_toString());
                api=new Api(acc.getAccessToken(), Constants.API_ID);
                
                if(ic.php_login_vk(acc.getVkID_toString())) {
					acc.setLoginType(1);
                	Intent i = new Intent(LoginActivity.this, MainActivity.class);
    				startActivity(i);
                }
                else Toast.makeText(LoginActivity.this, "You are not registred", Toast.LENGTH_SHORT).show();
                	
            }
        }
    }
}
