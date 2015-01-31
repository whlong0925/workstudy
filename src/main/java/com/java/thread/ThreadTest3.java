package com.java.thread;
/**
 * 主线程运行10次后接着子线程运行10次，然后在主线程运行10次，如此交替5次
 * 采用synchronized wait nofify方法
 * ThreadTest4 采用ReentrantLock Condition 方法
 *
 */
public class ThreadTest3 {

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

		public synchronized void execMain(int i){
			while (!isMain) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int j = 0; j < 10; j++) {
				System.out.println("主线程运行i=" + i + ",j=" + j);
			}
			isMain = false;
			this.notify();
		}

		public synchronized void execSub(int i) {
			while (isMain) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			for (int j = 0; j < 10; j++) {
				System.out.println("-----子线程运行i=" + i + ",j=" + j);
			}
			isMain = true;
			this.notify();
		}
	}
}
