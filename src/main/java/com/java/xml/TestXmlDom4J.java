package com.java.xml;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class TestXmlDom4J {
    public static void main(String[] args) {
       testconf();
    }
    public static void testconf() {
    	try {
            //创建SAXReader对象
            SAXReader reader=new SAXReader();
            //得到XML文件的文件流
            InputStream in=TestXmlDom4J.class.getResourceAsStream("/conf.xml");
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
}