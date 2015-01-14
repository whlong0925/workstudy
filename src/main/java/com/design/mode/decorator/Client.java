package com.design.mode.decorator;
/**
 * 在这个模式中主要有四大组件

　　1.原始类接口（接口类型或者抽象类）
　　2.原始类的具体实现类
　　3.装饰器接口（一般类或抽象类）
　　4.继承了装饰器接口的一系列具体装饰器
  装饰模式的特点

　　装饰对象和真实对象有相同的接口。这样客户端对象就可以以和真实对象相同的方式和装饰对象交互。
　　装饰对象包含一个真实对象的引用（reference）。
　　装饰对象接收所有来自客户端的请求，它把这些请求转发给真实的对象。
　　装饰对象可以在转发这些请求之前或之后附加一些功能。
　　这样就确保了在运行时，不用修改给定对象的结构就可以在外部增加附加的功能。
 * @author rain
 *
 */
public class Client {
	public static void main(String[] args) {
		System.out.println("-----------------------------普通手机-------------------------------");
		Phone commonPhone = new CommonPhone();
		commonPhone.feature();
		System.out.println("-----------------------------智能手机-------------------------------");
		// 将普通手机包一层智能的皮就成了智能手机
		AdvancedPhone advancedPhone = new AdvancedPhone(commonPhone);
		advancedPhone.feature();

		System.out.println("-----------------------------山寨手机-------------------------------");
		// 将普通手机包一层山寨的皮就成了强大的山寨手机
		ShanZhaiPhone shangZhaiPhone = new ShanZhaiPhone(commonPhone);
		shangZhaiPhone.feature();

		System.out.println("-----------------------------山寨智能手机----------------------------");
		// 将智能手机包一层山寨的皮就成了山寨智能机
		ShanZhaiPhone shangZhaiAdvancedPhone = new ShanZhaiPhone(new AdvancedPhone(new CommonPhone()));
		shangZhaiAdvancedPhone.feature();

	}
}
