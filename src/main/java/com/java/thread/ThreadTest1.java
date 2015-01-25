package com.java.thread;

/**
 * 不加锁的情况下两个线程调用一个对象的同一个方法，会出现问题，必须同步
 * 
 */
public class ThreadTest1 {
	public static void main(String[] args) {
		new ThreadTest1().init();
	}

	private void init() {
		final Bussiness t = new Bussiness();
		new Thread() {
			public void run() {
				while (true) {
					try {
						Thread.currentThread().sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					t.output("1111111111");
				}
			}
		}.start();

		new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.currentThread().sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					t.output2("2222222222");
				}
			}
		}).start();
	}

	class Bussiness {
		//同一对象再调用output方法时候 output2只能等待
		public void output(String name) {
			synchronized(this){
				for (int i = 0; i < name.length(); i++) {
					System.out.print(name.charAt(i));
				}
				System.out.println();
			}
		}
		public synchronized void output2(String name) {
			for (int i = 0; i < name.length(); i++) {
				System.out.print(name.charAt(i));
			}
			System.out.println();
		}
	}
}
