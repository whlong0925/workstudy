package com.java.thread;

import java.util.Calendar;
import java.util.concurrent.locks.ReentrantLock;

public class TestReentrantLock{
	private ReentrantLock lock = null;
	// 用于线程同步访问的共享数据
	public int data = 100;

	public TestReentrantLock() {
		// 创建一个自由竞争的可重入锁
		lock = new ReentrantLock();
	}

	public static void main(String[] args) {

		TestReentrantLock tester = new TestReentrantLock();

		// 测试可重入，函数testReentry() 执行获取锁后，显示信息的功能
		tester.testReentry();
		// 能执行到这里而不阻塞，表示锁可重入
		tester.testReentry();
		// 再次重入
		tester.testReentry();

		// 释放重入测试的锁，要按重入的数量解锁，否则其他线程无法获取该锁。
		tester.getLock().unlock();
		tester.getLock().unlock();
		tester.getLock().unlock();

		// 启动3个线程测试在锁保护下的共享数据data的访问
		tester.test();
	}

	public ReentrantLock getLock() {
		return lock;
	}

	public void test() {
		new Thread(new workerThread(this)).start();
		new Thread(new workerThread(this)).start();
		new Thread(new workerThread(this)).start();

	}

	public void testReentry() {
		lock.lock();

		Calendar now = Calendar.getInstance();

		System.out.println(now.getTime() + " " + Thread.currentThread() + " get lock.");
	}

	// 线程调用的方法
	public void testRun() throws Exception {
		// 加锁
		lock.lock();

		Calendar now = Calendar.getInstance();
		try {
			// 获取锁后显示 当前时间 当前调用线程 共享数据的值（并使共享数据 + 1）
			System.out.println(now.getTime() + " "
					+ Thread.currentThread().getName() + " accesses the data "
					+ data++);

			// 模拟其他处理，这里假设休眠一下
			Thread.sleep(5);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 解锁
			lock.unlock();
		}
	}
}

// 工作线程，调用TestServer.testRun
class workerThread implements Runnable {

	private TestReentrantLock tester = null;

	public workerThread(TestReentrantLock testLock) {
		this.tester = testLock;
	}

	public void run() {
		// 循环调用，尝试加锁，并对共享数据+1，然后显示出来
		while (true) {
			try {
				// 调用tester.testRun()
				tester.testRun();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}