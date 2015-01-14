package com.design.mode.templeate;
/**
 * 抽象模板角色类
 * @author rain
 *
 */
public abstract class AbstractTemplate {
    /**
     * 模板方法
     */
    public void templateMethod(){
        abstractMethod();//不同的子类有不同的实现
        hookMethod();//可选择实现的逻辑，不是必须实现的
        finalMethod();//骨干算法，不能由子类重写
    }
    /**
     * 基本方法的声明（由子类实现）
     */
    protected abstract void abstractMethod();
    /**
     * 基本方法(空方法)
     */
    protected void hookMethod(){
    	System.out.println("我是模版默认实现的方法，可以被子类覆盖");
    }
    /**
     * 基本方法（已经实现,不能修改）
     */
    private final void finalMethod(){
        //业务相关的代码
    	System.out.println("骨干算法，不能被子类重写");
    }
}