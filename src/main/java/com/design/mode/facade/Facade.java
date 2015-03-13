package com.design.mode.facade;
public class Facade {
    //示意方法，满足客户端需要的功能
    public void test(){
        ModelA a = new ModelA();
        a.a1();
        ModelB b = new ModelB();
        b.b1();
        ModelC c = new ModelC();
        c.c1();
    }
}