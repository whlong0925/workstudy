package com.design.mode.adapter;
/**
 * 　   适配器模式把一个类的接口变换成客户端所期待的另一种接口，从而使原本因接口不匹配而无法在一起工作的两个类能够在一起工作。
  ●　　目标(Target)角色：这就是所期待得到的接口。注意：由于这里讨论的是类适配器模式，因此目标不可以是类。
　　●　　源(Adapee)角色：现在需要适配的接口。
　　●　　适配器(Adaper)角色：适配器类是本模式的核心。适配器把源接口转换成目标接口。显然，这一角色不可以是接口，而必须是具体类。
 * @author rain
 *
 */
public class Client {

	public static void main(String[] args) {
		Adaptee adaptee = new Adaptee();
		//类适配器
		Target adapterClz = new ClassAdapter();
		adapterClz.sampleOperation1();
		adapterClz.sampleOperation2();
		
		
		//对象适配器
		Target adapterObj = new ObjectAdapter(adaptee); 
		adapterObj.sampleOperation1();
		adapterObj.sampleOperation2();
	}

}
