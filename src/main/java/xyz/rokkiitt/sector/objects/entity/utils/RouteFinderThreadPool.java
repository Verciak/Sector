package xyz.rokkiitt.sector.objects.entity.utils;

import java.util.concurrent.*;

public class RouteFinderThreadPool
{
    public static ThreadPoolExecutor executor;
    
    public static void executeRouteFinderThread(final RouteFinderSearchTask t) {
        if (!RouteFinderThreadPool.executor.isShutdown() && !RouteFinderThreadPool.executor.isTerminating()) {
            RouteFinderThreadPool.executor.execute(t);
        }
    }
    
    public static void shutDownNow() {
        RouteFinderThreadPool.executor.shutdownNow();
    }
    
    static {
        RouteFinderThreadPool.executor = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors() + 1, 1L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadPoolExecutor.AbortPolicy());
    }
}
