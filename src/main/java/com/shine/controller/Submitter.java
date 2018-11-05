package com.shine.controller;

import com.shine.po.MyFile;
import com.shine.service.DispatcherBaseOperate;
import com.shine.utils.BeanFactory;

/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月29日 下午3:09:33
* @describe
*/
public class Submitter implements Runnable{

	private DispatcherBaseOperate dispatcher;
	private MyFile myFile;

	public Submitter(MyFile myFile) {
		this.dispatcher = BeanFactory.getBean("dispatcher");
		this.myFile = myFile;
	}

	@Override
	public void run() {
		dispatcher.push(myFile);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
