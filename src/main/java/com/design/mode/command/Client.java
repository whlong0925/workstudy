package com.design.mode.command;

/**
 * 命令模式把一个请求或者操作封装到一个对象中。
 * 命令模式允许系统使用不同的请求把客户端参数化，对请求排队或者记录请求日志，可以提供命令的撤销和恢复功能。
 * 
 * 命令模式涉及到五个角色，它们分别是：
　　客户端(Client)角色：创建一个具体命令(ConcreteCommand)对象并确定其接收者。
　　命令(Command)角色：声明了一个给所有具体命令类的抽象接口。
　　具体命令(ConcreteCommand)角色：定义一个接收者和行为之间的弱耦合；实现execute()方法，负责调用接收者的相应操作。execute()方法通常叫做执行方法。
　　请求者(Invoker)角色：负责调用命令对象执行请求，相关的方法叫做行动方法。
　　接收者(Receiver)角色：负责具体实施和执行一个请求。任何一个类都可以成为接收者，实施和执行请求的方法叫做行动方法。
 */
public class Client {

	public static void main(String[] args) {
		// 创建接收者
		Receiver receiver = new Receiver();
		// 创建命令对象，设定它的接收者
		Command command = new ConcreteCommand(receiver);
		// 创建请求者，把命令对象设置进去
		Invoker invoker = new Invoker(command);
		// 执行方法
		invoker.action();
	}

}