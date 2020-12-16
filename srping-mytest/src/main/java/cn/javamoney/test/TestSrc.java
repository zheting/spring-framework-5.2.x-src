package cn.javamoney.test;

import cn.javamoney.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TestSrc {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
		System.out.println(ac.getBean("user"));
	}
}
