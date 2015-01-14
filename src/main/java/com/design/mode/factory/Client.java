package com.design.mode.factory;
/**
 *　工厂方法模式：核心工厂类不再负责对象的创建，将创建具体的对象延迟到具体的工厂类中，核心工厂类只负责子工厂类必须实现的接口，而屏蔽创建的细节.
 *　抽象工厂模式：与工厂方法模式的区别就在于，工厂方法模式针对的是一个产品等级结构；而抽象工厂模式则是针对的多个产品等级结构
 * @author rain
 *
 */
public class Client{
    public static void main(String[] args){
    	//简单工厂模式
        System.out.println("进入商店买水果:");
        System.out.println("－－－－－－－－－－－－－简单工厂模式－－－－－－－－－－－－－－－");
        FriutFactory.makeFriut("apple");
        FriutFactory.makeFriut("banana");
        FriutFactory.makeFriut("orange");
        
        //工厂方法模式
        System.out.println("－－－－－－－－－－－－－工厂方法模式－－－－－－－－－－－－－－－");
        FriutFactoryForSimple applefactory  = new AppleFactory();
        applefactory.makeFriut();
        FriutFactoryForSimple orangefactory = new OrangeFactory();
        orangefactory.makeFriut();
        FriutFactoryForSimple bananafactory = new BananaFactory();
        bananafactory.makeFriut();
        
        //抽象工厂模式
        System.out.println("－－－－－－－－－－－－－抽象工厂方法模式－－－－－－－－－－－－－－－");
        FriutFactoryForAbstract applefactoryForAbstract = new AppleFactoryAbstract();
        applefactoryForAbstract.makeFriut();
        applefactoryForAbstract.makeDrink();
    }


}