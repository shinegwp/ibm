package com.ibm.finall;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月25日 下午10:15:55
* @describe
*/
public class DispatcherOne {

	private int corePoolSize = 1;
	private int maximumPoolSize = 1;
	private int keepAliveTime = 1;
	ExecutorService scheduler;
	private static DispatcherOne dispatcher;
	
	private DispatcherOne() {
		scheduler = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.DAYS, new PriorityBlockingQueue<Runnable>());
	}
	public static DispatcherOne getInstance() {
		if (dispatcher == null) {
			synchronized (DispatcherOne.class) {
				if (dispatcher == null)
					dispatcher = new DispatcherOne();
			}
		}
		return dispatcher;
	}
	public void schedule(RunnablePriority event) {
		scheduler.execute(event);;
	}
	public void shutdown() {
		scheduler.shutdown();
	}
	public void sendTask(Goods goods){
		DispatcherOne tasker = new DispatcherOne();
		
        tasker.schedule(new RunnablePriority(goods));
		tasker.shutdown();
	}
}
class RunnablePriority implements Runnable, Comparable<RunnablePriority> {
	private DispatcherTwo dis2 = DispatcherTwo.getInstance();
	private Goods goods;
    public RunnablePriority(Goods goods) {
        this.goods = goods;
    }
    public Goods getGoods(){
    	return this.goods;
    }
    @Override
    public void run() {
        System.out.println("准备执行"+goods.toString());
        dis2.sendTask(goods);
    }
	@Override
	public int compareTo(RunnablePriority o) {
		if (goods.executorTime < o.getGoods().executorTime) {
    		return -1;
    	} else if (goods.executorTime == o.getGoods().executorTime) {
    		if (goods.priority < o.getGoods().priority) {
                return -1;
            } else if (goods.priority == o.getGoods().priority) {
            	if (goods.submitTime <= o.getGoods().submitTime) {
            		return -1;
            	} else {
            		return 1;
            	}
            } else {
            	return 1;
            }
    	} else {
    		return 1;
    	}
	}
 
}