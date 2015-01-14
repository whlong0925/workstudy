package com.design.mode.templeate;
/**
 * 具体模板角色类，实现了父类所声明的基本方法，abstractMethod()方法所代表的就是强制子类实现的剩余逻辑，
 * 而hookMethod()方法是可选择实现的逻辑，不是必须实现的。
 * @author rain
 *
 */
public class ConcreteTemplate extends AbstractTemplate{
    //基本方法的实现
    @Override
    public void abstractMethod() {
        //业务相关的代码
    	System.out.println("我是定义自己具体的业务");
    }
    //重写父类的方法
    @Override
    public void hookMethod() {
        //业务相关的代码
    	System.out.println("我是重写模版类的默认实现");
    }
}