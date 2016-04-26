package com.java.thread;

public class ThreadTest5 {
public static void main(String[] args) {
	final Bussiness bussiness = new Bussiness();
	new Thread(new Runnable() {
		public void run() {
			try {
				bussiness.printNumber();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}).start();
	new Thread(new Runnable() {
		public void run() {
			try {
				bussiness.printChar();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}).start();
	new Thread(new Runnable() {
		public void run() {
			try {
				bussiness.printChar2();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}).start();
}
}
class Bussiness{
	private Boolean isNumber = true;
	private Boolean ischar = false;
	private Boolean ischar2 = false;
	public  void  printNumber() throws Exception{
		
		for(int i=1;i<27;i++){
			synchronized(this){
				while(!isNumber){
					this.wait();
				}
				System.out.print(i);
				isNumber = false;
				ischar = true;
				ischar2 = false;
				this.notifyAll();
			}
		}
		
	}
	public synchronized void printChar() throws Exception{
		for(int i=65;i<97;i++){
			synchronized(this){
				while(!ischar){
					this.wait();
				}
				System.out.print((char)i);
				isNumber = false;
				ischar = false;
				ischar2 = true;
				this.notifyAll();
			}
		}
	}
	public synchronized void printChar2() throws Exception{
		for(char i='a';i<='z';i++){
			synchronized(this){
				while(!ischar2){
					this.wait();
				}
				System.out.print(i);
				isNumber = true;
				ischar = false;
				ischar2 = false;
				this.notifyAll();
			}
		}
	}
}
