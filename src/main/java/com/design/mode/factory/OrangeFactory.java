package com.design.mode.factory;
/**
 * 工厂方法模式
 * @author rain
 *
 */
class OrangeFactory implements FriutFactoryForSimple{
     public void makeFriut() {
        new Orange().get();
    }
}