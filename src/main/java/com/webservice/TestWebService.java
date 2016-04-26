/*package com.webservice;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;

public class TestWebService {

	public static void main(String[] args) throws Exception {
		JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
        Client client = factory.createClient("http://localhost:8080/oasis/WebService/DataWarehouseService?wsdl");
        Object[] objs = client.invoke("webScreenShot", "阿福");
        System.out.println(objs[0].toString());

	}

}*/
