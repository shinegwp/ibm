package com.ibm.restart;

import java.util.Iterator;
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


	public void SubmitterAndHandleThread() {
		ExecutorService executor = getThreadPoolExecutor();
		try {
			executor.execute(new Submitter(dispatcher,"A"));
			executor.execute(new Submitter(dispatcher,"B"));
			executor.execute(new Submitter(dispatcher,"C"));
			Thread.sleep(200);
			executor.execute(new HandlerA(dispatcher,"A"));
			executor.execute(new HandlerB(dispatcher,"B"));
			executor.execute(new HandlerC(dispatcher,"C"));
			Thread.sleep(1000);
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

class HandlerA implements Runnable {
	private BaseOperate dispatcher;
	private String str;

	public HandlerA(BaseOperate dispatcher,String str) {
		this.dispatcher = dispatcher;
		this.str = str;
	}

	@Override
	public void run() {
		String strresult = dispatcher.take(str);
		System.out.println("我是AHandler，这是我获取处理的数据"+strresult);
        
        
	}
}

class HandlerB implements Runnable {
	private BaseOperate dispatcher;
	private String str;

	public HandlerB(BaseOperate dispatcher,String str) {
		this.dispatcher = dispatcher;
		this.str = str;
	}

	@Override
	public void run() {
		String strresult = dispatcher.take(str);
		System.out.println("我是BHandler，这是我获取处理的数据"+strresult);
        
        
	}
}
class HandlerC implements Runnable {
	private BaseOperate dispatcher;
	private String str;

	public HandlerC(BaseOperate dispatcher,String str) {
		this.dispatcher = dispatcher;
		this.str = str;
	}

	@Override
	public void run() {
		String strresult = dispatcher.take(str);
		System.out.println("我是CHandler，这是我获取处理的数据"+strresult);
        
        
	}
}

interface BaseOperate {
	public void push(String str);

	public String take(String str);
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

	public synchronized String take(String str) {
		DispatcherTake dt = new DispatcherTake(queue,str);
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
	private BlockingQueue<String> queue = null;
	private String str = null;

	public DispatcherTake(BlockingQueue<String> queue, String str) {
		this.queue = queue;
		this.str = str;
	}

	@Override
	public String call() throws Exception {
		synchronized(queue){
			Iterator<String> it = queue.iterator();
			String temp = null;
			while (it.hasNext()) {
				temp = it.next();
				if (temp.equals(str)) {
					queue.remove(temp);
					return temp;
				}
			}
		}
		return null;
	}
}