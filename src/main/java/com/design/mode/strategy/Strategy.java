package com.design.mode.strategy;
/**
 * 抽象策略类
 * @author rain
 *
 */
public interface Strategy {
	/**
     * 计算打折后的价格
     * @param price    原价
     * @return 计算出打折的价格
     */
    public double getDisCount(double price);
}
