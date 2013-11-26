package com.bazanski.myrubyblog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.perm.kate.api.Auth;

public class VkLoginActivity extends BaseActivity {
    private static final String TAG = "Kate.LoginActivity";
    //private final static String API_ID = "4018538";
    
    WebView webview;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vk_login);
        
        webview = (WebView) findViewById(R.id.vkontakteview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.clearCache(true);
        
        //����� �������� ����������� �� ��������� �������� ��������
        webview.setWebViewClient(new VkontakteWebViewClient());
                
        //otherwise CookieManager will fall with java.lang.IllegalStateException: CookieSyncManager::createInstance() needs to be called before CookieSyncManager::getInstance()
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	if(extras.getString("what").equals("req")) {
        		setTitle(R.string.actiontitle_registervk);
        		CookieSyncManager.createInstance(this);
                
                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
        	}
        	else setTitle(R.string.actiontitle_loginvk);
        }
        
        
        String url=Auth.getUrl(com.bazanski.myrubyblog.Constants.API_ID, Auth.getSettings());
        webview.loadUrl(url);
    }
    
    class VkontakteWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            parseUrl(url);
        }
    }
    
    private void parseUrl(String url) {
        try {
            if(url==null)
                return;
            Log.i(TAG, "url="+url);
            if(url.startsWith(Auth.redirect_url))
            {
                if(!url.contains("error=")){
                    String[] auth=Auth.parseRedirectUrl(url);
                    Intent intent=new Intent();
                    intent.putExtra("token", auth[0]);
                    intent.putExtra("user_id", Long.parseLong(auth[1]));
                    setResult(Activity.RESULT_OK, intent);
                }
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}