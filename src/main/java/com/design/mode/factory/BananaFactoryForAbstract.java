package com.design.mode.factory;
/**
 * 香蕉工厂生产两种产品：香蕉和香蕉汁
 * @author rain
 *
 */
public class BananaFactoryForAbstract implements FriutFactoryForAbstract {

	public void makeFriut() {
		new Banana().get();
	}

	public void makeDrink() {
		new BananaCider().get();
	}

}
