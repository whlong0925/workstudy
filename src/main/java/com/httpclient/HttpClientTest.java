package com.httpclient;

import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientTest extends TestCase {
	
	public void test1() throws Exception{
		CloseableHttpClient httpclient = HttpClients.createDefault(); 
		HttpGet httpget = new HttpGet("http://www.baidu.com/");  
		CloseableHttpResponse response = httpclient.execute(httpget); 
		
		InputStream input = response.getEntity().getContent();
		byte[] b = new byte[input.available()];
		System.out.println(response.getEntity().getContent().read(b));
		System.out.println(new String(b));
	}
	public void test2(){
		System.out.println(2);
	}
}
