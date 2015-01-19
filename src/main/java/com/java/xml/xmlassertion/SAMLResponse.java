package com.java.xml.xmlassertion;

public class SAMLResponse {
	private String issuerValue;
	private String statusCodeValue;
	private Assertion assertion;
	private Signature signature;
	public String getIssuerValue() {
		return issuerValue;
	}
	public void setIssuerValue(String issuerValue) {
		this.issuerValue = issuerValue;
	}
	public String getStatusCodeValue() {
		return statusCodeValue;
	}
	public void setStatusCodeValue(String statusCodeValue) {
		this.statusCodeValue = statusCodeValue;
	}
	public Assertion getAssertion() {
		return assertion;
	}
	public void setAssertion(Assertion assertion) {
		this.assertion = assertion;
	}
	public Signature getSignature() {
		return signature;
	}
	public void setSignature(Signature signature) {
		this.signature = signature;
	}
}
