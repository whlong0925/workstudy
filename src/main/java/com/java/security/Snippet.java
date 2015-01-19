package com.java.security;

import java.io.FileInputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.crypto.KeyGenerator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.apache.xml.security.Init;
import org.apache.xml.security.encryption.EncryptedData;
import org.apache.xml.security.encryption.EncryptedKey;
import org.apache.xml.security.encryption.EncryptionMethod;
import org.apache.xml.security.encryption.XMLCipher;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.utils.Base64;
import org.apache.xml.security.utils.Constants;
import org.apache.xml.security.utils.EncryptionConstants;

public class Snippet {
	public static Element EncryptElement(
			final String strTagNameOfElementToEncrypt,
			final Element elemParent) throws Exception {
		
		Init.init();

		String strEncryptionAlgorithm = XMLCipher.AES_128;

		String strEncryptionAlgorithmProvider = null;

		String strEncryptionDigestAlgorithm = Constants.ALGO_ID_DIGEST_SHA1;

		String strEncryptionKeyGeneratorAlgorithm = "AES";

		String strEncryptionKeyGeneratorAlgorithmProvider = null;

		int nEncryptionKeyGeneratorSize = 128;

		String strEncryptionKeyWrapAlgorithm = XMLCipher.RSA_OAEP;

		String strEncryptionKeyWrapAlgorithmProvider = null;

		final Element domParent = elemParent;
		final Document domDocument = domParent.getOwnerDocument();
		final NodeList nlToEncrypts = domParent.getElementsByTagName(strTagNameOfElementToEncrypt);
		if (null == nlToEncrypts) {
			final String strErrorMessage = "Element.getElementsByTagNameNS unexpectedly returned null";
			// XMLSecurityApacheExtension.log.error(strErrorMessage);
			return null;
		}

		if (1 != nlToEncrypts.getLength()) {
			final String strErrorMessage = "Element.getElementsByTagNameNS unexpectedly returned " + nlToEncrypts.getLength() + " nodes";
			// XMLSecurityApacheExtension.log.error(strErrorMessage);
			return null;
		}
		final Element domToEncrypt = (Element) nlToEncrypts.item(0);
		final Element domTempParent = domDocument.createElement("Parent");
		final Element domTempToEncrypt = (Element) domTempParent.appendChild(domToEncrypt.cloneNode(true));

		String pass = "123456";
		FileInputStream in = new FileInputStream("C:\\Users\\mina\\tomcat.keystore");
		KeyStore ks = KeyStore.getInstance("JKS");
		ks.load(in, pass.toCharArray());
		Certificate cert = ks.getCertificate("tomcat");

		final X509Certificate certificate = (X509Certificate) cert;
		// final String certificate =
		// "MIIEsjCCApoCAQAwDQYJKoZIhvcNAQEEBQAwga8xCzAJBgNVBAYTAkNIMQ8wDQYDVQQIEwZadXJpY2gxDzANBgNVBAcTBlp1cmljaDEhMB8GA1UEChMYQWJoaUNlcnRpZmljYXRlQXV0aG9yaXR5MRUwEwYDVQQLEwxGaXJzdFR5cGUgQ0ExITAfBgNVBAMTGEFiaGlDZXJ0aWZpY2F0ZUF1dGhvcml0eTEhMB8GCSqGSIb3DQEJARYSYWJzQHp1cmljaC5pYm0uY29tMB4XDTA3MDUwNzEzMDAwNloXDTA3MDgxNTEzMDAwNlowgY0xCzAJBgNVBAYTAkNIMQ8wDQYDVQQIEwZadXJpY2gxDzANBgNVBAcTBlp1cmljaDEQMA4GA1UEChMHSUJNIFpSTDESMBAGA1UECxMJYWJoaSB0ZXN0MRMwEQYDVQQDEwphYmhpbGFwdG9wMSEwHwYJKoZIhvcNAQkBFhJhYmhpQGNzYWlsLm1pdC5lZHUwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCskkmbBSXMRT9FuoE+hn0XvSKCJhedXG6ktgnjqODbDXlWHqDW+Jxj/N7CD1Nxry0lIsKNVcgJ8QXpX21aTDH3lMgdAZVyQYkVA24fGolV6fRFSybwaZ5/IxTW3H29wiZJhSKii5jSFfA/XPDTEFucT6u4VWR81dXCtd528T4vMXbF+QuWhfn5u5MZBncsWZr9YC5cKeV5Z6/qx9LpnfjHKeTNAlscv93YDYavkKt6kJX6hV/zZ3bIiJ90RxUE8UiVbY5PORRfY7uHU+Ga0MTbOj0Y0VGr++DQfrYDm1Prm0IRWFm4pjkNRZTmEZRxDtJ+IbNFfQC/SrcCSwO+e+odAgMBAAEwDQYJKoZIhvcNAQEEBQADggIBAMVyOhAHcw4iZnsjneSFWLSveEutSicl6nzjL7rRm9vQjZYna4VCYlUnnoCQoqms82KKxYohlvU1zFS90x7xKE5Of20NPIfzFSuUsxBQOtsDto+DCPoAAk3HmXcUQS/l2ZGJPeL4W6FEJrx4ewwMdN5LnrtqbaBMGJPonDWYqQHv+kPyc0a8djq3C3vFN1MfYIVSMDser7NLqgdqKjNKP00KeuiWoN72c5bgsRl+/S+QuVaZAgLkOVzN4r3RX+jIZe2TJgYzOlCuUC8/PFnFLGcukpIv2aUzGXx8mc7yhboInwbBlleN6xJnDWLwRivA9Tf6WrymH1L6KjPBBIFq9S17dXQyFRGy0BNVHaLEu3jBk8nv0cnrjI2bCBiH9rxnmqrq5VUy0gCHxlnNNKDx7cyf98/BlbtKh43t9gb3goaKUSYWO8mkA6eT6AAIpjCWFP+K8Y3R4T9jhXBfCROTa/fpzKtN9uIMMgV6oKTwnB+ATEhGhkzkSfnymz5H0orYA2gkU8OimcZCQTAtSLkXyiyE8u3O61vR5SEyb2diVIdCkqwK8dQT4NMX2LZwsXULUV7z8fclKlvImC++r1DwBDsVtbwwlE9DxviL9sBtS2MG+oZmYA0YwrIOqOvnjI7avxFV7MzzOWRC+06WNJAnHVlFCdiR1b9Q7kteqDZ0RxzC";

		// org.eclipse.higgins.sts.utilities.CertificateHelper
		// .fromString(strCertificate);

		javax.crypto.KeyGenerator keyGenerator = null;
		if (null == strEncryptionKeyGeneratorAlgorithmProvider)
			keyGenerator = javax.crypto.KeyGenerator.getInstance("AES");
		// .getInstance("strEncryptionKeyGeneratorAlgorithm");
		else
			keyGenerator = KeyGenerator.getInstance("strEncryptionKeyGeneratorAlgorithm",strEncryptionKeyGeneratorAlgorithmProvider);
		
		keyGenerator.init(nEncryptionKeyGeneratorSize);
		final javax.crypto.SecretKey secretKey = keyGenerator.generateKey();
		final PublicKey publicKeyRP = certificate.getPublicKey();
		XMLCipher keyCipher = null;
		if (null == strEncryptionKeyWrapAlgorithmProvider)
			keyCipher = XMLCipher.getInstance(strEncryptionKeyWrapAlgorithm);
		else
			keyCipher = XMLCipher.getProviderInstance(strEncryptionKeyWrapAlgorithm,strEncryptionKeyWrapAlgorithmProvider);
		
		keyCipher.init(XMLCipher.WRAP_MODE,publicKeyRP);
		final KeyInfo keyInfoKey = new KeyInfo(domDocument);
		final MessageDigest mdSha1 = MessageDigest.getInstance("SHA-1");
		final byte[] byteThumbPrint = mdSha1.digest(certificate.getEncoded());
		final Document domParentDocument = domParent
				.getOwnerDocument();
		final Element domSTR = domParentDocument
				.createElementNS(
						"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
						"SecurityTokenReference");
		final Element domKeyIdentifier = domParentDocument
				.createElementNS(
						"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
						"KeyIdentifier");
		domKeyIdentifier.setAttribute(
						"ValueType",
						"http://docs.oasis-open.org/wss/oasis-wss-soap-message-security-1.1#ThumbprintSHA1");
		domKeyIdentifier.setAttribute(
						"EncodingType",
						"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#Base64Binary");
		String strThumbprint = Base64.encode(byteThumbPrint);
		// org.eclipse.higgins.sts.utilities.XMLHelper.setTextContent(
		// domKeyIdentifier, strThumbprint);
		domKeyIdentifier.setTextContent(strThumbprint);
		domSTR.appendChild(domKeyIdentifier);
		keyInfoKey.addUnknownElement(domSTR);
		final EncryptedKey encryptedKey = keyCipher.encryptKey(domDocument, secretKey);
		encryptedKey.setKeyInfo(keyInfoKey);
		final EncryptionMethod encryptionMethod = encryptedKey.getEncryptionMethod();
		final Element elemDigestMethod = domDocument.createElementNS(Constants.SignatureSpecNS,"DigestMethod");
		elemDigestMethod.setAttribute("Algorithm", strEncryptionDigestAlgorithm);
		encryptionMethod.addEncryptionMethodInformation(elemDigestMethod);
		XMLCipher xmlCipher = null;
		if (null == strEncryptionAlgorithmProvider)
			xmlCipher = XMLCipher.getInstance(strEncryptionAlgorithm);
		else
			xmlCipher = XMLCipher.getProviderInstance(strEncryptionAlgorithm,strEncryptionAlgorithmProvider);
		xmlCipher.init(XMLCipher.ENCRYPT_MODE,secretKey);
		final EncryptedData encryptedData = xmlCipher.getEncryptedData();
		final KeyInfo keyInfoEncryption = new KeyInfo(domDocument);
		keyInfoEncryption.add(encryptedKey);
		encryptedData.setKeyInfo(keyInfoEncryption);
		System.out.println("do final, encrypt the Element");
		// xmlCipher.doFinal(domDocument, domTempToEncrypt, false);
		xmlCipher.doFinal(domDocument, domTempToEncrypt, false);
		final NodeList nlEncryptedData = domTempParent.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS,"EncryptedData");
		if (1 != nlEncryptedData.getLength()) {
			throw new Exception("One EncryptedData Not Found!");
		}
		Element domEncryptedData = (Element) nlEncryptedData.item(0);
		System.out.println("********************"+ domEncryptedData.getNodeType());
		// org.eclipse.higgins.sts.utilities.XMLHelper
		// .stripNewLinesFromElement((Element) nlEncryptedData
		// .item(0));
		domParent.replaceChild(domEncryptedData, domToEncrypt);

		System.out.println("encrypt:" + domEncryptedData);
		// Element elemResult = null;
		// elemResult.set(domParent);
		return domParent;
		// return domEncryptedData;
	}

	public static Element DecryptElement(
			final Element elemEncryptedData,
			final PrivateKey privateKey) throws Exception {
		String strEncryptionAlgorithm = XMLCipher.AES_128;

		String strEncryptionAlgorithmProvider = null;

		String strEncryptionKeyGeneratorAlgorithm = "AES";

		// int nEncryptionKeyGeneratorSize = 128;

		String strEncryptionKeyWrapAlgorithm = XMLCipher.RSA_OAEP;

		String strEncryptionKeyWrapAlgorithmProvider = null;

		// XMLSecurityApacheExtension.log.trace("DecryptElement");

		final Element domEncryptedData = elemEncryptedData;
		final NodeList nlEncryptedKey = domEncryptedData.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS,EncryptionConstants._TAG_ENCRYPTEDKEY);
		if (null == nlEncryptedKey) {
			// XMLSecurityApacheExtension.log.trace("No EncryptedKey found (getElementsByTagName returned null)");
		} else if (0 == nlEncryptedKey.getLength()) {
			// XMLSecurityApacheExtension.log.trace("No EncryptedKey found (0 == getLength())");
		} else {
			final Document domDocument = domEncryptedData.getOwnerDocument();
			final Element elemEncryptedKey = (Element) nlEncryptedKey.item(0);
			final NodeList nlKeyEncryptionMethod = elemEncryptedKey.getElementsByTagNameNS(EncryptionConstants.EncryptionSpecNS,EncryptionConstants._TAG_ENCRYPTIONMETHOD);
			String strKeyEncryptionMethod = null;
			if (null == nlKeyEncryptionMethod) {
				// XMLSecurityApacheExtension.log.trace("No EncryptionMethod found");
				strKeyEncryptionMethod = strEncryptionKeyWrapAlgorithm;
			} else if (0 == nlKeyEncryptionMethod.getLength()) {
				// XMLSecurityApacheExtension.log.trace("No EncryptionMethod found");
				strKeyEncryptionMethod = strEncryptionKeyWrapAlgorithm;
			} else {
				final Element elemKeyEncryptionMethod = (Element) nlKeyEncryptionMethod.item(0);
				strKeyEncryptionMethod = elemKeyEncryptionMethod.getAttribute(EncryptionConstants._ATT_ALGORITHM);
			}
			XMLCipher keyCipher = null;
			if (null == strEncryptionKeyWrapAlgorithmProvider)
				keyCipher = XMLCipher.getInstance(strKeyEncryptionMethod);
			else
				keyCipher = XMLCipher.getProviderInstance(strKeyEncryptionMethod,strEncryptionKeyWrapAlgorithmProvider);
			keyCipher.init(XMLCipher.UNWRAP_MODE,privateKey);
			final EncryptedKey encryptedKey = keyCipher.loadEncryptedKey(domDocument, elemEncryptedKey);
			final Key keySecret = keyCipher.decryptKey(encryptedKey, strEncryptionKeyWrapAlgorithm);// 用私钥去解密出对称密钥

			final javax.crypto.SecretKey secretKey = (javax.crypto.SecretKey) keySecret;// 对称密钥
			final byte[] encodedSecretKey = secretKey.getEncoded();
			final javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(encodedSecretKey, strEncryptionKeyGeneratorAlgorithm);// AES类型的密钥
			XMLCipher xmlCipher = null;
			if (null == strEncryptionAlgorithmProvider)
				xmlCipher = XMLCipher.getInstance(strEncryptionAlgorithm);
			else
				xmlCipher = XMLCipher.getProviderInstance(strEncryptionAlgorithm,strEncryptionAlgorithmProvider);
			xmlCipher.init(XMLCipher.DECRYPT_MODE,secretKeySpec);
			final Document domResultDocument = xmlCipher.doFinal(domDocument, domEncryptedData, false);
			// final org.eclipse.higgins.sts.api.IElement elemResult = new
			// org.eclipse.higgins.sts.common.Element();
			// elemResult.set
			// (domResultDocument.getDocumentElement());
			return domResultDocument.getDocumentElement();
		}
		return null;
	}
}
