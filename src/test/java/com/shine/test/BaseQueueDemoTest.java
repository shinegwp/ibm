package com.shine.test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月26日 上午9:28:11
 * @describe
 */
public class BaseQueueDemoTest {
	int corePoolSize = 10;
	int executorTime = 10;
	int submitTime = 10;
	Dispatcher dispatcher = Dispatcher.getInstance();

	@Test
	public void SubmitterAndHandleThread() {
		ExecutorService executor = getThreadPoolExecutor();
		try {
			executor.execute(new Submitter(dispatcher,getString()));
			executor.execute(new Submitter(dispatcher,getString()));
			executor.execute(new Submitter(dispatcher,getString()));
			Thread.sleep(200);
			executor.execute(new Handler(dispatcher));
			executor.execute(new Handler(dispatcher));
			executor.execute(new Handler(dispatcher));
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ThreadPoolExecutor getThreadPoolExecutor() {
		return new ThreadPoolExecutor(corePoolSize, executorTime, submitTime, TimeUnit.DAYS,
				new ArrayBlockingQueue<Runnable>(corePoolSize >> 1));
	}
	public String getString() {
		String strSource = "ABC";
		String str = String.valueOf(strSource.charAt((int) (Math.random() * 3)));
		System.out.println("我提供了" + str + "任务");
		return str;
	}
}

class Submitter implements Runnable {
	private BaseOperate dispatcher;
    private String str;
	public Submitter(BaseOperate dispatcher, String str) {
		this.dispatcher = dispatcher;
		this.str = str;
	}

	@Override
	public void run() {
		dispatcher.push(str);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	
}

class Handler implements Runnable {
	private BaseOperate dispatcher;

	public Handler(BaseOperate dispatcher) {
		this.dispatcher = dispatcher;
	}

	@Override
	public void run() {
		String flag = dispatcher.take();
		System.out.println("这是我获取的数据" + flag);
		switch (flag) {
		case "A":
			System.out.println("我会发送到处理A的handle");
			break;
		case "B":
			System.out.println("我会发送到处理B的handle");
			break;
		case "C":
			System.out.println("我会发送到处理C的handle");
			break;
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

interface BaseOperate {
	public void push(String str);

	public String take();
}

class Dispatcher implements BaseOperate {
	public final int MAX_COUNT = 20;
	private static Dispatcher dispatcher;
	private static BlockingQueue<String> queue;
	private ExecutorService executor;

	private Dispatcher() {
		this.queue = new ArrayBlockingQueue<>(MAX_COUNT);
		executor = Executors.newCachedThreadPool();
	}

	public static Dispatcher getInstance() {
		if (dispatcher == null) {
			synchronized (Dispatcher.class) {
				if (dispatcher == null)
					dispatcher = new Dispatcher();
			}
		}
		return dispatcher;
	}

	public synchronized void push(String str) {
		DispatcherPush dp = new DispatcherPush(str, queue);
		executor.execute(dp);
		
	}

	public synchronized String take() {
		DispatcherTake dt = new DispatcherTake(queue);
		Future<String> future = executor.submit(dt);
		try {
			String result = future.get();
			System.out.println("获取数据后" + queue.toString());
			return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}

class DispatcherPush implements Runnable {
	private String str;
	private BlockingQueue<String> queue;

	public DispatcherPush(String str, BlockingQueue<String> queue) {
		this.str = str;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			queue.put(str);
			
			System.out.println("添加数据后" + queue.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class DispatcherTake implements Callable<String> {
	private BlockingQueue<String> queue;

	public DispatcherTake(BlockingQueue<String> queue) {
		this.queue = queue;
	}

	@Override
	public String call() throws Exception {
		return queue.take();
	}
}