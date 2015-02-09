package com.java.classloader;

public class Sample // 运行时, jvm 把Sample的信息都放入方法区
{
	/** 范例名称 */
	private String name; // new Sample实例后， name 引用放入栈区里， name 对象放入堆里

	/** 构造方法 */
	public Sample(String name) {
		this.name = name;
	}

	/** 输出 */
	public void printName() { // print方法本身放入 方法区里。
		System.out.println(name);
	}

	public static void main(String[] args) { // main 方法本身放入方法区。
		Sample test1 = new Sample(" 测试1 "); // test1是引用，所以放到栈区里，new Sample(" 测试1 ")放入到堆区
		test1.printName();
	}
}