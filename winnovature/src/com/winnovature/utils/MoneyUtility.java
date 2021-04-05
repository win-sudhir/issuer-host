/**
 * 
 */
package com.winnovature.utils;


public class MoneyUtility {
	public static String getPaiseFromRupees(String rupees){
		if(!rupees.contains(".")){
			long paisa = 100*Long.valueOf(rupees);
			return String.valueOf(paisa);
		}
		if(rupees.contains(".")){
			float paisa = 100*Float.valueOf(rupees);
			return String.valueOf(paisa).substring(0, String.valueOf(paisa).indexOf("."));
		}
		return null;
	}
	
	public static String getRupeesFromPaisa(String paisa){
		if(paisa!=null){
			long rupees = Long.valueOf(paisa)/100;
			return String.valueOf(rupees);
		}
		return null;
	}
	
	public static String getRupeesToRupees(String value){
		if(!value.contains(".")){
			return value;
		}
		if(value.contains(".")){
			String rupees = String.valueOf(value).substring(0, String.valueOf(value).indexOf("."));
			String pai = String.valueOf(value).substring(String.valueOf(value).indexOf("."));
			//System.out.println(pai);
			if(Double.valueOf(pai)<=0){
				//System.out.println("true");
				return rupees;
			}else{
				return value;
			}
			//double conversion = 100/Double.valueOf(pai);
			//System.out.println(conversion);
			//.substring(0, String.valueOf(rupees).indexOf("."));
		}
		return null;
	}
	
	public static void main(String[] args) {
		String s = getPaiseFromRupees("20.25");
		System.out.println(s);
		String s1 = getRupeesFromPaisa("1000");
		//String s = getRupeesFromPaisaFromRupees("200.00");
		//String s = getRupeesToRupees("2002.00");
		System.out.println(s1);
		//String s1 = "1003.20";
		//System.out.println(s1.substring(0,s1.indexOf(".")));
	}
	
}
