package com.shine.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.shine.po.MyFile;
import com.shine.service.DispatcherBaseOperate;
import com.shine.utils.PriorityCompare;

/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月29日 下午3:19:07
 * @describe
 */
public class Dispatcher implements DispatcherBaseOperate {
	private static int corePoolSize;
	private static int MAX_COUNT;
	private static BlockingQueue<MyFile> queue;
	private static ScheduledExecutorService executor;
	Lock lock = new ReentrantLock();
	public Dispatcher() {

	}
	public void push(MyFile myFile) {
		Runnable dp = new DispatcherPush(myFile, queue);
		executor.execute(dp);
	}

	public MyFile take() {
		Callable<MyFile> dt = new DispatcherTake(queue);

		lock.lock();
		Future<MyFile> futureTemp = executor.submit(dt);
		try {
			MyFile resultTemp = futureTemp.get();
			Callable<MyFile> dtp = new DispatcherTakePriority(resultTemp);
			lock.unlock();
			System.out.println("我将在" + resultTemp.executeTime + "执行该任务" + resultTemp.operate);
			lock.lock();
			Future<MyFile> future = executor.schedule(dtp, getDelay(resultTemp.executeTime), TimeUnit.MILLISECONDS);
			MyFile result = future.get();

			lock.unlock();
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public long getDelay(String date) {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime() - System.currentTimeMillis();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	static {
		Properties properties = null;
		InputStream is = null;
		try {
			properties = new Properties();
			is = Dispatcher.class.getClassLoader().getResourceAsStream("configuration/ThreadPool.properties");
			properties.load(is);
			MAX_COUNT = Integer.valueOf(properties.getProperty("maxCount"));
			corePoolSize = Integer.valueOf(properties.getProperty("corePoolSize"));
			queue = new PriorityBlockingQueue<>(MAX_COUNT, new PriorityCompare());
			executor = new ScheduledThreadPoolExecutor(corePoolSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}