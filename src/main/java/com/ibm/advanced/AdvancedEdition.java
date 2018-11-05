package com.ibm.advanced;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;


/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月24日 下午9:43:26
* @describe
*/
public class AdvancedEdition {

	public static void main(String[] args) throws ParseException {
		String[][] flags = new String[3][2];
		Scanner input = new Scanner(System.in);
		System.out.println("是否提交A任务？（“1”代表提交，“0”代表不提交）");
		flags[0][0] = input.nextLine();
		if (flags[0][0].equals("1")) {
			System.out.println("请输入执行时间：（实例：2018-10-24 12：00：00）");
			flags[0][1] = input.nextLine();
			System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flags[0][1]).getTime()-System.currentTimeMillis())/1000+"秒后执行");
		}
		System.out.println("是否提交B任务？（“1”代表提交，“0”代表不提交）");
		flags[1][0] = input.nextLine();
		if (flags[1][0].equals("1")) {
			System.out.println("请输入执行时间：（实例：2018-10-24 12：00：00）");
			flags[1][1] = input.nextLine();
			System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flags[1][1]).getTime()-System.currentTimeMillis())/1000+"秒后执行");
		}
		System.out.println("是否提交C任务？（“1”代表提交，“0”代表不提交）");
		flags[2][0] = input.nextLine();
		if (flags[2][0].equals("1")) {
			System.out.println("请输入执行时间：（实例：2018-10-24 12：00：00）");
			flags[2][1] = input.nextLine();
			System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flags[2][1]).getTime()-System.currentTimeMillis())/1000+"秒后执行");
		}
		if (flags[0][0].equals("1")) {
			Submitter subA = new Submitter("A",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flags[0][1]).getTime()-System.currentTimeMillis());
			Thread tA = new Thread(subA);
			tA.start();
		}
		if (flags[1][0].equals("1")) {
			Submitter subB = new Submitter("B",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flags[1][1]).getTime()-System.currentTimeMillis());
			Thread tB = new Thread(subB);
			tB.start();
		}
		if (flags[2][0].equals("1")) {
			Submitter subC = new Submitter("C",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(flags[2][1]).getTime()-System.currentTimeMillis());
			Thread tC = new Thread(subC);
			tC.start();
		}
		input.close();
	}
}
