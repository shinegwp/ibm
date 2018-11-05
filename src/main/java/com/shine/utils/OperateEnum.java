package com.shine.utils;


/**
 * @author 高伟鹏
 * @email gaoweipeng3@gmail.com
 * @version 创建时间：2018年10月29日 下午5:10:55
 * @describe
 */
public enum OperateEnum {
	CUT, DELETE, COPY;
	public String filePath;
	public String targetPath;

	private OperateEnum() {

	}
	public String getFilePath() {
		return filePath;
	}
	public  void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public  String getTargetPath() {
		return targetPath;
	}
	public  void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}
	public String toString() {
		return "OperateEnum [filePath=" + filePath + ", targetPath=" + targetPath + "]";
	}
}
