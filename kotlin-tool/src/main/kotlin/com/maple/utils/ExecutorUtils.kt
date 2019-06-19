package com.maple.utils

import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 线程池工具类
 *
 * @author maple
 * @version V1.0
 * @since 2019-01-08 19:25
 */
object ExecutorUtils {

    private val singleThreadPool: ExecutorService? = null

    /**
     * 返回一个单线程的单例线程池
     * @return 线程池服务
     */
    fun getSingleThreadPool(): ExecutorService {
        return singleThreadPool ?: ThreadPoolExecutor(
            1, 1, 30, TimeUnit.MINUTES,
            LinkedBlockingQueue(1024), ThreadPoolExecutor.AbortPolicy()
        )
    }

    /**
     * 返回一个指定大小的线程池
     *
     * @param corePoolSize 核心线程数
     * @param maximumPoolSize 最大线程数
     * @return 线程池服务
     */
    fun getExecutorService(corePoolSize: Int, maximumPoolSize: Int): ExecutorService {
        return ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            0,
            TimeUnit.HOURS,
            LinkedBlockingQueue(maximumPoolSize),
            ThreadPoolExecutor.AbortPolicy()
        )
    }
}
