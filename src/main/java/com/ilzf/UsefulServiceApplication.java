package com.ilzf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsefulServiceApplication {

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		SpringApplication.run(UsefulServiceApplication.class, args);
		long e = System.currentTimeMillis();
		System.out.print("启动耗时:" + (e - s) / 1000 + "秒");
	}

}
