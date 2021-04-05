package com.winnovature.utils;

import java.security.MessageDigest;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.winnovature.dao.LoginDao;


public class HashGenerator 
{
	static Logger log = Logger.getLogger(HashGenerator.class.getName()); 

	public static String sha256(String  baseString) 
	{
	    try{
	    	
	    	byte[] base = baseString.getBytes();
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base);//base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++)
	        {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }
	        
	        
	        log.info("HashGenerator.java ::  sha256() :: "+hexString.toString());
	        
	        return hexString.toString();
	    } catch(Exception ex){
	    	log.error("Exeption in sha256() "+ex); 
	    }
	    return null;
	}
	
	public  boolean validateData(String userId, JSONObject jsonObject) throws Exception
	{
		String hash = HashGenerator.sha256(jsonObject.toString());
		log.info("HashGenerator.java ::  validateData() "+hash);
		boolean isValid = LoginDao.isValidRequest(userId, hash);
		return isValid;		
	}
	
	/*public static boolean validateData(String userId, JSONObject jsonObject) {
		String hash = SHAPass.sha256(jsonObject.toString());
		boolean isValid = false;
		try{
			isValid = LoginDao.isValidRequest(userId, hash);
		}
		catch (Exception e) {
			log.error("Exeption in validateData() "+e); 
		}
		return isValid;		
	}*/
}
