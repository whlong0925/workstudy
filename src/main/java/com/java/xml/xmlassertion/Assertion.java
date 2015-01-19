package com.java.xml.xmlassertion;

public class Assertion {
	private String issuerValue;
	private Signature signature;
	private Subject subject;
	private Authnstatement authnstatement;
	private Conditions conditions;
	private Attributestatement attributeStatement;
	public String getIssuerValue() {
		return issuerValue;
	}
	public void setIssuerValue(String issuerValue) {
		this.issuerValue = issuerValue;
	}
	public Signature getSignature() {
		return signature;
	}
	public void setSignature(Signature signature) {
		this.signature = signature;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public Authnstatement getAuthnstatement() {
		return authnstatement;
	}
	public void setAuthnstatement(Authnstatement authnstatement) {
		this.authnstatement = authnstatement;
	}
	public Conditions getConditions() {
		return conditions;
	}
	public void setConditions(Conditions conditions) {
		this.conditions = conditions;
	}
	public Attributestatement getAttributeStatement() {
		return attributeStatement;
	}
	public void setAttributeStatement(Attributestatement attributeStatement) {
		this.attributeStatement = attributeStatement;
	}
}
