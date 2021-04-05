package com.winnovature.service;

import org.apache.log4j.Logger;
import org.json.JSONObject;

public class ErrorResponse {
	static Logger log = Logger.getLogger(ErrorResponse.class.getName());

	JSONObject job;

	public String generateError(String message, String refnumber, String errorCode) {
		job = new JSONObject();
		try {
			job.put("status", errorCode);
			job.put("ref_number", refnumber);
			job.put("resp_msg", message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e);
			e.printStackTrace();
		}

		return job.toString();
	}

	public static void main(String args[]) {
		ErrorResponse errorResponse = new ErrorResponse();
		try {
			String arr[] = { "Resp_code", "ref_number", "error_msage" };
			JSONObject resp = new JSONObject(errorResponse.generateError("abc", "poi", "sdf"));
			for (int i = 0; i < arr.length; i++) {
				if (resp.has(arr[i]) && resp.getString(arr[i]) != null && !resp.getString(arr[i]).equals("")) {
					log.info(arr[i] + " :: " + resp.getString(arr[i]));
				}
			}

			// log.info(resp.has("resp_code")+" :: "+resp.has("resp_code1"));
		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
		}
	}

}
