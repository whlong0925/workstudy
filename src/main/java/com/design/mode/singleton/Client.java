package com.design.mode.singleton;

public class Client {

	public static void main(String[] args) throws Exception {
//		testEagerSingleton();
		testLazySingleton();
	}

	public static void testEagerSingleton()  throws Exception {
		new Thread() {
			public void run() {
				EagerSingleton instance = EagerSingleton.getInstance();
				System.out.println("第一次：" + instance.toString());
			};
		}.start();
		Thread.sleep(5000);
		new Thread() {
			public void run() {
				EagerSingleton instance = EagerSingleton.getInstance();
				System.out.println("第二次：" + instance.toString());
			};
		}.start();
	}
	
	public static void testLazySingleton()  throws Exception {
		new Thread() {
			public void run() {
				LazySingleton instance = LazySingleton.getInstance();
				System.out.println("第一次：" + instance.toString());
			};
		}.start();
		Thread.sleep(5000);
		new Thread() {
			public void run() {
				LazySingleton instance = LazySingleton.getInstance();
				System.out.println("第二次：" + instance.toString());
			};
		}.start();
	}
}