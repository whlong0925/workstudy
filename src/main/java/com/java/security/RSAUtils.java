package com.java.security;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

/**
 * RSA安全编码组件
 * 
 */
public class RSAUtils extends EncryptUtils {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";//也可以是　SHA1withRSA MD5withRSA
	private  static final int  KEYSIZE = 1024;//单位：bit
	//加密1byte的明文,需要至少1+11=12bytes的密钥
	private  static final int  MAX_DECRYPT_BLOCK = KEYSIZE / 8;//密钥长度
	private  static final int  MAX_ENCRYPT_BLOCK = MAX_DECRYPT_BLOCK - 11;//RSA最大加密明文大小
	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";
	public enum KeyType{ PUBLIC,PRIVATE; }
	/**
	 * 用私钥对信息生成数字签名
	 * 
	 * @param data  加密数据
	 * @param privateKey 私钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		// 解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);

		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取私钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);

		return encryptBASE64(signature.sign());
	}

	/**
	 * 校验数字签名
	 * 
	 * @param data  加密数据
	 * @param publicKey 公钥
	 * @param sign 数字签名
	 * 
	 * @return 校验成功返回true 失败返回false
	 * @throws Exception
	 * 
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {

		// 解密由base64编码的公钥
		byte[] keyBytes = decryptBASE64(publicKey);

		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);

		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);

		// 验证签名是否正常
		return signature.verify(decryptBASE64(sign));
	}

	/**
	 * 解密<br>
	 * 用私钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据解密
		byte[] result = decrypt(data,privateKey);

		return result;
	}

	/**
	 * 解密<br>
	 * 用公钥解密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据解密
		byte[] result = decrypt(data,publicKey);

		return result;
	}

	/**
	 * 加密<br>
	 * 用公钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {
		// 对公钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);

		// 对数据加密
		byte[] result = encrypt(data, publicKey);
		return result;
	}

	/**
	 * 加密<br>
	 * 用私钥加密
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

		// 对数据加密
		byte[] result = encrypt(data, privateKey);
		return result;
	}
	/**
	 * 加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	 public static byte[] encrypt(byte[] data, Key key) throws Exception {
	        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        int inputLen = data.length;
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int offSet = 0;
	        byte[] cache;
	        int i = 0;
	        while (inputLen - offSet > 0) {
	            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
	                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
	            } else {
	                cache = cipher.doFinal(data, offSet, inputLen - offSet);
	            }
	            out.write(cache, 0, cache.length);
	            i++;
	            offSet = i * MAX_ENCRYPT_BLOCK;
	        }
	        byte[] encryptedData = out.toByteArray();
	        out.close();
	        return encryptedData;
	    }
	  /**
		 * 解密
		 * @param data
		 * @param key
		 * @return
		 * @throws Exception
		 */
	 public static byte[] decrypt(byte[] data, Key key) throws Exception {
	        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
	        cipher.init(Cipher.DECRYPT_MODE, key);
	        int inputLen = data.length;
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int offSet = 0;
	        byte[] cache;
	        int i = 0;
	        while (inputLen - offSet > 0) {
	            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
	                cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
	            } else {
	                cache = cipher.doFinal(data, offSet, inputLen - offSet);
	            }
	            out.write(cache, 0, cache.length);
	            i++;
	            offSet = i * MAX_DECRYPT_BLOCK;
	        }
	        byte[] decryptedData = out.toByteArray();
	        out.close();
	        return decryptedData;
	    }
	/**
	 * 取得私钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 取得公钥
	 * 
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return encryptBASE64(key.getEncoded());
	}

	/**
	 * 初始化密钥
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(KEYSIZE);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		// 公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
	private  Map<String, Object> initKey(File publicKeyFile,File privateKeyFile) throws Exception {
        
		BufferedReader publicBR = new BufferedReader(new FileReader(publicKeyFile));   
	    String s = publicBR.readLine();   
	    StringBuffer publickeyStr = new StringBuffer();   
        s = publicBR.readLine();   
        while (s.charAt(0) != '-') {   
            publickeyStr.append(s + "\r");   
            s = publicBR.readLine();   
         }   
         publicBR.close();
         BufferedReader privateBR = new BufferedReader(new FileReader(privateKeyFile));   
         String privateLine = privateBR.readLine();   
         StringBuffer privateKeyStr = new StringBuffer();   
         privateLine = privateBR.readLine();   
         while (privateLine.charAt(0) != '-') {   
        	 privateKeyStr.append(privateLine + "\r");   
        	 privateLine = privateBR.readLine();   
         }   
         privateBR.close();
	         
	    byte[] publickeybyte  = decryptBASE64(publickeyStr.toString());   
	    byte[] privatekeybyte = decryptBASE64(privateKeyStr.toString());   
		
		
	    KeyFactory keyfactory = KeyFactory.getInstance(KEY_ALGORITHM);
		EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privatekeybyte);
		RSAPrivateKey privateKey = (RSAPrivateKey)keyfactory.generatePrivate(privateKeySpec);
        
        X509EncodedKeySpec x509eks = new X509EncodedKeySpec(publickeybyte);
        RSAPublicKey publicKey = (RSAPublicKey) keyfactory.generatePublic(x509eks);
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
    }
	
	public void testEncrypt() throws Exception {  
		File publickeyfile  =  new File(this.getClass().getResource("rsa_public_key.pem").getPath());
		File privatekeyfile =  new File(this.getClass().getResource("rsa_pub_pk8.pem").getPath());//生成公钥和私钥后要将私钥转换为PKCS8格式
//		Map<String, Object> keyMap = initKey();  
		Map<String, Object> keyMap = initKey(publickeyfile,privatekeyfile);  
		
        String publicKey = getPublicKey(keyMap); 
        
        String privateKey = getPrivateKey(keyMap);  
        
        System.err.println("公钥: \n\r" + publicKey);  
        
        System.err.println("私钥： \n\r" + privateKey); 
        
		System.err.println("公钥加密——私钥解密");  
		
		//加密数据超过117byte 验证是否通过
        String inputStr = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";  
        
        byte[] data = inputStr.getBytes();  
  
        byte[] encodedData = encryptByPublicKey(data, publicKey);  
  
        byte[] decodedData = decryptByPrivateKey(encodedData,privateKey);  
  
        String outputStr = new String(decodedData);  
        
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);  
	}
	
	public void testSign() throws Exception {  
		
		Map<String, Object> keyMap = initKey();  
		
        String publicKey = getPublicKey(keyMap);  
        
        String privateKey = getPrivateKey(keyMap);  
        
        System.err.println("私钥加密——公钥解密");  
        
        String inputStr = "sign";  
        
        byte[] data = inputStr.getBytes();  
  
        byte[] encodedData = encryptByPrivateKey(data, privateKey);  
  
        byte[] decodedData = decryptByPublicKey(encodedData, publicKey);  
  
        String outputStr = new String(decodedData);  
        
        System.err.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);  
  
        System.err.println("私钥签名——公钥验证签名");  
        // 产生签名  
        String sign = sign(encodedData, privateKey); 

        System.err.println("签名:\r" + sign);  
  
        // 验证签名  
        boolean status = verify(encodedData, publicKey, sign);
        
        System.err.println("状态:\r" + status); 
        
        System.out.println(encryptBASE64(encodedData));
  
    }  
	public static boolean verify2(byte[] data,String publicKey,String sign)throws Exception{
        //解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        CertificateFactory fty = CertificateFactory.getInstance("X.509");
		ByteArrayInputStream bais = new ByteArrayInputStream(keyBytes);
		X509Certificate x509Cert = (X509Certificate)fty.generateCertificate(bais);
		PublicKey publicKey2 =x509Cert.getPublicKey();
        //验证签名是否正常
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);// 声名签名器，可用来签名与验签
        signature.initVerify(publicKey2);// 初使化公钥到签名器
        signature.update(data);// 初使化要验签的内容
        
        
        return signature.verify(decryptBASE64(sign));
         
    }
	
	public void testSign2() throws Exception{
		
		    String inputStr = "sign";  
	        
	        byte[] encodedData = inputStr.getBytes();  
	  
	        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANwdmGb6HPk8+PZyk5oPUbPk2VCMW2GLPkKPOPZYPc11jlMKAXI5b6YNvlIOaUntou7zp8rpG6l7itqD"+
								"XIwUdoJLsTf8vBibEIuFcTH9lRmpy2lFURpIk9RiWUucyLGpnavMT9+jsmybmrgCS6aYqUbiHF0bNp6kP762XdK8LQybAgMBAAECgYAGzftqI41FvzbLF5ushZC3CPoW"+
								"3V/t0goosJjINM4kTeAKfSKyT0g+T+p0SvHCUVBaJronMbGjOah/PNRdmDxZmCNNKQDdIrOU8ivXyfkbYROT4W5Qbf5JzllMwkeADXa0dknj4W45OfqoqifQIGSYyYSN"+
								"VMm0ef85qjmT4jZ54QJBAPJ4+vZuYQvdOIe43zq9h9rdxFzVIzTzqXOyh/SxJT9lnkXozT7krNBhlhqzEMlkUv/kuMKunyQVJihPlp2vpJMCQQDoZU9vLVocH+jh0luh"+
								"Hiin46rYieHPTU15QIcO6gSV18clEB3aDNhIFCedPNBp1t9mN8mUCn3lSjpcnoVuzsTZAkBAlz2zO8AaWvneHb9JdIemJAFVAWn5hxcSvPI+mpnjg3xf/x39rQjkEbrc"+
								"rerA2zrI8/LL2ZyHDiM2Bc2Hf+yFAkEAzFxiuPKZvge1g/e3Cfz6ZYEIOPkvMFvbGBhNbCkQNTCV+BqvFZOcEe5fU58p6xjARSQjyGJdiWd34QCpd+KuKQJBAMdh2SvU"+
								"JtT7IlgLfNBuHAHYfM646GocNpxVU0JQm65vxSsWDhUIZTTrXU9oPQkUsW+3Pl3N4h2ixaa1R7nvpwY=";
	        
	        String publicKey = "MIICkDCCAfmgAwIBAgIJAIiT278OEDRBMA0GCSqGSIb3DQEBBQUAMGExCzAJBgNVBAYTAkNOMQswCQYDVQQIDAJDTjELMAkGA1UEBwwCQ04xCzAJBgNVBAoMAkNOMQsw"+
								"CQYDVQQLDAJDTjELMAkGA1UEAwwCQ04xETAPBgkqhkiG9w0BCQEWAkNOMB4XDTE0MTEyMDA5NTkzNFoXDTI1MTEwMjA5NTkzNFowYTELMAkGA1UEBhMCQ04xCzAJBgNV"+
								"BAgMAkNOMQswCQYDVQQHDAJDTjELMAkGA1UECgwCQ04xCzAJBgNVBAsMAkNOMQswCQYDVQQDDAJDTjERMA8GCSqGSIb3DQEJARYCQ04wgZ8wDQYJKoZIhvcNAQEBBQAD"+
								"gY0AMIGJAoGBANwdmGb6HPk8+PZyk5oPUbPk2VCMW2GLPkKPOPZYPc11jlMKAXI5b6YNvlIOaUntou7zp8rpG6l7itqDXIwUdoJLsTf8vBibEIuFcTH9lRmpy2lFURpI"+
								"k9RiWUucyLGpnavMT9+jsmybmrgCS6aYqUbiHF0bNp6kP762XdK8LQybAgMBAAGjUDBOMB0GA1UdDgQWBBRn3nzPyXeqjRdg/m7xJTBu71LJ+TAfBgNVHSMEGDAWgBRn"+
								"3nzPyXeqjRdg/m7xJTBu71LJ+TAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBBQUAA4GBAEDsvBy7bOl7ZGHUDUNr9Y4cWdGnYeezrtEv5px180BwlKPnb/n5flewZBTZ"+
								"THq2uXzwenzq2VJFfAMk2YMNGZhNwcVjsJOBX1ingV14njrE+q9XVYCrcBu1nZN2lNKJmPhzUfHfzToL9j/2g4kkwQQgD5a2gPyc8/483Z7fmr3g";
	        //byte[] encryptData = "fCfIX24v1unnfhvEAjNm5thTK7k=".getBytes();
	        
	        byte[] encryptData = encryptByPrivateKey(encodedData,privateKey); 
	  
	        // 产生签名  
	        String sign = sign(encryptData, privateKey);
	        //String sign = "H0CddEnhzXfaFXnhM5XjkhaqhEYD4w3Pd3TQKEBQfdO9uiJWhHHZJtIwafmZsb9LiGBh4yTmbUP1+Jl3b4p8r29oLWrsEA3jPAQSzdAzgntwQxE1Dp9WDTqdTVkkU5oD62KYHJUVoj6G8AK8JDdCft9e++5IkdlpK4PF2HfvtyQ=";

	        System.err.println("签名:\r" + sign);  
	  
	        // 验证签名  
	        boolean status = verify2(encryptData, publicKey, sign);
	        
	        System.err.println("状态:\r" + status); 
	        
	}
	
	public static void main(String[] args) throws Exception{
		RSAUtils rsaUtil = new RSAUtils();
		rsaUtil.testEncrypt();
		rsaUtil.testSign();
		rsaUtil.testSign2();
	}
}
