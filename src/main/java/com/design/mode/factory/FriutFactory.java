package com.design.mode.factory;
/**
 * 简单工厂模式
 * @author rain
 *
 */
public class FriutFactory{
    public static void makeFriut(String name){
    	Friut friut = null;
    	if("apple".equals(name)){
    		friut = new Apple();
        }else if("orange".equals(name)){
        	friut = new Orange();
        }else if("banana".equals(name)){
        	friut = new Banana();
        }else{
            System.out.println("暂时没该水果供售");
        }
        if(friut!=null){
        	friut.get();
        }
    }
}