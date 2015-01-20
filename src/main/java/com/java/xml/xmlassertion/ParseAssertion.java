package com.java.xml.xmlassertion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseAssertion {

	public static void main(String[] args) throws Exception {
		SAMLResponse response = new ParseAssertion().readStringXml();
		System.out.println(response);
	}

	public SAMLResponse readStringXml() throws Exception {
		// 读取并解析XML文档
		SAXReader reader = new SAXReader();
		// 得到XML文件的文件流
		InputStream in = Thread.currentThread().getClass().getResourceAsStream("/assertion.xml");
		// 注意:Document对象是org.dom4j包下的Document.通过SAXReader对象的read(InputStream)方法得到Document对象
		SAMLResponse response = new SAMLResponse();

		Document doc = reader.read(in);
		Element rootElement = doc.getRootElement(); // 获取根节点

		System.out.println("根节点：" + rootElement.getName()); // 拿到根节点的名称
		
		parseIssuerAndStatus(rootElement, response);
		
		Element signatureElement = rootElement.element(Constants.SIGNATURE);//获取根节点下的子节点Signature
		
		parseSignature(signatureElement, response);
		
		parseAsseration(rootElement, response);
		
		Element assertionElement = rootElement.element(Constants.ASSERTION); // /获取根节点下的子节点Assertion
		List<Element> assertionElements = assertionElement.elements();
		for (Element e : assertionElements) {
			if(e.getName().equals(Constants.SIGNATURE)){
				parseSignature(e, response);
			}else if(e.getName().equals(Constants.SUBJECT)){ 
				parseSubject(e,response);
			}else if(e.getName().equals(Constants.CONDITIONS)){ 
				parseConditions(e,response);
			}else if(e.getName().equals(Constants.AUTHNSTATEMENT)){ 
				parseAuhtstatement(e,response);
			}
		}
		return response;
	}
    private void parseAuhtstatement(Element e, SAMLResponse response) {
		// TODO Auto-generated method stub
		
	}

	private void parseConditions(Element e, SAMLResponse response) {
		// TODO Auto-generated method stub
		
	}

	private void parseSubject(Element e, SAMLResponse response) {
		// TODO Auto-generated method stub
		
	}

	private void parseAsseration(Element rootElement, SAMLResponse response) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 对整个response进行签名进行解析
	 * @param element
	 * @param response
	 */
	private void parseSignature(Element signatureElement, SAMLResponse response) {
		
		if(signatureElement==null) return;
		Signature signature = new Signature();
		List<Element> signatureElements = signatureElement.elements();
		for (Element e : signatureElements) {
			String name = e.getName();
			if(name.equals(Constants.SIGNEDINFO)){
				
				Element canonicalizationMethodEL = e.element(Constants.CANONICALIZATIONMETHOD);
				String canonicalizationMethod = canonicalizationMethodEL.attribute(Constants.ALGORITHM).getValue();
				signature.setCanonicalizationmethod(canonicalizationMethod);
				
				Element signaturemethodEL = e.element(Constants.SIGNATUREMETHOD);
				String signatureMethod = signaturemethodEL.attributeValue(Constants.ALGORITHM);
				signature.setSignaturemethod(signatureMethod);
				
				Element referenceEL = e.element(Constants.REFERENCE);
				String referenceUri = referenceEL.attributeValue(Constants.URI);
				signature.setReferenceUri(referenceUri);
				
				List<Element> transforms = referenceEL.element(Constants.TRANSFORMS).elements(Constants.TRANSFORM);
				List<String> transformList = new ArrayList<String>();
				for(Element transformElement : transforms){
					transformList.add(transformElement.attributeValue(Constants.ALGORITHM));
				}
				signature.setTransforms(transformList);
				
				String digestmethod = referenceEL.attributeValue(Constants.DIGESTMETHOD);
				signature.setDigestmethod(digestmethod);
				String digestvalue = referenceEL.attributeValue(Constants.DIGESTVALUE);
				signature.setDigestvalue(digestvalue);
			}else if(name.equals(Constants.SIGNATUREVALUE)){
				String signatureValue = e.getText();
				signature.setSignaturevalue(signatureValue);
			}else if(name.equals(Constants.KEYINFO)){
				String x509certificate = e.element(Constants.X509DATA).elementText(Constants.X509CERTIFICATE).trim();
				signature.setX509certificate(x509certificate);
			}
//			System.out.println(e.getName()+"==========="+e.getText());
		}
		response.setSignature(signature);
	}

	/**
     * 解析issuer and statuscode
     * @param element
     * @param response
     */
	private void parseIssuerAndStatus(Element element, SAMLResponse response) {
		Element issuerElement = element.element(Constants.ISSUER);
		Element statusElement = element.element(Constants.STATUS);
		String issuerValue = issuerElement.getText();
		String statusCodeValue = statusElement.attributeValue(Constants.VALUE);
		response.setIssuerValue(issuerValue);
		response.setStatusCodeValue(statusCodeValue);
	}

}
