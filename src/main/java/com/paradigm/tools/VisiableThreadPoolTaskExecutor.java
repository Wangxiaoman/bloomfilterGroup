package com.paradigm.tools;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.paradigm.log.CommonLogger;

public class VisiableThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private void showThreadPoolInfo(String prefix) {
        
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();

        if (null == threadPoolExecutor) {
            return;
        }

        CommonLogger.infoOneInThousand(this.getThreadNamePrefix() + ", " + prefix + ",taskCount ["
                + threadPoolExecutor.getTaskCount() + "], completedTaskCount ["
                + threadPoolExecutor.getCompletedTaskCount() + "], activeCount ["
                + threadPoolExecutor.getActiveCount() + "], queueSize ["
                + threadPoolExecutor.getQueue().size() + "]");
    }

    @Override
    public void execute(Runnable task) {
        showThreadPoolInfo("task do execute");
        super.execute(task);
    }

    @Override
    public void execute(Runnable task, long startTimeout) {
        showThreadPoolInfo("task with start timeout do execute");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(Runnable task) {
        showThreadPoolInfo("task do submit");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        showThreadPoolInfo("task with callable do submit");
        return super.submit(task);
    }
}
