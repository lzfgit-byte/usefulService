package com.ilzf;

import com.ilzf.utils.NetUtilILZF;
import com.ilzf.utils.StringUtilIZLF;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsefulServiceApplication {

	public static void main(String[] args) {
		long s = System.currentTimeMillis();
		SpringApplication.run(UsefulServiceApplication.class, args);
		long e = System.currentTimeMillis();
		StringUtilIZLF.print("启动耗时:" + (e - s) / 1000 + "秒");
		NetUtilILZF.getLocalhost().forEach(url -> StringUtilIZLF.print("地址：" + url));
	}

}
