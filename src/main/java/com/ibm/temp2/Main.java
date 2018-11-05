package com.ibm.temp2;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
	public static void main(String[] args) {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1, TimeUnit.SECONDS, new PriorityBlockingQueue<Runnable>());
		for (int i = 0; i < 40; i++) {
			MyPriorityTask task = new MyPriorityTask("Task " + i, 0);
			executor.execute(task);
			System.out.println(executor.getTaskCount()+"diyige");
		}
		System.out.println("第一条信息执行完毕");
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for (int j = 41; j < 80; j++) {
			MyPriorityTask task = new MyPriorityTask("Task " + j, 1);
			executor.execute(task);
			System.out.println(executor.getTaskCount()+"敌得过撒");
		}
		System.out.println("第二个循环执行完毕");
		try {
			 executor.awaitTermination(50, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.printf("Main: End of the program.\n");
	}
}
