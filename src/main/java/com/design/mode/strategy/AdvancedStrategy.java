package com.design.mode.strategy;

/**
 * 高级会员
 * @author rain
 *
 */
public class AdvancedStrategy implements Strategy {

	public double getDisCount(double price) {
		System.out.println("对于高级会员的折扣为20%");
        return price * 0.2;
	}

}
