package com.design.mode.factory;
/**
 * 苹果工厂生产两种产品：苹果和苹果汁
 * @author rain
 *
 */
public class AppleFactoryAbstract implements FriutFactoryForAbstract {

	public void makeFriut() {
		new Apple().get();
	}

	public void makeDrink() {
		new AppleCider().get();
	}

}
