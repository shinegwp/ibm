package com.shine.po;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.shine.utils.OperateEnum;

/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月29日 下午3:06:01
* @describe
*/
public class MyFile {

	public OperateEnum operate;
	public String executeTime;
	private int priority;

	public MyFile(OperateEnum operate, String executeTime, int priority) {
		this.operate = operate;
		this.executeTime = executeTime;
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "FiMyFile [operate=" + operate + ", executeTime=" + executeTime + ", priority=" + priority + "]";
	}

	public int getPriority() {
		return this.priority;
	}

	public long getTime() {
		try {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(executeTime).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
}
