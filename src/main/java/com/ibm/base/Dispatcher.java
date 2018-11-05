package com.ibm.base;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月24日 下午8:56:16
* @describe
*/
public class Dispatcher {
	private int corePoolSize = 3;
	private static Dispatcher dispatcher;
    ExecutorService scheduler;
    private Dispatcher() {
       scheduler = new ThreadPoolExecutor(corePoolSize, 10, 10000, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(corePoolSize));
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
    public void schedule(Runnable event) {
       scheduler.execute(event);
    }
    public void shutdown() {
    	scheduler.shutdown();
    }
    private Handler handler = new Handler();
	public void sendTask (String task) {
		Dispatcher tasker = new Dispatcher();
		switch (task) {
		case "A" : tasker.schedule(new Runnable(){
			public void run() {
				handler.handleA();
			}
		});
		break;
		case "B" : tasker.schedule(new Runnable(){
			public void run() {
				handler.handleB();
			}
		});
		break;
		case "C" : tasker.schedule(new Runnable(){
			public void run() {
				handler.handleC();
			}
		});
		break;
		}
		tasker.shutdown();
	}
}
