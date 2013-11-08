package com.bazanski.myrubyblog;

import android.app.ActionBar;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BaseActivity extends Activity{

	ActionBar bar;
	View BarView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //getActionBar().setCustomView(R.layout.item_actionbar);
        
        bar = getActionBar();
        BarView = new View(this);
        BarView.inflate(this, R.layout.item_actionbar, null);
        
        bar.setCustomView(BarView);
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
    	title_tv = (TextView)BarView.findViewById(R.id.action_title);
    	title_tv.setText(title);
    }
    
    public void setTitle(int resId) {
    	//getActionBar().setTitle(resId);
    	TextView title_tv = new TextView(this);
    	title_tv = (TextView)BarView.findViewById(R.id.action_title);
    	title_tv.setText(getResources().getString(resId));
    }
    
    public void setEmptyActionItems() {
    	TextView item1st_tv = new TextView(this);
    	item1st_tv = (TextView)BarView.findViewById(R.id.action_1stitem);
    	
    	TextView item2nd_tv = new TextView(this);
    	item2nd_tv = (TextView)BarView.findViewById(R.id.action_2nditem);
    	
    	View sep = new View(this);
    	sep = (View)BarView.findViewById(R.id.separator);
    	
    	item1st_tv.setVisibility(LinearLayout.INVISIBLE);
    	item2nd_tv.setVisibility(LinearLayout.INVISIBLE);
    	sep.setVisibility(LinearLayout.INVISIBLE);
    }
}
