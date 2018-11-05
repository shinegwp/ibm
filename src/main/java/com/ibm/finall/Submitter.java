package com.ibm.finall;

import java.text.SimpleDateFormat;

/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月25日 下午2:44:49
* @describe
*/
public class  Submitter implements Runnable{
	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss::SSS");
	private Goods goods;
    private DispatcherOne dis = DispatcherOne.getInstance();
	public Submitter(Goods goods){
    	this.goods = goods; 
    }
	@Override
	public void run() {
		System.out.println(sdf.format(System.currentTimeMillis())+"已经加入队列中"+goods.toString());
		dis.sendTask(goods);
	}
}
