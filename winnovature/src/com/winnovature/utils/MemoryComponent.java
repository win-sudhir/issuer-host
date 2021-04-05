package com.winnovature.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;

import org.apache.log4j.Logger;


public class MemoryComponent {
	static Logger logger = Logger.getLogger(MemoryComponent.class.getName());
	
	public static void closeZIPOutputStream(ZipOutputStream stream) {
		try {
			if (stream != null)
				stream.close();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}
    public static void closeFileOutputStream(FileOutputStream stream) {
		try {
			if (stream != null)
				stream.close();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}
    public static void closeFileInputStream(FileInputStream stream) {
		try {
			if (stream != null)
				stream.close();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}
    
    public static void closePrintWriter(PrintWriter pw) {
		try {
			if (pw != null)
				pw.flush();
				pw.close();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}
    
    public static void closeBufferedReader(BufferedReader br) {
		try {
			if (br != null)
				br.close();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}
    public static void closeOutputStream(OutputStream stream) {
		try {
			if (stream != null)
				stream.flush();
				stream.close();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}
    public static void closeServletOutputStream(ServletOutputStream stream) {
		try {
			if (stream != null)
				stream.flush();
				stream.close();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}
    public static void closeOutputStreamWriter(OutputStreamWriter stream) {
		try {
			if (stream != null)
				stream.flush();
				stream.close();
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
	}
    
}
