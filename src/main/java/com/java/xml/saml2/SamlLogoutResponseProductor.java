package com.java.xml.saml2;

import java.util.Random;

import org.joda.time.DateTime;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLException;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.LogoutResponse;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;
/**
 * 
 *单点退出生成的SAMLResponse
 */
public class SamlLogoutResponseProductor {
	private XMLObjectBuilderFactory builderFactory;
	
	public SamlLogoutResponseProductor(XMLObjectBuilderFactory builderFactory) {
		this.builderFactory = builderFactory;
	}

	public static void main(String[] args) throws Exception{
		DefaultBootstrap.bootstrap();
		XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
		SamlLogoutResponseProductor sp = new SamlLogoutResponseProductor(builderFactory);
		String destination = "http://192.168.1.123:6060/saml/SingleLogout/memberapi/241901148045330/alias/defaultAlias";
		String issuer = "https://sso.everbridge.net/memberapi/241901148045330";
		LogoutResponse response = sp.getLogoutResponse(destination,issuer);
		String logoutResponseStr = sp.base64Encode(response);
		System.out.println("压缩后：---------------------------------------------");
		System.out.println(logoutResponseStr);
	}

	@SuppressWarnings("unchecked")
	protected LogoutResponse getLogoutResponse(String destination,String issure) throws SAMLException,MetadataProviderException {

		SAMLObjectBuilder<LogoutResponse> builder = (SAMLObjectBuilder<LogoutResponse>) builderFactory.getBuilder(LogoutResponse.DEFAULT_ELEMENT_NAME);
		LogoutResponse response = builder.buildObject();
		response.setDestination(destination);



		response.setID(generateID());
		response.setIssuer(getIssuer(issure));
		response.setVersion(SAMLVersion.VERSION_20);
		response.setIssueInstant(new DateTime());
		
		StatusCodeBuilder statusCodeBuilder = new StatusCodeBuilder();
		StatusCode statusCode = statusCodeBuilder.buildObject();
		statusCode.setValue(StatusCode.SUCCESS_URI);

		StatusBuilder statusBuilder = new StatusBuilder();
		Status status = statusBuilder.buildObject();
		status.setStatusCode(statusCode);
		
		response.setStatus(status);
		return response;

	}

	@SuppressWarnings("unchecked")
	protected Issuer getIssuer(String localEntityId) {
		SAMLObjectBuilder<Issuer> issuerBuilder = (SAMLObjectBuilder<Issuer>) builderFactory.getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
		Issuer issuer = issuerBuilder.buildObject();
		issuer.setFormat(NameIDType.ENTITY);
		issuer.setValue(localEntityId);
		return issuer;
	}
	protected String generateID() {
        Random r = new Random();
        return 'a' + Long.toString(Math.abs(r.nextLong()), 20) + Long.toString(Math.abs(r.nextLong()), 20);
    }
	
	protected String base64Encode(SAMLObject message) throws Exception {
            String messageStr = XMLHelper.nodeToString(marshallMessage(message));
            System.out.println("压缩前：－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
            System.out.println(messageStr);
            return Base64.encodeBytes(messageStr.getBytes());
    }
	
	protected Element marshallMessage(XMLObject message) throws Exception {
        Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(message);
        Element messageElem = marshaller.marshall(message);
        return messageElem;
}
}
