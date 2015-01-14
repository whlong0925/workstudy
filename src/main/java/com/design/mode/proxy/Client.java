package com.design.mode.proxy;
/**
 * 代理模式是为其他对象提供一种代理以控制这个对象的访问。 在某些情况下，一个客户不想或者不能直接引用另一个对象，
 * 而代理对象可以在客户端和目标对象之间起到中介的作用。
 * 
 * 抽象角色：声明真实对象和代理对象的共同接口。
　　代理角色：代理对象角色内部含有对真实对象的引用，从而可以操作真实对象，同时代理对象提供与真实对象相同的接口以便在任何时刻都能够代替真实对象。
　　同时，代理对象可以在执行真实对象操作时，附加其他的操作，相当于对真实对象进行封装。
　　真实角色：代理角色所代表的真实对象，是我们最终要引用的对象
 * @author rain
 *
 */
public class Client {

	public static void main(String[] args) {
		//静态代理
		ProxyForStatic p = new ProxyForStatic(new RealSubject());
		p.request();

		//动态代理
		ProxyForJDKDync proxy = new ProxyForJDKDync();
		Subject subject = (Subject)proxy.bind(new RealSubject());
		subject.request();
	}

}