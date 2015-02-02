package com.design.mode.builder;
/**
 * 建造模式是对象的创建模式。
 * 建造模式可以将一个产品的内部表象（internal representation）与产品的生产过程分割开来，
 * 从而可以使一个建造过程生成具有不同的内部表象的产品对象。
 *
 */
public class Client {

	public static void main(String[] args) {
		Builder builder = new WelcomeBuilder();
		Director director = new Director(builder);
		AutoMessage autoMessage = director.construct("toAddress@126.com", "fromAddress@126.com");
		autoMessage.send();

	}

}