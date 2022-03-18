package xyz.rokkiitt.sector.database.threads;

import java.util.concurrent.*;
import java.util.*;

public class QueryThread
{
    private final Thread t;
    private static final int TICK_TIME = 50000000;
    private boolean run;
    protected Queue<Runnable> que;
    
    public QueryThread() {
        this.que = new ConcurrentLinkedQueue<Runnable>();
        this.run = true;
        (this.t = new Thread(this::loop, "Query Thread")).start();
    }
    
    public void loop() {
        try {
            long lastTick = System.nanoTime();
            long catchupTime = 0L;
            while (this.run) {
                final long curTime = System.nanoTime();
                final long wait = 50000000L - (curTime - lastTick) - catchupTime;
                if (wait > 0L) {
                    Thread.sleep(wait / 1000000L);
                    catchupTime = 0L;
                }
                else {
                    catchupTime = Math.min(1000000000L, Math.abs(wait));
                    int limit = 0;
                    while (this.que.size() > 0 && limit <= 50) {
                        final Runnable x = this.que.poll();
                        try {
                            x.run();
                            ++limit;
                        }
                        catch (Exception e) {
                            this.que.add(x);
                            e.printStackTrace();
                        }
                    }
                    lastTick = curTime;
                }
            }
        }
        catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }
    
    public void shutdown() {
        for (final Runnable rr : this.que) {
            rr.run();
        }
        this.run = false;
    }
    
    public void addQueue(final Runnable run) {
        this.que.add(run);
    }
    
    public Thread getThread() {
        return this.t;
    }
}
