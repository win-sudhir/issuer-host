package com.winnovature.service;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLParser {
	static Logger log = Logger.getLogger(XMLParser.class.getName());

	public String createFinalResponse(String xmlData, String ErrorMessage) {
		String sResp = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xmlData));
			Document document = builder.parse(is);
			document.getDocumentElement().normalize();
			log.info("Root element :" + document.getDocumentElement().getNodeName());
			NodeList nlist = document.getChildNodes();
			if (nlist.getLength() > 0) {
				JSONObject json = new JSONObject();
				
				NodeList nList = nlist.item(0).getChildNodes();
				for (int i = 0; i < nList.getLength(); i++) {
					Node nNode1 = nList.item(i);
					json.put(nNode1.getNodeName(), nNode1.getTextContent());
				}
				sResp = json.toString();
			}

		} catch (Exception e) {
			log.error(e);
			e.printStackTrace();
			sResp = new ErrorResponse().generateError(e.getMessage(), "NA", "-1");
		}
		log.info("Final Response :" + sResp);
		return sResp;

	}

	/*public static void main(String[] args) {
		String xmlData = "<WalletDetails><name>Rahul</name><mobileNo>9833700965</mobileNo><emailId>rahul_r_singh@hotmail.com</emailId><balance>2831.00</balance></WalletDetails>";
		new XMLParser().createFinalResponse(xmlData, "document");

	}*/

}
