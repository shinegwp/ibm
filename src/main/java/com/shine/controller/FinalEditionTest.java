package com.shine.controller;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.shine.po.MyFile;
import com.shine.utils.OperateEnum;

/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月29日 下午3:04:07
 * @describe
 */
public class FinalEditionTest {

	static int corePoolSize;
	static int maxPoolSize;
	static int keepAliveTime;
	static String executeTime;

	
	public void SubmitterAndHandleThread() {
		ExecutorService executor = getThreadPoolExecutor();
		try {
			executor.execute(getSubmitterInstance(OperateEnum.CUT));
			executor.execute(getSubmitterInstance(OperateEnum.COPY));
			executor.execute(getSubmitterInstance(OperateEnum.DELETE));
			Thread.sleep(200);
			executor.execute(getHandlerInstance());
			executor.execute(getHandlerInstance());
			executor.execute(getHandlerInstance());
			Thread.sleep(120000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Runnable getHandlerInstance() {
		return new Handler();
	}

	private MyFile getMyFileInstance(OperateEnum operate) {
		MyFile myFile = new MyFile(operate, getDate(), getPriority());
        System.out.println(myFile.operate+"的执行时间是:"+myFile.executeTime+",优先级为:"+myFile.getPriority());
		return myFile;
	}

	private ThreadPoolExecutor getThreadPoolExecutor() {
		return new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, TimeUnit.DAYS,
				new ArrayBlockingQueue<Runnable>(corePoolSize >> 1));
	}

	// public OperateEnum getOperate() {
	//
	// OperateEnum[] operateSource = { OperateEnum.CUT, OperateEnum.COPY,
	// OperateEnum.DELETE};
	// OperateEnum operate = operateSource[(int) (Math.random() * 3)];
	// System.out.println("我将执行" + operate + "操作");
	// return operate;
	// }

	private String getDate() {
		String executorTimeTemp = executeTime /*+ ((int) (Math.random() * 50) + 10)*/;
		try {
			long flag = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(executorTimeTemp).getTime() - System.currentTimeMillis();
			if (flag < 0) {
				throw new IllegalArgumentException("输入时间不合法，早于当前时间");
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return executorTimeTemp;
	}

	private int getPriority() {
		return (int) (Math.random() * 10 + 1);
	}

	static String copyFilePath;
	static String copyTargetPath;
	static String cutFilePath;
	static String cutTargetPath;
	static String deleteFilePath;

	static {
		Properties properties = null;
		InputStream is = null;
	
		try {
			properties = new Properties();
			is = FinalEditionTest.class.getClassLoader().getResourceAsStream("configuration/ThreadPool.properties");
			properties.load(is);
			corePoolSize = Integer.valueOf(properties.getProperty("corePoolSize"));
			maxPoolSize = Integer.valueOf(properties.getProperty("maxPoolSize"));
			keepAliveTime = Integer.valueOf(properties.getProperty("keepAliveTime"));
			executeTime = properties.getProperty("executeTime");
			is = FinalEditionTest.class.getClassLoader().getResourceAsStream("configuration/FilePath.properties");
			properties.load(is);
			copyFilePath = properties.getProperty("copyFilePath");
			copyTargetPath = properties.getProperty("copyTargetPath");
			cutFilePath = properties.getProperty("cutFilePath");
			cutTargetPath = properties.getProperty("cutTargetPath");
			deleteFilePath = properties.getProperty("deleteFilePath");
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

	private  Runnable getSubmitterInstance(OperateEnum operate) {
		switch (operate) {
		case COPY:
			OperateEnum.COPY.setFilePath(copyFilePath);
			OperateEnum.COPY.setTargetPath(copyTargetPath);
			break;
		case CUT:
			OperateEnum.CUT.setFilePath(cutFilePath);
			OperateEnum.CUT.setTargetPath(cutTargetPath);
			break;
		case DELETE:
			OperateEnum.DELETE.setFilePath(deleteFilePath);
			break;
		}
		return new Submitter(getMyFileInstance(operate));
	}
}
