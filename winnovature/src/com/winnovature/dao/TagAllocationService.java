package com.winnovature.dao;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TagAllocationService {
	static Logger log = Logger.getLogger(TagAllocationService.class.getName());

	public JSONObject parseTagAllocationProcess(String XMLData) {

		try {
			String txnID = "0", seqNO = null;
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(XMLData));

			Document doc = db.parse(is);
			// for Resp node
			NodeList respnode = doc.getElementsByTagName("Resp");
			Element respelement = (Element) respnode.item(0);
			NamedNodeMap respattributes = respelement.getAttributes();
			for (int i = 0; i < respattributes.getLength(); i++) {
				Attr attr = (Attr) respattributes.item(i);
				String attrName = attr.getNodeName();
				// String attrValue = attr.getNodeValue();
				if (attrName.equalsIgnoreCase("respCode")) {
					String respCode = attr.getNodeValue();
					log.info("TagAllocationService mngTagApiCall() respCode : " + respCode);
					break;
				}
			}
			// for tag node
			NodeList exception = doc.getElementsByTagName("Tag");
			Element element = (Element) exception.item(0);
			NamedNodeMap attributes = element.getAttributes();
			String errCode = null, result = null;
			// boolean flag = false;
			for (int i = 0; i < attributes.getLength(); i++) {
				Attr attr = (Attr) attributes.item(i);
				String attrName = attr.getNodeName();
				// String attrValue = attr.getNodeValue();
				if (attrName.equals("errCode")) {
					errCode = attr.getNodeValue();
				}
				if (attrName.equals("result")) {
					result = attr.getNodeValue();
				}
				if (attrName.equals("seqNum")) {
					seqNO = attr.getNodeValue();
				}

			}
			if ((errCode.equals("000") || errCode.equals("240")) && result.equalsIgnoreCase("SUCCESS")) {
				log.info("TagAllocationService Result Success...");
				NodeList txn = doc.getElementsByTagName("Txn");
				Element txnelement = (Element) txn.item(0);
				NamedNodeMap txnattributes = txnelement.getAttributes();
				for (int i = 0; i < txnattributes.getLength(); i++) {
					Attr attr = (Attr) txnattributes.item(i);
					String attrName = attr.getNodeName();
					// String attrValue =
					// attr.getNodeValue();
					if (attrName.equals("id")) {
						txnID = attr.getNodeValue();
						log.info("txnID : " + txnID);
					}
				}
			}
			log.info("errCode : " + errCode + " result : " + result + " seqNO : " + seqNO + " txnID : " + txnID);
			log.info("TagAllocationService.java ::: Root element of the response is :: "
					+ doc.getDocumentElement().getNodeName());
			JSONObject serviceResp = new JSONObject();
			serviceResp.put("errCode", errCode);
			serviceResp.put("result", result);
			serviceResp.put("seqNO", seqNO);
			serviceResp.put("txnID", txnID);
			return serviceResp;
		} catch (Exception e) {
			log.info("Exception in TagAllocationService :: " + e.getMessage());
		}
		return null;
	}

	
}
