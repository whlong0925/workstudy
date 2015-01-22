package com.java.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class TestXmlDom4J {
	public static void main(String[] args) throws Exception {
//		new TestXmlDom4J().readXml();
		new TestXmlDom4J().createXml();
	}

	@SuppressWarnings("unchecked")
	public  void readXml() throws Exception {
		// 创建SAXReader对象
		SAXReader reader = new SAXReader();
		// 得到XML文件的文件流
		InputStream in = Thread.currentThread().getClass().getResourceAsStream("/conf.xml");
		// 注意:Document对象是org.dom4j包下的Document.通过SAXReader对象的read(InputStream)方法得到Document对象
		// Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
		Document document = reader.read(in);
		// 获得根元素.注意:Element是org.dom4j包下的Element
		Element root = document.getRootElement();
		// 获得employee元素集合
		List<Element> employeeElements = root.elements("employee");
		// 遍历employeeElements得到属性name的值
		for (Element e : employeeElements) {
			String name = e.attributeValue("name");
			System.out.println(name);

			// 得到age元素
			Element a = e.element("age");
			// 得到age元素的值
			String age = a.getText();
			System.out.println(age);
		}
	}

	public  void createXml() throws Exception {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");// 添加文档根
		root.addComment("这个一个注释");// 加入一行注释
		Element request = root.addElement("request"); // 添加root的子节点
		request.addAttribute("type", "cat");
		request.addAttribute("flow", "tong");
		request.addAttribute("time", "2009");
		Element pro = request.addElement("pro");
		pro.addAttribute("type", "att");
		pro.addAttribute("name", "附件");
		pro.addText("测试哈子");

		Element cd = request.addElement("pro");
		cd.addAttribute("type", "cd");
		cd.addAttribute("name", "特殊字符过滤");
		cd.addCDATA("特殊字符");
		String filePath = this.getClass().getResource("").getPath()+"test.xml";
		// 输出全部原始数据，在编译器中显示
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");// 根据需要设置编码
		XMLWriter writer = new XMLWriter(System.out, format);
		document.normalize();
		writer.write(document);
		writer.close();
		// 输出全部原始数据，并用它生成新的我们需要的XML文件
		XMLWriter writer2 = new XMLWriter(new FileWriter(new File(filePath)),format);
		writer2.write(document); // 输出到文件
		writer2.close();
	}

}