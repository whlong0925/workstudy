package com.design.mode.facade;
/**
 * 
 *门面模式是对象的结构模式，外部与一个子系统的通信必须通过一个统一的门面对象进行。
 *门面模式提供一个高层次的接口，使得子系统更易于使用。
 */
public class Client {

    public static void main(String[] args) {
        
        Facade facade = new Facade();
        facade.test();
    }

}