package com.design.mode.builder;
/**
 * 导演者（Director）
 */
public class Director {
	Builder builder;

	public Director(Builder builder) {
		this.builder = builder;
	}

	/**
	 * 产品构造方法，负责调用各零件的建造方法,一步步的建造出对象
	 */
	public void construct(String toAddress, String fromAddress) {
		this.builder.buildTo(toAddress);
		this.builder.buildFrom(fromAddress);
		this.builder.buildSubject();
		this.builder.buildBody();
		this.builder.buildSendDate();
	}
}