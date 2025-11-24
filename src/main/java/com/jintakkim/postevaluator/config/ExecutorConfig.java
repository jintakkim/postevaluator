package com.jintakkim.postevaluator.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorConfig {
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final int MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    private static final long KEEP_ALIVE_TIME = 0L;

    public final ExecutorService executor;

    public ExecutorConfig() {
        this.executor = new ThreadPoolExecutor(CORE_POOL_SIZE,  MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        //앱 종료시 대기
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            this.executor.shutdown();
            try {
                if (!this.executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    this.executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                this.executor.shutdownNow();
            }
        }));
    }
}
