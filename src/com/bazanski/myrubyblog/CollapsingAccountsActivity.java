package com.bazanski.myrubyblog;

import com.perm.kate.api.Api;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class CollapsingAccountsActivity extends BaseActivity{

	Button vk_union;
	
	InternetConnection ic;
	Account acc;
	Api api;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collapse);
	
		setTitle(R.string.actiontitle_collapse);
		
		ic = new InternetConnection(this);
		acc = new Account(this);
		
		vk_union = (Button)findViewById(R.id.but_vkunion);
		vk_union.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(CollapsingAccountsActivity.this, VkLoginActivity.class);
				i.putExtra("what", "union");
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
                
                if(ic.php_vkUnion(acc.getVkID_toString())) {
                	finish();
                }
                else Toast.makeText(CollapsingAccountsActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
