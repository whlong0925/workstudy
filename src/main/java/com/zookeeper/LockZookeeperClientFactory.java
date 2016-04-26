package com.zookeeper;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class LockZookeeperClientFactory implements LockZookeeperClient {
    private static final Log LOG = LogFactory
        .getLog(LockZookeeperClientFactory.class);

    private boolean hasGc = true;
    private Timer gcTimer;
    private TimerTask gcTimerTask;
    private ConcurrentSkipListSet<String> gcPaths = new ConcurrentSkipListSet<String>();
    private int gcIntervalSecond = 60;

    private CuratorFramework curatorFramework;
    private String zookeeperIpPort = "localhost:2181";
    private int sessionTimeoutMs = 10000;
    private int connectionTimeoutMs = 10000;
    private String basePath = "/locks";

    public void setHasGc(boolean hasGc) {
        this.hasGc = hasGc;
    }

    public void setGcIntervalSecond(int gcIntervalSecond) {
        this.gcIntervalSecond = gcIntervalSecond;
    }

    public void setZookeeperIpPort(String zookeeperIpPort) {
        this.zookeeperIpPort = zookeeperIpPort;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public void setBasePath(String basePath) {
        basePath = basePath.trim();
        if (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        this.basePath = basePath;
    }

    public void init() {
        if (StringUtils.isBlank(zookeeperIpPort)) {
            throw new NullPointerException("zookeeperIpPort");
        }

        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000,
            3);
        curatorFramework = CuratorFrameworkFactory.newClient(
            zookeeperIpPort.trim(), sessionTimeoutMs, connectionTimeoutMs,
            retryPolicy);
        curatorFramework.start();
        LOG.info("CuratorFramework initialise succeed.");

        if (hasGc) {
            gc();
        }
    }

    public void destroy() {
        gcPaths.clear();
        gcPaths = null;
        gcStop();
        curatorFramework.close();
        curatorFramework = null;
    }

    public void gc(String gcPath) {
        if (hasGc && StringUtils.isNotBlank(gcPath)) {
            gcPaths.add(gcPath.trim());
        }
    }

    public CuratorFramework getCuratorFramework() {
        return this.curatorFramework;
    }

    public String getBasePath() {
        return this.basePath;
    }

    private synchronized void gc() {
        gcStop();

        try {
            scanningGCNodes();
        } catch (Throwable e) {
            LOG.warn(e);
        }

        gcTimerTask = new TimerTask() {
            @Override
            public void run() {
                doingGc();
            }
        };

        Date begin = new Date();
        begin.setTime(begin.getTime() + (10 * 1000L));
        gcTimer = new Timer("lock-gc", true);
        gcTimer.schedule(gcTimerTask, begin, gcIntervalSecond * 1000L);
    }

    private synchronized void gcStop() {
        if (null != gcTimer) {
            gcTimer.cancel();
            gcTimer = null;
        }

        if (null != gcTimerTask) {
            gcTimerTask.cancel();
            gcTimerTask = null;
        }
    }

    private synchronized void scanningGCNodes() throws Exception {
        if (null == curatorFramework.checkExists().forPath(basePath)) {
            return;
        }

        List<String> paths = curatorFramework.getChildren().forPath(basePath);
        if (CollectionUtils.isEmpty(paths)) {
            gcPaths.add(basePath);
            return;
        }

        for (String path : paths) {
            try {
                String tmpPath = basePath + "/" + path;
                if (null == curatorFramework.checkExists().forPath(tmpPath)) {
                    continue;
                }

                gcPaths.add(tmpPath);
            } catch (Throwable e) {
                LOG.warn("scanning gc nodes error.", e);
            }
        }
    }

    private synchronized void doingGc() {
        LOG.debug("GC beginning.");

        if (CollectionUtils.isNotEmpty(gcPaths)) {
            for (String path : gcPaths) {
                try {
                    if (null != curatorFramework.checkExists().forPath(path)) {
                        if (CollectionUtils.isEmpty(curatorFramework
                            .getChildren().forPath(path))) {
                            curatorFramework.delete().forPath(path);
                            gcPaths.remove(path);
                            LOG.debug("GC " + path);
                        }
                    } else {
                        gcPaths.remove(path);
                    }
                } catch (Throwable e) {
                    gcPaths.remove(path);
                    LOG.warn(e);
                }
            }
        }

        LOG.debug("GC ended.");
    }

}