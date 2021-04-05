package com.winnovature.utils;

import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class IssuerPropertyReader {
	
	static Logger log = Logger.getLogger(IssuerPropertyReader.class.getName());
	static String propFilename = "issuer_env.properties";
	static Properties issuerProps = new Properties();

	static {
		loadPropFile();
	}
	// returns from Acquirer.prop first and the from regular props
	public static String getPropertyValue(String propertyName) {
		//loadPropFile();
		String url = issuerProps.getProperty(propertyName);
		if (url == null || url.isEmpty()) {
			// look in regular file
			url = getProperty(propertyName);
			if (url == null || url.isEmpty())
				throw new RuntimeException("Issuer - cannot find property " + propertyName + " in file " + propFilename);
			return url;
		} 
		return url;
	}

	
	// returns from Acquirer.props
	public static String getProperty(String key) {
		return issuerProps.getProperty(key);
	}

	// loads propfile
	private static void loadPropFile() {
		//String ev = System.getenv("NETCISSHOME");
		String ev = System.getProperty("NETCISSHOME");
		log.info("Before EV");
		log.info("Values EV "+ev);
		if (ev == null || ev.isEmpty())
			throw new RuntimeException("ISSUER - NETCISSHOME env var is not set. cannot start");
		String pfilename = ev + "/config/" + propFilename;
		log.info("PROPERTY FILE PATH : "+pfilename);
		try {
			FileInputStream is = new FileInputStream(pfilename);
			issuerProps.load(is);
		} catch (Exception e) {
			log.error("ISSUER: exception loading properties file " + pfilename, e);
			throw new RuntimeException(e);
		}
		
	}

	
	public static void main(String[] args) {
		String propertyValue = getPropertyValue("otpURL");
		log.info("propertyValue : "+propertyValue);
	}
}
