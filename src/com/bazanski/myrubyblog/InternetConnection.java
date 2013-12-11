package com.bazanski.myrubyblog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class InternetConnection {
	
	final static String PHP_USERS_SERVER_ADRESS = "http://bazanski.url.ph/blog/users.php";
	final static String PHP_POSTS_SERVER_ADRESS = "http://bazanski.url.ph/blog/posts.php";
	
	RSA_lib rsa, server_rsa;
	private String modulus, publicKey, privateKey;
	
	private String server_modulus, server_publicKey;
	
	
	private Context ctx;
	private Account acc;
	
	public InternetConnection(Context context) {
		this.ctx = context;
		acc = new Account(this.ctx);
		
		this.rsa = new RSA_lib(1024);
		this.server_rsa = new RSA_lib(1024);
		this.modulus = this.rsa.getModulus().toString();
		this.publicKey = this.rsa.getPublicKey().toString();
		this.privateKey = this.rsa.getPrivateKey().toString();
	}

	//======RSA_TEST==========
	public String sendKeysAndWaitForMess(String N, String e){
		String result = "";
		SendRSA sr = new SendRSA();
		sr.execute(this.rsa.getModulus().toString(), rsa.getPublicKey().toString());
		try {
			result = sr.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return rsa.decode(result, this.rsa);
		//return result;
	}
	
	public String sendKeysRequest(String mess) {
		String result = "";
		ReciveRSAKeys rrk = new ReciveRSAKeys();
		rrk.execute();
		try {
			result = rrk.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		RSA_lib rsa = new RSA_lib(1024);
		rsa.setModulus(new BigInteger(result.split(";")[0]));
		rsa.setPublicKey(new BigInteger(result.split(";")[1]));
		mess = rsa.encode(mess, rsa);
		
		//mess = rsa.encrypt(new BigInteger(mess.getBytes())).toString();
		Toast.makeText(ctx, mess, Toast.LENGTH_SHORT).show();
		SendRSAMess srm = new SendRSAMess();
		srm.execute(mess);
		String result2 = "";
		try {
			result2 = srm.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return result2;
	}

	//======RSA==========	
	public void getRSAkeysFromServer() {
		String result = "";
		ReciveRSAKeys rrk = new ReciveRSAKeys();
		rrk.execute();
		try {
			result = rrk.get();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		server_rsa.setModulus(new BigInteger(result.split(";")[0]));
		server_rsa.setPublicKey(new BigInteger(result.split(";")[1]));
		
		this.server_modulus = this.server_rsa.getModulus().toString();
		this.server_publicKey = this.server_rsa.getPublicKey().toString();
		//Toast.makeText(ctx, server_modulus + " " + server_publicKey, Toast.LENGTH_SHORT).show();
	}

	//======VK_API========
	public String getProfileName_Sex(String vk_u_id) {
		String result = "";
		Vk_GetProfileName vgpn = new Vk_GetProfileName();
		vgpn.execute(vk_u_id);
		try {
			result = vgpn.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	//======PHP CONNECTION======
	public boolean php_reg(String email, String password, String name, String sex) {
		String result = "";
		Php_Registration pr = new Php_Registration();
		pr.execute(email, password, name, sex);
		try {
			result = pr.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			String[] tmp = result.split(";");
			acc.saveUID(Long.parseLong(tmp[1]));
			return true;
		}
		else return false; 
	}

	public boolean php_reg_vk(String vk_id, String name, String sex) {
		String result = "";
		Php_RegistrationVk prv = new Php_RegistrationVk();
		prv.execute(vk_id, name, sex);
		try {
			result = prv.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			String[] tmp = result.split(";");
			acc.saveUID(Long.parseLong(tmp[1]));
			return true;
		}
		else return false;
	}
	
	public boolean php_login(String email, String password) {
		String result = "";
		Php_Login pl = new Php_Login();
		pl.execute(email, password);
		try {
			result = rsa.decode(pl.get(), this.rsa);//pl.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//result = rsa.decode(result, this.rsa);
		if(result.contains("ok")) {
			String[] tmp = result.split(";");
			acc.saveUID(Long.parseLong(tmp[1]));
			return true;
		}
		else return false;
	}
	
	public boolean php_login_vk(String vk_id) {
		String result = "";
		Php_LoginVk plv = new Php_LoginVk();
		plv.execute(vk_id);
		try {
			result = plv.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!result.equals("ok;")) {
			String[] tmp = result.split(";");
			acc.saveUID(Long.parseLong(tmp[1]));
			return true;
		}
		else return false;
	}
		
	public boolean php_addPost(String title, String text) {
		String result = "";
		Php_AddingPost pad = new Php_AddingPost();
		Log.v("uID", acc.getUID_toString());
		pad.execute(title, text);
		Log.v("RESPONSE", "after execute");
		try {
			result = pad.get();
			Log.v("RESPONSE after get", result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			return true;
		}
		else return false; 
	}

	public String php_showAllPosts() {
		String result = "";
		Php_ShowAllPost psap = new Php_ShowAllPost();
		psap.execute();
		try {
			result = rsa.decode(psap.get(), this.rsa);//psap.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.v("show all posts RESULT after decrypt", result);
		if(!result.equals("ok")) {
			if(result.contains("ok")) {
				result = result.replaceFirst("ok;;", "");
				Log.v("afterReplase", result);
				return result;
			}
			else return null;
		}
		else return null; 
	}

	public String php_showOnePost(String p_id) {
		String result = "";
		Php_ShowOnePost psop = new Php_ShowOnePost();
		psop.execute(p_id);
		try {
			result = rsa.decode(psop.get(), this.rsa);//psop.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			
			result = result.replaceFirst("ok;;", "");
//			Log.v("res.split0", tmp[0]);
			Log.v("res.split1", result);
			return result;
		}
		else return null; 
	}
	
	public boolean php_deletePost(String p_id) {
		String result = "";
		Php_DeletePost pdp = new Php_DeletePost();
		//Log.v("uID", acc.getUID_toString());
		pdp.execute(p_id);
		Log.v("RESPONSE", "after execute");
		try {
			result = pdp.get();
			Log.v("RESPONSE after get", result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			return true;
		}
		else return false; 
	}

	public boolean php_updatePost(String title, String text, String p_id) {
		String result = "";
		Php_UpdatePost pup = new Php_UpdatePost();
		//Log.v("uID", acc.getUID_toString());
		pup.execute(title, text, p_id);
		Log.v("RESPONSE", "after execute");
		try {
			result = pup.get();
			Log.v("RESPONSE after get", result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			return true;
		}
		else return false; 
	}
	
	public boolean php_addComment(String text, String p_id) {
		String result = "";
		Php_AddingComment pac = new Php_AddingComment();
		Log.v("uID", acc.getUID_toString());
		pac.execute(text, p_id);
		Log.v("RESPONSE", "after execute");
		try {
			result = pac.get();
			Log.v("RESPONSE after get", result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			return true;
		}
		else return false; 
	}

	public String php_showAllComments(String p_id) {
		String result = "";
		Php_ShowAllComments psac = new Php_ShowAllComments();
		psac.execute(p_id);
		try {
			result = rsa.decode(psac.get(), this.rsa);//psac.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			
			result = result.replaceFirst("ok;;", "");
//			Log.v("res.split0", tmp[0]);
			Log.v("res.split1", result);
			return result;
		}
		else return null; 
	}

	public boolean php_deleteComment(String c_id) {
		String result = "";
		Php_DeleteComment pdc = new Php_DeleteComment();
		//Log.v("uID", acc.getUID_toString());
		pdc.execute(c_id);
		Log.v("RESPONSE", "after execute delete comment");
		try {
			result = pdc.get();
			Log.v("RESPONSE after", result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			return true;
		}
		else return false; 
	}

	public boolean php_vkUnion(String vk_id) {
		String result = "";
		Php_VkUnion pvu = new Php_VkUnion();
		pvu.execute(vk_id);
		try {
			result = pvu.get();
			Log.v("RESPONSE after", result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			return true;
		}
		else return false; 
	}
	
	public boolean php_hasVkID() {
		String result = "";
		Php_HasVkID pvi = new Php_HasVkID();
		pvi.execute();
		try {
			result = pvi.get();
			Log.v("RESPONSE after", result);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(result.contains("ok")) {
			return true;
		}
		else return false; 
	}
	
	//======RSA CONNECTION=======
	class SendRSA extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return sendRsa(params[0], params[1]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String sendRsa(String N, String e) {
			Account acc = new Account(ctx);
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			//HttpPost httppost = new HttpPost("https://api.vkontakte.ru/method/getProfiles");
			HttpPost httppost = new HttpPost("http://bazanski.url.ph/blog/test2.php");
			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("do", "recive"));
				nameValuePairs.add(new BasicNameValuePair("N", N));
				nameValuePairs.add(new BasicNameValuePair("e", e));

				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),"UTF-8");//

			} catch (ClientProtocolException e1) {

				// TODO Auto-generated catch block

			} catch (IOException e1) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE RSAsend", responseString);
			return responseString;
		}
 	}
	
	class ReciveRSAKeys extends AsyncTask<Void, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return keysRequestRsa();
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String keysRequestRsa() {
			Account acc = new Account(ctx);
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			//HttpPost httppost = new HttpPost("https://api.vkontakte.ru/method/getProfiles");
			//HttpPost httppost = new HttpPost("http://bazanski.url.ph/blog/test.php");
			HttpPost httppost = new HttpPost(PHP_USERS_SERVER_ADRESS);
			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

				nameValuePairs.add(new BasicNameValuePair("do", "keys"));
				nameValuePairs.add(new BasicNameValuePair("u_id", acc.getUID_toString()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e1) {

				// TODO Auto-generated catch block

			} catch (IOException e1) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE RSAsend", responseString);
			return responseString;
		}
 	}
	
	class SendRSAMess extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return sendMessRsa(params[0]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String sendMessRsa(String mess) {
			Account acc = new Account(ctx);
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			//HttpPost httppost = new HttpPost("https://api.vkontakte.ru/method/getProfiles");
			HttpPost httppost = new HttpPost("http://bazanski.url.ph/blog/test.php");
			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("do", "mess"));
				nameValuePairs.add(new BasicNameValuePair("mess", mess));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e1) {

				// TODO Auto-generated catch block

			} catch (IOException e1) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE RSAsend", responseString);
			return responseString;
		}
 	}
	
	
	//======RUBY CONNECTION======
	
	
	//======PHP CONNECTION CLASSES======
	class Php_Registration extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doPhpReg(params[0], params[1] , params[2], params[3]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doPhpReg(String email, String password, String name, String sex) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_USERS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "reg"));
				nameValuePairs.add(new BasicNameValuePair("email", email));
				nameValuePairs.add(new BasicNameValuePair("password", password));
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("sex", sex));

				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				//httppost.setHeader("content", "text/html; charset=utf-8");
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return responseString;
		}
		
 		
 	}

	class Php_RegistrationVk extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doPhpRegVk(params[0], params[1] , params[2]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doPhpRegVk(String vk_id, String name, String sex) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_USERS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "reg_vk"));
				nameValuePairs.add(new BasicNameValuePair("vk_id", vk_id));
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("sex", sex));

				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return responseString;
		}
		
 		
 	}
	
	class Php_Login extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doPhpLogin(params[0], params[1]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doPhpLogin(String email, String password) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_USERS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "login"));
				nameValuePairs.add(new BasicNameValuePair("email", email));
				nameValuePairs.add(new BasicNameValuePair("N", modulus));
				nameValuePairs.add(new BasicNameValuePair("e", publicKey));
				nameValuePairs.add(new BasicNameValuePair("password", password));

				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return responseString;
		}
		
 		
 	}
	
	class Php_LoginVk extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doPhpLoginVk(params[0]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doPhpLoginVk(String vk_id) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_USERS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "login_vk"));
				nameValuePairs.add(new BasicNameValuePair("N", modulus));
				nameValuePairs.add(new BasicNameValuePair("e", publicKey));
				nameValuePairs.add(new BasicNameValuePair("vk_id", vk_id));

				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return rsa.decode(responseString, rsa);//responseString;
		}
		
 		
 	}
	
	class Php_AddingPost extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doPhpAddingPost(params[0], params[1]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			Log.v("RESPONSE result", result);
		}
		
		private String doPhpAddingPost(String title, String text) {
			Log.v("CON", "enter send func");
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_POSTS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
				
				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "add_post"));
				nameValuePairs.add(new BasicNameValuePair("title", title));
				nameValuePairs.add(new BasicNameValuePair("text", text));
				nameValuePairs.add(new BasicNameValuePair("u_id", acc.getUID_toString()));

				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return responseString;
		}
	
 	}
	
	class Php_ShowAllPost extends AsyncTask<Void, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return doShowAllPost();
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doShowAllPost() {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_POSTS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "show_all_posts"));
				nameValuePairs.add(new BasicNameValuePair("N", modulus));
				nameValuePairs.add(new BasicNameValuePair("e", publicKey));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE showAllposts", responseString);
			return responseString;//decrypt(responseString);
		}
		
 		
 	}
	
	class Php_ShowOnePost extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doShowOnePost(params[0]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doShowOnePost(String p_id) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_POSTS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "show_one_post"));
				nameValuePairs.add(new BasicNameValuePair("p_id", p_id));
				nameValuePairs.add(new BasicNameValuePair("N", modulus));
				nameValuePairs.add(new BasicNameValuePair("e", publicKey));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return responseString;//decrypt(responseString);
		}
		
 		
 	}
	
	class Php_DeletePost extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doDeletePost(params[0]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doDeletePost(String p_id) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_POSTS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "delete_post"));
				nameValuePairs.add(new BasicNameValuePair("p_id", p_id));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return responseString;
		}
		
 		
 	}
	
	class Php_UpdatePost extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doUpdatePost(params[0], params[1], params[2]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doUpdatePost(String title, String text, String p_id) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_POSTS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "edit_post"));
				nameValuePairs.add(new BasicNameValuePair("title", title));
				nameValuePairs.add(new BasicNameValuePair("text", text));
				nameValuePairs.add(new BasicNameValuePair("p_id", p_id));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return responseString;
		}
		
 		
 	}
	
	class Php_AddingComment extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doPhpAddingComment(params[0], params[1]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			Log.v("RESPONSE result", result);
		}
		
		private String doPhpAddingComment(String text, String p_id) {
			Log.v("Add comment", "enter send func");
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_POSTS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
				
				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "add_comment"));
				nameValuePairs.add(new BasicNameValuePair("p_id", p_id));
				nameValuePairs.add(new BasicNameValuePair("text", text));
				nameValuePairs.add(new BasicNameValuePair("u_id", acc.getUID_toString()));

				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return responseString;
		}
	
 	}
	
	class Php_ShowAllComments extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doShowAllComments(params[0]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doShowAllComments(String p_id) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_POSTS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "show_all_comments"));
				nameValuePairs.add(new BasicNameValuePair("p_id", p_id));
				nameValuePairs.add(new BasicNameValuePair("N", modulus));
				nameValuePairs.add(new BasicNameValuePair("e", publicKey));

				//httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE", responseString);
			return responseString;
		}
		
 		
 	}
	
	class Php_DeleteComment extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doDeleteComment(params[0]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doDeleteComment(String c_id) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_POSTS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "delete_comment"));
				nameValuePairs.add(new BasicNameValuePair("c_id", c_id));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE DELETE", responseString);
			return responseString;
		}
		
 		
 	}
	
	class Php_VkUnion extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doVkUnion(params[0]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doVkUnion(String vk_id) {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_USERS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "add_vk_to_acc"));
				nameValuePairs.add(new BasicNameValuePair("u_id", acc.getUID_toString()));
				nameValuePairs.add(new BasicNameValuePair("vk_id", vk_id));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE DELETE", responseString);
			return responseString;
		}
		
 		
 	}
	
	class Php_HasVkID extends AsyncTask<Void, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return doHasVkID();
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doHasVkID() {
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(PHP_USERS_SERVER_ADRESS);

			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				nameValuePairs.add(new BasicNameValuePair("os", "android"));
				nameValuePairs.add(new BasicNameValuePair("do", "has_vk_id"));
				nameValuePairs.add(new BasicNameValuePair("u_id", acc.getUID_toString()));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE has", responseString);
			return responseString;
		}
		
 		
 	}

	
	class Vk_GetProfileName extends AsyncTask<String, Void, String> {

 		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(ctx, "Внимание", "Идет связь с сервером");
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			return doGetProfileName(params[0]);
		}
		

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
		}
		
		private String doGetProfileName(String u_id) {
			Account acc = new Account(ctx);
			String responseString = "";
			HttpClient httpclient = new DefaultHttpClient();

			//HttpPost httppost = new HttpPost("https://api.vkontakte.ru/method/getProfiles");
			HttpPost httppost = new HttpPost("https://api.vk.com/method/users.get");
			try {
				// определяешь элементы массива POST
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);

				//nameValuePairs.add(new BasicNameValuePair("uid", u_id));
				//nameValuePairs.add(new BasicNameValuePair("access_token", acc.getAccessToken()));
				nameValuePairs.add(new BasicNameValuePair("user_ids", u_id));
				nameValuePairs.add(new BasicNameValuePair("fields", "sex"));
				nameValuePairs.add(new BasicNameValuePair("name_case", "Nom"));

				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
				// выполняешь POST-запрос

				HttpResponse response = httpclient.execute(httppost);

				// получение ответа от сервера
				responseString = EntityUtils.toString(response.getEntity(),
						"UTF-8");//

			} catch (ClientProtocolException e) {

				// TODO Auto-generated catch block

			} catch (IOException e) {

				// TODO Auto-generated catch block

			}
			Log.v("RESPONSE DELETE", responseString);
			return JSONparse(responseString);
		}
		
		private String JSONparse(String strJson) {
			String result = "";
			
			JSONObject jsonResponse;
                  
            try {
                  
                 /****** Creates a new JSONObject with name/value mappings from the JSON string. ********/
                 jsonResponse = new JSONObject(strJson);
                 JSONArray jsonMainNode = jsonResponse.optJSONArray("response");
                  
                 /***** Returns the value mapped by name if it exists and is a JSONArray. ***/
                 
                 /*********** Process each JSON Node ************/
                 int lengthJsonArr = jsonMainNode.length();
                 for(int i=0; i < lengthJsonArr; i++) 
                 {
                     //****** Get Object for each JSON node.***********
                	 JSONObject jsonChildNode = jsonMainNode.getJSONObject(i); 
                     //******* Fetch node values **********
                     String name1   = jsonChildNode.optString("first_name").toString();
                     String name2   = jsonChildNode.optString("last_name").toString();
                     String sex   = jsonChildNode.optString("sex").toString();
                     Log.v("JSONparse", sex);
                     result = name1 + " " + name2 + ";" + sex ;
                }
                return result;
             } catch (JSONException e) {
                 e.printStackTrace();
             }
            Log.v("JSONparse", "returning null");
            return null;
		}
 	}
	
	//======RUBY CONNECTION CLASSES======
}
