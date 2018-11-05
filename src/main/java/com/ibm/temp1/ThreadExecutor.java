package com.ibm.temp1;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
/**
 * 线程池队列插队Demo，自定义线程池然后使用PriorityBlockingQueue类实现，
 * @author notreami
 */
public class ThreadExecutor {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss::SSS");
    public ExecutorService singleThreadExecutor = new FinalizableDelegatedExecutorService(new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new PriorityBlockingQueue<Runnable>()));
    public static void main(String[] args) {
        ThreadExecutor threadExecutor = new ThreadExecutor();
        AddMsg addMsg = new AddMsg(threadExecutor);// 启动模拟不断加入新消息数据
        InsertMsg insertMsg = new InsertMsg(threadExecutor);// 启动模拟在线程池里插入一条消息
        // 这里不用担心队列的问题
        addMsg.start();
        insertMsg.start();
    }
}
// 启动模拟不断加入新消息数据
class AddMsg extends Thread {
    private ThreadExecutor threadExecutor;
    private int count = 0;
 
    public AddMsg(ThreadExecutor threadExecutor) {
        this.threadExecutor = threadExecutor;
    }
 
    @Override
    public void run() {
        // 模拟10次消息添加
        for (int i = 0; i < 10; i++) {
            System.out.println(ThreadExecutor.sdf.format(System.currentTimeMillis()) + "加入消息～～～加入队列第" + (++count) + "条消息！");
            threadExecutor.singleThreadExecutor.execute(getRunnablePriority(count));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
 
    // 模拟不断加入的新消息数据
    private RunnablePriority getRunnablePriority(final int count) {
        return new RunnablePriority(5,1,System.currentTimeMillis()) {
 
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(ThreadExecutor.sdf.format(System.currentTimeMillis()) + "加入消息～～～加入第" + count + "条消息！");
            }
        };
    }
};
 
// 启动模拟在线程池里插入一条消息
class InsertMsg extends Thread {
    private ThreadExecutor threadExecutor;
    private int count = 0;
 
    public InsertMsg(ThreadExecutor threadExecutor) {
        this.threadExecutor = threadExecutor;
    }
    @Override
    public void run() {
        // 模拟3次消息插入
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(ThreadExecutor.sdf.format(System.currentTimeMillis()) + "插入消息～～～～插入队列第" + (++count) + "条消息！");
            threadExecutor.singleThreadExecutor.execute(getRunnablePriority(count));
        }
    }
    // 模拟插入消息数据
    private RunnablePriority getRunnablePriority(final int count) {
        return new RunnablePriority(3,1,System.currentTimeMillis()) {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(ThreadExecutor.sdf.format(System.currentTimeMillis()) + "插入消息～～～～插入第" + count + "条消息！");
            }
        };
    }
}