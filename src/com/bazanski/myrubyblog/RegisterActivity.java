package com.bazanski.myrubyblog;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.perm.kate.api.Api;

public class RegisterActivity extends BaseActivity{

	Button reg, regvk;
	EditText et_name, et_email, et_pass, et_rep_pass;
	RadioButton male, female;
	TextView info;
	
	private String sex = "1", name, email, pass, rep_pass;
	
	Api api;
	Account acc;
	InternetConnection ic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		setTitle(R.string.actiontitle_register);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		acc = new Account(this);
		ic = new InternetConnection(this);
		
		info = (TextView)findViewById(R.id.tv_info);
		
		et_name = (EditText)findViewById(R.id.et_name);
		et_email = (EditText)findViewById(R.id.et_email);
		et_pass = (EditText)findViewById(R.id.et_1stpass);
		et_rep_pass = (EditText)findViewById(R.id.et_2ndpass);
		
		male = (RadioButton)findViewById(R.id.rb_male);
		male.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					sex = "1";
					female.setChecked(false);
				}
			}
		});
		female = (RadioButton)findViewById(R.id.rb_female);
		female.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					sex = "0";
					male.setChecked(false);
				}
			}
		});
		
		reg = (Button)findViewById(R.id.but_reg);
		reg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				name = et_name.getText().toString();
				email = et_email.getText().toString();
				pass = et_pass.getText().toString();
				rep_pass = et_rep_pass.getText().toString();
				if(!name.isEmpty() && !email.isEmpty() && !pass.isEmpty() && !rep_pass.isEmpty()) {
					if(pass.equals(rep_pass)) {
						if(ic.php_reg(email, pass, name, sex)) {
							acc.setLoginType(0);
							Intent i = new Intent(RegisterActivity.this, MainActivity.class);
							startActivity(i);
						}
						else info.setText("This email has already used");
						
					}
					else info.setText("passwords don't match");
				}
				else info.setText("Fill all fields");
			}
		});
		
		regvk = (Button)findViewById(R.id.but_regvk);
		regvk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(RegisterActivity.this, VkLoginActivity.class);
				i.putExtra("what", "req");
				startActivityForResult(i, 1001);
			}
		});
	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) {
                //авторизовались успешно 
                acc.saveVkInfo(data.getLongExtra("user_id", 0), data.getStringExtra("token"));
                Log.v("VK",acc.getVkID_toString());
                api=new Api(acc.getAccessToken(), Constants.API_ID);
                String info = ic.getProfileName_Sex(acc.getVkID_toString());
                String name = info.split(";")[0];
                int sex = Integer.parseInt(info.split(";")[1]) - 1;
                if(ic.php_reg_vk(acc.getVkID_toString(), name, String.valueOf(sex))) {
					acc.setLoginType(1);
                	Intent i = new Intent(RegisterActivity.this, MainActivity.class);
    				startActivity(i);
                }
            }
        }
    }
}
