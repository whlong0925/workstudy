package com.java.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import sun.misc.BASE64Encoder;
/**
 * 
 * 从jks文件中导出私钥和证书
 *
 */

public class ExportKey {
	private File keystoreFile;
	private String keyStoreType;
	private char[] password;
	private String alias;
	private File exportedPrivateKeyFile;
	private File exportedPublicKeyFile;

	public static KeyPair getKeyPair(KeyStore keystore, String alias,char[] password) {
		try {
			Key key = keystore.getKey(alias, password);
			if (key instanceof PrivateKey) {
				Certificate cert = keystore.getCertificate(alias);
				PublicKey publicKey = cert.getPublicKey();
				return new KeyPair(publicKey, (PrivateKey) key);
			}
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void exportPrivate() throws Exception {
		KeyStore keystore = KeyStore.getInstance(keyStoreType);
		keystore.load(new FileInputStream(keystoreFile), password);
		KeyPair keyPair = getKeyPair(keystore, alias, password);
		BASE64Encoder encoder = new BASE64Encoder();
		PrivateKey privateKey = keyPair.getPrivate();
		String encoded = encoder.encode(privateKey.getEncoded());
		FileWriter fw = new FileWriter(exportedPrivateKeyFile);
		fw.write("-----BEGIN PRIVATE KEY-----\n");
		fw.write(encoded);
		fw.write("\n");
		fw.write("-----END PRIVATE KEY-----");
		fw.close();
	}
	public void exportCertificate() throws Exception {
		KeyStore keystore = KeyStore.getInstance(keyStoreType);
		keystore.load(new FileInputStream(keystoreFile), password);
		BASE64Encoder encoder = new BASE64Encoder();
		Certificate cert = keystore.getCertificate(alias);
		String encoded = encoder.encode(cert.getEncoded());
		FileWriter fw = new FileWriter(exportedPublicKeyFile);
		fw.write("-----BEGIN CERTIFICATE-----\n");
		fw.write(encoded);
		fw.write("\n");
		fw.write("-----END CERTIFICATE-----");
		fw.close();
	}

	public static void main(String args[]) throws Exception {
		ExportKey export = new ExportKey();
		export.keystoreFile = new File("/home/rain/key/nalle123/nalle123.jks");
		export.keyStoreType = "JKS";
		export.password = "nalle123".toCharArray();
		export.alias = "apollo";
		export.exportedPrivateKeyFile = new File("/home/rain/key/nalle123/exported-pkcs8.key");
		export.exportedPublicKeyFile = new File("/home/rain/key/nalle123/exported-public.key");
		export.exportPrivate();
		export.exportCertificate();
	}
}