package com.winnovature.constants;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IDGenerator {
	public String getWalletId() {
		//String id;
		//String jdate = new SimpleDateFormat("yyMMDDDHHMMSSSSSS").format(new Date());
		//id = "W" + jdate; 
		//return id;
		
		return "W" + new SimpleDateFormat("yyMMDDDHHMMSSSSSS").format(new Date());
	}
	public String getCustomerId() {
		//String id;
		//String jdate = new SimpleDateFormat("yyMMDDDHHMMSSSSSS").format(new Date());
		//String number = Double.toString(Math.random()); -- Not Used
		//id = "C" + jdate;
		//return id;
		
		return "C" + new SimpleDateFormat("yyMMDDDHHMMSSSSSS").format(new Date());
	}
	public String getTagSupplierId() {
		String id;
		String jdate = new SimpleDateFormat("yyMMDDDHHMMSSSSSS").format(new Date());
		id = "SP" + jdate;
		return id;
	}
	
	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final String ALPHA_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final String NUMERIC_STRING = "0123456789";
	
	public static String randomAlphaNumeric(int count) 
	{
		StringBuilder builder = new StringBuilder();
		while (count-- != 0)
		{
			int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	public static String randomAlpha(int count) 
	{
		StringBuilder builder = new StringBuilder();
		while (count-- != 0)
		{
			int character = (int)(Math.random()*ALPHA_STRING.length());
			builder.append(ALPHA_STRING.charAt(character));
		}
		return builder.toString();
	}
	
	public static String randomNumeric(int count) 
	{
		StringBuilder builder = new StringBuilder();
		while (count-- != 0)
		{
			int character = (int)(Math.random()*NUMERIC_STRING.length());
			builder.append(NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
	public String getUserId() {
		String id;
		String jdate = new SimpleDateFormat("yyMMDDDHHMMSSSSSS").format(new Date());
		id = jdate;
		return id;
	}
	public String getAgentId() {
		String id;
		String jdate = new SimpleDateFormat("yyMMDDDHHMMSSSSSS").format(new Date());
		id = "A"+jdate;
		return id;
	}
	
	public String getBranchId() {
		String id;
		String jdate = new SimpleDateFormat("yyMMDDDHHMMSSSSSS").format(new Date());
		id = "B"+jdate;
		return id;
	}
	
	public String getVirtualAccountNumber() {
		String id;
		String jdate = new SimpleDateFormat("yyMMDDDHHMMSSSSSS").format(new Date());
		id = jdate;
		return id;
	}
	
	public static void main(String[] args) {
		//System.out.println(getCustomerId());
	}
}
