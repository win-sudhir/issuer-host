package com.winnovature.utils;

public class EmailTemplate {
	public static String getEmailBody(String userId, String password) {
		String  bodyText = "<br><b>Greetings From UPC Bank !!!!!</b><br>";
  	  bodyText += "<br>Your Login details are as follows: <br>";
  	  bodyText += "<br><b>User/Login Id   	: </b>"+userId+"</i><br>";
  	  bodyText += "<br><b> Password 	: </b>"+password+"</i><br>";			    	  
  	  bodyText += "<br><a href = "+PropertyReader.getPropertyValue("loginPageURL")+"  > Click here </a> to Login to your portal.<br>";			   			    
  	  bodyText += "<br>If you have any questions, please contact us on <font color='red'>sales@gmail.com</font>.<br>";
  	  bodyText += "<br><br>Best Regards,</b>";
  	  bodyText += "<br>"+PropertyReader.getPropertyValue("webSiteLink")+"<br>";
  	  return bodyText;
	}
	
	public static String getUserEmailBody(String userId, String password) {
		String  bodyText = "<br><b>Greetings From UPC Bank !!!!!</b><br>";
  	  bodyText += "<br>Your Login details are as follows: <br>";
  	  bodyText += "<br><b>User/Login Id   	: </b>"+userId+"</i><br>";
  	  bodyText += "<br><b> Password 	: </b>"+password+"</i><br>";			    	  
  	  bodyText += "<br><a href = "+PropertyReader.getPropertyValue("loginPageURL")+"  > Click here </a> to Login to your portal.<br>";			   			    
  	  return bodyText;
	}
}
