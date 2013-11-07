package com.bazanski.myrubyblog;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;

public class BaseActivity extends Activity{

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getActionBar().setCustomView(R.layout.item_actionbar);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launcher, menu);
        return false;//было true
    }
    
    public void setTitle(String title) {
    	getActionBar().setTitle(title);
    }
    
    public void setTitle(int resId) {
    	getActionBar().setTitle(resId);
    }
}
