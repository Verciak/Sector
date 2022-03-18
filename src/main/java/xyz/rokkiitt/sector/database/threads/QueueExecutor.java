package xyz.rokkiitt.sector.database.threads;

import java.util.*;

import xyz.rokkiitt.sector.Main;
import com.google.common.collect.*;

public class QueueExecutor
{
    private static Queue<String> queue;
    private static TaskExecutor tasks;
    
    public static void writeThenFlush(final String e) {
        QueueExecutor.queue.add(e);
        if (QueueExecutor.tasks.addTask()) {
            writeQueueAndFlush();
        }
    }
    
    public static void writeQueueAndFlush() {
        while (QueueExecutor.tasks.fetchTask()) {
            while (QueueExecutor.queue.size() > 0) {
                Main.getProvider().update(QueueExecutor.queue.poll());
            }
        }
    }
    
    static {
        QueueExecutor.queue = Queues.newConcurrentLinkedQueue();
        QueueExecutor.tasks = new TaskExecutor();
    }
}
