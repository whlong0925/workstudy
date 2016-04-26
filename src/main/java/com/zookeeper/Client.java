package com.zookeeper;

import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) {
        LockZookeeperClientFactory lzc = new LockZookeeperClientFactory();
        lzc.setZookeeperIpPort("localhost:2181");
        lzc.setBasePath("/locks/sharedLock/");
        lzc.init();

        SharedLock sharedLock = new SharedLock(lzc, "sharedLock1");
        try {
            if (sharedLock.acquire(100, TimeUnit.MILLISECONDS)) {
                System.out.println("sharedLock1 get");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                sharedLock.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        lzc.destroy();
    }

}
