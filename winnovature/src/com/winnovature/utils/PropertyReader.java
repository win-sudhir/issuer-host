package com.winnovature.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class PropertyReader
{
	static Logger log = Logger.getLogger(PropertyReader.class.getName());
	
	
	public static String getPropertyValue(String variableName) 
	{
		String propertyValue = null;
    	try {
            	Properties prop = new Properties();
            	//InputStream inputStream = FleetUrl.class.getClassLoader().getResourceAsStream("/configLocal.properties");
                InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream("/config.properties");
                
                prop.load(inputStream);
                propertyValue = prop.getProperty(variableName);
                log.info("variableName : "+variableName);
                log.info("propertyValue : "+propertyValue);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            return propertyValue;
	}
	
	/*
	 * public static String getUrlString(String url) { String finalURL = null; try {
	 * Properties prop = new Properties(); //InputStream inputStream =
	 * FleetUrl.class.getClassLoader().getResourceAsStream("/configLocal.properties"
	 * ); InputStream inputStream =
	 * PropertyReader.class.getClassLoader().getResourceAsStream(
	 * "/config71.properties");
	 * 
	 * prop.load(inputStream); finalURL = prop.getProperty(url);
	 * log.info("getUrlString value : "+url);
	 * log.info("Final UrlString value : "+finalURL); } catch (FileNotFoundException
	 * e) { e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
	 * 
	 * return finalURL; }
	 * 
	 */

	public static void main(String[] args)
	{
		
		try 
		{
			PropertyReader.getPropertyValue("xxxxxxxxxx");
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	

	public static String sha256(byte[] base) 
	{
	    try
	    {
	        MessageDigest digest = MessageDigest.getInstance("SHA-256");
	        byte[] hash = digest.digest(base);//base.getBytes("UTF-8"));
	        StringBuffer hexString = new StringBuffer();

	        for (int i = 0; i < hash.length; i++) 
	        {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) hexString.append('0');
	            hexString.append(hex);
	        }
	        
	        System.out.println(hexString.toString());
	        
	        return hexString.toString();
	    } 
	    catch(Exception ex)
	    {
	       throw new RuntimeException(ex);
	    }
	}
	
	
	
	public static synchronized String byteToHex(byte byData[])
    {
          StringBuffer sb = new StringBuffer(byData.length * 2);
          for (int i = 0; i < byData.length; i++)

          {
                 int v = byData[i] & 0xff;
                 if (v < 16)
                 {
                       sb.append('0');
                 }
                 sb.append(Integer.toHexString(v));
          }
          return sb.toString().toUpperCase();
    }
	

	
	
	public static String httpServerCall(String URL, String data)
	{
		String line = null;	
		BufferedReader br = null;
		StringBuffer respJson = null;
		
		System.out.println("PropertyReader.java ::: httpServerCall() :: Posting URL : " +URL+" , Request Data : "+data);
		try
		{
			URL url = new URL(URL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();

			con.setRequestMethod("POST");
			//con.addRequestProperty("Content-Type", "text/plain;charset=UTF-8");
			//con.addRequestProperty("Content-Length", data.getBytes().length+"");
			con.setDoOutput(true);
			con.connect();

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			//System.out.println(new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(new Date())+" ::: Data length :: " + data.getBytes().length);
			wr.write(data);
			wr.flush();

			respJson = new StringBuffer(); 
			
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				System.out.println("PropertyReader.java ::: httpServerCall :: HTTP OK");
				br = new BufferedReader(new
						InputStreamReader(con.getInputStream()));

				while((line = br.readLine()) != null)
				{
					System.out.println(line);
					respJson.append(line);
				}
				
				br.close();
				
				return respJson.toString();
			}
		}
		catch (Exception e)
		{	
			e.printStackTrace();
			System.out.println("PropertyReader.java ::: httpServerCall :: Error Occurred while Processing Request : "+e.getMessage());
		}
		
		return null;
	}
	
	public static String httpServerCallInactiveTag(String URL, String data)
	{
		String line = null;	
		BufferedReader br = null;
		StringBuffer respJson = null;
		
		System.out.println("PropertyReader.java ::: httpServerCallInactiveTag() :: Posting URL : " +URL+" , Request Data : "+data);
		try
		{
			URL url = new URL(URL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();

			con.setRequestMethod("POST");
			con.addRequestProperty("Content-Type", "text/plain;charset=UTF-8");
			con.addRequestProperty("Content-Length", data.getBytes().length+"");
			
			(con).setRequestProperty("csrfPreventionSalt","2018CORPORATE");
		    (con).setRequestProperty("userId","OneMove");
		    
			con.setDoOutput(true);
			con.connect();

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			//System.out.println(new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(new Date())+" ::: Data length :: " + data.getBytes().length);
			wr.write(data);
			wr.flush();

			respJson = new StringBuffer(); 
			
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				System.out.println("PropertyReader.java ::: httpServerCallInactiveTag() :: HTTP OK");
				br = new BufferedReader(new
						InputStreamReader(con.getInputStream()));

				while((line = br.readLine()) != null)
				{
					System.out.println(line);
					respJson.append(line);
				}
				
				br.close();
				
				return respJson.toString();
			}
		}
		catch (Exception e)
		{	
			e.printStackTrace();
			System.out.println("PropertyReader.java ::: httpServerCallInactiveTag() :: Error Occurred while Processing Request : "+e.getMessage());
		}
		
		return null;
	}
	
	public static String getLeftPaddedString(String sInputData, String padd, int iLength)
	{

		if (sInputData.length() > iLength)
		{
			sInputData = sInputData.substring(0, iLength);
		}
		else 
		{
			while(sInputData.length()<iLength)
			{
				sInputData = padd + sInputData;
			}
		}
		return sInputData;
	}
	
	public static String getVC(String vcCode)
  	{
		if(vcCode.equals("VC4")||vcCode.equals("VC5")||vcCode.equals("VC6")||vcCode.equals("VC7")||vcCode.equals("VC8")||vcCode.equals("VC9"))
			return "0"+vcCode.substring(2);
				
		else if(vcCode.equals("VC10"))
			return "0A";
		else if(vcCode.equals("VC11"))
			return "0B";
		else if(vcCode.equals("VC12"))
			return "0C";
		else if(vcCode.equals("VC13"))
			return "0D";
		else if(vcCode.equals("VC14"))
			return "0E";
		else if(vcCode.equals("VC15"))
			return "0F";
		else		
			return "10";
	}
	
	public static String retrieveUserid(HttpServletRequest req)
	{
	      String pathInfo = req.getPathInfo();
	      log.info("PropertyReader.java  :::  Path Info : "+pathInfo);
	      if (pathInfo.startsWith("/"))
	      {
	          pathInfo = pathInfo.substring(1);
	      }
	      return pathInfo.toString();
	 }

}
