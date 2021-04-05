/**
 * 
 */
package com.winnovature.utils;

import java.io.BufferedReader;

import javax.servlet.http.HttpServletRequest;


public class RequestReaderUtility {
	public static StringBuffer getStringBufferRequest(HttpServletRequest request) {
		BufferedReader reader = null;
		try {
			StringBuffer stringBuffer = new StringBuffer();
			String line = null;
			reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				stringBuffer.append(line);
			}
			return stringBuffer;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MemoryComponent.closeBufferedReader(reader);
		}
		return null;
	}
}
