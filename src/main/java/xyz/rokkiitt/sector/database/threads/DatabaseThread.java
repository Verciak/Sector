package xyz.rokkiitt.sector.database.threads;

import java.util.concurrent.*;

import xyz.rokkiitt.sector.utils.Time;

import java.util.*;

public class DatabaseThread
{
    private Thread t;
    protected int updated;
    private long msgtime;
    private static final int TICK_TIME = 50000000;
    private boolean run;
    protected Queue<Runnable> que;
    
    public DatabaseThread() {
        this.que = new ConcurrentLinkedQueue<Runnable>();
        this.run = true;
        (this.t = new Thread(this::loop, "Database Thread")).start();
        this.updated = 0;
        this.msgtime = System.currentTimeMillis() + Time.MINUTE.getTime(5);
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
                    if (this.que.size() > 0) {
                        final Runnable x = this.que.poll();
                        try {
                            x.run();
                            ++this.updated;
                        }
                        catch (Exception e) {
                            this.que.add(x);
                            e.printStackTrace();
                            continue;
                        }
                    }
                    if (this.msgtime <= System.currentTimeMillis()) {
                        System.out.println("DATABASE: " + this.updated + " rzeczy zostalo zaktualizowanych w bazie danych od ostatniej wiadomosci");
                        this.msgtime = System.currentTimeMillis() + Time.MINUTE.getTime(5);
                        this.updated = 0;
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



    
    public void addQueue(final String e) {
        this.que.add(() -> QueueExecutor.writeThenFlush(e));
    }
    
    public Thread getThread() {
        return this.t;
    }
}
