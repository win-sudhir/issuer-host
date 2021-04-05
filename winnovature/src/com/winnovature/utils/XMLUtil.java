package com.winnovature.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class XMLUtil {

	//static Logger log = Logger.getLogger(XMLUtil.class);

	public static String xmlMarshal(Object object) {

		try {

			JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty("com.sun.xml.internal.bind.xmlHeaders",
					"<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			StringWriter sw = new StringWriter();

			jaxbMarshaller.marshal(object, sw);

			return sw.toString();

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Object xmlUnmarshal(String inputXML, Class<?> cls) {

		try {

			InputStream stream = new ByteArrayInputStream(inputXML.getBytes());
			InputSource is = new InputSource(stream);
			is.setEncoding("UTF-8");

			JAXBContext jaxbContext = JAXBContext.newInstance(cls);
			Unmarshaller unmarshallerObj = jaxbContext.createUnmarshaller();
			return unmarshallerObj.unmarshal(is);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String prepareNETCReqSignedXML(String xmlData) throws Exception {
		try {
/////////////////////////////////////commented by sud/////////////////////			
			String alias = "";
					//Config.CERT_ALIAS;
			//String filePath = Config.CERT_PATH; // "C://netc_cert//mynew.jks";
			String password =""; 
					//Config.CERT_PWD;

			// Creamos XML Signature Factory
			FileInputStream is = new FileInputStream("");
					//Config.CERT_PATH);
///////////////////////////////////////////////////////////////////////////////			 

			KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
			keystore.load(is, password.toCharArray());

			KeyPair kep = null;
			Key key = keystore.getKey(alias, password.toCharArray());
			if (key instanceof PrivateKey) {
				// Get certificate of public key
				Certificate cert = keystore.getCertificate(alias);

				//log.info("SignXMLData.java ::: createSignedDataNew() :: Get Keys");
				// Get public key

				PublicKey publicKey = cert.getPublicKey();

				// Return a key pair
				kep = new KeyPair(publicKey, (PrivateKey) key);
				// (PrivateKey) key;
			}

			X509Certificate x509C = (X509Certificate) keystore.getCertificate(alias);

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			InputSource source = new InputSource(new StringReader(xmlData));
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document docXML = db.parse(source);

			XMLSignatureFactory xmlSigFactory = XMLSignatureFactory.getInstance("DOM");
			DOMSignContext domSignCtx = new DOMSignContext(kep.getPrivate(), docXML.getDocumentElement());
			Reference ref = null;
			SignedInfo signedInfo = null;

			// Transformadores

			List<Transform> transforms = new ArrayList<Transform>();
			transforms.add(xmlSigFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));
			// transforms.add(xmlSigFactory.newTransform(Transform.XPATH, new
			// XPathFilterParameterSpec("ancestor-or-self::*[local-name()='" + XPathFilter +
			// "']")));

			ref = xmlSigFactory.newReference("", xmlSigFactory.newDigestMethod(DigestMethod.SHA256, null), transforms,
					null, null);

			SignatureMethod sm = xmlSigFactory.newSignatureMethod("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256",
					null);

			signedInfo = xmlSigFactory.newSignedInfo(
					xmlSigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
							(C14NMethodParameterSpec) null),
					xmlSigFactory.newSignatureMethod(sm.getAlgorithm(), null), Collections.singletonList(ref));

			// Pasamos la llave publica (.cer)

			KeyInfoFactory kif = xmlSigFactory.getKeyInfoFactory();
			List<X509Certificate> x509Content = new ArrayList<X509Certificate>();
			x509Content.add(x509C);
			X509Data xd = kif.newX509Data(x509Content);
			KeyInfo ki = kif.newKeyInfo(Collections.singletonList(xd));

			// Creamos un nuevo XML Signature

			XMLSignature xmlSignature = xmlSigFactory.newXMLSignature(signedInfo, ki);

			// Firmamos el documento

			try {
				xmlSignature.sign(domSignCtx);
			} catch (Exception ex) {
				//log.error("SignXMLData.java ::: createSignedDataNew() :: Exception Occurred while Signing : ", ex);
				ex.printStackTrace();
			}
			// OutputStream os = new FileOutputStream("C://netc_cert//mysignedDoc.xml");
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			// trans.transform(new DOMSource(docXML), new StreamResult(os));
			// Grabamos el documento firmado
			trans.transform(new DOMSource(docXML), result);
			//log.info("SignXMLData.java ::: createSignedDataNew() :: Signed Data : " + writer.toString());
			return writer.toString();
		} catch (Exception ex) {
			//log.error("SignXMLData.java ::: createSignedDataNew() :: Exception Occurred in Main Catch Block : ", ex);
			ex.printStackTrace();
		}

		return null;
	}

	public static boolean validateNETCResSignedXML(String xmlData) throws Exception {

		FileInputStream fin=null;
		InputStream stream=null;
		try {

			stream = new ByteArrayInputStream(xmlData.getBytes());
			// InputSource is = new InputSource(inputXML);
			InputSource is = new InputSource(stream);
			is.setEncoding("UTF-8");
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			Document doc = dbf.newDocumentBuilder().parse(is);
			
			NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");

			if (nl.getLength() == 0) {
				System.out
						.println("SignatureValidation.java ::: validateSignedData() :: Cannot find Signature element");

				throw new Exception("Cannot find Signature element");
			}

			XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
//////////////////////////////commented by sud
			//fin = new FileInputStream(Config.NETC_CERT_PATH);
			CertificateFactory f = CertificateFactory.getInstance("X.509");
			X509Certificate certificate = (X509Certificate) f.generateCertificate(fin);
			PublicKey publicKey = certificate.getPublicKey();

			DOMValidateContext valContext = new DOMValidateContext(publicKey, nl.item(0));

			XMLSignature signature = fac.unmarshalXMLSignature(valContext);

			boolean coreValidity = signature.validate(valContext);

			if (coreValidity == false) {
				System.out.println(
						"SignatureValidation.java ::: validateSignedData() :: Signature Failed Core Validation");

				boolean sv = signature.getSignatureValue().validate(valContext);

				System.out.println(
						"SignatureValidation.java ::: validateSignedData() :: Signature Value Validation Status : "
								+ sv);

				if (sv == false) {
					// Check the validation status of each Reference.
					Iterator<?> i = signature.getSignedInfo().getReferences().iterator();
					for (int j = 0; i.hasNext(); j++) {
						boolean refValid = ((Reference) i.next()).validate(valContext);
						System.out.println("SignatureValidation.java ::: validateSignedData() :: ref[" + j
								+ "] validity status : " + refValid);
					}
				}

				return signature.getSignatureValue().validate(valContext);
			} else {
				System.out.println(
						"SignatureValidation.java ::: validateSignedData() :: Signature Passed Core Validation");
				return true;
			}
		} catch (Exception ex) {
			// log.error("SignatureValidation.java ::: validateSignedData() :: Exception
			// Occurred in Main Catch Block : ",ex);
			ex.printStackTrace();
		} finally {

			try{
				if(fin!=null) fin.close();
			}catch(Exception e ) { e.printStackTrace(); }
			
			try {
				stream.close();
			}catch(Exception e) { e.printStackTrace(); }
			
			
		}

		return false;
	}
}