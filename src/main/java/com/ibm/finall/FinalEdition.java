package com.ibm.finall;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;


/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月24日 下午9:43:26
 * @describe
 */
public class FinalEdition {
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) throws InterruptedException, ParseException {
		ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(10);
		for (int i = 0; i < 3; i++) {
			int priority = getPriority();
			long executorTime = getExecutorTime();
			long submitTime = System.currentTimeMillis();
			System.out.println(submitTime);
			Phone phone = new Phone(priority, executorTime, submitTime);
			Submitter submit = new Submitter(phone);
			executor.execute(submit);
		}
		for (int i = 0; i < 5; i++) {
			int priority = getPriority();
			long executorTime = getExecutorTime();
			long submitTime = System.currentTimeMillis();
			Book book = new Book(priority, executorTime, submitTime);
			Submitter submit = new Submitter(book);
			executor.execute(submit);
		}
		for (int i = 0; i < 8; i++) {
			int priority = getPriority();
			long executorTime = getExecutorTime();
			long submitTime = System.currentTimeMillis();
			Pen pen = new Pen(priority, executorTime, submitTime);
			Submitter submit = new Submitter(pen);
			executor.execute(submit);
		}
		System.out.printf("Main: End of the program.\n");
		executor.shutdown();
	}

	public static int getPriority() {
		return (int) (Math.random() * 7) + 1;
	}

	@SuppressWarnings("finally")
	public static long getExecutorTime() throws ParseException {
		String executorTimeTemp = "2018-10-26 10:26:" + ((int) (Math.random() * 50) + 10);
		return sdf.parse(executorTimeTemp).getTime();
	}

}
