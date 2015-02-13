package com.java.xml.saml2;

import java.util.Random;

import org.joda.time.DateTime;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLException;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.SessionIndex;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Element;
/**
 * 单点退出时候生成samlrequest　待完善
 *
 */
public class SamlLogoutRequestProductor {
	private XMLObjectBuilderFactory builderFactory;
	
	public SamlLogoutRequestProductor(XMLObjectBuilderFactory builderFactory) {
		this.builderFactory = builderFactory;
	}

	public static void main(String[] args) throws Exception{
		DefaultBootstrap.bootstrap();
		XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
		SamlLogoutRequestProductor sp = new SamlLogoutRequestProductor(builderFactory);
		String destination = "http://192.168.1.123:6060/saml/SingleLogout/memberapi/241901148045330/alias/defaultAlias";
		String sessionIndex = "s2da42cf3e98514df9d7098c773d28f53c68c02a01";
		LogoutRequest request = sp.getLogoutRequest(destination, sessionIndex);
		String logoutRequestStr = sp.deflateAndBase64Encode(request);
		System.out.println("压缩后：---------------------------------------------");
		System.out.println(logoutRequestStr);
	}

	@SuppressWarnings("unchecked")
	protected LogoutRequest getLogoutRequest(String destination,String sessionIndex) throws SAMLException,MetadataProviderException {

		SAMLObjectBuilder<LogoutRequest> builder = (SAMLObjectBuilder<LogoutRequest>) builderFactory.getBuilder(LogoutRequest.DEFAULT_ELEMENT_NAME);
		LogoutRequest request = builder.buildObject();
		request.setDestination(destination);

		// Add session indexes
		SAMLObjectBuilder<SessionIndex> sessionIndexBuilder = (SAMLObjectBuilder<SessionIndex>) builderFactory.getBuilder(SessionIndex.DEFAULT_ELEMENT_NAME);
		SessionIndex index = sessionIndexBuilder.buildObject();
		index.setSessionIndex(sessionIndex);
		request.getSessionIndexes().add(index);

		SAMLObjectBuilder<NameID> nameIDBuilder = (SAMLObjectBuilder<NameID>) builderFactory.getBuilder(NameID.DEFAULT_ELEMENT_NAME);
		NameID nameID = nameIDBuilder.buildObject();
		nameID.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress");
		nameID.setNameQualifier("http://idp.ssocircle.com");
		nameID.setValue("rain.wang@everbridge.com");
		request.setNameID(nameID);

		request.setID(generateID());
		request.setIssuer(getIssuer("http://idp.ssocircle.com"));
		request.setVersion(SAMLVersion.VERSION_20);
		request.setIssueInstant(new DateTime());

		return request;

	}

	@SuppressWarnings("unchecked")
	protected Issuer getIssuer(String localEntityId) {
		SAMLObjectBuilder<Issuer> issuerBuilder = (SAMLObjectBuilder<Issuer>) builderFactory.getBuilder(Issuer.DEFAULT_ELEMENT_NAME);
		Issuer issuer = issuerBuilder.buildObject();
		issuer.setValue(localEntityId);
		return issuer;
	}
	protected String generateID() {
        Random r = new Random();
        return 'a' + Long.toString(Math.abs(r.nextLong()), 20) + Long.toString(Math.abs(r.nextLong()), 20);
    }
	
	protected String deflateAndBase64Encode(SAMLObject message) throws Exception {
            String messageStr = XMLHelper.nodeToString(marshallMessage(message));
            System.out.println("压缩前：－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－－");
            System.out.println(messageStr);
            /*ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
            Deflater deflater = new Deflater(Deflater.DEFLATED, true);
            DeflaterOutputStream deflaterStream = new DeflaterOutputStream(bytesOut, deflater);
            deflaterStream.write(messageStr.getBytes("UTF-8"));
            deflaterStream.finish();

            return Base64.encodeBytes(bytesOut.toByteArray());*/
            return Base64.encodeBytes(messageStr.getBytes());
    }
	
	protected Element marshallMessage(XMLObject message) throws Exception {
        Marshaller marshaller = Configuration.getMarshallerFactory().getMarshaller(message);
        Element messageElem = marshaller.marshall(message);
        return messageElem;
}
}
