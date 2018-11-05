package com.shine.service.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.shine.po.MyFile;

/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月29日 下午3:23:18
* @describe
*/
public class DispatcherPush implements Runnable{

	private MyFile myFile;
	private BlockingQueue<MyFile> queue;
	public DispatcherPush(MyFile myFile, BlockingQueue<MyFile> queue) {
		this.myFile = myFile;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			Lock lock = new ReentrantLock();
			lock.lock();
			queue.put(myFile);
			System.out.println("添加数据后" + queue.toString());
			lock.unlock();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
