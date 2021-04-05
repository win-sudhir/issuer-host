
package com.winnovature.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;


public class DateUtility {
	static Logger log = Logger.getLogger(DateUtility.class.getClass());
	
	public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies, TimeUnit.DAYS);
	}
	
	public static String addOneDay(String date, int i) {
		return LocalDate.parse(date).plusDays(i).toString();
	}
	
	public static String getCurrentTime(String pattern) {
		return new SimpleDateFormat(pattern).format(new Date());
	}
	
	public static String getDateFormatInPattern(String pattern, String date) {
		try{
			if(pattern.equals("yyMMddHHmmss")){
				Date newDate = new SimpleDateFormat("yyMMddHHmmss").parse(date);
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
			}
			if(pattern.equals("yyMMdd")){
				Date newDate = new SimpleDateFormat("yyMMdd").parse(date);
				return new SimpleDateFormat("yyyy-MM-dd").format(newDate);
			}
			if(pattern.equals("dd/MM/yyyy")){
				Date newDate = new SimpleDateFormat("yyMMdd").parse(date);
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(newDate);
			}
			if(pattern.equals("yyyy-MM-dd HH:mm:ss")){
				Date newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
				return new SimpleDateFormat("yyMMddHHmmss").format(newDate);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getDateFormatDate(String pattern, Date date) {
		try{
			if(pattern.equals("yyMMddHHmmss")){
				return new SimpleDateFormat(pattern).format(date);
			}
			if(pattern.equals("yyMMdd")){
				return new SimpleDateFormat(pattern).format(date);
			}
			if(pattern.equals("dd/MM/yyyy")){
				return new SimpleDateFormat(pattern).format(date);
			}
			if(pattern.equals("yyyy-MM-dd")){
				return new SimpleDateFormat(pattern).format(date);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	//to get parsed date
	public static Date getParsedDate(String pattern, String date) {
		try{
			if(pattern.equals("yyMMddHHmmss")){
				return new SimpleDateFormat(pattern).parse(date);
			}
			if(pattern.equals("yyyy-MM-dd HH:mm:ss")){
				return new SimpleDateFormat(pattern).parse(date);
			}
			if(pattern.equals("dd/MM/yyyy")){
				return new SimpleDateFormat(pattern).parse(date);
			}
			if(pattern.equals("yyyy-MM-dd")){
				return new SimpleDateFormat(pattern).parse(date);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static boolean validateDate(String date, String datePattrn){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(datePattrn);
		try{
			simpleDateFormat.parse(date); 
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	public static String getDD_MM_YYYY_format(String oldDateFormat)
	{
		try {
			//log.info("Original Date Format :: "+oldDateFormat);
			oldDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(oldDateFormat));
			//log.info("New Date Format(dd-MM-yyyy HH:mm:ss) :: "+oldDateFormat);
			return oldDateFormat;
		} catch (ParseException e) {
			log.info("Exception while parsing date :: "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getJulianDateFormat(String date){
		try {
			return new SimpleDateFormat("yyDDD").format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
		} catch (ParseException e) {
			log.info("Error getting julian date "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getJulianDateOfDate(String format){
		try {
			return new SimpleDateFormat(format).format(new Date());
		} catch (Exception e) {
			log.info("Error getting julian date "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getNewFormattedDate(String newPattern, String currentPattern, String date)
	{
		try {
			String newDate = new SimpleDateFormat(newPattern).format(new SimpleDateFormat(currentPattern).parse(date));
			//log.info("New Date Format :: "+newDate);
			return newDate;
		} catch (ParseException e) {
			log.info("Exception while parsing date :: "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getpreviousDate(String sDate){
		String result = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date reconDate = dateFormat.parse(sDate);
			// Use the Calendar class to subtract one day
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(reconDate);
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			Date previousDate = calendar.getTime();
			result = dateFormat.format(previousDate);
		} catch (Exception e) {
			log.info("Exception while parsing date :: "+e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public static Date getReconDate(String date){
		try {
			SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date parsedDate = originalFormat.parse(date);
			date = targetFormat.format(parsedDate);
			Date newDate = targetFormat.parse(date);
			return newDate;
		} catch (Exception e) {
			log.info("Exception while parsing date :: "+e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) {
		//System.out.println(validateDate("202015-20", "yyMMddHHmmss"));
		//getFormattedDate("dd-MM-yyyy", "dd/MM/yyyy", "16-04-2020");
		getNewFormattedDate("dd/MM/yyyy", "dd-MM-yyyy", "16-04-2020");
		getNewFormattedDate("yyyy-MM-dd", "dd-MM-yyyy", "16-04-2020");
		getNewFormattedDate("yyyy-MM-dd HH:mm:ss", "yyMMddHHmmss", "200418230510");
	}
	
	
}
