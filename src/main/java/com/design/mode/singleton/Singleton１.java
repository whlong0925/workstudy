package com.design.mode.singleton;
/**
 * 双重检查加锁
 * @author rain
 *
 */
public class Singleton１ {
	private volatile static Singleton１ instance = null;
    private Singleton１(){}
    public static Singleton１ getInstance(){
        //先检查实例是否存在，如果不存在才进入下面的同步块
        if(instance == null){
            //同步块，线程安全的创建实例
            synchronized (Singleton１.class) {
                //再次检查实例是否存在，如果不存在才真正的创建实例
                if(instance == null){
                    instance = new Singleton１();
                }
            }
        }
        return instance;
    }
}
