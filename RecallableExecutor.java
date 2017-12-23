package me.jeremy.test;

import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

/**
 * save cancelled task, but there is a risk that the cancelledTasks may contain
 * some tasks which is already executed, because executor might be shutdown right
 * after the isShutdown() is executed in the execute(final Runnable task) method.
 * @author JeremyJi
 */
public class RecallableExecutor extends AbstractExecutorService {
    private final Set<Runnable> cancelledTasks = new ConcurrentSkipListSet<Runnable>();
    private final ExecutorService es;

    public RecallableExecutor(ExecutorService es) {
        this.es = es;
    }

    @Override
    public void shutdown() {
        es.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return es.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return es.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return es.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return es.awaitTermination(timeout, unit);
    }

    @Override
    public void execute(final Runnable task) {
        es.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    task.run();
                } finally {
                    if (isShutdown() && Thread.currentThread().isInterrupted()) {
                        cancelledTasks.add(task);
                    }
                }
            }
        });
    }

    public Set<Runnable> getCancelledTasks() {
        if (!isTerminated()) throw new IllegalStateException();
        return cancelledTasks;
    }

    public static void main(String[] args){
        ExecutorService es = Executors.newFixedThreadPool(10);
        if(es instanceof ThreadPoolExecutor){
            ((ThreadPoolExecutor) es).setRejectedExecutionHandler(new RejectedExecutionHandler() {
                @Override
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    //ignored
                }
            });
        }
        System.identityHashCode(es);
    }
}
