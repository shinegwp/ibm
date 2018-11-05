package com.ibm.advanced;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月24日 下午9:50:49
 * @describe
 */
public class Dispatcher {

	private int corePoolSize = 3;
	ScheduledExecutorService scheduler;
	private static Dispatcher dispatcher;

	private Dispatcher() {
		scheduler = new ScheduledThreadPoolExecutor(corePoolSize);
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
	public void schedule(Runnable event, long delay) {
		((ScheduledThreadPoolExecutor) scheduler).schedule(event, delay, TimeUnit.MILLISECONDS);
	}

	public void shutdown() {
		scheduler.shutdown();
	}

	private Handler handler = new Handler();

	public void sendTask(String task, long date) {
		Dispatcher tasker = new Dispatcher();
		switch (task) {
		case "A":
			tasker.schedule(new Runnable() {
				public void run() {
					handler.handleA();
				}
			}, date);
			;
			break;
		case "B":
			tasker.schedule(new Runnable() {
				public void run() {
					handler.handleB();
				}
			}, date);
			;
			break;
		case "C":
			tasker.schedule(new Runnable() {
				public void run() {
					handler.handleC();
				}
			}, date);
			;
			break;
		}
		tasker.shutdown();
	}
}