package com.design.mode.observer;
/**
 * 定义：定义对象间一种一对多的依赖关系，使得当每一个对象改变状态，则所有依赖于它的对象都会得到通知并自动更新。
 * 在最基础的观察者模式中，包括以下四个角色：
    被观察者(主题)：从类图中可以看到，类中有一个用来存放观察者对象的Vector容器（之所以使用Vector而不 使用List，是因为多线程操作时，Vector在是安全的，而List则是不安全的），这个Vector容器是被观察者类的核心，另外还有三个方 法：attach方法是向这个容器中添加观察者对象；detach方法是从容器中移除观察者对象；notify方法是依次调用观察者对象的对应方法。这个 角色可以是接口，也可以是抽象类或者具体的类，因为很多情况下会与其他的模式混用，所以使用抽象类的情况比较多。
    观察者：观察者角色一般是一个接口，它只有一个update方法，在被观察者状态发生变化时，这个方法就会被触发调用。
    具体的被观察者：使用这个角色是为了便于扩展，可以在此角色中定义具体的业务逻辑。
    具体的观察者：观察者接口的具体实现，在这个角色中，将定义被观察者对象状态发生变化时所要处理的逻辑。

 * @author rain
 *
 */
public class Client {

	public static void main(String[] args) {
		AbstractSubject sub = new Subject();
        sub.addObserver(new ObserverImp1()); //添加观察者1
        sub.addObserver(new ObserverImp2()); //添加观察者2
        sub.doSomething();//主体发生变化
	}

}
