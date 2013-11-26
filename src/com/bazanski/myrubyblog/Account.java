package com.bazanski.myrubyblog;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class Account {
    private static String access_token;//token из соц сети
    private static long vk_id;//id в соц сети
    private static long uID;//id на нашем сервере
    
    private static String login, password;
    private static boolean isUnited;
    private static int loginType; //0 - acc | 1 - vk
    
    private Context ctx;
    private SharedPreferences prefs;
    
    public Account(Context context) {
    	this.ctx = context;
    	prefs = PreferenceManager.getDefaultSharedPreferences(this.ctx);
	}
    
    public void setLoginType(int type) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.ctx);
        Editor editor=prefs.edit();
        editor.putInt("loginType", type);
        editor.commit();
    }
    
    public void setUnited(boolean set) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.ctx);
        Editor editor=prefs.edit();
        editor.putBoolean("isUnited", set);
        editor.commit();
    }
    
    public void saveLogPass(String login, String password) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.ctx);
        Editor editor=prefs.edit();
        editor.putString("login", login);
        editor.putString("password", password);
        editor.commit();
    }
    
    public void setVkId(long vk_id) {
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.ctx);
        Editor editor=prefs.edit();
        editor.putLong("vk_id", vk_id);
        editor.commit();
    }
    
    public void saveVkInfo(long vk_id, String token){
    	this.vk_id = vk_id;
    	this.access_token = token;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.ctx);
        Editor editor=prefs.edit();
        editor.putLong("vk_id", vk_id);
        editor.putString("access_token", token);
        editor.commit();
    }
    
    public int getLoginType() {
    	return prefs.getInt("loginType", 0);
    }
    
    public boolean isUnited() {
    	return prefs.getBoolean("isUnited", false);
    }
    
    public String getLogin() {
    	return prefs.getString("login", "");
    }
    
    public String getPassword() {
    	return prefs.getString("password", "");
    }
    
    public long getVkID(){
        return prefs.getLong("vk_id", 0);
    }
    
    public String getVkID_toString(){
        return String.valueOf(prefs.getLong("vk_id", 0));
    }
    
    public void saveUID(long uID){
    	this.uID = uID;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.ctx);
        Editor editor=prefs.edit();
        editor.putLong("uID", uID);
        editor.commit();
    }
    
    public long getUID(){
        return prefs.getLong("uID", 0);
    }
    
    public String getUID_toString(){
        return String.valueOf(prefs.getLong("uID", 0));
    }
    
    public String getAccessToken(){
        return prefs.getString("access_token", "0");
    }
}
