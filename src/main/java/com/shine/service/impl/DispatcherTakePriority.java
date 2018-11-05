package com.shine.service.impl;

import java.util.concurrent.Callable;
import com.shine.po.MyFile;

/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月29日 下午3:24:11
* @describe
*/
public class DispatcherTakePriority implements Callable<MyFile>{

	private MyFile myFile;

	public DispatcherTakePriority(MyFile myFile) {
		this.myFile = myFile;
	}

	@Override
	public MyFile call() {
		return myFile;
	}
}
