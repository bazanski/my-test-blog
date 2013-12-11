package com.bazanski.myrubyblog;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LauncherActivity extends BaseActivity {

	Button login, register;
	
	Account acc;
	RSA_lib rsa;
	InternetConnection ic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
		setTitle(R.string.actiontitle_launcher);
		
		ic = new InternetConnection(this);
		
		acc = new Account(this);
		acc.setVkId(0);
		
		login = (Button)findViewById(R.id.but_login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
				startActivity(i);
				/*		
				RSA_lib rsa;
				rsa = new RSA_lib();
				rsa.init(1024);
				//InternetConnection ic = new InternetConnection(LauncherActivity.this);
				//String resp = ic.sendKeysAndWaitForMess(rsa.getModulus().toString(), rsa.getPublicKey().toString());
				//BigInteger decryptMessage  = rsa.decrypt( new BigInteger(resp));
				
				
				
				String tmp[] = resp.split(" ");
				BigInteger encryptedText[] = new BigInteger[tmp.length];
				String textRes = "";
				for(int i = 0; i < tmp.length; i++) {
					encryptedText[i] = new BigInteger(tmp[i]);
					BigInteger decryptMessage = rsa.decrypt(encryptedText[i]);
					byte[] r = decryptMessage.toByteArray();	
					
					for(int j = r.length - 1; j >= 0 ; j--) {
						textRes += (char)r[j];
					}
					
				}
				//mark1.setText(textRes);
				Toast.makeText(LauncherActivity.this, textRes, Toast.LENGTH_SHORT).show();
				*/
			}
		});
		
		register = (Button)findViewById(R.id.but_register);
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//InternetConnection ic = new InternetConnection(LauncherActivity.this);
				//String text = ic.sendKeysRequest("wowo this is big thin be careful!? love you bitch )))((__");
				//Toast.makeText(LauncherActivity.this, text, Toast.LENGTH_SHORT).show();
				Intent i = new Intent(LauncherActivity.this, RegisterActivity.class);
				startActivity(i);
			}
		});
		//ic.getRSAkeysFromServer();
		/*
		rsa = new RSA_lib(1024);
		String res = "";
		res = ic.sendKeysAndWaitForMess("1024", "1");
		
		//String newString = new String(res.getBytes("UTF-8"), "Cp1251");
		Log.w("RESPONSE", res);
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
		//Toast.makeText(this, rsa.decode(rsa.encode("ÔË‚ÂÚ œ¿÷¿Õ€", rsa), rsa), Toast.LENGTH_SHORT).show();
		*/
	}
}
