package xyz.rokkiitt.sector.objects.entity.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RouteFinderThreadPool {
    public static void executeRouteFinderThread(RouteFinderSearchTask t) {
        if (!executor.isShutdown() && !executor.isTerminating())
            executor.execute(t);
    }

    public static void shutDownNow() {
        executor.shutdownNow();
    }

    public static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() + 1, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.AbortPolicy());
}
