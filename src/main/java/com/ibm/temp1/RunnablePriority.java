package com.ibm.temp1;
/**
 * 保存优先级
 * 
 * @author notreami
 *
 */
class RunnablePriorityBase {
	public int priority;
	public long executorTime;
	public long submitTime;
    public RunnablePriorityBase(int priority, long executorTime, long submitTime) {
        this.priority = priority;
        this.executorTime = executorTime;
    }
}
/**
 * 优先级比较
 * @author notreami
 *
 */
public class RunnablePriority extends RunnablePriorityBase implements Runnable, Comparable<RunnablePriorityBase> {
    public RunnablePriority(int priority, long executorTime, long submitTime) {
        super(priority, executorTime, submitTime);
    }
    @Override
    public int compareTo(RunnablePriorityBase o) {
    	System.out.println("好的，我已经进行比较了");
    	if (executorTime < o.executorTime) {
    		return -1;
    	} else if (executorTime == o.executorTime) {
    		if (priority < o.priority) {
                return -1;
            } else if (priority == o.priority) {
            	if (submitTime <= o.submitTime) {
            		return -1;
            	} else {
            		return 1;
            	}
            } else {
            	return 1;
            }
    	} else {
    		return 1;
    	}
    }
    @Override
    public void run() {
        // 执行任务代码..
    	System.out.println("看这里"+this.submitTime);
    }
 
}