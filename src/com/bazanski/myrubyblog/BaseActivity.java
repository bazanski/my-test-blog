package com.bazanski.myrubyblog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseActivity extends Activity{

	ActionBar bar;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getActionBar().setCustomView(R.layout.item_actionbar);
        
        bar = getActionBar();
        bar.setCustomView(R.layout.item_actionbar);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
        		| ActionBar.DISPLAY_SHOW_HOME 
        		| ActionBar.DISPLAY_HOME_AS_UP);
        setEmptyActionItems();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.launcher, menu);
        return false;//было true
    }
    
    public void setTitle(String title) {
    	//getActionBar().setTitle(title);
    	
    	TextView title_tv = new TextView(this);
    	title_tv = (TextView)bar.getCustomView().findViewById(R.id.action_title);
    	title_tv.setText(title);
    	
    }
    
    public void setTitle(int resId) {
    	//getActionBar().setTitle(resId);
    	
    	TextView title_tv = new TextView(this);
    	title_tv = (TextView)bar.getCustomView().findViewById(R.id.action_title);
    	title_tv.setText(getResources().getString(resId));
    	
    }
    
    public void setEmptyActionItems() {
    	TextView item1st_tv = new TextView(this);
    	item1st_tv = (TextView)bar.getCustomView().findViewById(R.id.action_1stitem);
    	item1st_tv.setText("haha");
    	
    	TextView item2nd_tv = new TextView(this);
    	item2nd_tv = (TextView)bar.getCustomView().findViewById(R.id.action_2nditem);
    	item2nd_tv.setText("hehe");
    	
    	View sep = new View(this);
    	sep = (View)bar.getCustomView().findViewById(R.id.separator);
    	
    	item1st_tv.setVisibility(LinearLayout.INVISIBLE);
    	item2nd_tv.setVisibility(LinearLayout.INVISIBLE);
    	sep.setVisibility(LinearLayout.INVISIBLE);
    }
}
