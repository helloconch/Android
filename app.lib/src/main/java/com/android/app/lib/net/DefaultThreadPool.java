package com.android.app.lib.net;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池、缓冲队列
 */
public class DefaultThreadPool {

    //阻塞队列最大任务数量
    static final int BLOCKING_QUEUE_SIZE = 20;
    static final int THREAD_POOL_MAX_SIZE = 10;
    static final int THREAD_POOL_SIZE = 6;

    static ArrayBlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<Runnable>(DefaultThreadPool.BLOCKING_QUEUE_SIZE);
    private static DefaultThreadPool instance = null;

    static AbstractExecutorService pool = new ThreadPoolExecutor(DefaultThreadPool.THREAD_POOL_SIZE,
            DefaultThreadPool.THREAD_POOL_MAX_SIZE,
            15L,
            TimeUnit.SECONDS,
            DefaultThreadPool.blockingQueue,
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static synchronized DefaultThreadPool getInstance() {
        if (DefaultThreadPool.instance == null) {
            DefaultThreadPool.instance = new DefaultThreadPool();
        }
        return DefaultThreadPool.instance;
    }

    public static void removeAllTask() {
        DefaultThreadPool.blockingQueue.clear();
    }

    public static void removeTaskFromQueue(final Object obj) {
        DefaultThreadPool.blockingQueue.remove(obj);
    }

    /**
     * 关闭，并等待任务执行完成，不接受新任务
     */
    public static void shutDown() {
        if (DefaultThreadPool.pool != null) {
            DefaultThreadPool.pool.shutdown();
        }
    }

    /**
     * 立即关闭，并挂起所有正在执行的线程，不接受新任务
     */

    public static void shutDownRighNow() {
        if (DefaultThreadPool.pool != null) {
            DefaultThreadPool.pool.shutdown();
            //设置超时极短，强制关闭所有任务
            try {
                DefaultThreadPool.pool.awaitTermination(1, TimeUnit.MICROSECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 执行任务
     *
     * @param r
     */
    public void execute(final Runnable r) {
        if (r != null) {
            try {
                DefaultThreadPool.pool.execute(r);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
