package com.java.xml.saml2;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.impl.ResponseMarshaller;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;

public class Generate {
	private Properties pps;

	public static void main(String[] args) throws Exception {
		Generate generate = new Generate();
		Properties pps = generate.getProperties();
		String destination = pps.getProperty("destination");
		String issuer = pps.getProperty("issuer");
		String attributes = pps.getProperty("attributes");
		//sp的公钥加密
		String publicKeyPath = pps.getProperty("publicKeyPath");
		//idp的私钥签名
		String privateKeyPath = pps.getProperty("publicKeyPath");
		Map<String,String> attributeMap = getAttributesMap(attributes);
		boolean isEncryptedAssertion = Boolean.valueOf(pps.getProperty("isEncryptedAssertion"));
	    boolean isSignatureForResponse = Boolean.valueOf(pps.getProperty("isSignatureForResponse"));
		boolean isSignatureForAssertion = Boolean.valueOf(pps.getProperty("isSignatureForAssertion"));
		DateTime responseTime = new DateTime();
		//生成response
		SamlResponseProductor productor = new SamlResponseProductor(destination,issuer,responseTime,attributeMap,isEncryptedAssertion,isSignatureForResponse,isSignatureForAssertion,publicKeyPath,privateKeyPath);
		Response response = productor.buildResponse();
		
		ResponseMarshaller marshaller = new ResponseMarshaller();
		Element element = marshaller.marshall(response);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLHelper.writeNode(element, baos);
		String responseStr = new String(baos.toByteArray());
		
		System.out.println(responseStr);
		System.out.println("encoded------------");
		String result =  new String(org.apache.commons.codec.binary.Base64.encodeBase64(responseStr.getBytes()),Charset.forName("UTF-8")) ;
		System.out.println(result);
	}
	
	public Generate() throws Exception {
		pps = new Properties();
		String filePath = this.getClass().getResource("config.properties").getPath();
		InputStream in = new BufferedInputStream(new FileInputStream(filePath));
		pps.load(in);
	}

	public Properties getProperties() {
		return pps;
	}
	private static Map<String, String> getAttributesMap(String attributes) {
		Map<String,String> attributeMap = new HashMap<String, String>();
		if(!StringUtils.isEmpty(attributes)){
			String[] arrs = attributes.split(",");
			for(String str:arrs){
				String[]  keyvalues = str.split(":");
				attributeMap.put(keyvalues[0], keyvalues[1]);
			}
		}
		return attributeMap;
	}
	
}
