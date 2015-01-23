package com.java.xml.saml;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallerFactory;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.KeyName;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.signature.Signer;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 */
public class KickTheTiresTest {

	private static final String WS_SECURITY_NS_URI = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

	private static final String START_XML = "<soapenv:Envelope xmlns:soapenv='http://schemas.xmlsoap.org/soap/envelope/' xmlns:xsd1='http://www.foobar.com/soapservice/xsd1'>"
			+ "<soapenv:Header>"
			+ "</soapenv:Header>"
			+ "<soapenv:Body wsu:Id='id-31752093' xmlns:wsu='http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssec urity-utility-1.0.xsd'>"
			+ "<xsd1:ListDirectory>"
			+ "<xsd1:directory>c:/</xsd1:directory>"
			+ "</xsd1:ListDirectory>"
			+ "</soapenv:Body>"
			+ "</soapenv:Envelope>";

	public static void main(String[] args) {
		try {
			KickTheTiresTest t = new KickTheTiresTest();
			String message = t.sign(START_XML, false);
			t.verify(message);
			System.out.println("assertion valid");
			t = new KickTheTiresTest();
			message = t.sign(START_XML, true);
			t.verify(message);
			System.out.println("assertion shouldn't be valid");
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	public String sign(String xml, boolean fail) throws Exception {
       
        dumpParserDetails();
        DefaultBootstrap.bootstrap();
        SOAPMessage message = createSoapMessage(xml);
        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        Name wsseHeaderName = soapEnvelope.createName("Security","wsse", WS_SECURITY_NS_URI);
        if (soapEnvelope.getHeader() == null) {
           soapEnvelope.addHeader();
        }
        SOAPHeaderElement securityElement =soapEnvelope.getHeader().addHeaderElement(wsseHeaderName);
        securityElement.addNamespaceDeclaration("ds","http://www.w3.org/2000/09/xmldsig#");

        Assertion assertion = (Assertion)
createSamlObject(Assertion.DEFAULT_ELEMENT_NAME);
        Namespace dsns = new Namespace("http://www.w3.org/2000/09/xmldsig#", "ds");
        assertion.addNamespace(dsns);
        Namespace xsins = new Namespace("http://www.w3.org/2001/XMLSchema-instance", "xsi");
        assertion.addNamespace(xsins);
        assertion.setVersion(SAMLVersion.VERSION_20);
        assertion.setID("123"); // in reality, must be unique for all assertions
        assertion.setIssueInstant(new DateTime());

        Issuer issuer = (Issuer) createSamlObject(Issuer.DEFAULT_ELEMENT_NAME);
        issuer.setValue("http://some.issuer.here");
        assertion.setIssuer(issuer);
       
        Subject subj = (Subject) createSamlObject(Subject.DEFAULT_ELEMENT_NAME);
        assertion.setSubject(subj);
       
        NameID nameId = (NameID) createSamlObject(NameID.DEFAULT_ELEMENT_NAME);
        nameId.setValue("ifauser");
        subj.setNameID(nameId);
       
        SubjectConfirmation subjConf = (SubjectConfirmation) createSamlObject(SubjectConfirmation.DEFAULT_ELEMENT_NAME);
        subjConf.setMethod("urn:oasis:names:tc:2.0:cm:holder-of-key");
        subj.getSubjectConfirmations().add(subjConf);
       
        SubjectConfirmationData subjData = (SubjectConfirmationData) createSamlObject(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
        subjData.getUnknownAttributes().put(new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi"), "saml:KeyInfoConfirmationDataType");
        subjConf.setSubjectConfirmationData(subjData);
       
        KeyInfo ki = (KeyInfo) createSamlObject(KeyInfo.DEFAULT_ELEMENT_NAME);
        subjData.getUnknownXMLObjects().add(ki);

        KeyName kn = (KeyName) createSamlObject(KeyName.DEFAULT_ELEMENT_NAME);
        kn.setValue("myclientkey");
        ki.getKeyNames().add(kn);        

        AttributeStatement as = (AttributeStatement) createSamlObject(AttributeStatement.DEFAULT_ELEMENT_NAME);
        assertion.getAttributeStatements().add(as);

        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        char[] password = "cspass".toCharArray();
        FileInputStream fis = new FileInputStream("c:/opt/keystores/clientKeystore.jks");
        ks.load(fis, password);
        fis.close();

        KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("myclientkey", new KeyStore.PasswordProtection("ckpass".toCharArray()));
        PrivateKey pk = pkEntry.getPrivateKey();
        X509Certificate certificate = (X509Certificate) pkEntry.getCertificate();

        BasicX509Credential credential = new BasicX509Credential();
        credential.setEntityCertificate(certificate);
        credential.setPrivateKey(pk);
        Signature signature = (Signature) createSamlObject(Signature.DEFAULT_ELEMENT_NAME);

        signature.setSigningCredential(credential);
 
        signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);
 
        signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

        KeyInfo keyinfo = (KeyInfo) createSamlObject(KeyInfo.DEFAULT_ELEMENT_NAME);
        signature.setKeyInfo(keyinfo);

        assertion.setSignature(signature);

        // marshall Assertion Java class into XML
        MarshallerFactory marshallerFactory = Configuration.getMarshallerFactory();
        Marshaller marshaller = marshallerFactory.getMarshaller(assertion);
        Element assertionElement = marshaller.marshall(assertion);

        try {
            Signer.signObject(signature);
        } catch (SignatureException e) {
            e.printStackTrace();
        }

 
        securityElement.appendChild(soapPart.importNode(assertionElement, true));
        String m = nodeToString(soapPart, fail);
        System.out.println(m);
        return m;
       
    }

	private XMLObject createSamlObject(QName qname) {
		return Configuration.getBuilderFactory().getBuilder(qname)
				.buildObject(qname);
	}

	private SOAPMessage createSoapMessage(String xml) throws SOAPException, IOException {
		MessageFactory factory = MessageFactory.newInstance();
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
		return factory.createMessage(null, bais);
	}

	private String nodeToString(Node node, boolean indent) throws Exception {

		StringWriter sw = new StringWriter();
		TransformerFactory tfactory = createTransformerFactory();
		Transformer transformer = tfactory.newTransformer();
		if (indent) {
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "2");
		}
		transformer.transform(new DOMSource(node), new StreamResult(sw));
		sw.close();
		return sw.toString();

	}

	private TransformerFactory createTransformerFactory() {

		TransformerFactory tfactory = TransformerFactory.newInstance();
		return tfactory;

	}

	private void verify(String xml) throws Exception {
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		char[] password = "sspass".toCharArray();
		FileInputStream fis = new FileInputStream("c:/opt/keystores/serviceKeystore.jks");
		ks.load(fis, password);
		fis.close();
		KeyStore.TrustedCertificateEntry tcEntry = (KeyStore.TrustedCertificateEntry) ks.getEntry("myclientkey", null);
		X509Certificate certificate = (X509Certificate) tcEntry.getTrustedCertificate();

		SOAPMessage message = createSoapMessage(xml);
		SOAPHeader soapHeader = message.getSOAPHeader();

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		xpath.setNamespaceContext(new NSContext());
		XPathExpression xpathExpr = xpath.compile("//saml:Assertion");

		NodeList result = (NodeList) xpathExpr.evaluate(soapHeader,XPathConstants.NODESET);
		if (result.getLength() == 0) {
			throw new RuntimeException("assertion not found");
		} else {
			Element assertionElement = (Element) result.item(0);
			UnmarshallerFactory unmarshallerFactory = Configuration.getUnmarshallerFactory();
			Unmarshaller unmarshaller = unmarshallerFactory.getUnmarshaller(assertionElement);
			Assertion samlAssertion = (Assertion) unmarshaller.unmarshall(assertionElement);
			samlAssertion.validate(true);
			Signature signature = samlAssertion.getSignature();
			SAMLSignatureProfileValidator pv = new SAMLSignatureProfileValidator();
			pv.validate(signature);
			BasicX509Credential credential = new BasicX509Credential();
			credential.setEntityCertificate(certificate);
			SignatureValidator sigValidator = new SignatureValidator(credential);
			sigValidator.validate(signature);
		}

	}

	private static void dumpParserDetails() {
		{
			DocumentBuilder builder = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			try {
				builder = factory.newDocumentBuilder();
				System.out.print("\nDocumentBuilderFactory Instance: "
						+ factory.getClass().getName()
						+ "\nDocumentBuilder Instance: "
						+ builder.getClass().getName() + "\n");
			} catch (Exception e) {
				System.out.println("Error processing DOM -- " + e.getMessage());
				e.printStackTrace(System.out);
			}
		}
		{
			SAXParser parser = null;
			SAXParserFactory factory = SAXParserFactory.newInstance();
			try {
				parser = factory.newSAXParser();
				System.out.print("\nSAXParserFactory Instance: "
						+ factory.getClass().getName()
						+ "\nDSAXParser Instance: "
						+ parser.getClass().getName() + "\n");
			} catch (Exception e) {
				System.out.println("Error processing SAX -- " + e.getMessage());
				e.printStackTrace(System.out);
			}
		}
		{
			Transformer parser = null;
			TransformerFactory factory = TransformerFactory.newInstance();
			try {
				parser = factory.newTransformer();
				System.out.print("\nTransformerFactory Instance: "
						+ factory.getClass().getName()
						+ "\nTransformer Instance: "
						+ parser.getClass().getName() + "\n");
			} catch (Exception e) {
				System.out.println("Error processing XSL -- " + e.getMessage());
				e.printStackTrace(System.out);
			}
		}
		{
			Schema schema = null;
			SchemaFactory factory = SchemaFactory
					.newInstance("http://www.w3.org/2001/XMLSchema");
			try {
				schema = factory.newSchema();
				System.out.print("\nSchemaFactory Instance: "
						+ factory.getClass().getName() + "\nSchema Instance: "
						+ schema.getClass().getName() + "\n");
			} catch (Exception e) {
				System.out.println("Error processing Schema -- "
						+ e.getMessage());
				e.printStackTrace(System.out);
			}
		}
	}

	private static class NSContext implements NamespaceContext {

		/** {@inheritDoc} */
		public String getNamespaceURI(String prefix) {
			return prefix.equals("saml") ? "urn:oasis:names:tc:SAML:2.0:assertion" : XMLConstants.NULL_NS_URI;
		}

		/** {@inheritDoc} */
		public String getPrefix(String namespaceURI) {
			return null;
		}

		/** {@inheritDoc} */
		public Iterator getPrefixes(String namespaceURI) {
			return null;
		}

	}

}