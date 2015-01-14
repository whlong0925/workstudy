package com.design.mode.strategy;
/**
 * 中级会员
 * @author rain
 *
 */
public class IntermediateStrategy implements Strategy {

	public double getDisCount(double price) {
		System.out.println("对于中级会员的折扣为10%");
        return price * 0.1;
	}

}
