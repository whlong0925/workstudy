package com.design.mode.singleton;
/**
 * 懒汉式单例模式
 * @author rain
 *
 */
public class LazySingleton {
	private static LazySingleton instance = null;
    /**
     * 私有默认构造子
     */
    private LazySingleton(){}
    /**
     * 静态工厂方法
     */
    public static synchronized LazySingleton getInstance(){
        if(instance == null){
            instance = new LazySingleton();
        }
        return instance;
    }

}
