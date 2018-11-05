package com.ibm.base;

import java.util.Scanner;

/**
* @author 高伟鹏 
* @email gaoweipeng3@gmail.com
* @version 创建时间：2018年10月24日 下午8:30:56
* @describe
*/
public class BasicEdition {

	public static void main(String[] args) {
		int[] flags = new int[3];
		Scanner input = new Scanner(System.in);
		System.out.println("是否提交A任务？（“1”代表提交，“0”代表不提交）");
		flags[0] = input.nextInt();
		System.out.println("是否提交B任务？（“1”代表提交，“0”代表不提交）");
		flags[1] = input.nextInt();
		System.out.println("是否提交C任务？（“1”代表提交，“0”代表不提交）");
		flags[2] = input.nextInt();
		if (flags[0] == 1) {
			Submitter subA = new Submitter("A");
			Thread tA = new Thread(subA);
			tA.start();
		}
		if (flags[1] == 1) {
			Submitter subB = new Submitter("B");
			Thread tB = new Thread(subB);
			tB.start();
		}
		if (flags[2] == 1) {
			Submitter subC = new Submitter("C");
			Thread tC = new Thread(subC);
			tC.start();
		}
		input.close();
	}
}
