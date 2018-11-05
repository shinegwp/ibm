package com.shine.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月29日 下午5:21:17
* @describe
*/
public class BeanFactory {
	
	
	@SuppressWarnings("unchecked")
	public static <T> T getBean (String id) {
		T obj = null;
		try {
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(BeanFactory.class.getClassLoader().getResourceAsStream("configuration/Beans.xml"));
			Element node = (Element)document.selectSingleNode("//bean[@id='"+id+"']");
			String className = node.attributeValue("class");
			return (T)Class.forName(className).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	
}
