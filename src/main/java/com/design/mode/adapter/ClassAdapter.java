package com.design.mode.adapter;
/**
 * 类适配器
 * @author rain
 *
 */
public class ClassAdapter extends Adaptee implements Target {
	 /**
     * 由于源类Adaptee没有方法sampleOperation2()
     * 因此适配器补充上这个方法
     */
    public void sampleOperation2() {
    	System.out.println("－－－－－－－－－－适配器类开始适配目标角色－－－－－－－－－－");
		System.out.println("执行了目标角色");
    }
    

}