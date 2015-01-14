package com.design.mode.strategy;

/**
 * 初级会员
 * @author rain
 *
 */
public class PrimaryStrategy implements Strategy {

	public double getDisCount(double price) {
		System.out.println("对于初级会员的没有折扣");
        return price * 0;
	}

}
