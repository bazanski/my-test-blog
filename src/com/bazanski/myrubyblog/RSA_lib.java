package com.bazanski.myrubyblog;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA_lib {
	//private final static BigInteger one      = new BigInteger("1");
    private final static SecureRandom random = new SecureRandom();
 
    private BigInteger privateKey;
    private BigInteger publicKey;
    private BigInteger modulus;
 
    public void init(int N) {
        BigInteger p = BigInteger.probablePrime(N/2, random);
        BigInteger q = BigInteger.probablePrime(N/2, random);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
 
        modulus    = p.multiply(q);                                  
        
        privateKey = publicKey.modInverse(phi);
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
    
    // generate an N-bit (roughly) public and private key
    RSA_lib() {
        publicKey  = new BigInteger("65537");     // common value in practice = 2^16 + 1
    }
 
    BigInteger encrypt(BigInteger message) {
        return message.modPow(publicKey, modulus);
    }
 
    BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, modulus);//.add(new BigInteger("848"));
    }
 
    public String toString() {
        String s = "";
        s += "public  = " + publicKey  + "\n";
        s += "private = " + privateKey + "\n";
        s += "modulus = " + modulus;
          
        return s;
    }
}
