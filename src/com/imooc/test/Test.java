package com.imooc.test;

public class Test {
	
	public static void main(String[] args) {
		String time1 = "2017-12-08";
		String time2 = "2017-12-20";
		int time = time1.compareTo(time2);
		System.out.println(time);
		if(time <= 0){
			System.out.println("ÉúÐ§");
		}
	}

}
