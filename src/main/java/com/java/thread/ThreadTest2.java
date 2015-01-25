package com.java.thread;
/**
 * 两个线程再同时运行的时候不被另一个线程打断，只有在一个线程的方法执行完后，才能进入另一个方法
 *
 */
public class ThreadTest2 {
	public static void main(String[] args) {
		new Thread(new Runnable() {

			public void run() {
				for (int i = 0; i < 50; i++) {
					synchronized (ThreadTest2.class) {
						for (int j = 0; j < 10; j++) {
							System.out.println("------子线程运行i="+i+",j="+j);
						}
					}
					
				}
			}
		}).start();

		for (int i = 0; i < 50; i++) {
			synchronized (ThreadTest2.class) {
				for (int j = 0; j < 10; j++) {
					System.out.println("主线程运行i="+i+",j="+j);
				}
			}
			
		}
	}

}
