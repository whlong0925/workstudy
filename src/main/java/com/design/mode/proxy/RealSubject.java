package com.design.mode.proxy;
/**
 * 具体主题角色
 * @author rain
 *
 */
public class RealSubject implements Subject {  
    public void request() {  
        //业务逻辑处理 
    	System.out.println("我是真实主题的业务逻辑");
    }  
}  