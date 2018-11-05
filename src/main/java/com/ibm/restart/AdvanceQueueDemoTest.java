package com.ibm.restart;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Test;


/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月26日 下午2:40:35
 * @describe
 */
public class AdvanceQueueDemoTest {
	int corePoolSize = 10;
	int maxPoolSize = 10;
	int keepAliveTime = 10;
	AdDispatcher dispatcher = AdDispatcher.getInstance();

	
	public void SubmitterAndHandleThread() {
		ExecutorService executor = getThreadPoolExecutor();
		try {
			executor.execute(getAdSubmitterInstance("cut"));
			executor.execute(getAdSubmitterInstance("paste"));
			executor.execute(getAdSubmitterInstance("delete"));
			Thread.sleep(200);
			executor.execute(new AdHandlerCut(dispatcher));
			executor.execute(new AdHandlerPaste(dispatcher));
			executor.execute(new AdHandlerDelete(dispatcher));
			Thread.sleep(700000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public AdSubmitter getAdSubmitterInstance(String str) {
		return new AdSubmitter(dispatcher, getMyFileInstance(str));
	}

	public MyFile getMyFileInstance(String str) {
		String date = getDate();
		MyFile myFile = new MyFile(str,date);
        System.out.println(str+"操作的执行时间时："+date);
		return myFile;
	}

	public ThreadPoolExecutor getThreadPoolExecutor() {
		return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.DAYS,
				new ArrayBlockingQueue<Runnable>(corePoolSize >> 1));
	}

	/*public String getString() {
		String[] strSource = { "copy", "cut", "paste" };
		String str = String.valueOf(strSource[(int) (Math.random() * 3)]);
		System.out.println("我将执行" + str + "操作");
		return str;
	}*/

	public String getDate() {
		String executorTimeTemp = "2018-11-02 10:48:" + ((int) (Math.random() * 50) + 10);
		return executorTimeTemp;
	}
}

class MyFile {
	String operate;
	String executeTime;

	public MyFile(String operate, String executeTime) {
		this.operate = operate;
		this.executeTime = executeTime;
	}

	@Override
	public String toString() {
		return "MyFile [operate=" + operate + ", executeTime=" + executeTime + "]";
	}

	public long getTime() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(executeTime).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}

class Compare implements Comparator<MyFile> {
	public int compare(MyFile file1, MyFile file2) {
		if (file1.getTime() == file2.getTime()) {
			return 0;
		} else {
			return (int) (file1.getTime() - file2.getTime());
		}
	}
}

class AdSubmitter implements Runnable {
	private AdBaseOperate adDispatcher;
	private MyFile myFile;

	public AdSubmitter(AdBaseOperate dispatcher, MyFile myFile) {
		this.adDispatcher = dispatcher;
		this.myFile = myFile;
	}

	@Override
	public void run() {
		adDispatcher.push(myFile);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

class AdHandlerCut implements Runnable {
	private AdBaseOperate adDispatcher;

	public AdHandlerCut(AdBaseOperate dispatcher) {
		this.adDispatcher = dispatcher;
	}

	@Override
	public void run() {
		MyFile flag = adDispatcher.take("cut");
        if (flag != null) {
        	System.out.println("我在执行cut操作"+flag.toString());
        } else {
        	System.out.println("没有获取到数据");
        }
        
	}
}

class AdHandlerPaste implements Runnable {
	private AdBaseOperate adDispatcher;

	public AdHandlerPaste(AdBaseOperate dispatcher) {
		this.adDispatcher = dispatcher;
	}

	@Override
	public void run() {
		MyFile flag = adDispatcher.take("paste");
        if (flag != null) {
        	System.out.println("我在执行paste操作"+flag.toString());
        } else {
        	System.out.println("没有获取到数据");
        }
        
	}
}

class AdHandlerDelete implements Runnable {
	private AdBaseOperate adDispatcher;

	public AdHandlerDelete(AdBaseOperate dispatcher) {
		this.adDispatcher = dispatcher;
	}

	@Override
	public void run() {
		MyFile flag = adDispatcher.take("delete");
        if (flag != null) {
        	System.out.println("我在执行delete操作"+flag.toString());
        } else {
        	System.out.println("没有获取到数据");
        }
	}
}


interface AdBaseOperate {
	public void push(MyFile myFile);

	public MyFile take(String str);
}

class AdDispatcher implements AdBaseOperate {
	public final int MAX_COUNT = 20;
	private static AdDispatcher AdDispatcher;
	private static BlockingQueue<MyFile> queue;
	private ScheduledExecutorService executor;

	private AdDispatcher() {
		this.queue = new PriorityBlockingQueue<>(MAX_COUNT, new Compare());
		executor = new ScheduledThreadPoolExecutor(3);
		;
	}

	public static AdDispatcher getInstance() {
		if (AdDispatcher == null) {
			synchronized (AdDispatcher.class) {
				if (AdDispatcher == null)
					AdDispatcher = new AdDispatcher();
			}
		}
		return AdDispatcher;
	}

	public  void push(MyFile myFile) {
		AdDispatcherPush dp = new AdDispatcherPush(myFile, queue);
		executor.execute(dp);
	}

	public  MyFile take(String str) {
		AdDispatcherTake dt = new AdDispatcherTake(queue, str);
		Lock lock = new ReentrantLock();
		lock.lock();
		Future<MyFile> futureTemp = executor.submit(dt);
		try {
			MyFile resultTemp = futureTemp.get();
			Callable<MyFile> aht = new AdDispatcherHandleTake(resultTemp);
			lock.unlock();
			System.out.println("我将在" + resultTemp.executeTime + "执行该任务" + resultTemp.operate);
			Future<MyFile> future = executor.schedule(aht, getDelay(resultTemp.executeTime), TimeUnit.MILLISECONDS);
			MyFile result = future.get();
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
}

class AdDispatcherPush implements Runnable {
	private MyFile myFile;
	private BlockingQueue<MyFile> queue;

	public AdDispatcherPush(MyFile myFile, BlockingQueue<MyFile> queue) {
		this.myFile = myFile;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			Lock lock = new ReentrantLock();
			lock.lock();
			queue.put(myFile);
			lock.unlock();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class AdDispatcherTake implements Callable<MyFile> {
	private BlockingQueue<MyFile> queue = null;
	private String str = null;

	public AdDispatcherTake(BlockingQueue<MyFile> queue, String str) {
		this.queue = queue;
		this.str = str;
	}

	@Override
	public MyFile call() throws Exception {
		synchronized(queue){
			Iterator<MyFile> it = queue.iterator();
			MyFile mf = null;
			while (it.hasNext()) {
				mf = it.next();
				if (mf.operate.equals(str)) {
					queue.remove(mf);
					return mf;
				}
			}
		}
		return null;
	}
}

class AdDispatcherHandleTake implements Callable<MyFile> {
	private MyFile myFile;

	public AdDispatcherHandleTake(MyFile myFile) {
		this.myFile = myFile;
	}

	@Override
	public MyFile call() {
		return myFile;
	}
}