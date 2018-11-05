package com.shine.service;

import com.shine.po.MyFile;

/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月29日 下午3:17:42
* @describe
*/
public interface DispatcherBaseOperate {
	public void push(MyFile myFile);
	public MyFile take();
}
