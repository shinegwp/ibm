package com.ibm.finall;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月24日 下午9:50:49
 * @describe
 */
public class DispatcherTwo {

	private int corePoolSize = 10;
	ScheduledExecutorService scheduler;
	private static DispatcherTwo dispatcher;

	private DispatcherTwo() {
		scheduler = new ScheduledThreadPoolExecutor(corePoolSize);
	}
	public static DispatcherTwo getInstance() {
		if (dispatcher == null) {
			synchronized (DispatcherTwo.class) {
				if (dispatcher == null)
					dispatcher = new DispatcherTwo();
			}
		}
		return dispatcher;
	}
	public void schedule(RunnableFinal event) {
		long delay = event.getGoods().executorTime-System.currentTimeMillis();
		System.out.println(delay/1000+"秒后还是执行任务"+event.getGoods().toString());
	    scheduler.schedule(event, event.getGoods().executorTime-System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	public void shutdown() {
		scheduler.shutdown();
	}

	private Handler handler = new Handler();

	public void sendTask(Goods goods) {
		DispatcherTwo tasker = new DispatcherTwo();
        tasker.schedule(new RunnableFinal(goods));
		tasker.shutdown();
	}
}
class RunnableFinal implements Runnable{
	private Handler handler = new Handler();
    private Goods goods;
    public RunnableFinal(Goods goods){
    	this.goods = goods;
    }
	@Override
	public void run() {
		handler.consume(goods);
	}
	public Goods getGoods() {
		return goods;
	}
	
}