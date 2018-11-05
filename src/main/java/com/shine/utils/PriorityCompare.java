package com.shine.utils;

import java.util.Comparator;

import com.shine.po.MyFile;

/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月29日 下午3:07:10
 * @describe
 */
public class PriorityCompare implements Comparator<MyFile> {
	public int compare(MyFile f1, MyFile f2) {
		if (f1.getTime() == f2.getTime()) {
			if (f1.getPriority() == f2.getPriority()) {
				return 0;
			} else {
				return f2.getPriority() - f1.getPriority();
			}
		} else {
			return (int) (f1.getTime() - f2.getTime());
		}
	}
}
