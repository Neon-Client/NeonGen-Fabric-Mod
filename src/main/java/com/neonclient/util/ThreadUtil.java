package com.neonclient.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class ThreadUtil {
    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public void task(Runnable runnable, long initial, long delay, TimeUnit timeUnit) {
        executorService.scheduleAtFixedRate(runnable, initial, delay, timeUnit);
    }

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    public void submit(Runnable runnable) {
        executorService.submit(runnable);
    }
}