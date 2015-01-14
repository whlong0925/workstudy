package com.design.mode.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
/**
 * JDK动态代理
 * @author rain
 *
 */
public class ProxyForJDKDync implements InvocationHandler{
	//被代理的对象
	private Subject target;
	/** 
	  * 绑定委托对象并返回一个代理类 
	  *  
	  * @param target 
	  * @return 
	  */  
	 public Object bind(Subject target) {  
	  this.target = target;  
	  // 取得代理对象  
	  // 要绑定接口(这是一个缺陷，CGLIB弥补了这一缺陷)  
	  return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);   
	 } 
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		  Object result = null;  
		  System.out.println("调用方法之前");  
		  // 执行方法  
		  result = method.invoke(target, args);  
		  System.out.println("调用方法之后");  
		  return result;  
	}

}
