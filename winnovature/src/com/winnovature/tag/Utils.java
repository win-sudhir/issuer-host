package com.winnovature.tag;

import java.util.Random;
import java.math.BigInteger;
import java.security.SecureRandom;

public class Utils
{
    public String getPassword() {
        final SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32).substring(0, 8);
    }
    
    public String getSecretCode() {
        final SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString().substring(2, 8);
    }
    
    public static void main(final String[] args) {
        final SecureRandom random = new SecureRandom();
        System.out.println(new BigInteger(130, random).toString(32).substring(0, 8));
    }
    
    public static byte[] hex2ByteArray(final String sHexData) {
        final byte[] rawData = new byte[sHexData.length() / 2];
        for (int i = 0; i < rawData.length; ++i) {
            final int index = i * 2;
            final int v = Integer.parseInt(sHexData.substring(index, index + 2), 16);
            rawData[i] = (byte)v;
        }
        return rawData;
    }
    
    public static synchronized String byteToHex(final byte[] byData) {
        final StringBuffer sb = new StringBuffer(byData.length * 2);
        for (int i = 0; i < byData.length; ++i) {
            final int v = byData[i] & 0xFF;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase();
    }
    
    public static String getLeftPaddedString(String sInputData, String padd, int iLength)
	  {
	    
	    if (sInputData.length() > iLength)
	    {
	      sInputData = sInputData.substring(0, iLength);
	    }
	    else 
	    {
	    	while(sInputData.length()<iLength)
	    	{
	    		sInputData = padd + sInputData;
	    	}
	    }
	    return sInputData;
	  }
}
