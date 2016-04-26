package com.zookeeper;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.nio.charset.Charset;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

public class QueueZooKeeper {
    public static final int queuesize = 10;//队列的最大容量
    public static final String queueroot = "/queue";//zookeeper队列根节点
    private static LinkedList<String> queuelst = null;
    //for test
    private static String host1 = "localhost:2181";
    private static String host2 = "localhost:2182";
    private static String host3 = "localhost:2183";

    /*
     * Main函数无参数输入运行doTest（）测试，
     * 一个参数为clean表示清空整个队列。一个数字参数表示执行出队操作，数字表示第几台zookeeper主机，
     * 取值范围1，2，3,如果是其他参数值，则连接host1，host2，host3任意可用zookeeper主机。
     * 两个参数时，第一个参数数字为zookeeper主机标识，第二个字符串参数表示新入队节点名称
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            doTest();
        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("clean")) {
                ZooKeeper zk = connection(host1);
                CleanQueue(zk);
                zk.close();
            } else {
                doAction(Integer.parseInt(args[0]), null);
            }
        } else {
            doAction(Integer.parseInt(args[0]), args[1]);
        }
    }

    /* 用于队列测试 */
    public static void doTest() throws Exception {
        ZooKeeper zk = connection(host1);
        InitQueue(zk);
        EnQueue(zk, "1");
        EnQueue(zk, "2");
        EnQueue(zk, "3");
        EnQueue(zk, "4");
        EnQueue(zk, "5");
        DeQueue(zk);
        DeQueue(zk);
        DeQueue(zk);
        DeQueue(zk);
        DeQueue(zk);
        zk.close();
       /* zk = connection(host3);
        EnQueue(zk, "test1");
        EnQueue(zk, "test2");
        EnQueue(zk, "test3");
        EnQueue(zk, "test4");
        EnQueue(zk, "test5");
        EnQueue(zk, "test6");
        EnQueue(zk, "test7");
        EnQueue(zk, "test8");
        EnQueue(zk, "test9");
        EnQueue(zk, "test10");
        DeQueue(zk);
        DeQueue(zk);
        DeQueue(zk);
        zk.close();*/
    }

    /* 根据参数个数判断入队和出队操作，第一个参数数字表示第几台zookeeper主机，第二个表示入队操作新加入节点名称，第二个节点为null时表示出队 */
    public static void doAction(int client, String nodeName) throws Exception {
        ZooKeeper zk = null;
        switch (client) {
            case 1:
                zk = connection(host1);
                break;

            case 2:
                zk = connection(host2);
                InitQueue(zk);
                EnQueue(zk, "2");
                break;
            case 3:
                zk = connection(host3);
                break;
            default:
                zk = connection(host1 + "," + host2 + "," + host3);
                break;
        }
        InitQueue(zk);
        if (nodeName == null)
            DeQueue(zk);
        else
            EnQueue(zk, nodeName);
        zk.close();
    }

    // 创建一个与服务器的连接
    public static ZooKeeper connection(String host) throws IOException {
        ZooKeeper zk = new ZooKeeper(host, 60000, new Watcher() {
            // 监控所有被触发的事件
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.NodeCreated
                    && event.getPath().equals("/queue")) {
                    System.out.println("Queue has Completed.Finish testing!!!");
                }
            }
        });
        return zk;
    }

    /*
     * 入队，出队操作后调用此函数，
     * 将队列:"节点数,节点列表..... "写入队列根节点/queue
     * 显示队列使用情况及其队列各个节点列表顺序
     */
    public static void isCompleted(ZooKeeper zk) throws KeeperException,
            InterruptedException {
        int length = zk.getChildren("/queue", true).size();
        // update list information to the root queue node
        StringBuilder sb = new StringBuilder();
        sb.append(length);
        for (String str : queuelst) {
            sb.append("," + str);
        }
        Stat stat = zk.exists(queueroot, false);
        zk.setData(queueroot, sb.toString().getBytes(), stat.getVersion());
        //for display
        System.out.println("Current Queue after Complete:" + length + "/"
            + queuesize);
        System.out.println("Current Queue List:"
            + new String(zk.getData(queueroot, null, null), Charset
                .defaultCharset()));
    }

    /* 初始化队列 */
    public static void InitQueue(ZooKeeper zk) throws KeeperException,
            InterruptedException// 初始化队列
    {
        //System.out.println("WATCH => /queue");
        queuelst = new LinkedList<String>();
        if (zk.exists("/queue", false) == null) {
            zk.create("/queue", ("0").getBytes(), Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        } else {
            String[] strs = new String(zk.getData(queueroot, null, null),
                Charset.defaultCharset()).split(",");
            for (int i = 1; i < strs.length; i++) {
                queuelst.addLast(strs[i]);
            }
        }
    }

    /*
     * 入队
     * 在队列根节点下创建队列子节点，用户可自定义节点名称，若名称已存在或者队列已满，则忽略该入队操作并提示用户
     */
    public static void EnQueue(ZooKeeper zk, String nodeName)
            throws KeeperException, InterruptedException// 进队列
    {
        if (IsQueueFull(zk)) {
            System.out.println("Queue is full, ignored the new node "
                + nodeName);
            return;
        }
        if (queuelst.contains(nodeName)) {
            System.out.println(nodeName
                + " already exist in the Queue, ignored the new node "
                + nodeName);
            return;
        }
        System.out.println("EnQueue: creating " + queueroot + "/" + nodeName);
        zk.create(queueroot + "/" + nodeName, nodeName.getBytes(),
            Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        queuelst.addLast(nodeName);
        //isCompleted(zk);
    }

    /*
     * 出队操作
     * 队列为空则忽略操作并提示用户
     * 出队并删除第一个节点
     */
    public static void DeQueue(ZooKeeper zk) throws KeeperException,
            InterruptedException// 出队列
    {
        if (IsQueueEmpty(zk)) {
            System.out.println("Queue is Empty, cannot dequeue!!");
            return;
        }
        String nodeName = queuelst.getFirst();
        System.out.println("Dequeue: Removing node" + queueroot + "/"
            + nodeName);
        zk.delete(queueroot + "/" + nodeName, -1);
        queuelst.removeFirst();
        //isCompleted(zk);
    }

    /* 获取队列长度 */
    public static int getQueueLength(ZooKeeper zk) throws KeeperException,
            InterruptedException// 判断队列长度
    {
        return zk.getChildren(queueroot, true).size();
    }

    /* 判断队列长度 */
    public static boolean IsQueueEmpty(ZooKeeper zk) throws KeeperException,
            InterruptedException// 判断队列是否为空
    {
        if (zk.getChildren(queueroot, true).size() == 0)
            return true;
        else
            return false;
    }

    /* 判断队列是否已经满 */
    public static boolean IsQueueFull(ZooKeeper zk) throws KeeperException,
            InterruptedException// ——判断队列是否已满
    {
        if (zk.getChildren(queueroot, true).size() >= queuesize)
            return true;
        else
            return false;
    }

    /* 清空队列并删除根节点 */
    public static void CleanQueue(ZooKeeper zk) throws KeeperException,
            InterruptedException {
        List<String> children = zk.getChildren(queueroot, true);
        for (String child : children) {// 清空节点
            zk.delete(queueroot + "/" + child, -1);
            System.out.println("Deleting node " + queueroot + "/" + child);
        }
        zk.delete(queueroot, -1);
        System.out.println("Deleting node " + queueroot);
    }
}