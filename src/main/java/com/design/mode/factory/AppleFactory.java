package com.design.mode.factory;
/**
 * 工厂方法模式
 * @author rain
 *
 */
public class AppleFactory implements FriutFactoryForSimple{
    public void makeFriut() {
         new Apple().get();
    }   
}