package com.ibm.finall;
/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月25日 下午9:45:23
* @describe
*/
public abstract class Goods {
    public int priority;
    public long executorTime;
    public long submitTime;
    public Goods(int priority, long executorTime, long submitTime){
    	this.priority = priority;
    	this.executorTime = executorTime;
    	this.submitTime = submitTime;
    }
	abstract void consume();
}

class Phone extends Goods{
	private static int count = 100;
    public Phone(int priority, long executorTime, long submitTime){
    	super(priority, executorTime, submitTime);
    }
	@Override
	public void consume() {
		System.out.println("消费一部手机，剩余"+(--count));
	}
	@Override
	public String toString() {
		return "Phone [priority=" + priority + ", executorTime=" + executorTime + ", submitTime=" + submitTime + "]";
	}
	
}
class Book extends Goods{
	private static int count = 200;
    public Book(int priority, long executorTime, long submitTime){
    	super(priority, executorTime, submitTime);
    }
	@Override
	public void consume() {
		System.out.println("消费一个本子，剩余"+(--count));
	}
	public String toString() {
		return "Book [priority=" + priority + ", executorTime=" + executorTime + ", submitTime=" + submitTime + "]";
	}
}
class Pen extends Goods{
	private static int count = 1000;
    public Pen(int priority, long executorTime, long submitTime){
    	super(priority, executorTime, submitTime);
    }
	@Override
	public void consume() {
		System.out.println("消费一只笔，剩余"+(--count));
	}
	public String toString() {
		return "Pen [priority=" + priority + ", executorTime=" + executorTime + ", submitTime=" + submitTime + "]";
	}
}