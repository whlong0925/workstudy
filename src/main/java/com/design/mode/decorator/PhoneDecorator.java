package com.design.mode.decorator;

/**
 * 装饰基类
 * 
 * @author rain
 * 
 */
public class PhoneDecorator implements Phone {
	protected Phone phone;

	public PhoneDecorator(Phone phone) {
		this.phone = phone;
	}

	public void feature() {
		phone.feature();
	}
}