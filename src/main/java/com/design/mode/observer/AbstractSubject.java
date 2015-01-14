package com.design.mode.observer;

import java.util.Vector;

public abstract class AbstractSubject {
	//观察者列表
	private Vector<Observer> obs = new Vector<Observer>();
	//添加观察者
	public void addObserver(Observer obs){
        this.obs.add(obs);
    }
	//删除观察者
    public void delObserver(Observer obs){
        this.obs.remove(obs);
    }
    //通知观察者
    protected void notifyObserver(){
        for(Observer o: obs){
            o.update();
        }
    }
    //具体的主题方法
    public abstract void doSomething();
}
