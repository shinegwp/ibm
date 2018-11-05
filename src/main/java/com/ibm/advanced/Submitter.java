package com.ibm.advanced;



/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月24日 下午9:44:49
* @describe
*/
public class Submitter implements Runnable{
	private String task;
    private long date;
    private Dispatcher dis = Dispatcher.getInstance();
	public Submitter(String task, long date){
    	this.task = task;
    	this.date = date;
    }
	@Override
	public void run() {
		dis.sendTask(task, date);
	}
}
