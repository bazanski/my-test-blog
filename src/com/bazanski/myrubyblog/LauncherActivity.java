package com.bazanski.myrubyblog;

import java.math.BigInteger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

public class LauncherActivity extends BaseActivity {

	Button login, register;
	
	Account acc;
	RSA_lib rsa;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		
		setTitle(R.string.actiontitle_launcher);
		
		acc = new Account(this);
		acc.setVkId(0);
		
		login = (Button)findViewById(R.id.but_login);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent i = new Intent(LauncherActivity.this, LoginActivity.class);
//				startActivity(i);
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
		
		
		rsa = new RSA_lib();
		rsa.init(1024);
 
		//создаем сообщение в BigInteger формате (которое и будем посылать)
		BigInteger message = new BigInteger("hello".getBytes());
		 
		//далее, зашифровываем секретное сообщение, чтобы послать серверу
		BigInteger encryptMessage = rsa.encrypt(message);
		//BigInteger decryptMessage = rsa.decrypt(encryptMessage);
		 
		/* Выводим наш результат на экран */
		Toast.makeText(LauncherActivity.this, decrypt(encryptMessage.toString()), Toast.LENGTH_SHORT).show(); 

	}
	private String decrypt(String text) {
		
		String tmp[] = text.split(" ");
		BigInteger encryptedText[] = new BigInteger[tmp.length];
		String textRes = "";
		for(int i = 0; i < tmp.length; i++) {
			encryptedText[i] = new BigInteger(tmp[i]);
			BigInteger decryptMessage = rsa.decrypt(encryptedText[i]);//.add(new BigInteger("848"));
			byte[] r = decryptMessage.toByteArray();	
			
			//for(int j = r.length - 1; j >= 0 ; j--) {
			for(int j = 0; j < r.length; j++) {
				textRes += (char)(r[j]);
			}					
		}
		return textRes;
	}
	
	
}
