package com.java.xml;
import java.io.File;
import java.io.InputStream;
import java.util.Iterator;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class TestXmlDom4J {
    public static void main(String[] args) throws Exception{
//       testconf();
       readStringXml();
    }
    public static void testconf() {
    	try {
            //创建SAXReader对象
            SAXReader reader=new SAXReader();
            //得到XML文件的文件流
            InputStream in=Thread.currentThread().getClass().getResourceAsStream("/conf.xml");
            //注意:Document对象是org.dom4j包下的Document.通过SAXReader对象的read(InputStream)方法得到Document对象
            Document document=reader.read(in);
            //获得根元素.注意:Element是org.dom4j包下的Element
            Element root=document.getRootElement();
            //获得employee元素集合
            List<Element> employeeElements=root.elements("employee");
            //遍历employeeElements得到属性name的值
            for(Element e:employeeElements){
                String name=e.attributeValue("name");
                System.out.println(name);
                
              //得到age元素
                Element a=e.element("age");
                //得到age元素的值
                String age=a.getText();
                System.out.println(age);
            }
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static void readStringXml(String xml) throws Exception{
            // 下面的是通过解析xml字符串的
        	Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            
            Element rootElt = doc.getRootElement(); // 获取根节点
            System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

            Element statusElement = rootElt.element("Status"); ///获取根节点下的子节点Status
            
            Element statusCodeElement=statusElement.element("StatusCode");
            //得到age元素的值
            System.out.println(statusCodeElement.attributeValue("Value"));
    }
    
    public static void readStringXml() throws Exception{
    		// 读取并解析XML文档
    		SAXReader reader=new SAXReader();
    		//得到XML文件的文件流
    		InputStream in=Thread.currentThread().getClass().getResourceAsStream("/assertion.xml");
    		//注意:Document对象是org.dom4j包下的Document.通过SAXReader对象的read(InputStream)方法得到Document对象
    		Document doc=reader.read(in);
    		Element rootElt = doc.getRootElement(); // 获取根节点
    		
    		System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称
    		
    		/*//statuscode
    		Element statusElement = rootElt.element("Status"); ///获取根节点下的子节点Status
    		Element statusCodeElement=statusElement.element("StatusCode");///获取根节点下的子节点StatusCode
    		System.out.println(statusCodeElement.attributeValue("Value"));
    		//issuer
    		Element issuerElement = rootElt.element("Issuer"); ///获取根节点下的子节点Issuer
    		System.out.println(issuerElement.getText());
    		*/
    		Element assertionElement = rootElt.element("Assertion"); ///获取根节点下的子节点Assertion
    		List<Element> assertionElements = assertionElement.elements();
    		for(Element e : assertionElements){
    			System.out.println(e.getName());
    		}
    }
}