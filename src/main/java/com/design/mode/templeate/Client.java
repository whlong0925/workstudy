package com.design.mode.templeate;
/**
 * 模板方法模式是类的行为模式。准备一个抽象类，将部分逻辑以具体方法以及具体构造函数的形式实现，然后声明一些抽象方法来迫使子类实现剩余的逻辑。
 * 不同的子类可以以不同的方式实现这些抽象方法，从而对剩余的逻辑有不同的实现。这就是模板方法模式的用意。
 * 模板模式的关键是：子类可以置换掉父类的可变部分，但是子类却不可以改变模板方法所代表的顶级逻辑。
 * @author rain
 *
 */
public class Client {
 public static void main(String[] args) {
	 AbstractTemplate t = new ConcreteTemplate();
	 t.templateMethod();
}
}
