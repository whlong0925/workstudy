package com.java.thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/**
 * 主线程运行10次后接着子线程运行10次，然后在主线程运行10次，如此交替5次
 * 采用ReentrantLock Condition 方法
 * ThreadTest3 采用synchronized wait nofify方法 
 *
 */
public class ThreadTest4 {

	public static void main(String[] args){
		final Business business = new Business();
		new Thread(new Runnable() {
			public void run() {
				for (int i = 1; i <= 5; i++) {
					business.execSub(i);
				}
			}
		}).start();
		for (int i = 1; i <= 5; i++) {
			business.execMain(i);
		}
	}

	static class Business {
		public boolean isMain = true;
		private ReentrantLock lock = null;
		private Condition mainCondition  = null;
		private Condition subCondition = null;
		public Business(){
			this.lock = new ReentrantLock();
			this.mainCondition  = lock.newCondition();
			this.subCondition = lock.newCondition();
		}
		public void execMain(int i){
			lock.lock();
			try {
				while (!isMain) {
					try {
						mainCondition.await();//阻塞
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for (int j = 0; j < 10; j++) {
					System.out.println("主线程运行i=" + i + ",j=" + j);
				}
				isMain = false;
				subCondition.signal();//唤醒sub线程执行
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				lock.unlock();
			}
		}

		public void execSub(int i) {
			lock.lock();
			try {
				while (isMain) {
					try {
						subCondition.await();//阻塞
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				for (int j = 0; j < 10; j++) {
					System.out.println("-----子线程运行i=" + i + ",j=" + j);
				}
				isMain = true;
				mainCondition.signal();//唤醒main线程执行
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				lock.unlock();
			}
		}
	}
}
