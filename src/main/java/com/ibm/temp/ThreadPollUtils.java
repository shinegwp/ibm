package com.ibm.temp;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import com.alibaba.fastjson.JSON;

public class ThreadPollUtils {

    
    public static <P, V> Map<Integer, V> process(final List<P> paramList, final Process<P, V> process, int threadNum) throws InterruptedException {
        //如果任务数少于线程数取任务为线程个数
        threadNum = Math.min(paramList.size(), threadNum);
        //保证任务是顺序执行的
        final AtomicInteger atomicInteger = new AtomicInteger(0);
        //用于统计已经执行完成的线程
        final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        //保存任务执行结果
        final ConcurrentHashMap<Integer, V> resultMap = new ConcurrentHashMap<>();
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadNum);
        threadPool.setMaximumPoolSize(threadNum);
        for (int i = 0; i < threadNum; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (true) {
                            int index = atomicInteger.getAndAdd(1);
                            if (index >= paramList.size()) {
                                break;
                            }
                            P param = paramList.get(index);
                            try {
                                System.out.println("index:{}"+ index);
                                V result = process.doTask(param);
                                if (result != null) {
                                    System.out.println("index:{}"+ index+ "result:{}"+JSON.toJSONString(result));
                                    resultMap.put(index, result);
                                }
                            } catch (Exception e) {
                                System.out.printf("运行失败 param:{}"+JSON.toJSONString(param), e);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    } finally {
                        countDownLatch.countDown();
                    }

                }
            });
        }
        try {
            countDownLatch.await();
        } catch (Exception e) {
        	System.out.println(e);
        }
        threadPool.shutdown();
        return resultMap;
    }

    /**
     * @param <P> 参数类型
     * @param <V> 返回值类型
     */
    public interface Process<P, V> {
        V doTask(P p);
    }

}
