package com.winnovature.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;

import com.winnovature.dto.WalletResponse;

public class Server2ServerCall
{	
	static Logger log = Logger.getLogger(Server2ServerCall.class.getName());
	
	static
	{
	    disableSslVerification();
	}
	
	public static String secureServerCall(String sURL, String data)
	{
		
		
		String line = null;	
		BufferedReader br = null;
		StringBuffer respString = null;
		
		log.info("Server2ServerCall.java ::: secureServerCall() :: Posting URL : " +sURL);
		
		try
		{
			URL obj = new URL(sURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	
			//add reuqest header
		
			con.setRequestMethod("POST");
			//con.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
			//con.addRequestProperty("Content-Length", data.getBytes().length+"");
			con.setDoOutput(true);
			con.connect();
	
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			//log.info(new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(new Date())+" ::: Data length :: " + data.getBytes().length);
			wr.write(data);
			MemoryComponent.closeOutputStreamWriter(wr);
	
			respString = new StringBuffer(); 
			
			log.info("Server2ServerCall.java ::: secureServerCall() :: Response Code : "+con.getResponseCode());
			
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				br = new BufferedReader(new
						InputStreamReader(con.getInputStream()));
	
				while((line = br.readLine()) != null)
				{
					log.info("Server2ServerCall.java ::: secureServerCall() :: Response Received : "+line);
					respString.append(line);
				}
				
				MemoryComponent.closeBufferedReader(br);
				
				return respString.toString();
			}
		}
		catch (Exception e)
		{				
			log.error("Server2ServerCall.java ::: secureServerCall() :: Error Occurred while Processing Request : ",e);
			
		}
		
		return null;
	}
	
	public static String secureServerCall(String sURL)
	{
		
		
		String line = null;	
		BufferedReader br = null;
		StringBuffer respString = null;
		
		log.info("Server2ServerCall.java ::: secureServerCall() :: Posting URL : " +sURL);
		
		try
		{
			URL obj = new URL(sURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
	
			//add reuqest header
		
			con.setRequestMethod("POST");
			//con.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
			//con.addRequestProperty("Content-Length", data.getBytes().length+"");
			con.setDoOutput(true);
			con.connect();
	
			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			//log.info(new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(new Date())+" ::: Data length :: " + data.getBytes().length);
			//wr.write(data);
			MemoryComponent.closeOutputStreamWriter(wr);
	
			respString = new StringBuffer(); 
			
			log.info("Server2ServerCall.java ::: secureServerCall() :: Response Code : "+con.getResponseCode());
			
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				br = new BufferedReader(new
						InputStreamReader(con.getInputStream()));
	
				while((line = br.readLine()) != null)
				{
					log.info("Server2ServerCall.java ::: secureServerCall() :: Response Received : "+line);
					respString.append(line);
				}
				
				MemoryComponent.closeBufferedReader(br);
				
				return respString.toString();
			}
		}
		catch (Exception e)
		{				
			log.error("Server2ServerCall.java ::: secureServerCall() :: Error Occurred while Processing Request : ",e);
			
		}
		
		return null;
		
	}
	
	public static void main(String[] args) {
		String url = "https://htapi.winnovature.com/send?username=winauth&password=DG7tg3&from=WECARE&to=91"+"9021494615&content=Hi%20Sudhir";
		secureServerCall(url);
	}
	
	
	// Fleet API CAll 
	public static String httpServerCall(String URL, String data)
	{
		// TODO Auto-generated method stub
		
		String line = null;	
		BufferedReader br = null;
		StringBuffer respString = null;
		
		log.info("Server2ServerCall.java ::: httpServerCall() :: Posting URL : " +URL);
		
		try
		{
			URL url = new URL(URL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();

			con.setRequestMethod("POST");
			con.addRequestProperty("Content-Type", "application/json;charset=UTF-8");
			con.addRequestProperty("Content-Length", data.getBytes().length+"");
			
			(con).setRequestProperty("csrfPreventionSalt","2018CORPORATE");
		    (con).setRequestProperty("userId","OneMove");
		    
			con.setDoOutput(true);
			con.connect();

			OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
			wr.write(data);
			MemoryComponent.closeOutputStreamWriter(wr);
	
			respString = new StringBuffer(); 
			
			log.info("Server2ServerCall.java ::: httpServerCall() :: Response Code : "+con.getResponseCode());
			
			if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				br = new BufferedReader(new
						InputStreamReader(con.getInputStream()));
	
				while((line = br.readLine()) != null)
				{
					log.info("Server2ServerCall.java ::: httpServerCall() :: Response Received : "+line);
					respString.append(line);
				}
				
				MemoryComponent.closeBufferedReader(br);
				
				return respString.toString();
			}
		}
		catch (Exception e)
		{				
			log.error("Server2ServerCall.java ::: httpServerCall() :: Error Occurred while Processing Request : ",e);
			
		}
		
		return null;
	}
	
	
	
	
	
	private static void disableSslVerification()
	{
	    try
	    {
	        // Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[]
	        {
	        	new X509TrustManager()
		        {
		            public X509Certificate[] getAcceptedIssuers() 
		            {
		                return null;
		            }
		            public void checkClientTrusted(X509Certificate[] certs, String authType)
		            {
		            }
		            public void checkServerTrusted(X509Certificate[] certs, String authType)
		            {
		            }
		        }
	        };

	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier()
	        {
	            public boolean verify(String hostname, SSLSession session)
	            {
	                return true;
	            }
	        };

	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	    }
	    catch (Exception e) 
	    {
	        
	        log.error("Server2ServerCall.java ::: disableSslVerification() :: Error Occurred while disabling SSL Verification : ",e);
	    } 
	}


	public static WalletResponse sendToWallet(String xmlData,String sURL) throws Exception
	{
		// TODO Auto-generated method stub	
		log.info("Server2ServerCall.java ::: Posting URL : " + sURL);
		
		WalletResponse walletResponse = new WalletResponse();
		URL obj = new URL(sURL);
		
		
		//Use for HTTP Connection
 		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		//add request header
	
		con.setRequestMethod("POST");
		con.addRequestProperty("Content-Type", "text/plain;charset=UTF-8");
		con.setDoOutput(true);
		con.setRequestProperty("csrfPreventionSalt","2018CORPORATE");
		con.setRequestProperty("userId","OneMove");
		
		con.connect();
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(xmlData);
		wr.flush();
		wr.close();

		// set response code
		int responseCode = con.getResponseCode();
		walletResponse.setHttpCode(responseCode);
		
		StringBuffer response = new StringBuffer();
		
		
		log.info("Server2ServerCall.java ::: Response Code : " + responseCode);
		BufferedReader in = null;
		String inputLine = null;
		if(responseCode==200)
		{
			log.info("Server2ServerCall.java ::: responseCode : "+responseCode);
			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		}
		else if(responseCode==202)
		{
			log.info("Server2ServerCall.java :::  walletCall() :: responseCode : "+responseCode);
			in = null;
			response.append("NO DATA");
		}
		else
		{
			in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		
		if(in != null)
		{
			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			
			in.close();
			
			//print result
			
			log.info("Server2ServerCall.java ::: walletCall() :: Response Received From fleet System :: "+response.toString());
			walletResponse.setResponse(response.toString());
		}

		return walletResponse;
	}
}
