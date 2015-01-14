package com.design.mode.observer;
/**
 *　具体的主题角色
 * @author rain
 *
 */
public class Subject extends AbstractSubject {

	@Override
	public void doSomething() {
		System.out.println("被观察者事件反生");
		this.notifyObserver();
	}

}
