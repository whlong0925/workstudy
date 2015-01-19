package com.java.xml.xmlassertion;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseAssertion {

	public static void main(String[] args) throws Exception {
		SAMLResponse response = new ParseAssertion().readStringXml();
	}

	public SAMLResponse readStringXml() throws Exception {
		// 读取并解析XML文档
		SAXReader reader = new SAXReader();
		// 得到XML文件的文件流
		InputStream in = Thread.currentThread().getClass()
				.getResourceAsStream("/assertion.xml");
		// 注意:Document对象是org.dom4j包下的Document.通过SAXReader对象的read(InputStream)方法得到Document对象
		SAMLResponse response = new SAMLResponse();

		Document doc = reader.read(in);
		Element rootElement = doc.getRootElement(); // 获取根节点

		System.out.println("根节点：" + rootElement.getName()); // 拿到根节点的名称
		parseIssuerAndStatus(rootElement, response);
		parseSignature(rootElement, response);
		parseAsseration(rootElement, response);
		
		Element assertionElement = rootElement.element("Assertion"); // /获取根节点下的子节点Assertion
		List<Element> assertionElements = assertionElement.elements();
		for (Element e : assertionElements) {
			if(e.getName().equals("Signature")){
				parseSignature(assertionElement, response);
			}
		}
		return response;
	}
    private void parseAsseration(Element rootElement, SAMLResponse response) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 对整个response进行签名进行解析
	 * @param element
	 * @param response
	 */
	private void parseSignature(Element element, SAMLResponse response) {
		//SignedInfo SignatureValue  KeyInfo X509Data X509Certificate  Signature Subject SubjectConfirmation Recipient AudienceRestriction
		//Audience	AudienceRestriction Conditions AuthnStatement AuthnContext Assertion Transform Algorithm CanonicalizationMethod
		Element signatureElement = element.element("Signature");
		if(signatureElement==null) return;
		List<Element> signatureElements = signatureElement.elements();
		for (Element e : signatureElements) {
			String name = e.getName();
			if(name.equals("SignedInfo")){
				
			}else if(name.equals("SignatureValue")){
				
			}else if(name.equals("KeyInfo")){
				
			}
			System.out.println(e.getName()+"==========="+e.getText());
		}
	}

	/**
     * 解析issuer and statuscode
     * @param element
     * @param response
     */
	private void parseIssuerAndStatus(Element element, SAMLResponse response) {
		Element issuerElement = element.element("Issuer");
		Element statusElement = element.element("Status");
		String issuerValue = issuerElement.getText();
		String statusCodeValue = statusElement.attributeValue("Value");
		response.setIssuerValue(issuerValue);
		response.setStatusCodeValue(statusCodeValue);
	}

}
