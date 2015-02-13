package com.java.xml.saml2;

import java.util.Map;
import java.util.UUID;

import org.joda.time.DateTime;
import org.opensaml.DefaultBootstrap;
import org.opensaml.common.SAMLVersion;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextClassRef;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.saml2.core.impl.AttributeBuilder;
import org.opensaml.saml2.core.impl.AttributeStatementBuilder;
import org.opensaml.saml2.core.impl.AudienceBuilder;
import org.opensaml.saml2.core.impl.AudienceRestrictionBuilder;
import org.opensaml.saml2.core.impl.AuthnContextBuilder;
import org.opensaml.saml2.core.impl.AuthnContextClassRefBuilder;
import org.opensaml.saml2.core.impl.AuthnStatementBuilder;
import org.opensaml.saml2.core.impl.ConditionsBuilder;
import org.opensaml.saml2.core.impl.IssuerBuilder;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.opensaml.saml2.core.impl.StatusBuilder;
import org.opensaml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.schema.impl.XSStringBuilder;

/**
 * 单点登陆生成的SAMLResponse响应　待完善
 *
 */
public class SamlResponseProductor {
	private String destination;
	private String issuer;
	private int expireddate;
	private Map<String, String> attributes;
	private boolean isEncryptedAssertion;
	private boolean isSignatureForResponse;
	private boolean isSignatureForAssertion;
	private DateTime responseTime;
	private String publicKeyPath;
	private String privateKeyPath;

	public Response buildResponse() throws Exception{
		Response response = createResponse();
		Status status = createStatus();
		Issuer responseIssuer = createIssuer(issuer);
		Assertion assertion = createAssertion();
		
		response.setStatus(status);
		response.setIssuer(responseIssuer);
		response.getAssertions().add(assertion);
		return response;
	}

	private Assertion createAssertion() {
		Issuer assertionIssuer = createIssuer(issuer);
		Subject subject = createSubject(issuer);
		AttributeStatement attributeStatement = createAttributeStatement(attributes);
		AuthnStatement authnStatement = createAuthnStatement();
		AssertionBuilder assertionBuilder = new AssertionBuilder();
		Conditions conditions = this.createConditions("");
		Assertion assertion = assertionBuilder.buildObject();
		assertion.setID(UUID.randomUUID().toString());
		assertion.setIssueInstant(new DateTime());
		assertion.setSubject(subject);
		assertion.setIssuer(assertionIssuer);
		assertion.setConditions(conditions);

		if (authnStatement != null){
			assertion.getAuthnStatements().add(authnStatement);
		}

		if (attributeStatement != null){
			assertion.getAttributeStatements().add(attributeStatement); // commented
		}
		return assertion;
	}

	private Conditions createConditions(String audienceURI) {
		//Build Audience
		AudienceBuilder audienceBuilder = new AudienceBuilder();
		Audience audience = audienceBuilder.buildObject();
		audience.setAudienceURI(audienceURI);
		
		//Builder AudienceRestriction
		AudienceRestrictionBuilder audienceRestrictionBuilder = new AudienceRestrictionBuilder();
		AudienceRestriction audienceRestriction = audienceRestrictionBuilder.buildObject();
		audienceRestriction.getAudiences().add(audience);
		
		//Builder Conditions
		ConditionsBuilder condidtionsBuilder = new ConditionsBuilder();
		Conditions conditions = condidtionsBuilder.buildObject();
		conditions.setNotBefore(responseTime);
		conditions.setNotOnOrAfter(responseTime.plusMinutes(30));
		conditions.getAudienceRestrictions().add(audienceRestriction);		
		
		return conditions;
	}

	private AuthnStatement createAuthnStatement() {
		AuthnContextClassRefBuilder classRefBuilder = new AuthnContextClassRefBuilder();
		AuthnContextClassRef classRef = classRefBuilder.buildObject();
		classRef.setAuthnContextClassRef("urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified");
		
		// create authcontext object
		AuthnContextBuilder authContextBuilder = new AuthnContextBuilder();
		AuthnContext authnContext = authContextBuilder.buildObject();
		authnContext.setAuthnContextClassRef(classRef);
		
		// create authenticationstatement object
		AuthnStatementBuilder authStatementBuilder = new AuthnStatementBuilder();
		AuthnStatement authnStatement = authStatementBuilder.buildObject();
		authnStatement.setAuthnInstant(new DateTime());
		authnStatement.setAuthnContext(authnContext);
		
		return authnStatement;
	}

	private Response createResponse() {
		ResponseBuilder responseBuilder = new ResponseBuilder();
		Response response = responseBuilder.buildObject();
		response.setID(UUID.randomUUID().toString());
		response.setIssueInstant(new DateTime());
		response.setVersion(SAMLVersion.VERSION_20);
		return response;
	}

	private AttributeStatement createAttributeStatement(
			Map<String, String> attributes) {
		AttributeStatementBuilder attributeStatementBuilder = new AttributeStatementBuilder();
		AttributeStatement attributeStatement = attributeStatementBuilder
				.buildObject();

		AttributeBuilder attributeBuilder = new AttributeBuilder();
		if (attributes != null) {
			for (Map.Entry<String, String> entry : attributes.entrySet()) {
				Attribute attribute = attributeBuilder.buildObject();
				attribute.setName(entry.getKey());
				attribute.setNameFormat("urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified");
				String value = entry.getValue();
				XSStringBuilder stringBuilder = new XSStringBuilder();
				XSString attributeValue = stringBuilder
						.buildObject(AttributeValue.DEFAULT_ELEMENT_NAME,XSString.TYPE_NAME);
				attributeValue.setValue(value);
				attribute.getAttributeValues().add(attributeValue);

				attributeStatement.getAttributes().add(attribute);
			}
		}

		return attributeStatement;
	}

	private Subject createSubject(String issuer2) {
		// TODO Auto-generated method stub
		return null;
	}

	private Issuer createIssuer(String issuerName) {
		IssuerBuilder issuerBuilder = new IssuerBuilder();
		Issuer issuer = issuerBuilder.buildObject();
		issuer.setValue(issuerName);
		return issuer;
	}

	private Status createStatus() {
		StatusCodeBuilder statusCodeBuilder = new StatusCodeBuilder();
		StatusCode statusCode = statusCodeBuilder.buildObject();
		statusCode.setValue(StatusCode.SUCCESS_URI);

		StatusBuilder statusBuilder = new StatusBuilder();
		Status status = statusBuilder.buildObject();
		status.setStatusCode(statusCode);
		return status;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public int getExpireddate() {
		return expireddate;
	}

	public void setExpireddate(int expireddate) {
		this.expireddate = expireddate;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public boolean isEncryptedAssertion() {
		return isEncryptedAssertion;
	}

	public void setEncryptedAssertion(boolean isEncryptedAssertion) {
		this.isEncryptedAssertion = isEncryptedAssertion;
	}

	public boolean isSignatureForResponse() {
		return isSignatureForResponse;
	}

	public void setSignatureForResponse(boolean isSignatureForResponse) {
		this.isSignatureForResponse = isSignatureForResponse;
	}

	public boolean isSignatureForAssertion() {
		return isSignatureForAssertion;
	}

	public void setSignatureForAssertion(boolean isSignatureForAssertion) {
		this.isSignatureForAssertion = isSignatureForAssertion;
	}
	public DateTime getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(DateTime responseTime) {
		this.responseTime = responseTime;
	}

	public String getPublicKeyPath() {
		return publicKeyPath;
	}

	public void setPublicKeyPath(String publicKeyPath) {
		this.publicKeyPath = publicKeyPath;
	}

	public String getPrivateKeyPath() {
		return privateKeyPath;
	}

	public void setPrivateKeyPath(String privateKeyPath) {
		this.privateKeyPath = privateKeyPath;
	}

	public SamlResponseProductor(String destination, String issuer,DateTime responseTime, Map<String, String> attributes,boolean isEncryptedAssertion, boolean isSignatureForResponse,boolean isSignatureForAssertion,String publicKeyPath,String privateKeyPath) throws Exception{
		this.destination = destination;
		this.issuer = issuer;
		this.responseTime = responseTime;
		this.attributes = attributes;
		this.isEncryptedAssertion = isEncryptedAssertion;
		this.isSignatureForResponse = isSignatureForResponse;
		this.isSignatureForAssertion = isSignatureForAssertion;
		this.publicKeyPath = publicKeyPath;
		this.privateKeyPath = privateKeyPath;
		DefaultBootstrap.bootstrap();
	}

}
