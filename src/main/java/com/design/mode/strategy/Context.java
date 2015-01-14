package com.design.mode.strategy;

public class Context {
	 //持有一个具体的策略对象
	private Strategy strategy;
	/**
     * 构造函数，传入一个具体的策略对象
     * @param strategy    具体的策略对象
     */
	public Context(Strategy strategy){
		this.strategy = strategy;
	}
	/**
     * 计算打折的价格
     * @param price    原价
     * @return    打折的价格
     */
	public double getDisCount(double price){
		return this.strategy.getDisCount(price);
	}
}
