package com.java.xml.xmlassertion;

import java.util.List;

public class Signature {
	private String canonicalizationmethod;
	private String signaturemethod;
	private String referenceUri;
	private List<String> transforms;
	private String digestmethod;
	private String digestvalue;
	private String signaturevalue;
	private String x509certificate;
	public String getCanonicalizationmethod() {
		return canonicalizationmethod;
	}
	public void setCanonicalizationmethod(String canonicalizationmethod) {
		this.canonicalizationmethod = canonicalizationmethod;
	}
	public String getSignaturemethod() {
		return signaturemethod;
	}
	public void setSignaturemethod(String signaturemethod) {
		this.signaturemethod = signaturemethod;
	}
	public String getReferenceUri() {
		return referenceUri;
	}
	public void setReferenceUri(String referenceUri) {
		this.referenceUri = referenceUri;
	}
	public List<String> getTransforms() {
		return transforms;
	}
	public void setTransforms(List<String> transforms) {
		this.transforms = transforms;
	}
	public String getDigestmethod() {
		return digestmethod;
	}
	public void setDigestmethod(String digestmethod) {
		this.digestmethod = digestmethod;
	}
	public String getDigestvalue() {
		return digestvalue;
	}
	public void setDigestvalue(String digestvalue) {
		this.digestvalue = digestvalue;
	}
	public String getSignaturevalue() {
		return signaturevalue;
	}
	public void setSignaturevalue(String signaturevalue) {
		this.signaturevalue = signaturevalue;
	}
	public String getX509certificate() {
		return x509certificate;
	}
	public void setX509certificate(String x509certificate) {
		this.x509certificate = x509certificate;
	}
}
