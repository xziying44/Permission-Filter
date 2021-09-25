package com.tjrac.filter.utils;

import java.util.concurrent.Semaphore;

/**
 * RWSignal 读者写者问题 -- 读者优先
 *
 * @author : xziying
 * @create : 2020-11-16 16:26
 */
public class RWSignal {
    /**
     * 对共享资源互斥信号
     */
    Semaphore rw = new Semaphore(1);

    /**
     * 读线程数
     */
    int count = 0;

    /**
     * count读写的互斥信号量
     */
    Semaphore mutex = new Semaphore(1);

    public synchronized void readStart() throws InterruptedException {
        mutex.acquire();
        count++;
        if (count == 1)
            rw.acquire();  // 如果有读进程则封锁写进程
        mutex.release();
        // 开始读操作
    }
    public synchronized void readEnd() throws InterruptedException {
        // 结束读操作
        mutex.acquire();
        count--;
        if (count == 0)
            rw.release();  // 如果有读进程则封锁写进程
        mutex.release();
    }

    public synchronized void writeStart() throws InterruptedException {
        rw.acquire();
        // 开始写操作
    }
    public synchronized void writeEnd(){
        // 结束写操作
        rw.release();
    }
}
