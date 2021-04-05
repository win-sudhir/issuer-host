package com.winnovature.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtils {
	public String getCurrnetDate() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
	
	public static String getValidUptoDate(String createdOn) throws Exception{
		Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(createdOn); 
		String created = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date); 
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern ("yyyy-MM-dd HH:mm:ss"); //"M/d/uuuu" //uuuu-M-d HH:mm:ss
    	LocalDate localDate = LocalDate.parse (created , formatter);
    	LocalDate yearLater = localDate.plusYears (5);
    	String time = new SimpleDateFormat(" HH:mm:ss").format(date); 
    	String validUpto = yearLater.toString() + time;
    	return validUpto;
    }
}
