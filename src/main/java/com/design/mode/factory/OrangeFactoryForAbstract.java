package com.design.mode.factory;
/**
 * 橘子工厂生产两种产品：橘子和橘子汁
 * @author rain
 *
 */
public class OrangeFactoryForAbstract implements FriutFactoryForAbstract {

	public void makeFriut() {
		new Orange().get();
	}

	public void makeDrink() {
		new OrangeCider().get();
	}

}
