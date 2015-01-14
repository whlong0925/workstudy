package com.design.mode.proxy;
/**
 * 静态代理类
 * @author rain
 *
 */
public class ProxyForStatic implements Subject {  
    //要代理哪个实现类  
    private Subject subject = null;  
    //默认被代理者  
    public ProxyForStatic() {  
        this.subject = new ProxyForStatic();  
    }  
  //通过构造方法传递代理者  
    public ProxyForStatic(Subject subject) {  
    	this.subject = subject;  
    } 
     
    //实现接口中的方法  
    public void request() {  
        this.before();  
        this.subject.request();  
        this.after();  
    }  
      
    //预处理  
    private void before() {  
       System.out.println("在调用真实主题业务之前被执行");
    }  
      
    //善后处理  
    private void after() {  
    	System.out.println("在调用真实主题业务之后被执行");
    }  
}  