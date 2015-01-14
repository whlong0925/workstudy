package com.design.mode.adapter;
/**
 * 对象适配器
 * @author rain
 *
 */
public class ObjectAdapter implements Target {
	//对源的引用
	private Adaptee adaptee;

	public ObjectAdapter(Adaptee adaptee) {
		this.adaptee = adaptee;
	}

	/**
     * 源类Adaptee有方法sampleOperation1
     * 因此适配器类直接委派即可
     */
	public void sampleOperation1() {
		System.out.println("－－－－－－－－－－适配器类适配源角色－－－－－－－－－－");
		this.adaptee.sampleOperation1();
	}
	/**
     * 源类Adaptee没有方法sampleOperation2
     * 因此由适配器类需要补充此方法
     */
	public void sampleOperation2() {
		System.out.println("－－－－－－－－－－适配器类开始适配目标角色－－－－－－－－－－");
		System.out.println("执行了目标角色");
	}

}
