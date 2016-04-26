package com.zookeeper;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

public class SharedLock {
    private InterProcessLock interProcessLock;

    public SharedLock(LockZookeeperClient lockZookeeperClient, String resourceId) {
        super();

        if (StringUtils.isBlank(resourceId)) {
            throw new NullPointerException("resourceId");
        }
        String path = lockZookeeperClient.getBasePath();
        path += ("/" + resourceId.trim());

        interProcessLock = new InterProcessMutex(
            lockZookeeperClient.getCuratorFramework(), path);
        lockZookeeperClient.gc(path);
    }

    /**
     * Acquire the mutex - blocking until it's available. Each call to acquire
     * must be balanced by a call
     * to {@link #release()}
     *
     * @throws Exception
     *         ZK errors, connection interruptions
     */
    public void acquire() throws Exception {
        interProcessLock.acquire();
    }

    /**
     * Acquire the mutex - blocks until it's available or the given time
     * expires. Each call to acquire that returns true must be balanced by a
     * call
     * to {@link #release()}
     *
     * @param time
     *        time to wait
     * @param unit
     *        time unit
     * @return true if the mutex was acquired, false if not
     * @throws Exception
     *         ZK errors, connection interruptions
     */
    public boolean acquire(long time, TimeUnit unit) throws Exception {
        return interProcessLock.acquire(time, unit);
    }

    /**
     * Perform one release of the mutex.
     *
     * @throws Exception
     *         ZK errors, interruptions, current thread does not own the lock
     */
    public void release() throws Exception {
        interProcessLock.release();
    }

    /**
     * Returns true if the mutex is acquired by a thread in this JVM
     *
     * @return true/false
     */
    public boolean isAcquiredInThisProcess() {
        return interProcessLock.isAcquiredInThisProcess();
    }
}