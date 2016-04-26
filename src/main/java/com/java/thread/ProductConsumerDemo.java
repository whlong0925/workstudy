package com.java.thread;
import java.util.LinkedList;
import java.util.List;
/**
 * 生产者　消费者模式
 * @author rain
 *
 */
public class ProductConsumerDemo{

   private List<String> synchedList;

   public ProductConsumerDemo() {
      synchedList = new LinkedList<String>();
   }

   public String removeElement() throws InterruptedException {
      synchronized (synchedList) {
         while (synchedList.isEmpty()) {
            System.out.println(Thread.currentThread().getName()+"----List is empty...,Wait...");
            synchedList.wait();
         }
         String element = synchedList.remove(0);
         System.out.println(Thread.currentThread().getName()+"----remove Element:"+element);
         synchedList.notifyAll();
         return element;
      }
   }

   public void addElement() {
      synchronized (synchedList) {
    	  while (!synchedList.isEmpty()) {
    		  try {
				synchedList.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	  }
    	  for(int i=0;i<10;i++){
    		  String element = Thread.currentThread().getName()+"---Hello"+i;
        	  synchedList.add(element);
        	  System.out.println(Thread.currentThread().getName()+"----create Element:'" + element + "'");
    	  }
    	  
	      synchedList.notifyAll();
      }
   }

   public static void main(String[] args) {
      final ProductConsumerDemo demo = new ProductConsumerDemo();
      
      Runnable runA = new Runnable() {
         public void run() {
        	 while(true){
        		 try {
					demo.removeElement();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	 }
         }
      };

      Runnable runB = new Runnable() {
         public void run() {
        	while(true){
        		demo.addElement();
        	}
         }
      };

         Thread threadA1 = new Thread(runA, "A1");
         threadA1.start();
         Thread threadA2 = new Thread(runA, "A2");
         threadA2.start();

         Thread threadB1 = new Thread(runB, "B1");
         threadB1.start();
         Thread threadB2 = new Thread(runB, "B2");
         threadB2.start();


   }
}