package com.winnovature.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class NPCIXMLParser {
	static Logger log = Logger.getLogger(NPCIXMLParser.class.getName());

	public HashMap<String, String> parseData(String XMLData) throws Exception 
	{
		DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		HashMap<String, String> rdData = new HashMap<String, String>();
		Document doc = docBuilder.parse(new ByteArrayInputStream(XMLData.getBytes()));
		Element element = doc.getDocumentElement();
		log.info("Root : " + element.getTagName());
		NodeList list = element.getChildNodes();
		
		if (list != null) 
		{
			int length = list.getLength();
			for (int i = 0; i < length; i++) 
			{
				log.info("List item : " + list.item(i) + " ::: Node Type : " + list.item(i).getNodeType());
				if (list.item(i).getNodeType() == Node.ELEMENT_NODE) 
				{
					Element el = (Element) list.item(i);
					log.info("Tag : " + el.getTagName());
					log.info("Attributes\n");
					NamedNodeMap node = el.getAttributes();
					rdData.put(el.getTagName(), el.getTextContent());
					for (int j = 0; j < node.getLength(); j++) 
					{
						log.info(el.getTagName() + node.item(j).getNodeName() + " :: " + node.item(j).getTextContent());
						rdData.put(el.getTagName() + node.item(j).getNodeName(), node.item(j).getTextContent());
					}

					if (el.getTagName().equalsIgnoreCase("Txn"))
					{
						Element cElement = (Element) el.getElementsByTagName("Resp").item(0);
						NamedNodeMap cNodes = cElement.getAttributes();
						for (int p = 0; p < cNodes.getLength(); p++) 
						{
							log.info("....." + el.getTagName() + cElement.getNodeName() + cNodes.item(p).getNodeName()
									+ " :: " + cNodes.item(p).getTextContent());
							rdData.put(el.getTagName() + cElement.getNodeName() + cNodes.item(p).getNodeName(),
									cNodes.item(p).getTextContent());
						}

						if (cElement.getTagName().equalsIgnoreCase("Resp")) 
						{
							Element chElement = (Element) cElement.getElementsByTagName("Tag").item(0);

							if (chElement != null) 
							{
								rdData.put(el.getTagName() + cElement.getTagName() + chElement.getTagName() + "Result",
										chElement.getAttribute("result"));
								log.info("....." + el.getTagName() + cElement.getTagName() + chElement.getTagName()
										+ "Result ::: " + chElement.getAttribute("result"));
							}

							chElement = (Element) cElement.getElementsByTagName("Vehicle").item(0);

							if (chElement != null) {
								rdData.put(el.getTagName() + cElement.getTagName() + chElement.getTagName() + "errCode",
										chElement.getAttribute("errCode"));
								log.info(".....V" + el.getTagName() + cElement.getTagName() + chElement.getTagName()
										+ "errCode ::: " + chElement.getAttribute("errCode"));
							}
						}
					}
					
					if (el.getTagName().equalsIgnoreCase("Merchant")) 
					{
						Element cElement = (Element) el.getElementsByTagName("Lane").item(0);
						NamedNodeMap cNodes = cElement.getAttributes();
						for (int p = 0; p < cNodes.getLength(); p++) {
							log.info("....." + el.getTagName() + cElement.getNodeName() + cNodes.item(p).getNodeName()
									+ " :: " + cNodes.item(p).getTextContent());
							rdData.put(el.getTagName() + cElement.getNodeName() + cNodes.item(p).getNodeName(),
									cNodes.item(p).getTextContent());
						}
					}
					
					if (el.getTagName().equalsIgnoreCase("Payer")) 
					{
						Element cElement = (Element) el.getElementsByTagName("Amount").item(0);
						if (cElement != null) 
						{
							rdData.put(el.getTagName() + cElement.getTagName(), cElement.getAttribute("value"));
							log.info(el.getTagName() + "Amount >>>>" + cElement.getAttribute("value"));
						}
						
						cElement = (Element) el.getElementsByTagName("Resp").item(0);
						if (cElement != null) 
						{
							Element chElement = (Element) el.getElementsByTagName("Tag").item(0);

							if (chElement != null) 
							{
								rdData.put(el.getTagName() + cElement.getTagName() + chElement.getTagName() + "Result",
										chElement.getAttribute("result"));
								log.info(el.getTagName() + cElement.getTagName() + chElement.getTagName()
										+ "Result >>>>" + chElement.getAttribute("result"));
							}
						}
						
						
					} 
					
					else if (el.getTagName().equalsIgnoreCase("Merchant")) 
					{
						Element cElement = (Element) el.getElementsByTagName("ReaderVerificationResult").item(0);
						rdData.put(el.getTagName() + cElement.getTagName(), cElement.getAttribute("tsRead"));
						log.info(el.getTagName() + cElement.getTagName() + " >>>>" + cElement.getAttribute("tsRead"));
					} 
					
					else if (el.getTagName().equalsIgnoreCase("VehicleDetails")) 
					{
						rdData.remove("VehicleDetails");
						NodeList detailsList = el.getChildNodes();

						if (detailsList != null) 
						{
							log.info("Details List Length : " + detailsList.getLength());

							for (int dL = 0; dL < detailsList.getLength(); dL++) 
							{
								log.info("List item : " + detailsList.item(dL) + " ::: Node Type : "
										+ detailsList.item(dL).getNodeType());
								if (detailsList.item(dL).getNodeType() == Node.ELEMENT_NODE) {
									Element detailsElements = (Element) detailsList.item(dL);
									log.info("VehicleDetails : " + detailsElements.getTagName());
									log.info("Attributes : \n");
									NamedNodeMap detailsNode = detailsElements.getAttributes();

									log.info(detailsNode.item(0).getTextContent() + " :: "
											+ detailsNode.item(1).getTextContent());
									rdData.put(detailsNode.item(0).getTextContent() + i,
											detailsNode.item(1).getTextContent());

								}
							}
						}
					} 
					else if (el.getTagName().equalsIgnoreCase("Resp")) 
					{
						Element cElement = (Element) el.getElementsByTagName("Tag").item(0);

						if (cElement != null) 
						{
							rdData.put(el.getTagName() + cElement.getTagName() + "Result",
									cElement.getAttribute("result"));
							log.info(el.getTagName() + cElement.getTagName() + " >>>>"
									+ cElement.getAttribute("result"));
						}
					}

					log.info("Value :: " + el.getTextContent());

					log.info("----------------------------");
				}
			}

		}
		
		log.info("NPCIXMLParser.java ::: HashMap Size :: " + rdData.size() + " And HashMap :: " + rdData);
		return rdData;
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException 
	{
		String url = "http://npci.org/etc/schema/";
		log.info(url);
		/*
		 * String input =
		 * "<?xml version=\"1.0\" encoding=\"UTF-8\"?><etc:ReqPay xmlns:etc=\""
		 * +url+"\">" +
		 * "  <Head msgId=\"ACQ000000255353812\" orgId=\"ICIA\" ts=\"2018-03-03T11:30:10\" ver=\"1.0\"/> "
		 * +
		 * " <Merchant geoCode=\"11,11\" id=\"005002\" name=\"Khalapur Toll Plaza\" subtype=\"State\" type=\"Toll\">"
		 * +
		 * "<Lane direction=\"W\" id=\"E03\" readerId=\"\"/>        <Parking floor=\"\" readerId=\"\" slotId=\"\" zone=\"\"/>        <ReaderVerificationResult procRestrictionResult=\"\" publicKeyCVV=\"fRjV48ABl0/ug8IkPe3HRz6gP6rsYnCXOIK6r7/gNgO/Y+TCg7LdbCGr9L0EW6e3yjgG2eBuN6/Qj16d3R+jH8EDuXkAag6qTziSmOXqn3ZWZsC/ErDurxNOTZcTM1xUM/WA4stl0mEF5QD21xytSdlqN0iXo7Iv6qOWIOyyc+TGPEESjFchN8Teo1uXaKXxfqjqsKQGyn8D9yE0r3qX1vqtfHvQMTg/dZD1n18LPUF/oplWh4gPWTcpdnYh2ZMDE7JBHs4k1y1uiVUurnXkYRo9tigpZlfk4jluVYiY0UyhyPFB84gMgcnP43+963XMPtprh51Q1U8rZlHyAZGtBA==\" signAuth=\"NOT_VERIFIED\" signData=\"\" tagVerified=\"NON NETC TAG\" tsRead=\"2018-03-03T11:25:29\" txnCounter=\"3495\" txnStatus=\"SUCCESS\" vehicleAuth=\"UNKNOWN\"/>"
		 * +
		 * " </Merchant>    <Meta/>    <Payee addr=\"720301@iin.npci\" name=\"\" type=\"MERCHANT\"/>"
		 * +
		 * "<Payer addr=\"34161FA82033E6EC02001260@652150.iin.npci\" name=\"\" type=\"PERSON\"> "
		 * + " <Amount curr=\"INR\" value=\"138\"/>    </Payer> " +
		 * " <Resp ts=\"\" result=\"SUCCESS|FAILURE|PARTIAL\" respCode=\"\" totReqCnt=\"\" sucessReqCnt=\"\">"
		 * +
		 * "<Tag op=\"ADD\" tagId=\"\" seqNum=\"1\" result=\"SUCCESS|FAILURE\" errCode=\"\" />"
		 * +
		 * "<Tag op=\"REMOVE\" tagId=\"\" seqNum=\"2\" result=\"SUCCESS|FAILURE\" errCode=\"\" />"
		 * + "</Resp>" +
		 * " <Txn id=\"00117131781\" note=\"\" orgTxnId=\"\" refId=\"180303010500434\" refUrl=\"\" ts=\"2018-03-03T11:30:05\" type=\"DEBIT\"> "
		 * +
		 * " <RiskScores/>   </Txn>   <Vehicle TID=\"E200341201551D0002285D1C\" avc=\"VC4\" tagId=\"34161FA82033E6EC02001260\" wim=\"\"> "
		 * + "  <VehicleDetails> <Detail name=\"VEHICLECLASS\" value=\"VC4\"/> "
		 * +
		 * " <Detail name=\"REGNUMBER\" value=\"ryaSX6KSH+lrOhvqA9EMj31UbufIeJVlLtQ/Xh/jemoQb8AOfQJmGa1Am1L1XR2cck8a2nCcIGN/HUNkP+L3aSHwqUoQ0QfYtMOtySX/IXIUC1OgoFglZznxKfyZqtMZwW1cMIBqSm5BoiZNEGyccqlLraRazViS35O4/ZDhOpJ0nuS+W0H73R3F/qCw42UX/O6L497hInr5Lv9Xp/a74Kxhlct43FqiT3TWj3ScoX7QEAKuRyBJIz0KkOpjA1D0QTN0b3gVkOd4Hizd5MEiMFK8gOKwOI4i9PJrUKNYuM1/GLRvzr14fr1UWaCnbDQkcLWC2TWNCKy8aokM8nzdSQ==\"/> "
		 * +
		 * "<Detail name=\"TAGSTATUS\" value=\"A\"/> <Detail name=\"ISSUEDATE\" value=\"23-02-2018\"/> "
		 * +
		 * "<Detail name=\"EXCCODE\" value=\"00\"/> <Detail name=\"BANKID\" value=\"652150\"/>  <Detail name=\"COMVEHICLE\" value=\"F\"/> </VehicleDetails>  </Vehicle> "
		 * +
		 * "<Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>II3cng99i5lk7IfQ1C3+3x26oxqN8Acm3Hdq+/u4GQI=</DigestValue></Reference></SignedInfo><SignatureValue>vUoiatdvnuUDcgnqr/T4rDhFsOp7S8Irbk0IxCC8dM8xo+9ofmnVBrFxVFoAKsgsT/B8iqRBYlg8"
		 * +
		 * "d1BVrdq5BM9Y2xJULw2AGh/wx0P5CqUYwMlKctccpDx3dBybb8IqPXPhe/QqOPub7pZG9wStDCAx"
		 * +
		 * "XDPCpbmc130OEWoX5x04goZYMmRId0IaldxfSFnABdulHy5uWNGmoj7Un+gEYNto+0ihdvhjzVxk"
		 * +
		 * "b1RW/ULN6T0V6eHN3hPjvffav+tgZNFA3QMjkMIByIPH9ccKBBpGqHlCiAkmvi37YAJzB+bXunRA"
		 * +
		 * "THmze+8T2HEdGLH3hklcZRf3UuQbJ6BAdK+hjw==</SignatureValue><KeyInfo><X509Data><X509SubjectName>CN=netcsigning.npci.org.in</X509SubjectName><X509Certificate>MIIEzDCCA7SgAwIBAgIKVAqJnCLzCTWYdDANBgkqhkiG9w0BAQsFADCB9zELMAkGA1UEBhMCSU4x"
		 * +
		 * "RTBDBgNVBAoTPEluc3RpdHV0ZSBmb3IgRGV2ZWxvcG1lbnQgYW5kIFJlc2VhcmNoIGluIEJhbmtp "
		 * +
		 * "bmcgVGVjaG5vbG9neTEdMBsGA1UECxMUQ2VydGlmeWluZyBBdXRob3JpdHkxDzANBgNVBBETBjUw"
		 * +
		 * "MDA1NzEXMBUGA1UECBMOQW5kaHJhIFByYWRlc2gxKTAnBgNVBAkTIFJvYWQgTm8uMSwgTWFzYWIg"
		 * +
		 * "VGFuaywgSHlkZXJhYmFkMRUwEwYDVQQzEwxDYXN0bGUgSGlsbHMxFjAUBgNVBAMTDUlEUkJUIENB"
		 * +
		 * "IDIwMTQwHhcNMTYwODIzMDg1MjQwWhcNMTgwODIzMDg1MjQwWjAiMSAwHgYDVQQDExduZXRjc2ln"
		 * +
		 * "bmluZy5ucGNpLm9yZy5pbjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBANNwUyswG69R"
		 * +
		 * "f+DI5E2DKgkrXk8wEF6gADYAurDfHfN8R2aAH+26yUPet6kyFdW/NIH+KxoPmMh5YRZXIHMzxsT/"
		 * +
		 * "shFrwFGfDFP3pD6ecvYWFbFPCJy2kANnL+f/aq/6TOavy/nTP5MH2JIPuGchiJPR5TM3JMhgGMXN"
		 * +
		 * "uXiza1cYF4rhCttx7yocQw8IZH822gpXbXzt1lXIZBpMnXU3tXp09LKG+wgqyZmGM3jXcixLusSu "
		 * +
		 * "AonPXRNsBwhlmCQP+AHtDHLiPodD9+I1rQ3VsLoXuvCPdG9Fzjqd7Qvtw/wE6U9JxaLKBO9p9uou"
		 * +
		 * "iM76gj5Apr3czwANsjKumXYiUZcCAwEAAaOCASwwggEoMA4GA1UdDwEB/wQEAwIFoDAdBgNVHQ4E"
		 * +
		 * "FgQUfDyfcdZWEs9P1ZuADdwNWkzXNfEwHwYDVR0jBBgwFoAUgHUCNAfUXg7OBWq1rPAQa0IbB8cw"
		 * +
		 * "HQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUFBwMCMDgGA1UdIAQxMC8wLQYGYIJkZAIDMCMwIQYI"
		 * +
		 * "KwYBBQUHAgIwFRoTQ2xhc3MgMyBDZXJ0aWZpY2F0ZTAiBgNVHREEGzAZghduZXRjc2lnbmluZy5u"
		 * +
		 * "cGNpLm9yZy5pbjBZBgNVHR8EUjBQMCSgIqAghh5odHRwOi8vMTAuMC42NS42NS9jcmxfMjdCMC5j"
		 * +
		 * "cmwwKKAmoCSGImh0dHA6Ly9pZHJidGNhLm9yZy5pbi9jcmxfMjdCMC5jcmwwDQYJKoZIhvcNAQEL"
		 * +
		 * "BQADggEBAE32kS6ojjT+OEICWBBfi7lZQB4+vFe699G9P5Y9cDG5kb8MYwUhgjer+XmyWezwgRoY"
		 * +
		 * "cI8Z2LJxKhDeJ6DEqryBmy7wgdO8ARA45YdNZNU16VtRAehklCCc5U/SLaU02AjvhzAQehLDgP8H"
		 * +
		 * "viEkiV1JkuQpvec0JnaGTjMI3lUalPgpoWMK4rgEAVN125sABxiB03NeG71tySubQ9XYREHPBx3H"
		 * +
		 * "XvTaOHnPZxjUKUguvlII1StZxTcBOOfHM729IctKLzVBwPREXLIKyBWtpf7nHeYfSrb0rtmBKdxf"
		 * +
		 * "Jhp1hUiD5ylUXaEHhdUlmCk4SpPxx/EK839wWgokDM0l3R0=</X509Certificate></X509Data></KeyInfo></Signature></etc:ReqPay>";
		 */

		/*
		 * String input =
		 * "<Txn id=\"00000000001234560301\" note=\"\" orgTxnId=\"\" refId=\"\" refUrl=\"\" ts=\"2016-08-10T14:28:22\" type=\"FETCH\">"
		 * +"<Resp respCode=\"000\" result=\"SUCCESS\" successReqCnt=\"1\" totReqCnt=\"1\" ts=\"2016-08-10T19:57:02\">"
		 * +"<Vehicle errCode=\"000\">" +"<VehicleDetails>"
		 * +"<Detail name=\"TAGID\" value=\"34161FA82032D69802008A60\"/>"
		 * +"<Detail name=\"REGNUMBER\" value=\"ZZ00BB007\"/>"
		 * +"<Detail name=\"TID\" value=\"34161FA82032D69802008A60\"/>"
		 * +"<Detail name=\"VEHICLECLASS\" value=\"VC1\"/>"
		 * +"<Detail name=\"TAGSTATUS\" value=\"A\"/>"
		 * +"<Detail name=\"ISSUEDATE\" value=\"2016-06-11\"/>"
		 * +"<Detail name=\"EXCCODE\" value=\"01\"/>"
		 * +"<Detail name=\"BANKID\" value=\"617292\"/>"
		 * +"<Detail name=\"COMVEHICLE\" value=\"F\"/>" +"</VehicleDetails>"
		 * +"</Vehicle>" +"</Resp>" +"</Txn>";
		 */

		String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<etc:RespMngException xmlns:etc=\"http://npci.org/etc/schema/\">"
				+ "    <Head msgId=\"Msg201\" orgId=\"DCBX\" ts=\"2016-07-04T21:18:38\" ver=\"1.0\"/>"
				+ "    <Txn id=\"00000000000000022021\" note=\"\" orgTxnId=\"\" refId=\"\" refUrl=\"\" ts=\"2016-07-04T21:18:38\" type=\"ManageException\">"
				+ "        <Resp respCode=\"000\" result=\"SUCCESS\" sucessReqCnt=\"1\" totReqCnt=\"1\" ts=\"2016-08-10T19:26:23\">"
				+ "            <Tag errCode=\"000\" op=\"ADD\" result=\"SUCCESS\" seqNum=\"2\" tagId=\"34161FA82032D6980202B680\"/>"
				+ "        </Resp>" + "    </Txn>" + "</etc:RespMngException>";

		try {
			/*
			 * log.info(input.substring(input.indexOf("<Vehicle"),input.indexOf(
			 * "</Vehicle>")+10));
			 */
			Map<String, String> parsedXml = new NPCIXMLParser().parseData(input);
			log.info(parsedXml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
