package com.bazanski.myrubyblog;

import java.math.BigInteger;
import java.security.SecureRandom;

import android.util.Log;
    

public class RSA_lib {
   private final static BigInteger one      = BigInteger.ONE;
   private final static SecureRandom random = new SecureRandom();

   private BigInteger privateKey;
   private BigInteger publicKey;
   private BigInteger modulus;

   // generate an N-bit (roughly) public and private key
   RSA_lib(int N) {
      BigInteger p = BigInteger.probablePrime(N/2, random);
      BigInteger q = BigInteger.probablePrime(N/2, random);
      BigInteger phi = (p.subtract(one)).multiply(q.subtract(one));

      modulus    = p.multiply(q);                                  
      publicKey  = new BigInteger("65537");     // common value in practice = 2^16 + 1
      privateKey = publicKey.modInverse(phi);
   }

   public String encode(String mess, RSA_lib rsa) {
	   char[] chars = mess.toCharArray();
	   int[] codes = new int[chars.length];
	   BigInteger[] message = new BigInteger[chars.length];
	   String res = "";
	   for(int i = 0; i < chars.length; i++) {
		   codes[i] = (int)chars[i];
		   message[i] = new BigInteger(String.valueOf(codes[i]));
		   res += rsa.encrypt(message[i]) + " ";
	   }
	   return res;
   }
   
   public String decode(String mess, RSA_lib rsa) {
	   Log.w("RECIVED MESS", mess);
	   String[] tmp = mess.split(" ");
	   String result = "";
	   for(int i = 0; i < tmp.length; i++) {
		   //char letter = (char)(rsa.decrypt(new BigInteger(tmp[i])).intValue() + 848);
		   char letter = (char)rsa.decrypt(new BigInteger(tmp[i])).intValue();
		   Log.v("LETTER CODE", (rsa.decrypt(new BigInteger(tmp[i])).intValue()) + "");
		   result += letter;//new String(String.valueOf(letter).getBytes(), "UTF-8");
	   }
	   return result;
   }
   
   private BigInteger encrypt(BigInteger message) {
      return message.modPow(publicKey, modulus);
   }

   private BigInteger decrypt(BigInteger encrypted) {
      return encrypted.modPow(privateKey, modulus);
   }

   public void setPrivateKey(BigInteger privateKey) {
       this.privateKey = privateKey;
   }
   
   public void setPublicKey(BigInteger publicKey) {
       this.publicKey = publicKey;
   }
   
   public void setModulus(BigInteger modulus) {
       this.modulus = modulus;
   }
   
   public BigInteger getPrivateKey() {
       return privateKey;
   }
   
   public BigInteger getPublicKey() {
       return publicKey;
   }
   
   public BigInteger getModulus() {
       return modulus;
   }
   
   public String toString() {
      String s = "";
      s += "public  = " + publicKey  + "\n";
      s += "private = " + privateKey + "\n";
      s += "modulus = " + modulus;
      return s;
   }
}