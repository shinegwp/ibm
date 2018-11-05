package com.ibm.base;
/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月24日 下午8:52:29
* @describe
*/
public class Submitter implements Runnable{
    private String task;
    private Dispatcher dis = Dispatcher.getInstance();
	public Submitter(String task){
    	this.task = task;
    }
	@Override
	public void run() {
		dis.sendTask(task);
	}
}
