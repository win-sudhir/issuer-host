/**
 * 
 */
package com.winnovature.utils;


public class FileTransferUtility {

	
	public static String moveDocument(String srcFileName, String dstFileName, String custId) throws Exception {
		
		String srcFile = srcFileName;
		String srcFilePath = "";//TMP_DIRECTORY;
		
		String extension = srcFileName.substring(srcFileName.lastIndexOf("."));
		
		String dstFile = dstFileName + extension;
		String dstFilePath = "";//PEM_DIRECTORY + custId + "/";
		
		System.out.println("src : "+srcFilePath+srcFile);
		System.out.println("dest : "+dstFilePath+dstFile);
		//sSFTPUtil.moveFile(srcFile, srcFilePath, dstFile, dstFilePath); 
		
		return dstFilePath + dstFileName;
	}
}
