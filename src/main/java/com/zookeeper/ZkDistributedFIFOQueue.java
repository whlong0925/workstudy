package com.zookeeper;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分布式队列
 * @author renzhengzhi
 * 分布式队列的实现思想：
 * 在指定path下创建顺序子节点，序号从小到大递增，出队时获取序号小的节点
 */
public class ZkDistributedFIFOQueue {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ZkDistributedFIFOQueue.class);
	
	private String queue = "/_fifo_queue_";
	
	private ZooKeeper zk;
	
	public ReentrantLock lock = new ReentrantLock();
	
	@Before
	public void connect(){
		try {
			final CountDownLatch semaphore = new CountDownLatch(1);
			this.zk = new ZooKeeper("127.0.0.1:2181", 60000, new Watcher() {
				public void process(WatchedEvent event) {
					if (KeeperState.SyncConnected == event.getState()){
						if (EventType.None == event.getType() && event.getPath() == null){
							System.out.println("链接成功!");
							semaphore.countDown();
						} else if (EventType.NodeChildrenChanged == event.getType()){
							try {
								System.out.println("节点发生变化，当前节点个数：" + ZkDistributedFIFOQueue.this.zk.getChildren(ZkDistributedFIFOQueue.this.queue, true).size());
							} catch (KeeperException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
			semaphore.await();
		} catch (IOException e) {
			e.printStackTrace();
			if (LOGGER.isDebugEnabled()){
				LOGGER.debug("链接zookeeper服务异常！");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			if (LOGGER.isDebugEnabled()){
				LOGGER.debug("主线程中断！");
			}
		}
	}
	
	private static final int coreNum = Runtime.getRuntime().availableProcessors();
	
	private static final ExecutorService threadPool = Executors.newFixedThreadPool(1);
	
	/**
	 * 使用zookeeper的有序临时节点模拟先进先出队列
	 */
	@Test
	public void createQueue(){
		try {
            /**
            *创建队列根节点
            */
			Stat rootStat = zk.exists(queue, true);
			if (rootStat == null){
				zk.create(queue, "distributed fifo queue".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			}
            /**
            *入队
            */
			for (int i=0;i<10;i++){
				String id = zk.create(queue + "/q" + i, ("我是第" + i +"个入队的人。").getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
				System.out.println(id);
			}
			
			/**
            *出队
            */
			List<String> children = zk.getChildren(queue, true);
			for(String str :children){
                System.out.println("-----------"+str);
            }
			if (children != null){
				int size = children.size();
				CountDownLatch semaphore = new CountDownLatch(size);
				for (int i=0;i<size;i++){
					Consumer consumer = new Consumer(semaphore);
					threadPool.submit(consumer);
				}
				semaphore.await();
			}
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	class Consumer implements Runnable{
		CountDownLatch semaphore;
		public Consumer(CountDownLatch semaphore){
			this.semaphore = semaphore;
		}
		public void run() {
			try {
				lock.lock();
				List<String> children = zk.getChildren(queue, true);
				if (children != null && children.size() > 0){
					final String name = children.get(0);
					String data = (new String(zk.getData(queue + "/" + name,null,null)));
					zk.delete(queue + "/" + name, 0);
					System.out.println(data+"-当前线程：" + Thread.currentThread().getId() + "消费数据：" + name);
					semaphore.countDown();
				}
			} catch (KeeperException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally{
				lock.unlock();
			}
		}
	}
}