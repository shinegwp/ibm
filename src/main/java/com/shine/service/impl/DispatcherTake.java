package com.shine.service.impl;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import com.shine.po.MyFile;


/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月29日 下午3:23:36
* @describe
*/
public class DispatcherTake implements Callable<MyFile>{

	private BlockingQueue<MyFile> queue;
	public DispatcherTake(BlockingQueue<MyFile> queue) {
		this.queue = queue;
	}
	@Override
	public MyFile call() throws Exception {
		return queue.take();
	}
}
